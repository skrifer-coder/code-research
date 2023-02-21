package skrifer.github.com.designpattern.adapter;

/**
 * 适配器模式（Adapter Pattern）的定义如下：
 * Convert the interface of a class into another interface clients expect.Adapter lets classes work together that couldn't otherwise because of incompatible interfaces.
 * （将一个类的接口变换成客户 端所期待的另一种接口，从而使原本因接口不匹配而无法在一起工作的两个类能够在一起工 作。）
 * <p>
 * ● Target目标角色 该角色定义把其他类转换为何种接口，也就是我们的期望接口，例子中的IUserInfo接口 就是目标角色。
 * ● Adaptee源角色 你想把谁转换成目标角色，这个“谁”就是源角色，它是已经存在的、运行良好的类或对 象，经过适配器角色的包装，它会成为一个崭新、靓丽的角色。
 * ● Adapter适配器角色 适配器模式的核心角色，其他两个角色都是已经存在的角色，而适配器角色是需要新建 立的，它的职责非常简单：把源角色转换为目标角色，怎么转换？通过继承或是类关联的方 式。
 *
 * 实现要素：
 * 1.自己的开发时要遵守依赖倒转和里氏替换原则
 * 2.装饰器继承需要适配的源（类适配器），实现自己的业务接口
 *
 * 如果需要被装饰的源有多个,则不能适用类适配器(java不支持类的多继承)
 * 此时需要通过适配器的内聚关联多个需要适配的源(也就是上述实现要素第2点，不需要继承某一个需要适配的源，需要把多个适配源定义成内部属性关联)
 *
 *
 */
public class Adapter_ {

    public interface Target {
        //目标角色有自己的方法
        public void request();
    }

    //需要被对接业务
    public class ConcreteTarget implements Target {
        public void request() {
            System.out.println("if you need any help,pls call me!");
        }
    }

    //需要被包装的业务
    public class Adaptee {
        //原有的业务逻辑
        public void doSomething() {
            System.out.println("I'm kind of busy,leave me alone,pls!");
        }
    }

    //适配器
    public class Adapter extends Adaptee implements Target {
        public void request() {
            super.doSomething();
        }
    }

    public class Client {
        public void main(String[] args) {
            //原有的业务逻辑
            Target target = new ConcreteTarget();
            target.request(); //现在增加了适配器角色后的业务逻辑
            Target target2 = new Adapter();
            target2.request();
        }
    }


}
