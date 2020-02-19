package com.ltyc.sms.connect.manager;

import com.google.common.util.concurrent.ListenableScheduledFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/5
 */
public enum EventLoopGroupFactory {
    INS;

    private final static RejectedExecutionHandler rejected = new RejectedExecutionHandler(){
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor){

        }
    };

    private static int bossThreads =1;
    private static int workThreads =0;//默认为2*cpu核数的线程数

    public static void setBossThreads(int bossThreads) {
        EventLoopGroupFactory.bossThreads = bossThreads;
    }

    public static void setWorkThreads(int workThreads) {
        EventLoopGroupFactory.workThreads = workThreads;
    }

    private  final static EventLoopGroup bossGroup = new NioEventLoopGroup(bossThreads, newThreadFactory("bossGroup"));
    private  final static EventLoopGroup workGroup = new NioEventLoopGroup(workThreads, newThreadFactory("workGroup"));
    /**
     解决Netty-EventLoopGroup无法submit阻塞任务的问题。
     netty的特性：
     EventLoopGroup.submit(callable)方法不能提交阻塞任务。
     如果callable阻塞，即使EventLoopGroup中有其它空闲的线程，也无法执行部分提交的任务。

     原因：EventLoopGroup的任务队列不是共享的， 每个EventLoop都有独立的任务队列，
     如果队列中一个任务阻塞，其余的任务也无法执行。
     */

    private final static ListeningScheduledExecutorService busiWork = MoreExecutors.listeningDecorator(new ScheduledThreadPoolExecutor(Integer.parseInt("4"),newThreadFactory("busiWork-"),rejected));
    //private  final static EventLoopGroup busiWork = new ShareTaskQueueDefaultEventLoopGroup(Integer.valueOf(PropertiesUtils.getproperties("GlobalBusiWorkThreadCount","4")),new DefaultExecutorServiceFactory("busiWork"));

    public EventLoopGroup getBoss(){return bossGroup;};
    public EventLoopGroup getWorker(){return workGroup;};
    public ListeningScheduledExecutorService getBusiWork(){return busiWork;};


    /**
     *使用netty线程池实现一个无限循环任务，
     *@param task
     *需要执行的任务
     *@param exitCondition
     *任务的关闭条件
     *@param delay
     *任务的执行间隔
     */
    public <T> void submitUnlimitCircleTask(Callable<T> task, ExitUnlimitCirclePolicy<T> exitCondition, long delay){
        addTask(busiWork,task,exitCondition,delay);
    }

    private <T> void addTask(final ListeningScheduledExecutorService executor ,final Callable<T> task ,final ExitUnlimitCirclePolicy<T> exitCondition,final long delay) {

        if(executor.isShutdown()) {
            return;
        }
        final ListenableScheduledFuture<T> future = executor.schedule(task, delay, TimeUnit.MICROSECONDS);
        future.addListener(new Runnable(){

            @Override
            public void run() {

                DefaultPromise<T> nettyfuture = new DefaultPromise<T>(GlobalEventExecutor.INSTANCE);
                try {
                    nettyfuture.setSuccess(future.get());
                } catch (InterruptedException e) {
                    nettyfuture.tryFailure(e);
                } catch (ExecutionException e) {
                    nettyfuture.tryFailure(e);
                }catch(Exception e){
                    nettyfuture.tryFailure(e);
                }
                try {
                    if(exitCondition.notOver(nettyfuture)) {
                        addTask(executor, task, exitCondition, delay);
                    }
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }, executor);
    }

    /**
     *close方法会阻塞，
     *如果有死循环任务，线程池会关闭不掉。
     *
     */
    public void closeAll(){
        //先停业务线程池
        //getBusiWork().shutdownGracefully().syncUninterruptibly();
        getBusiWork().shutdown();
        getBoss().shutdownGracefully().syncUninterruptibly();

        //最后停worker
        getWorker().shutdownGracefully().syncUninterruptibly();
    }


    private static ThreadFactory  newThreadFactory(final String name){

        return new ThreadFactory() {

            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread( r,name + threadNumber.getAndIncrement());

                t.setDaemon(true);
                if (t.getPriority() != Thread.NORM_PRIORITY) {
                    t.setPriority(Thread.NORM_PRIORITY);
                }
                return t;
            }
        };

    }
}
