package csdn;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Execute {
    public static void main(String[] args) throws InterruptedException, IOException {

        // 初始化需要刷的博客地址

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 1; i <= 20; i++) {
            executorService.execute(new Thread(new RefreshBlogThread(i*3000),"thread-refresh-"+i));
        }

    }

}
