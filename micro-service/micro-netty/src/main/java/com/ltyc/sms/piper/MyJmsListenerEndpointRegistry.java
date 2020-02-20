package com.ltyc.sms.piper;


import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpoint;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/20
 */
public class MyJmsListenerEndpointRegistry extends JmsListenerEndpointRegistry {
    protected final Log logger = LogFactory.getLog(getClass());

    private final Map<String, MessageListenerContainer> listenerContainers =
            new ConcurrentHashMap<>();

    private int phase = DEFAULT_PHASE;

    @Nullable
    private ApplicationContext applicationContext;

    private boolean contextRefreshed;

    public void removeContainer(String endpointId){
        listenerContainers.remove(endpointId);
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext() == this.applicationContext) {
            this.contextRefreshed = true;
        }
    }


    /**
     * Return the {@link MessageListenerContainer} with the specified id or
     * {@code null} if no such container exists.
     * @param id the id of the container
     * @return the container or {@code null} if no container with that id exists
     * @see JmsListenerEndpoint#getId()
     * @see #getListenerContainerIds()
     */
    @Override
    @Nullable
    public MessageListenerContainer getListenerContainer(String id) {
        Assert.notNull(id, "Container identifier must not be null");
        return this.listenerContainers.get(id);
    }

    /**
     * Return the ids of the managed {@link MessageListenerContainer} instance(s).
     * @since 4.2.3
     * @see #getListenerContainer(String)
     */
    @Override
    public Set<String> getListenerContainerIds() {
        return Collections.unmodifiableSet(this.listenerContainers.keySet());
    }

    /**
     * Return the managed {@link MessageListenerContainer} instance(s).
     */
    @Override
    public Collection<MessageListenerContainer> getListenerContainers() {
        return Collections.unmodifiableCollection(this.listenerContainers.values());
    }

    /**
     * Create a message listener container for the given {@link JmsListenerEndpoint}.
     * <p>This create the necessary infrastructure to honor that endpoint
     * with regards to its configuration.
     * <p>The {@code startImmediately} flag determines if the container should be
     * started immediately.
     * @param endpoint the endpoint to add
     * @param factory the listener factory to use
     * @param startImmediately start the container immediately if necessary
     * @see #getListenerContainers()
     * @see #getListenerContainer(String)
     */
    @Override
    public void registerListenerContainer(JmsListenerEndpoint endpoint, JmsListenerContainerFactory<?> factory,
                                          boolean startImmediately) {

        Assert.notNull(endpoint, "Endpoint must not be null");
        Assert.notNull(factory, "Factory must not be null");
        String id = endpoint.getId();
        Assert.hasText(id, "Endpoint id must be set");

        synchronized (this.listenerContainers) {
            if (this.listenerContainers.containsKey(id)) {
                throw new IllegalStateException("Another endpoint is already registered with id '" + id + "'");
            }
            MessageListenerContainer container = createListenerContainer(endpoint, factory);
            this.listenerContainers.put(id, container);
            if (startImmediately) {
                startIfNecessary(container);
            }
        }
    }

    /**
     * Create a message listener container for the given {@link JmsListenerEndpoint}.
     * <p>This create the necessary infrastructure to honor that endpoint
     * with regards to its configuration.
     * @param endpoint the endpoint to add
     * @param factory the listener factory to use
     * @see #registerListenerContainer(JmsListenerEndpoint, JmsListenerContainerFactory, boolean)
     */
    @Override
    public void registerListenerContainer(JmsListenerEndpoint endpoint, JmsListenerContainerFactory<?> factory) {
        registerListenerContainer(endpoint, factory, false);
    }

    /**
     * Create and start a new container using the specified factory.
     */
    @Override
    protected MessageListenerContainer createListenerContainer(JmsListenerEndpoint endpoint,
                                                               JmsListenerContainerFactory<?> factory) {

        MessageListenerContainer listenerContainer = factory.createListenerContainer(endpoint);

        if (listenerContainer instanceof InitializingBean) {
            try {
                ((InitializingBean) listenerContainer).afterPropertiesSet();
            }
            catch (Exception ex) {
                throw new BeanInitializationException("Failed to initialize message listener container", ex);
            }
        }

        int containerPhase = listenerContainer.getPhase();
        if (containerPhase < Integer.MAX_VALUE) {  // a custom phase value
            if (this.phase < Integer.MAX_VALUE && this.phase != containerPhase) {
                throw new IllegalStateException("Encountered phase mismatch between container factory definitions: " +
                        this.phase + " vs " + containerPhase);
            }
            this.phase = listenerContainer.getPhase();
        }

        return listenerContainer;
    }


    // Delegating implementation of SmartLifecycle

    @Override
    public int getPhase() {
        return this.phase;
    }

    @Override
    public void start() {
        for (MessageListenerContainer listenerContainer : getListenerContainers()) {
            startIfNecessary(listenerContainer);
        }
    }

    @Override
    public void stop() {
        for (MessageListenerContainer listenerContainer : getListenerContainers()) {
            listenerContainer.stop();
        }
    }

    @Override
    public boolean isRunning() {
        for (MessageListenerContainer listenerContainer : getListenerContainers()) {
            if (listenerContainer.isRunning()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Start the specified {@link MessageListenerContainer} if it should be started
     * on startup or when start is called explicitly after startup.
     * @see MessageListenerContainer#isAutoStartup()
     */
    private void startIfNecessary(MessageListenerContainer listenerContainer) {
        if (this.contextRefreshed || listenerContainer.isAutoStartup()) {
            listenerContainer.start();
        }
    }

    @Override
    public void destroy() {
        for (MessageListenerContainer listenerContainer : getListenerContainers()) {
            if (listenerContainer instanceof DisposableBean) {
                try {
                    ((DisposableBean) listenerContainer).destroy();
                }
                catch (Throwable ex) {
                    logger.warn("Failed to destroy message listener container", ex);
                }
            }
        }
    }

}
