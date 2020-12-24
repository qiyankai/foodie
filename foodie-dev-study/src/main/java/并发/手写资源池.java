package 并发;

import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

public class 手写资源池 {
    public static void main(String[] args) throws Exception {
        PoolDemo<Integer, String> poolDemo = new PoolDemo<Integer, String>(10, 2);
        poolDemo.exec(t -> {
            System.out.println(t);
            return t.toString();
        });
    }
}

/**
 * 使用信号量模型实现限流器
 * 信号量模型，通过三个方法init()、down()、up()将资源管理起来，内部阻塞队列、计数器
 * 手写一个资源池（限流器）
 * 第一步，模拟信号量
 */
class SemaphoreEasy {
    int count;
    Queue queue;

    // init
    public SemaphoreEasy(int count) {
        this.count = count;
    }

    // down
    void acquire() {
        count--;
        if (count < 0) {
            // 将当前线程添加到阻塞队列
            // 阻塞当前线程
        }
    }

    // up
    void release() {
        count++;
        if (count <= 0) {
            // 移除阻塞线程队列中的一个
            // 唤醒该线程
        }
    }
}

// 第二步，多个资源，可被同时获取，进入临界区
class PoolDemo<T, R> {
    // 资源池
    final List<T> pool;
    // 限流器
    final Semaphore sem;

    public PoolDemo(int size, T t) {
        this.sem = new Semaphore(size);
        this.pool = new Vector<T>() {
        };
        for (int i = 0; i < size; i++) {
            pool.add(t);
        }
    }

    // 直接获取到资源，或者被唤醒，执行方法后将资源释放
    R exec(Function<T, R> func) throws InterruptedException {
        T t = null;
        sem.acquire();
        try {
            t = pool.remove(0);
            return func.apply(t);
        } finally {
            pool.add(t);
            sem.release();
        }
    }
}
