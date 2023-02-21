package skrifer.github.com.designpattern.单来源调用;

import lombok.Getter;

public class Factory {

    @Getter
    private boolean isAllowed;

    public Product createProdcut(String name) {
        isAllowed = true;
        return new Product(this, name);
    }

}
