package skrifer.github.com.designpattern.protype;

import java.util.ArrayList;
import java.util.List;

/**
 * 原型模式
 * 不通过new关键字来产生一个对象，而是通过对象复制来实现的模式就叫做原型模式
 * Specify the kinds of objects to create using a prototypical instance,and create new objects by copying this prototype.
 * （用原型实例指定创建对象的种类，并且通过拷贝这些原型创建新的对 象。）
 * Object.clone()
 * 优点:原型模式是在内存二进制流的拷贝，要比直接new一个对象性能好很多.
 * 缺点:直接在内存中拷贝，构造函数是不会执行的。
 * <p>
 * 使用场景:
 * 1.类初始化需要消化非常多的资源，这个资源包括数据、硬件资源等
 * 2.通过new产生一个对象需要非常繁琐的数据准备或访问权限，则可以使用原型模式。
 * 3.一个对象需要提供给其他对象访问，而且各个调用者可能都需要修改其值时，可以考虑 使用原型模式拷贝多个对象供调用者使用。
 */
public class Protype_ implements Cloneable {

    @Override
    protected Protype_ clone() throws CloneNotSupportedException {
        Protype_ thing = null;
        try {
            thing = (Protype_) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return thing;
    }

    final List<String> list = new ArrayList<>();


    public static void main(String[] args) throws Exception {
        Protype_ p1 = new Protype_();
        p1.list.add("1");
        Protype_ p2 = p1.clone();
        p2.list.add("2");
        System.out.println(String.join(",", p1.list));
    }
}
