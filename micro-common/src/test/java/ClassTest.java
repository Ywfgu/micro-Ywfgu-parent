import org.junit.Test;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/3/2
 */
public class ClassTest {
    interface IEat{
        public void eat(String thing);
    }

    interface IRun {
        public void  run() ;
    }

    interface ISay{
        public String say(String thing);
    }

    interface ISing{
        public void sing(final String thing);
    }

    interface IDog extends IEat,IRun{

    }

    class Cat implements IEat{
        public void eat(String thing) {
            System.out.println("I am eating "+thing);
        }
    }

    class Dog implements IDog,ISay{
        public void  run() {
            System.out.println("I am running...");
        }
        public void eat(String thing) {
            System.out.println("I am eating "+thing);
        }
        public String say(String thing) {
            System.out.println("I am saying "+thing);
            return thing;
        }
    }
    @Test
    public void testClass(){
        //实现接口
        Cat c=new Cat();
        c.eat("food");

        //匿名内部类
        IEat c1=new IEat() {
            public void eat(String thing) {
                System.out.println("I am eating "+thing);
            }
        };
        c1.eat("fish");

		/*
		 * lambda表达式
		 * 格式:(参数)->{语句};
		 * */
        //不带参数,无返回值
        IRun c2=()->System.out.println("run");
        c2.run();

        //带参数,无返回值
        IEat c3=(thing)->System.out.println("eat "+thing);
        c3.eat("apple");

        //带参数,有返回值
        ISay c4=(thing)->{
            System.out.println("say "+thing);
            return thing;
        };
        c4.say("good night");

        //参数中有final修饰时
        ISing c5=(thing)->System.out.println("sing "+thing);
        c5.sing("小苹果");

    }

}
