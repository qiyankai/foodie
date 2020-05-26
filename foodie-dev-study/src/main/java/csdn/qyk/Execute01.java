package csdn.qyk;

import csdn.RefreshBlogThread;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Execute01 {
    public static void main(String[] args) throws InterruptedException, IOException {

        // 初始化需要刷的博客地址

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        for (int i = 1; i <= 50; i++) {
            executorService.execute(new Thread(new RefreshBlogThread(i*3000),"thread-refresh-"+i));
        }

    }

}
