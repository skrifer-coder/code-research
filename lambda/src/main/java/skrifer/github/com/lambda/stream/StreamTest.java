package skrifer.github.com.lambda.stream;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * https://www.cnblogs.com/quanxiaoha/p/10767776.html
 */
public class StreamTest {
    static List<String> myList =
            Arrays.asList("a1", "a2", "b1", "c2", "c1");

    static {
        myList
                .stream() // 创建流
                .filter(s -> s.startsWith("c")) // 执行过滤，过滤出以 c 为前缀的字符串
                .map(String::toUpperCase) // 转换成大写
                .sorted() // 排序
                .forEach(System.out::println); // for 循环打印
    }

    /**
     * 中间操作会再次返回一个流，所以，我们可以链接多个中间操作，注意这里是不用加分号的。上面中filter 过滤，map 对象转换，sorted 排序，就属于中间操作
     * 终端操作是对流操作的一个结束动作，一般返回 void 或者一个非流的结果。上面的 forEach循环 就是一个终止操作, collect也是终端操作
     * peek可以模拟循环中间操作
     * 延迟性:当终端操作不存在时，中间操作也不会被执行
     * 链式垂直性:中间操作 与 终端操作 是垂直式执行（sort是水平执行的，因为需要全量排序），并不会等全部中间操作结束后再执行终端操作(好处是可以节约性能，提高效率，e.g anymatch等终端操作只需要匹配到任意一个就可以中止操作了）
     * Stream 排队执行每个月元素的所有流程，执行完一个元素后再执行下一个元素。
     * parallelStream仍然能保持同一个元素内的垂直式执行顺序，只是多个元素之间的顺序会被打乱，e.g第一个元素执行了两步后 其他元素也开始执行了，并且最后一步的执行也有可能比第一个元素早。
     * sorted操作 会使所有流在此节点同步进度 后续步骤仍然会按照流的各自特性的顺序执行[stream 仍然是一条条执行，parallel仍然是多条之间的乱序]
     * 因为sorted放在filter之后会大大提高性能,当流中只有一个元素时 sorted节点并不会被执行!
     */

    static {
        Arrays.asList("a1", "a2", "a3")
                .stream() // 创建流
                .findFirst() // 找到第一个元素
                .ifPresent(System.out::println);  // 如果存在，即输出

        Stream.of("a1", "a2", "a3")
                .findFirst()
                .ifPresent(System.out::println);  // a1


        //除了常规对象流之外，Java 8还附带了一些特殊类型的流，用于处理原始数据类型int，long以及double。说道这里，你可能已经猜到了它们就是IntStream，LongStream还有DoubleStream
        //支出额外的终端聚合操作:sum() average()
        //常规对象流需要这些特殊操作 需要通过 mapToInt mapToLong mapToDouble 转换流里的对象类型，反过来通过mapToObj转换成常规对象流
        IntStream.range(1, 4)
                .forEach(System.out::println); // 相当于 for (int i = 1; i < 4; i++) {}
    }


    static {
        //终端操作只能执行一次
        Stream<String> stream =
                Stream.of("d2", "a2", "b1", "b3", "c")
                        .filter(s -> s.startsWith("a"));

        stream.anyMatch(s -> true);    // ok
//        stream.noneMatch(s -> true);   // exception

        /**
         * java.lang.IllegalStateException: stream has already been operated upon or closed
         *     at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:229)
         *     at java.util.stream.ReferencePipeline.noneMatch(ReferencePipeline.java:459)
         *     at com.winterbe.java8.Streams5.test7(Streams5.java:38)
         *     at com.winterbe.java8.Streams5.main(Streams5.java:28)
         */

        //通过构建多个相同的流来实现多次执行终端调用

        Supplier<Stream<String>> streamSupplier =
                () -> Stream.of("d2", "a2", "b1", "b3", "c")
                        .filter(s -> s.startsWith("a"));

        streamSupplier.get().anyMatch(s -> true);   // ok
        streamSupplier.get().noneMatch(s -> true);  // ok

    }

