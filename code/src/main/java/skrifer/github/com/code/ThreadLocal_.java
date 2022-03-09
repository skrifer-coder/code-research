package skrifer.github.com.code;

/**
 * ThreadLocalMap类定义在ThreadLocal的内部
 * ThreadLocalMap变量维护在Thread类中.
 * threadLocal set get 操作本质就是操作ThreadLocalMap对象, key是 threadLocal本身,value 是需要使用的数据
 * threadLocal 使用时需要避开线程池的影响，工程内部创建的线程池会复用线程线程，一旦之前业务set过后 没有remove，后面的业务使用到之前的线程时，通过get可以获取到之前的数据
 * tomcat http线程池 也会有一样的问题，http request线程也会存在复用情况，threadLocal好习惯用完了要remove掉数据
 */
public class ThreadLocal_ {
    Thread thread = Thread.currentThread();
    ThreadLocal threadLocal = new ThreadLocal();

    public static void main(String[] args) {
        new ThreadLocal_().threadLocal.set("");
    }
}
