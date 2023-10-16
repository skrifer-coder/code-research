package skrifer.github.com.lambda.函数;

import java.util.function.Predicate;

/**
 * https://zhuanlan.zhihu.com/p/531651771
 * 主要用来返回判断
 */
public class Predicate_ {

    static Predicate<Integer> predicate = (val) -> val == 6;

    static {
        System.out.println(predicate.test(5));
        System.out.println(predicate.negate().test(5));
        System.out.println(Predicate.isEqual(7).test(7));
    }

    public static void main(String[] args) {

    }


    /**
     * 具体过滤操作 需要被子类实现.
     * 用来处理参数T是否满足要求,可以理解为 条件A
     */
    //Predicate.test


    /**
     * 调用当前Predicate的test方法之后再去调用other的test方法,相当于进行两次判断
     * 可理解为 条件A && 条件B
     */
    /**
     * default Predicate<T> and(Predicate<? super T> other) {
     *         Objects.requireNonNull(other);
     *         return (t) -> test(t) && other.test(t);
     *     }
     */

    /**
     * 对当前判断进行"!"操作,即取非操作，可理解为 !条件A
     */
    /**
     * default Predicate<T> negate() {
     *         return (t) -> !test(t);
     *     }
     */

    /**
     * 对当前判断进行"||"操作,即取或操作,可以理解为 条件A ||条件B
     */
    /**
     * default Predicate<T> or(Predicate<? super T> other) {
     *         Objects.requireNonNull(other);
     *         return (t) -> test(t) || other.test(t);
     *     }
     */

    /**
     * 对当前操作进行"="操作,即取等操作,可以理解为 A == B
     */
    /**
     * static <T> Predicate<T> isEqual(Object targetRef) {
     *         return (null == targetRef)
     *                 ? Objects::isNull
     *                 : object -> targetRef.equals(object);
     *     }
     */
}
