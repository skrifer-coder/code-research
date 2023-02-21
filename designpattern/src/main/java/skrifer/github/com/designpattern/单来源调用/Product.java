package skrifer.github.com.designpattern.单来源调用;

import lombok.Getter;

public class Product {

    @Getter
    private String name;

    @Getter
    private boolean valild;

    public Product(Factory factory, String name) {
        if (factory.isAllowed()) {
            this.name = name;
            this.valild = true;
        }
    }
}
