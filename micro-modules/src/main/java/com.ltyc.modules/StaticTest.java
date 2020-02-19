package com.ltyc.modules;

import java.util.Random;

/**
 * @author ltyc
 * @version 1.0
 * @Description staticTest
 * @create 18.2.6
 *
 *  * 在创建Test6的一个实例时：即Test6 test6=new Test6();
 * 调用的顺序依次是：静态代码块、非静态代码块、构造方法。并且静态代码块只运行依次。
 * 每创建一个Test6实例，非静态代码块、构造方法都会执行。这与静态代码块只执行一次是不同的。
 */
public class StaticTest {
    static{
        System.out.println("Test6 static block");
    }

    {
        System.out.println("Test6 non-static block");
    }

    public StaticTest() {
        System.out.println("Test6 constructor with no arguments");
    }
    public StaticTest(String id){
        System.out.println("Test6 constructor with one argument id"+ id);
    }
    public static int book=1;
    public final int a =11;
    public static  void sixi(){
        System.out.println("static method");
    }

    public static void main(String[] args) {
//    new StaticTest();
//    System.out.println("*****************");
//    new StaticTest("1");

//    System.out.println(StaticTest.book);//调用静态变量时，只有静态代码块会执行。
//        StaticTest.sixi();//在调用静态方法时，只有静态代码块会执行。
    //Test6.class.getClassLoader().loadClass(Test6.class.getName());//在Test6下面的main方法写这个语句，静态代码块会执行，
    //但是不在Test6这个类下面，这句话就不能让静态代码块执行。
    //Class.forName(Test6.class.getName());//加载Test6时，只有静态代码块会执行。

        for (int i = 0; i < 10; i++) {
            System.out.println(new Random().nextInt(5));
        }


    }

}
