package skrifer.github.com.lambda.函数;

import java.util.function.Consumer;

/**
 * https://zhuanlan.zhihu.com/p/531651771
 * 主要用来消费，并不需要返回任何信息
 */
public class Consumer_ {
    static Consumer<String> consumer = (str) -> System.out.println(str);
    static Consumer<String> consumer2 = (str) -> System.out.println(str.toUpperCase());

    static {
        Consumer<String> stringConsumer = consumer.andThen(consumer2);
        stringConsumer.accept("abc");
    }

    public static void main(String[] args) {

    }
}
