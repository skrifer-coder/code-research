package skrifer.github.com.designpattern.singleton;

public class Singleton_ {
    // 饿汉模式-----------------------------------------------------(首选方式)
    private static final Singleton_ singleton = new Singleton_();

    //限制产生多个对象
    private Singleton_() {
    }

    //通过该方法获得实例对象
    public static Singleton_ getSingleton() {
        return singleton;
    }
    //类中其他方法， 尽量是static

    public static void doSomething() {

    }

    // 懒汉模式 ---------------------------------------------------

    private static Singleton_ singleton2 = null;

    //通过该方法获得实例对象
    public static Singleton_ getSingleton2() {
        synchronized (Singleton_.class) {
            if (singleton2 == null) {
                singleton2 = new Singleton_();
            }
        }
        return singleton2;
    }

}