    static {
        /**
         * collect 是一个非常有用的终端操作，它可以将流中的元素转变成另外一个不同的对象，
         * 例如一个List，Set或Map。collect 接受入参为Collector（收集器），它由四个不同的操作组成：供应器（supplier）、累加器（accumulator）、组合器（combiner）和终止器（finisher）。
         */

        class Person {
            String name;
            int age;

            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }

            @Override
            public String toString() {
                return name;
            }
        }

// 构建一个 Person 集合
        List<Person> persons =
                Arrays.asList(
                        new Person("Max", 18),
                        new Person("Peter", 23),
                        new Person("Pamela", 23),
                        new Person("David", 12),
                        new Person("David", 15));


        List<Person> filtered =
                persons
                        .stream() // 构建流
                        .filter(p -> p.name.startsWith("P")) // 过滤出名字以 P 开头的
                        .collect(Collectors.toList()); // 生成一个新的 List（或Collectors.toSet()/Collectors.toMap()）

        System.out.println(filtered);    // [Peter, Pamela]


        Map<Integer, List<Person>> personsByAge = persons
                .stream()
                .collect(Collectors.groupingBy(p -> p.age)); // 以年龄为 key,进行分组

        personsByAge
                .forEach((age, p) -> System.out.format("age %s: %s\n", age, p));

        // age 18: [Max]
        // age 23: [Peter, Pamela]
        // age 12: [David]

        Double averageAge = persons
                .stream()
                .collect(Collectors.averagingInt(p -> p.age)); // 聚合出平均年龄

        System.out.println(averageAge);     // 19.0


        IntSummaryStatistics ageSummary =
                persons
                        .stream()
                        .collect(Collectors.summarizingInt(p -> p.age)); // 生成摘要统计

        System.out.println(ageSummary);
// IntSummaryStatistics{count=4, sum=76, min=12, average=19.000000, max=23}


        String phrase = persons
                .stream()
                .filter(p -> p.age >= 18) // 过滤出年龄大于等于18的
                .map(p -> p.name) // 提取名字
                .collect(Collectors.joining(" and ", "In Germany ", " are of legal age.")); // 以 In Germany 开头，and 连接各元素，再以 are of legal age. 结束

        System.out.println(phrase);
// In Germany Max and Peter and Pamela are of legal age.


        Map<Integer, String> map = persons
                .stream()
                .collect(Collectors.toMap(
                        p -> p.age,
                        p -> p.name,
                        (name1, name2) -> name1 + ";" + name2)); // 对于同样 key 的，将值拼接

        System.out.println(map);
// {18=Max, 23=Peter;Pamela, 12=David}


        Map<String, ArrayList<Object>> collect = persons.stream().collect(Collectors.toMap(p -> p.name, e -> {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(e);
            return objects;
        }, (e1, e2) -> {
            e1.addAll(e2);
            return e1;
        }));

        System.out.println(collect);

        //自定义收集器(Collector)

        Collector<Person, StringJoiner, String> personNameCollector =
                Collector.of(
                        () -> new StringJoiner(" | "),          // supplier 供应器
                        (j, p) -> j.add(p.name.toUpperCase()),  // accumulator 累加器
                        (j1, j2) -> j1.merge(j2),               // combiner 组合器
                        StringJoiner::toString);                // finisher 终止器


        String names = persons
                .stream()
                .collect(personNameCollector); // 传入自定义的收集器

        System.out.println(names);  // MAX | PETER | PAMELA | DAVID


        //Reduce 规约操作可以将流的所有元素组合成一个结果。Java 8 支持三种不同的reduce方法。第一种将流中的元素规约成流中的一个元素。

