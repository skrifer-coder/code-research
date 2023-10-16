package skrifer.github.com.lambda;

/**
 * 递归
 */
public class Recursion {

    @FunctionalInterface
    interface Functional {
        int apply(int a);
    }

    @FunctionalInterface//标志它只有一个待实现方法(能用lambda表达式的前提)
    interface Func {
        int apply(Func self, int n);
    }

    public static void main(String[] args) {
        //1.
        Functional[] functionals = new Functional[1];
        functionals[0] = n -> n <= 0 ? 1 : n * functionals[0].apply(n - 1);
        System.out.println(functionals[0].apply(2));

        //2.
        Func func = ((self, n) -> n <= 0 ? 1 : (n * self.apply(self, n - 1)));
        System.out.println(func.apply(func, 5));

        //3.
        System.out.println(((Func) (self, n) -> n <= 0 ? 1 : n * self.apply(self, n - 1)).apply((self, n) -> n <= 0 ? 1 : n * self.apply(self, n - 1), 10));



    }
}
