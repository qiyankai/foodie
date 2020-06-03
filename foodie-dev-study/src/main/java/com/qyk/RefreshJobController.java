package com.qyk;

import com.qyk.utils.RedisOperator;
import csdn.RefreshBlogThread;
import csdn.RefreshBlogThreadNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class RefreshJobController {

    @Autowired
    private RedisOperator redisOperator;

    @RequestMapping("startRedis")
    public String autoRefreshRedis() {

        // 初始化需要刷的博客地址
        HashMap<Integer, String> localBlogUrl = getLocalBlogUrl();
        // 初始化对应博客计数map，前面是博客地址，后面是博客被访问次数
        HashMap<Integer, AtomicInteger> localBlogUrlCount = new HashMap<Integer, AtomicInteger>();
        for (int i = 0; i < localBlogUrl.size(); i++) {
            localBlogUrlCount.put(i, new AtomicInteger());
        }
        // 初始化总计数器，使用原子类，直接调用incr方法，防止线程间同时写入，导致的ABA问题
        AtomicInteger count = new AtomicInteger();

        ExecutorService executorService = Executors.newFixedThreadPool(300);
        for (int i = 1; i <= 300; i++) {
            executorService.execute(new Thread(new RefreshBlogThreadNew(i * 10000, redisOperator, localBlogUrl, localBlogUrlCount, count), "thread-refresh-" + i));
        }
        return "ok";
    }

    @RequestMapping("start")
    public String autoRefresh() {
        String proxyOrderId = "558114351287931";

        // 初始化需要刷的博客地址
        HashMap<Integer, String> localBlogUrl = getLocalBlogUrl();
        // 初始化对应博客计数map，前面是博客地址，后面是博客被访问次数
        HashMap<Integer, AtomicInteger> localBlogUrlCount = new HashMap<Integer, AtomicInteger>();
        for (int i = 0; i < localBlogUrl.size(); i++) {
            localBlogUrlCount.put(i, new AtomicInteger());
        }
        // 初始化总计数器，使用原子类，直接调用incr方法，防止线程间同时写入，导致的ABA问题
        AtomicInteger count = new AtomicInteger();

        ExecutorService executorService = Executors.newFixedThreadPool(300);
        for (int i = 1; i <= 300; i++) {
            executorService.execute(new Thread(new RefreshBlogThreadNew(i * 10000, localBlogUrl, localBlogUrlCount, count,proxyOrderId), "thread-refresh-" + i));
        }
        return "ok";
    }

    //获取本地BlogUrl.txt文本中的博客地址，并装入hashMap中，key=Integer,value=博客地址
    public static HashMap<Integer, String> getLocalBlogUrl() {

        HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
        int id = 1;
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106178717");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106242794");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/80622506");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106319166");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106298394");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106453822");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106174047");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106451002");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106300644");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/88639291");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/80533631");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/105280200");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/80533552");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/80449534");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106364852");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/105286124");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106245934");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106298365");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/83149050");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106298383");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/99590533");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/80449107");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106171001");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/81743216");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106174196");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106363632");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106102567");
        return hashMap;
    }


}
