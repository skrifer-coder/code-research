package skrifer.github.com.designpattern.strategy;

/**
 * 策略模式（Strategy Pattern）是一种比较简单的模式，也叫做政策模式（Policy Pattern）。
 * 其定义如下： Define a family of algorithms,encapsulate each one,and make them interchangeable.
 * （定义一组 算法，将每个算法都封装起来，并且使它们之间可以互换。）
 * <p>
 * 优点
 * 算法可以自由切换
 * 避免使用多重条件判断
 * 扩展性良好
 * <p>
 * 缺点
 * 策略类数量增多
 * 所有的策略类都需要对外暴露
 */
public class Strategy_ {

    public interface Strategy {
        //策略模式的运算法则
        public void doSomething();
    }

    public class ConcreteStrategy1 implements Strategy {
        public void doSomething() {
            System.out.println("具体策略1的运算法则");
        }
    }

    public class ConcreteStrategy2 implements Strategy {
        public void doSomething() {
            System.out.println("具体策略2的运算法则");
        }
    }

    public class Context {
        //抽象策略
        private Strategy strategy = null;

        //构造函数设置具体策略
        public Context(Strategy _strategy) {
            this.strategy = _strategy;
        }//封装后的策略方法

        public void doAnythinig() {
            this.strategy.doSomething();
        }
    }

//    public void main(String[] args) {
//        //声明一个具体的策略
//        Strategy strategy = new ConcreteStrategy1();
//        //声明上下文对象
//        Context context = new Context(strategy);
//        //执行封装后的方法
//        context.doAnythinig();
//    }


    //----------------------------枚举策略-----------------------------

    public static enum Calculator {

        ADD("+") {
            public int exec(int a, int b) {
                return a + b;
            }
        },
        SUB("-") {
            public int exec(int a, int b) {
                return a - b;
            }
        };

        String value = "";

        //定义成员值类型
        private Calculator(String _value) {
            this.value = _value;
        }

        //获得枚举成员的值
        public String getValue() {
            return this.value;
        }//声明一个抽象函数

        public abstract int exec(int a, int b);
    }


}
