package skrifer.github.com.designpattern.dynamicproxy;


import skrifer.github.com.base.utils.FileUtil;
import sun.misc.ProxyGenerator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy_JDK {

    public interface ITarget {
        void say();
    }

    public static class Target implements ITarget {
        private String name;

        public Target(String name) {
            this.name = name;
        }

        public void say() {
            System.out.println(this.name + "lalala");
        }
    }

    //jdk代理(基于接口)

    /**
     * 因为代理对象(被编译后的源码显示)已经继承了Proxy类，而Java又是基于单继承的，所以jdk动态代理只能基于接口来实现
     * 代理对象需要继承Proxy的原因是,InvocationHandler的变量 定义在Proxy类中,代理对象需要调用InvocationHandler的 invoke方法来执行被代理对象的目标方法
     */

    public static class MyInvocationHandler implements InvocationHandler {

        Object source;

        public MyInvocationHandler(Object source) {
            this.source = source;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(this.source, args);
        }
    }

    public static class DynamicProxy<T> {
        /**
         * @param classLoader 类加载器
         * @param classes     该类的所有接口
         * @param handler     实现所有的方法，由其invoke方法接管所有方法的实现
         * @param <T>
         * @return
         */
        public static <T> T newProxyInstance(ClassLoader classLoader, Class<?>[] classes, InvocationHandler handler) {
            return (T) Proxy.newProxyInstance(classLoader, classes, handler);
        }


        public static <T> T newProxyInstance(Object subject) {
            //获得ClassLoader
            ClassLoader loader = subject.getClass().getClassLoader();
            //获得接口数组
            Class<?>[] classes = subject.getClass().getInterfaces();
            //获得handler
            InvocationHandler handler = new MyInvocationHandler(subject);
            return newProxyInstance(loader, classes, handler);
        }
    }

//    public static class Client {
//        static {
//            Target target = new Target("喔");
//            MyInvocationHandler invocationHandler = new MyInvocationHandler(target);
//            Target targetProxy = new DynamicProxy<Target>().newProxyInstance(target.getClass().getClassLoader(), target.getClass().getClasses(), invocationHandler);
//            targetProxy.say();
//        }
//
//    }

    public static void main(String[] args) {
        ITarget target = new Target("喔");

        System.out.println(target.getClass().getInterfaces()[0]);
        MyInvocationHandler invocationHandler = new MyInvocationHandler(target);
        //.getInterfaces() 它能够获得这个对象所实现的所有接口(是接口类不是方法)
        ITarget targetProxy = new DynamicProxy().newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), invocationHandler);
        targetProxy.say();

        //通过ProxyGenerator拿到代理对象的字节码，通过流生成代理对象的class文件
//        byte[] generateProxyClass = ProxyGenerator.generateProxyClass("target", new Class[]{Target.class});
//        FileUtil.getFile(generateProxyClass, "/Users/junshen/Desktop/upload/", "target.class");

    }


}
