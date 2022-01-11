package skrifer.github.com.lambda;

public class FunctionalTest {
    public static void main(String[] args) {
        Functional functional = param -> param * param;
        /**
         *  等同于声明了一个语句定义
         *  new Functional() {
         *             @Override
         *             public int apply(int a) {
         *                 return 0;
         *             }
         *         }
         */


        System.out.println(functional.apply(5));
    }
}
