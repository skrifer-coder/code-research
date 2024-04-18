package skrifer.github.com.lambda.函数;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * https://zhuanlan.zhihu.com/p/531651771
 * 主要用来返回 数据仓库，不需要入参
 */
public class Suppiler_ {
    static Supplier<List<String>> supplier = () -> Arrays.asList("6666");

    static {
        supplier.get().forEach(System.out::println);
    }

    public static void main(String[] args) {
        Suppiler_ suppiler_ = new Suppiler_();
        suppiler_.test(suppiler_::get);
    }

    public String get() {
        return "";
    }

    public <R> R test(Supplier<R> supplier) {
        return supplier.get();
    }
}
