package skrifer.github.com.designpattern.singleton;

/**
 * 构建者模式
 * 将一个复杂对象的构建与它的表示分 离，使得同样的构建过程可以创建不同的表示。
 * 与工厂模式相比 主要体现一个执行顺序的调整支持(工厂模式不关注产品功能的执行顺序)
 */
public class Create_ {

    public class Product {
        public void doSomething() { //独立业务处理
        }
    }

    public abstract class Builder {
        //设置产品的不同部分，以获得不同的产品
        public abstract void setPart();

        // 建造产品
        public abstract Product buildProduct();
    }

    public class ConcreteProduct extends Builder {
        private Product product = new Product(); //设置产品零件

        public void setPart() {
            /** 产品类内的逻辑处理 e.g调整执行顺序等等,... */
        }

        //组建一个产品
        public Product buildProduct() {
            return product;
        }
    }

    public class Director {
        private Builder builder = new ConcreteProduct();

        //构建不同的产品
        public Product getAProduct() {
            builder.setPart();
            /** 设置不同的零件，产生不同的产品 */
            return builder.buildProduct();
        }
    }
}