package skrifer.github.com.designpattern.dynamicproxy;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;

public class DynamicProxy_CGLIB {
    //cglib代理(基于继承)
    //CGLIB包的底层是通过使用一个小而快的字节码处理框架ASM，来转换字节码并生成新的类

    public static void main(String[] args) {
        // 代理类class文件存入本地磁盘方便我们反编译查看源码
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/Users/junshen/Desktop/WorkSpace/Java/code-research/designpattern/src/main/java/skrifer/github/com/designpattern/dynamicproxy");
        // 通过CGLIB动态代理获取代理对象的过程
        Enhancer enhancer = new Enhancer();
        // 设置enhancer对象的父类
        enhancer.setSuperclass(HelloService.class);
        // 设置enhancer的回调对象
        enhancer.setCallback(new MyMethodInterceptor());
        // 创建代理对象
        HelloService proxy= (HelloService)enhancer.create();
        // 通过代理对象调用目标方法
        proxy.sayHello();
    }

    //生成的代理类通过设置的callbacks变量来操作intercept方法,此方法有自定义的拦截器实现，最终实现代理对象访问实际对象。

}
