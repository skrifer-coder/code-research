package skrifer.github.com.thread;

import org.springframework.util.StopWatch;

import java.util.concurrent.*;
import java.util.function.Function;

/**
 * 在Java8中，CompletableFuture提供了非常强大的Future的扩展功能，可以帮助我们简化异步编程的复杂性，
 * 并且提供了函数式编程的能力，可以通过回调的方式处理计算结果，也提供了转换和组合 CompletableFuture 的方法。
 * <p>
 * 异步计算很难推理。通常，我们希望将任何计算都视为一系列步骤，但是在异步计算的情况下，以回调表示的动作往往分散在代码中或彼此深深地嵌套在一起。
 * 当我们需要处理其中一个步骤中可能发生的错误时，情况变得更加糟糕。Future是Java 5中添加作为异步计算的结果，但它没有任何方法处理计算可能出现的错误。
 * Java 8引入了CompletableFuture类。除Future接口外，它还实现了CompletionStage接口。该接口为异步计算步骤定义了合同，我们可以将其与其他步骤结合使用。
 */
public class CompletableFuture_ {

    /**
     * 首先，CompletableFuture类实现了Future接口，因此我们可以将其用作将来的实现，但需要附加完成逻辑。
     * 例如，我们可以用一个无参数构造函数创建这个类的实例来表示Future的结果，将它分发给使用者，并在将来的某个时候使用complete方法完成它。
     * 使用者可以使用get方法阻塞当前线程，直到提供此结果。
     * 在下面的示例中，我们有一个方法，它创建一个CompletableFuture实例，然后在另一个线程中派生一些计算，并立即返回Future。计算完成后，该方法通过向完整方法提供结果来完成Future：
     */
    public static Future<String> calculateAsync() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(500);
            completableFuture.complete("Hello");
            return null;
        });

        return completableFuture;
    }

