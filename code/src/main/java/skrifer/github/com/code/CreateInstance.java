package skrifer.github.com.code;

import java.lang.reflect.Constructor;

/**
 * 新建对象的几种方式
 */
public class CreateInstance {

    public static void main(String[] args) throws Exception {
        //1
        new CreateInstance();

        //2 inside java.lang  只能反射无参构造，并且要求无参构造可视化 异常均由构造器抛出
        Class.forName("skrifer.github.com.code.CreateInstance").newInstance();
        CreateInstance.class.newInstance();

        //3 inside java.lang.reflect 可以调用所有构造，包含私有化构造(开源框架首选) 异常封装成InvocationTargetException抛出
        Constructor constructor = CreateInstance.class.getConstructor();
        constructor.newInstance();

        //4 克隆，调用Object protected clone 必须实现 Cloneable接口(仅做标记，clone native c++源码中会对当前类进行检查，没有实现会报错)
        // 并且要覆写clone()方法 访问权限要提升至public（不然不同包下会有访问权限的问题）
        // clone 并不会调用构造函数（克隆时，根据源对象类型先分配和源对象相同的内存，然后将源对象中的各个域中数据拷贝过来，最后返回对象地址。new时，第1步也是先分配内存，然后调用构造方法初始化数据，最后将对象地址返回，外界就可以通过这个对象地址(引用)操作此对象）
        new CreateInstance().clone();

        //5 序列化反序列化

    }
}
