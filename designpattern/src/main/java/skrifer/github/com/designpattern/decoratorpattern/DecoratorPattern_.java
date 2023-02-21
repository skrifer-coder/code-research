package skrifer.github.com.designpattern.decoratorpattern;

/**
 * 装饰模式（Decorator Pattern）是一种比较常见的模式，
 * 其定义如下：Attach additional responsibilities to an object dynamically keeping the same interface.Decorators provide a flexible alternative to subclassing for extending functionality.
 * （动态地给一个对象添加一些额外的职责。 就增加功能来说，装饰模式相比生成子类更为灵活。）
 * <p>
 * 装饰器模式与代理模式的相似及区别:
 * 装饰器:要求装饰对象和被装饰对象实现同一个接口,装饰对象持有被装饰对象的实例（通过构造注入）
 * 代理器:一般需要代理对象和被代理对象也要实现同一个接口(保证代理对象最大程度减少对程度的影响),代理对象在内部直接实例化被代理对象.
 * 代理模式的耦合性 比装饰器更高一些
 */
public class DecoratorPattern_ {

    public abstract class Component {
        //抽象的方法
        public abstract void operate();
    }

    //实际构件
    public class ConcreteComponent extends Component {
        //具体实现
        @Override
        public void operate() {
            System.out.println("do Something");
        }
    }

    //抽象装饰器
    public abstract class Decorator extends Component {
        private Component component = null;

        //通过构造函数传递被修饰者
        public Decorator(Component _component) {
            this.component = _component;
        }//委托给被修饰者执行

        @Override
        public void operate() {
            this.component.operate();
        }
    }

    //具体装饰器1
    public class ConcreteDecorator1 extends Decorator {
        //定义被修饰者
        public ConcreteDecorator1(Component _component) {
            super(_component);
        }//定义自己的修饰方法

        private void method1() {
            System.out.println("method1 修饰");
        }

        //重写父类的Operation方法
        public void operate() {
            this.method1();
            super.operate();
        }
    }

    //具体装饰器2
    public class ConcreteDecorator2 extends Decorator {
        //定义被修饰者
        public ConcreteDecorator2(Component _component) {
            super(_component);
        }//定义自己的修饰方法

        private void method2() {
            System.out.println("method2修饰");
        }//重写父类的Operation方法

        public void operate() {
            super.operate();
            this.method2();
        }
    }

}
