package csdn.qyk;

import csdn.RefreshBlogThread;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Execute03 {
    public static void main(String[] args) throws InterruptedException, IOException {

        // 初始化需要刷的博客地址

        ExecutorService executorService = Executors.newFixedThreadPool(300);
        for (int i = 1; i <= 300; i++) {
            executorService.execute(new Thread(new RefreshBlogThread(i*5000),"thread-refresh-"+i));
        }

    }

}
