import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        runnables();
        callables();
        manyCallables();
        scheduled();
        completable();

        /*Scanner sc = new Scanner(System.in);
        String line = sc.next();
        System.out.println(line);*/
    }

    static void runnables() throws ExecutionException, InterruptedException {
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        ExecutorService es = Executors.newWorkStealingPool(numberOfCores);

        Runnable runnable = () -> System.out.println("Saluton Mondo from Runnable!"); //void

        Future<?> submit = es.submit(runnable);

        submit.get();

        es.shutdown();

    }

    static void callables() throws ExecutionException, InterruptedException {
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        ExecutorService es = Executors.newWorkStealingPool(numberOfCores);

        Callable<String> callable = () -> "Saluton Mondo from Callable!"; //returns something

        Future<?> submit = es.submit(callable);

        System.out.println(submit.get());

        es.shutdown();
    }

    static void manyCallables() throws ExecutionException, InterruptedException {
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        ExecutorService es = Executors.newWorkStealingPool(numberOfCores);

        Callable<String> callable1 = () -> Thread.currentThread().getName() + "-Saluton Mondo from Callable1!"; //returns something
        Callable<String> callable2 = () -> Thread.currentThread().getName() + "-Saluton Mondo from Callable2!"; //returns something
        Callable<String> callable3 = () -> Thread.currentThread().getName() + "-Saluton Mondo from Callable3!"; //returns something

        Set<Callable<String>> set = new HashSet<Callable<String>>() {{
            add(callable1);
            add(callable2);
            add(callable3);
        }};

        List<Future<String>> futures = es.invokeAll(set);

        for (Future<String> future : futures) {
            System.out.println(future.get());
        }

        es.shutdown();
    }

     static void scheduled() throws ExecutionException, InterruptedException {
         int numberOfCores = Runtime.getRuntime().availableProcessors();
         ScheduledExecutorService es = Executors.newScheduledThreadPool(numberOfCores);

         Runnable runnable = () -> System.out.println(Thread.currentThread().getName() + "-" + "Scheduluer running..." + Instant.now().toString());

         //This shit don't stop LOL
//         ScheduledFuture<?> scheduledFuture = es.scheduleAtFixedRate(runnable, 1000, 1000, TimeUnit.MILLISECONDS);
         ScheduledFuture<?> scheduledFuture = es.schedule(runnable, 1000, TimeUnit.MILLISECONDS);

         scheduledFuture.get();

         es.shutdown();
     }

     static void completable() throws ExecutionException, InterruptedException {
         int numberOfCores = Runtime.getRuntime().availableProcessors();
         ExecutorService es = Executors.newWorkStealingPool(numberOfCores);

         CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "Saluton", es);
         CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "Mondo", es);
         CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> "Bithces", es);

         String collected = Stream.of(f1, f2, f3)
                 .map(CompletableFuture::join)
                 .collect(Collectors.joining(" "));

         System.out.println(collected);

         es.shutdown();

     }


}
