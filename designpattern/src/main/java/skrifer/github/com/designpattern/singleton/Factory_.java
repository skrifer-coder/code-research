package skrifer.github.com.designpattern.singleton;

/**
 * 工厂模式有时候需要结合单例模式来保证产品的唯一性
 */
public class Factory_ {


    //普通工厂模式(工厂与产品一对一,由工厂对象来决定具体产出产品)----------------------------------------------------------


    public abstract class Product { //产品类的公共方法 public void method1(){ //业务逻辑处理 }//抽象方法 public abstract void method2(); }

    }

    public class ConcreteProduct1 extends Product {
        public void method2() { //业务逻辑处理 }
        }
    }

    public class ConcreteProduct2 extends Product {
        public void method2() { //业务逻辑处理
        }
    }


    public abstract class Creator {
        /**
         * 创建一个产品对象，其输入参数类型可以自行设置 * 通常为String、Enum、Class等，当然也可以为空
         */
        public abstract Product createProduct();
    }

    public class ConcreteCreator1 extends Creator {
        @Override
        public Product createProduct() {
            return new ConcreteProduct1();
        }
    }

    public class ConcreteCreator2 extends Creator {
        @Override
        public Product createProduct() {
            return new ConcreteProduct2();
        }
    }


    //简单工厂模式（一个工厂对应多个产品,根据入参来决定生产的产品）----------------------------------------------------------
    //相对于标准工厂模式取消了对工厂的模拟化抽象，好处就是简单明了，缺点就是损失了以后对工厂的扩展，不符合开闭原则

    public class ProductFactory {

        //此处用static 修饰(jdk8内部类不允许所以未写)
        public <T extends Product> T createProduct(Class<T> c) {
            T t = null;
            try {
                t = c.getConstructor().newInstance();
            } catch (Exception e) {

            }
            return t;
        }
    }

    //抽象工厂模式----------------------------------------------------------
    //抽象工厂与普通工厂最大的区别是将工厂生产的产品多了多维度(普通工厂/简单工厂都是一种产品)，抽象工厂可以产生多种产品
    //当抽象工厂的产品类型只有一种时，即为普通工厂模式
    //抽象工厂在新增工厂时，无需对现有工厂和产品做改动，只需增加一个工厂及对应的产品即可(新增的工厂、产品均保持与现有一样的继承与实现)
    //抽象工厂在新增产品时，需要改动现有工厂的生产方法，这一点无法避免

    //产品族----------------------------------------------------------

    //鼠标产品
    public abstract class Mouse {

    }

    //键盘产品
    public abstract class Keyboard {

    }

    //罗技鼠标
    public class LogitechMouse extends Mouse {

    }

    //惠普鼠标
    public class HPMouse extends Mouse {

    }

    //罗技键盘
    public class LogitechKeyboard extends Keyboard {

    }

    //惠普键盘
    public class HPKeyboard extends Keyboard {

    }

    //工厂族----------------------------------------------------------
    //抽象工厂
    public abstract class Factory {

        //生产鼠标
        public abstract Mouse makeMouse();

        //生产键盘
        public abstract Keyboard makeKeyboard();
    }

    //罗技工厂
    public class LogitechFactory extends Factory {
        @Override
        public Mouse makeMouse() {
            return new LogitechMouse();
        }

        @Override
        public Keyboard makeKeyboard() {
            return new LogitechKeyboard();
        }
    }

    //惠普工厂
    public class HPFactory extends Factory {
        @Override
        public Mouse makeMouse() {
            return new HPMouse();
        }

        @Override
        public Keyboard makeKeyboard() {
            return new HPKeyboard();
        }
    }

    //新增一个工厂(双飞燕)
    //步骤1 新增工厂对现有产品的定义支持
    public class DoubleSwallowMouse extends Mouse {

    }

    public class DoubleSwallowKeyboard extends Keyboard {

    }

    //步骤2 新增工厂

    public class DoubleSwallowFactory extends Factory {
        @Override
        public Mouse makeMouse() {
            return new DoubleSwallowMouse();
        }

        @Override
        public Keyboard makeKeyboard() {
            return new DoubleSwallowKeyboard();
        }
    }

    //新增一个产品
    //步骤1 新增新产品定义
    public abstract class MIC {

    }

    //步骤2 新增已有工厂对产品的支持
    //罗技麦克风
    public class LogitechMIC extends MIC {

    }

    //惠普麦克风
    public class HPMIC extends MIC {

    }

    //步骤3 在已有工厂新增对新产品的生产支持
    //LogitechFactory 新增
    // public MIC makeMIC() {
    //            return new LogitechMIC();
    //        }
    //HPFactory 新增
    // public MIC makeMIC() {
    //            return new HPMIC();
    //        }

    //由上可见 抽象工厂模式 对新增工厂的扩展比较友好 符合开闭原则，而新增产品的修改则破坏了关闭原则的定义

    //如何挑选使用哪种模式
    //1.所有产品的生产方式基本一致，并且不关注生产过程，只关注产品本身的，选择简单工厂即可,产品相关的业务逻辑 放在产品类本身即可.
    //2.产品生产过程并非一成不变,并且个产品的生产流程层次不齐的，用普通或者 抽象生产模式
}
