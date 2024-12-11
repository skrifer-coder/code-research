package skrifer.github.com.base.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListUtil {

    /**
     * @param list
     * @param indicesToRemove 要删除的索引列表
     * @param <T>
     * @return
     */
    public static <T> void deleteElementByIndexList(List<T> list, List<Integer> indicesToRemove) {

        List<T> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!indicesToRemove.contains(i)) {
                newList.add(list.get(i));
            }
        }
        list.clear();
        list.addAll(newList);

    }

    /**
     * 获取返回list集合的重复率最高的【limitElements】个元素(倒序从高到低)
     *
     * @param list
     * @param limitElements
     * @param <E>           如果是自定义对象 需要重写 equals 和 hashcode
     * @return List<Map.Entry < E, Integer>> 为了保持顺序外层包装list
     */
    public static <E> List<Map.Entry<E, Integer>> getTopDuplicateElements(List<E> list, int limitElements) {
        Map<E, Integer> collect = list.stream()                              // list 对应的 Stream
                .collect(Collectors.toMap(e -> e, e -> 1, Integer::sum));// 获得元素出现频率的 Map，键为元素，值为元素出现的次数

        List<Map.Entry<E, Integer>> collect1 = collect.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue((o1, o2) -> o2.compareTo(o1)))
                .limit(limitElements)
                .collect(Collectors.toList());
        return collect1;
    }

    /**
     * 去除 list内满足predicate表达式的第一个元素之前的部分 和 满足predicate表达式最后一个元素 之后的部分
     *
     * @param list      如需要排序 则提前自己排序好 并且 必须支持clear操作！！！
     * @param predicate 条件删选器
     * @param <T>       e.g  list = [0, 0, 1, 0, 2, 3, 0, 5, 0, 0, 0]
     *                  predicate = (integer -> integer > 0)
     *                  return [1, 0, 2, 3, 0, 5]
     */
    public static <T> void deleteFrontEleAndRearEleByPredicate(List<T> list, Predicate<T> predicate) {
        int firstIndex = -1;
        int lastIndex = -1;

        for (int i = 0; i < list.size(); i++) {
            if (predicate.test(list.get(i))) {
                if (firstIndex == -1) {
                    firstIndex = i;
                }
                lastIndex = i;
            }
        }

        if (lastIndex != -1 && lastIndex < list.size() - 1) {
            list.subList(lastIndex + 1, list.size()).clear();
        }

        if (firstIndex > 0) {
            list.subList(0, firstIndex).clear();
        }
    }

    /**
     * deleteFrontEleAndRearEleByPredicate 的 多并行list 增强版
     *
     * @param list      list 内各子list 的长度必须一致！！！！
     * @param predicate 条件删选器
     * @param <T>       e.g  list = [[0, 0, 1, 0, 5, 0, 0, 0],[0, 0, 0, 1, 0, 0, 9, 0]]
     *                  *   predicate = (integer -> integer > 0)
     *                  *   return [[1, 0, 5, 0, 0], [0, 1, 0, 0, 9]]
     */
    public static <T> void deleteMultiListFrontEleAndRearEleByPredicate(List<List<T>> list, Predicate<T> predicate) {
        int firstIndex = -1;
        int lastIndex = -1;

        int size = list.get(0).size();

        for (int i = 0; i < size; i++) {
            final int index = i;
            if (list.stream().anyMatch(e -> predicate.test(e.get(index)))) {
                if (firstIndex == -1) {
                    firstIndex = i;
                }
                lastIndex = i;
            }
        }

        for (List<T> ts : list) {
            if (lastIndex != -1 && lastIndex < ts.size() - 1) {
                ts.subList(lastIndex + 1, ts.size()).clear();
            }

            if (firstIndex > 0) {
                ts.subList(0, firstIndex).clear();
            }
        }

    }

    /**
     * 输出 source1 专有的元素 和 source2 专有的元素 及 source1 和 source2 共有的元素
     *
     * @param source1
     * @param source2
     * @param <T>     如果是自定义类型 必须重写 hashcode equals
     */
    private static <T> List<List<T>> compareAndDiff(List<T> source1, List<T> source2) {
        List<List<T>> result = new ArrayList<>();
        List<T> source1Copy = new ArrayList<>(source1);
        source1Copy.removeAll(source2);
        result.add(source1Copy);

        List<T> source2Copy = new ArrayList<>(source2);
        source2Copy.removeAll(source1);
        result.add(source2Copy);

        source1.retainAll(source2);
        result.add(source1);
        return result;
    }

    public static void main(String[] args) {
//        List<String> list1 = new ArrayList<>();
//        list1.add("Apple");
//        list1.add("Banana");
//        list1.add("Cherry");
//
//        List<String> list2 = new ArrayList<>();
//        list2.add("Banana");
//        list2.add("Dragonfruit");
//        list2.add("Elderberry");
//
//        compareAndDiff(list1, list2);

//        List<Integer> list = new ArrayList<>(Arrays.asList(0, 0, 1, 0, 5, 0, 0, 0));
//        List<Integer> list2 = new ArrayList<>(Arrays.asList(0, 0, 0, 1, 0, 0, 9, 0));
//
//        List<List<Integer>> all = new ArrayList<>();
//        all.add(list);
//        all.add(list2);
//        deleteMultiListFrontEleAndRearEleByPredicate(all, integer -> integer > 0);

//        System.out.println(all);

//        deleteFrontEleAndRearEleByPredicate(list, integer -> integer > 0);
//        System.out.println(list);
    }

}