//    public static void main(String[] args) {
//        StopWatch stopWatch = new StopWatch("代码执行时间统计");
//        try {
//            stopWatch.start("开始");
//            Future<String> stringFuture = calculateAsync();
//            stopWatch.stop();
//            stopWatch.start("开始2");
//            System.out.println(stringFuture.get());
//            stopWatch.stop();
//            System.out.println("task count:" + stopWatch.getTaskCount());
//            System.out.println("last task name:" + stopWatch.getLastTaskName());
//            System.out.println("total time:" + stopWatch.getTotalTimeMillis() + " ms");
//            System.out.println("last task time:" + stopWatch.getLastTaskTimeMillis() + " ms");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * 我们使用executorapi。这种创建和完成CompletableFuture的方法可以与任何并发机制或API（包括原始线程）一起使用。
     * 请注意，calculateAsync方法将返回一个Future的实例。我们只需调用该方法，接收Future实例，并在准备阻塞结果时对其调用get方法。
     * 还要注意get方法抛出一些已检查的异常，即ExecutionException（封装计算期间发生的异常）和interruptedeexception（表示执行方法的线程被中断的异常）:
     *
     * Future<String> completableFuture = calculateAsync();
     *
     * // ...
     *
     * String result = completableFuture.get();
     * assertEquals("Hello", result);
     *
     */

    /**
     * 如果我们已经知道计算的结果，我们可以使用静态completedFuture方法，并使用一个参数，该参数表示此计算的结果。
     * 因此，将来的get方法永远不会阻塞，立即返回此结果：
     *
     * Future<String> completableFuture =
     *   CompletableFuture.completedFuture("Hello");
     *
     * // ...
     *
     * String result = completableFuture.get();
     * assertEquals("Hello", result);
     */


    /**
     * 上面的代码允许我们选择任何并发执行的机制，但是如果我们想跳过这个样板文件，简单地异步执行一些代码呢？
     *
     * 静态方法runAsync和supplyAsync允许我们相应地使用Runnable和Supplier函数类型创建一个可完成的未来实例。
     *
     * Runnable和Supplier都是函数接口，由于新的java8特性，它们允许将实例作为lambda表达式传递。
     *
     * Runnable接口与线程中使用的旧接口相同，不允许返回值。
     *
     * Supplier接口是一个通用函数接口，它有一个方法，该方法没有参数，并且返回一个参数化类型的值。
     *
     * 这允许我们提供一个供应商实例作为lambda表达式来执行计算并返回结果。简单到：
     *
     * CompletableFuture<String> future
     *   = CompletableFuture.supplyAsync(() -> "Hello");
     *
     * // ...
     *
     * assertEquals("Hello", future.get());
     *
     */

    /**
     * 处理计算结果的最通用的方法是将其提供给函数。thenApply方法正是这样做的；
     * 它接受一个函数实例，用它来处理结果，并返回一个包含函数返回值的Future：
     *
     * CompletableFuture<String> completableFuture
     *   = CompletableFuture.supplyAsync(() -> "Hello");
     *
     * CompletableFuture<String> future = completableFuture
     *   .thenApply(s -> s + " World");
     *
     * assertEquals("Hello World", future.get());
     *
     *
     * 如果我们不需要在Future中返回值，我们可以使用Consumer函数接口的实例。它的单个方法接受一个参数并返回void。
     *
     * 在可完成的将来，有一种方法可以解决这个用例。thenAccept方法接收使用者并将计算结果传递给它。最后一个future.get（）调用返回Void类型的实例：
     *
     * CompletableFuture<String> completableFuture
     *   = CompletableFuture.supplyAsync(() -> "Hello");
     *
     * CompletableFuture<Void> future = completableFuture
     *   .thenAccept(s -> System.out.println("Computation returned: " + s));
     *
     * future.get();
     *
     * 最后，如果我们既不需要计算的值，也不想返回值，那么我们可以将一个可运行的lambda传递给thenRun方法。
     * 在下面的示例中，我们只需在调用future.get（）后在控制台中打印一行：
     *
     * CompletableFuture<String> completableFuture
     *   = CompletableFuture.supplyAsync(() -> "Hello");
     *
     * CompletableFuture<Void> future = completableFuture
     *   .thenRun(() -> System.out.println("Computation finished."));
     *
     * future.get();
     *
     */

    /**
     * CompletableFuture API最好的部分是能够在一系列计算步骤中组合CompletableFuture实例。
     *
     * 这种链接的结果本身就是一个完整的Future，允许进一步的链接和组合。这种方法在函数语言中普遍存在，通常被称为享元模式。
     *
     * 在下面的示例中，我们使用thenCompose方法按顺序链接两个Future。
     *
     * 请注意，此方法接受一个返回CompletableFuture实例的函数。此函数的参数是上一计算步骤的结果。这允许我们在下一个CompletableFuture的lambda中使用此值：
     *
     * CompletableFuture<String> completableFuture
     *   = CompletableFuture.supplyAsync(() -> "Hello")
     *     .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));
     *
     * assertEquals("Hello World", completableFuture.get());
     *
     *
     * thenCompose方法与thenApply一起实现了享元模式的基本构建块。它们与流的map和flatMap方法以及java8中的可选类密切相关。
     *
     * 两个方法都接收一个函数并将其应用于计算结果，但是thencomose（flatMap）方法接收一个返回另一个相同类型对象的函数。这种功能结构允许将这些类的实例组合为构建块。
     *
     * 如果我们想执行两个独立的未来，并对它们的结果进行处理，我们可以使用thenCombine方法，该方法接受一个未来和一个具有两个参数的函数来处理这两个结果：
     *
     * CompletableFuture<String> completableFuture
     *   = CompletableFuture.supplyAsync(() -> "Hello")
     *     .thenCombine(CompletableFuture.supplyAsync(
     *       () -> " World"), (s1, s2) -> s1 + s2));
     *
     * assertEquals("Hello World", completableFuture.get());
     *
     * 一个简单的例子是，当我们想处理两个CompletableFuture的结果时，但不需要将任何结果值传递给CompletableFuture的链。thenAcceptBoth方法可以帮助：
     *
     * CompletableFuture future = CompletableFuture.supplyAsync(() -> "Hello")
     *   .thenAcceptBoth(CompletableFuture.supplyAsync(() -> " World"),
     *     (s1, s2) -> System.out.println(s1 + s2));
     *
     */

    /**
     * 在前面的部分中，我们展示了有关thenApply（）和thenCompose（）的示例。两个api都有助于链接不同的CompletableFuture调用，但这两个函数的用法不同。
     *
     * thenApply()
     * 我们可以使用此方法处理上一次调用的结果。但是，需要记住的一点是，返回类型将由所有调用组合而成。
     *
     * 因此，当我们要转换CompletableFuture调用的结果时，此方法非常有用：
     *
     * CompletableFuture<Integer> finalResult = compute().thenApply(s-> s + 1);
     *
     *
     * thenCompose()
     * thenCompose（）方法与thenApply（）类似，因为两者都返回一个新的完成阶段。但是，thenCompose（）使用前一阶段作为参数。
     * 它将展平并直接返回一个带有结果的CompletableFuture，而不是我们在thenApply（）中观察到的嵌套CompletableFuture：
     *
     * CompletableFuture<Integer> computeAnother(Integer i){
     *     return CompletableFuture.supplyAsync(() -> 10 + i);
     * }
     * CompletableFuture<Integer> finalResult = compute().thenCompose(this::computeAnother);
     * 因此，如果要链接可完成的CompletableFuture方法，那么最好使用thenCompose（）。
     *
     * 另外，请注意，这两个方法之间的差异类似于map（）和flatMap（）之间的差异。
     *
     */

    /**
     * 当我们需要并行执行多个期货时，我们通常希望等待所有Supplier执行，然后处理它们的组合结果。
     *
     * CompletableFuture.allOf静态方法允许等待的所有Supplier的完成：
     *
     *
     * CompletableFuture<String> future1
     *   = CompletableFuture.supplyAsync(() -> "Hello");
     * CompletableFuture<String> future2
     *   = CompletableFuture.supplyAsync(() -> "Beautiful");
     * CompletableFuture<String> future3
     *   = CompletableFuture.supplyAsync(() -> "World");
     *
     * CompletableFuture<Void> combinedFuture
     *   = CompletableFuture.allOf(future1, future2, future3);
     *
     * // ...
     *
     * combinedFuture.get();
     *
     * assertTrue(future1.isDone());
     * assertTrue(future2.isDone());
     * assertTrue(future3.isDone());
     *
     * 注意CompletableFuture.allOf（）的返回类型是CompletableFuture。
     * 这种方法的局限性在于它不能返回所有Supplier的组合结果。
     * 相反，我们必须从未来手动获取结果。
     * 幸运的是，CompletableFuture.join（）方法和Java 8 Streams API使它变得简单：
     *
     * String combined = Stream.of(future1, future2, future3)
     *   .map(CompletableFuture::join)
     *   .collect(Collectors.joining(" "));
     *
     * assertEquals("Hello Beautiful World", combined);
     *
     * join（）方法类似于get方法，但是如果Future不能正常完成，它会抛出一个未检查的异常。
     *
     */

    /**
     * CompletableFuture类中fluentapi的大多数方法都有另外两个带有异步后缀的变体。这些方法通常用于在另一个线程中运行相应的执行步骤。
     * 说人话:不带异步后缀的方式，执行的所有的任务是同一个线程！！带异步后缀的方式，后续任务会提交到线程池中执行，因此执行的线程是其他线程!!!
     * <p>
     * 没有异步后缀的方法使用调用线程运行下一个执行阶段。相反，不带Executor参数的Async方法使用Executor的公共fork/join池实现运行一个步骤，该实现通过ForkJoinPool.commonPool（）方法访问。
     * 最后，带有Executor参数的Async方法使用传递的Executor运行步骤。
     * <p>
     * CompletableFuture<String> completableFuture
     * = CompletableFuture.supplyAsync(() -> "Hello");
     * <p>
     * CompletableFuture<String> future = completableFuture
     * .thenApplyAsync(s -> s + " World");
     * <p>
     * assertEquals("Hello World", future.get());
     */


    //其他参考资料 ：http://news.sohu.com/a/656784373_497772

//
    public static void main(String[] args) throws Exception {
//        System.out.println(calculateAsync().get());



//        CompletableFuture.runAsync()
        BlockingDeque<Runnable> blockingDeque = new LinkedBlockingDeque(1);
        ExecutorService executorService = new ThreadPoolExecutor(10, 10, 0, TimeUnit.SECONDS, blockingDeque);

        CompletableFuture<String> future1
                = CompletableFuture.supplyAsync(() -> "Hello", executorService);
        CompletableFuture<String> future2
                = CompletableFuture.supplyAsync(() -> "Beautiful", executorService);
        CompletableFuture<String> future3
                = CompletableFuture.supplyAsync(() -> {
//            if (System.currentTimeMillis() > 0) {
//                throw new RuntimeException("");
//            }
            return "World";
        }, executorService);
        CompletableFuture<Void> combinedFuture
                = CompletableFuture.allOf(future1, future2, future3);

        // ...

        System.out.println(combinedFuture.get());
        System.out.println(future1.get());
        System.out.println(future2.get());
        System.out.println(future2.get());

//      assertTrue(future1.isDone());
//      assertTrue(future2.isDone());
//      assertTrue(future3.isDone());
    }

}
