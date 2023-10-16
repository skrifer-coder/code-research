package skrifer.github.com.code.接口;

public class 抽象接口实现类 {

    I抽象接口 inter = 抽象接口实现类::b;

    private static void b(int i) {
        System.out.println(i);
    }

    public static void main(String[] args) {
        new 抽象接口实现类().inter.a(2);
    }
}
