package skrifer.github.com.lambda.函数;

import java.util.StringJoiner;
import java.util.function.Function;

/**
 * https://zhuanlan.zhihu.com/p/531651771
 * 主要用来 输入 input 输出 output
 */
public class Function_ {
    static Function<String, Integer> function1 = (str) -> {
        return Integer.parseInt(str);
    };


    static {
        System.out.println(function1.apply("111"));
    }



    public static void main(String[] args) {
    }
}