        //筛选出年龄最大的那个人
        /**
         * reduce方法接受BinaryOperator积累函数。该函数实际上是两个操作数类型相同的BiFunction。
         * BiFunction功能和Function一样，但是它接受两个参数。示例代码中，我们比较两个人的年龄，来返回年龄较大的人。
         */
        persons
                .stream()
                .reduce((p1, p2) -> p1.age > p2.age ? p1 : p2)
                .ifPresent(System.out::println);    // Pamela


        //第二种reduce方法接受标识值和BinaryOperator累加器。此方法可用于构造一个新的 Person，其中包含来自流中所有其他人的聚合名称和年龄：
        Person result =
                persons
                        .stream()
                        .reduce(new Person("", 0), (p1, p2) -> {
                            p1.age += p2.age;
                            p1.name += p2.name;
                            return p1;
                        });

        System.out.format("name=%s; age=%s", result.name, result.age);
// name=MaxPeterPamelaDavid; age=76


        //第三种reduce方法接受三个参数：标识值，BiFunction累加器和类型的组合器函数BinaryOperator。
        // 由于初始值的类型不一定为Person，我们可以使用这个归约函数来计算所有人的年龄总和
        Integer ageSum = persons
                .stream()
                .reduce(0, (sum, p) -> sum += p.age, (sum1, sum2) -> sum1 + sum2);

        System.out.println(ageSum);  // 76


        BinaryOperator<BigDecimal> accumulator = (x, y) -> x;

    }

    static {
        /**
         * FlatMap 能够将流的每个元素, 转换为其他对象的流。因此，每个对象可以被转换为零个，一个或多个其他对象，并以流的方式返回。之后，这些流的内容会被放入flatMap返回的流中
         */

        class Bar {
            String name;

            Bar(String name) {
                this.name = name;
            }
        }

        class Foo {
            String name;
            List<Bar> bars = new ArrayList<>();

            Foo(String name) {
                this.name = name;
            }
        }


        List<Foo> foos = new ArrayList<>();

// 创建 foos 集合
        IntStream
                .range(1, 4)
                .forEach(i -> foos.add(new Foo("Foo" + i)));

// 创建 bars 集合
        foos.forEach(f ->
                IntStream
                        .range(1, 4)
                        .forEach(i -> f.bars.add(new Bar("Bar" + i + " <- " + f.name))));

        foos.stream().flatMap(f -> f.bars.stream()).forEach(b -> System.out.println(b.name));


    }

    static {

        class Inner {
            String foo;
        }

        class Nested {
            Inner inner;
        }

        class Outer {
            Nested nested;
        }


        Optional.of(new Outer())
                .flatMap(o -> Optional.ofNullable(o.nested))
                .flatMap(n -> Optional.ofNullable(n.inner))
                .flatMap(i -> Optional.ofNullable(i.foo))
                .ifPresent(System.out::println);

    }


    static {
        //流是可以并行执行的，当流中存在大量元素时，可以显著提升性能。并行流底层使用的ForkJoinPool,
        // 它由ForkJoinPool.commonPool()方法提供。底层线程池的大小最多为五个 - 具体取决于 CPU 可用核心数

        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        System.out.println(commonPool.getParallelism());    // 3

        //-Djava.util.concurrent.ForkJoinPool.common.parallelism=5  可以修改默认


    }


    public static void main(String[] args) {
        Arrays.asList("a1", "a2", "b1", "c2", "c1")
                .parallelStream()
//                .stream()
                .filter(s -> {
                    System.out.format("filter: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return true;
                })
                .map(s -> {
                    System.out.format("map: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return s.toUpperCase();
                })
                .sorted((s1, s2) -> {
                    System.out.format("sort: %s <> %s [%s]\n",
                            s1, s2, Thread.currentThread().getName());
                    return s1.compareTo(s2);
                })//可以使整个流在此同步进度

                .forEach(s -> System.out.format("forEach: %s [%s]\n",
                        s, Thread.currentThread().getName()));
    }

}
