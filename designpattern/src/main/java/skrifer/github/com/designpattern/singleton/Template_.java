package skrifer.github.com.designpattern.singleton;

/**
 * 模板模式
 * <p>
 * <p>
 * <p>
 * <p>
 * 优点
 * 封装不变部分，扩展可变部分
 * 提取公共部分代码，便于维护
 * 行为由父类控制，子类实现
 */
public class Template_ {

    /**
     * 定义模板
     */
    public abstract class Model {

        //基本方法（抽象模板中的基本方法尽量设计为protected类型，符合迪米特法则，不需要暴露 的属性或方法尽量不要设置为protected类型。实现类若非必要，尽量不要扩大父类中的访问 权限。）
        protected abstract void p1();

        protected abstract void p2();

        protected abstract void p3();

        //模板方法(一般对外对公服务，并且不允许被重写)
        public final void run() {
            p1();
            p2();
            p3();

            if (flag()) {
                System.out.println("111111");
            }
        }

        /**
         * 钩子方法(影响模板方法执行效果)
         * @return
         */
        protected boolean flag() {
            return true;
        }
    }


    public class Sub extends Model {
        @Override
        protected void p1() {
            System.out.println("p1");
        }

        @Override
        protected void p2() {
            System.out.println("p1");
        }

        @Override
        protected void p3() {
            System.out.println("p1");
        }

        @Override
        protected boolean flag() {
            return false;
        }
    }


}
