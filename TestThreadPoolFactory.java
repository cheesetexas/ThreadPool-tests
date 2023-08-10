import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class TestThreadPoolFactory implements ThreadFactory {

    private final AtomicInteger threadIdx = new AtomicInteger(0);

    private final String threadNamePrefix;

    public TestThreadPoolFactory(String prefix) {
        threadNamePrefix = prefix;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(threadNamePrefix + "-xxljob-" + threadIdx.getAndIncrement());
        return thread;
    }
}
