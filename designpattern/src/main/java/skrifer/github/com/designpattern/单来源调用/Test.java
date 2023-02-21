package skrifer.github.com.designpattern.单来源调用;

public class Test {

    public static void main(String[] args) {

        Factory factory = new Factory();
        Product p1 = factory.createProdcut("标准产品");
        Product p2 = new Product(new Factory(), "山寨产品");

        System.out.println(p1.getName() + "---" + p1.isValild());
        System.out.println(p2.getName() + "----" + p2.isValild());


        //必须由指定方式生成的对象才是符合规范的，单例模式，简单工厂模式
    }
}
