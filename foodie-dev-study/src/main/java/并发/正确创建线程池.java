package 并发;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class 正确创建线程池 {
    public static void main(String[] args) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("pool-module-id").build();
        LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,
                7,
                30,
                TimeUnit.MILLISECONDS,
                workQueue,
                threadFactory
        );
    }
}
