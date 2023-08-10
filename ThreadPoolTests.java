import org.junit.Test;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTests {

    /**
     * ThreadPoolExecutor的构造组成
     * public ThreadPoolExecutor(int corePoolSize,
     *                          int maximumPoolSize,
     *                          long keepAliveTime,
     *                          TimeUnit unit,
     *                          BlockingQueue<Runnable> workQueue,
     *                          ThreadFactory threadFactory,
     *                          RejectedExecutionHandler handler)
     * corePoolSize: 线程池中允许的基本线程数量
     * maximumPoolSize: 线程池中允许的最大线程数量
     * keepAliveTime: 线程空闲时存活的时间
     * unit: 空闲存活时间单位
     * workQueue: 任务队列，用于存放已提交的任务
     * threadFactory: 线程工厂，用于创建线程执行任务, 默认类为DefaultThreadFactory
     * handler: 拒绝策略，当线程池处于饱和时，使用某种策略来拒绝任务提交，默认策略为ThreadPoolExecutor.AbortPolicy
     *
     * 线程池的五种状态：RUNNING, SHUTDOWN, STOP, TIDYING, TERMINATED.
     * SHUTDOWN不会终端正在执行任务的线程，而shutdownNow则会调用interrupt()方法来中断阻塞的任务，如在任务中调用了sleep, wait等方法的线程
     *
     *                          corePoolSize    maximumPoolSize     keepAliveTime   workQueue
     * newCachedThreadPool:     0               Integer.MAX_VALUE   60s             SynchronousQueue
     * newFixedThreadPool:      nThreads        nThreads            0               LinkedBlockingQueue
     * newSingleThreadExecutor: 1               1                   0               LinkedBlockingQueue
     * newScheduledThreadPool:  corePoolSize    Integer.MAX_VALUE   0               DelayedWorkQueue
     */

    @Test
    public void cachedThreadPoolTest() {
        ExecutorService executorService = Executors.newCachedThreadPool(
                new TestThreadPoolFactory("cachedThreadPool"));
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                System.out.println(Thread.currentThread().getName());
            });
        }
    }

    @Test
    public void fixedThreadPoolTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(
                5, new TestThreadPoolFactory("fixedThreadPool"));
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                System.out.println(Thread.currentThread().getName());
            });
        }
    }

    @Test
    public void singleThreadPoolTest() {
        ExecutorService executorService = Executors.newSingleThreadExecutor(new TestThreadPoolFactory(
                "singleThreadExecutor"));
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                System.out.println(Thread.currentThread().getName());
            });
        }
    }

    @Test
    public void scheduleThreadPoolTest() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5, new TestThreadPoolFactory(
           "scheduledThreadPool"));

        // Executing the task in a period of 1s.
        executorService.schedule(() -> {
            System.out.println(Thread.currentThread().getName() + ", delay 1s");
        }, 1, TimeUnit.SECONDS);

        // Delaying 2s, and then executing the task in a period of 3s.
        executorService.scheduleAtFixedRate(() -> {
            System.out.println(Thread.currentThread().getName() + ", every 3s");
        }, 2, 3, TimeUnit.SECONDS);


        executorService.scheduleWithFixedDelay(() -> {
            long start = new Date().getTime();
            System.out.println("scheduleWithFixedDelay EXECUTING START TIME: " +
                    DateFormat.getTimeInstance().format(new Date()));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long end = new Date().getTime();
            System.out.println("scheduleWithFixedDelay EXECUTING COST TIME: " + (end - start) / 1000 + "m");
            System.out.println("scheduleWithFixedDelay EXECUTING FINISH TIME: " +
                    DateFormat.getTimeInstance().format(new Date()));
            System.out.println("============================================================");
        }, 1, 2, TimeUnit.SECONDS);
    }
}
