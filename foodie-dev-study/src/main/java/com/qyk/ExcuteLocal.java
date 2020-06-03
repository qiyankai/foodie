package com.qyk;

import csdn.RefreshBlogThreadNew;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcuteLocal {

    /**
     * 使用指南：
     * 1、先按照提示执行 StepOne 的Main方法，获取你的全部博客Url（如果你可以手动粘贴复制你自己所有的博客地址也行）
     * 2、把博客地址（已经拼接成java代码），复制粘贴到本类getLocalBlogUrl ---》指定位置！
     * 3、访问http://www.daxiangdaili.com/      购买（推荐一天9块的），把订单ID填充到下面proxyOrderId位置
     * 4、点击运行下面main方法，一天1w-2w访问量
     * @param args
     */


    public static void main(String[] args) {


        // ==========================================================
        // http://www.daxiangdaili.com/   访问这个购买id，然后就能执行了
        // 购买一天的就行，买了把订单号写这儿！！
        // ==========================================================
        String proxyOrderId = "558114351287931";


        // ==========================================================
        // 看你心情设置
        // 线程数量，电脑好点的自信点来个1000，大概一天能刷2w-4w，不建议设置太高size（50-2000）=访问量（5k～50k）
        // ==========================================================
        int threadSize = 300;



        // 初始化需要刷的博客地址
        HashMap<Integer, String> localBlogUrl = getLocalBlogUrl();
        // 初始化对应博客计数map，前面是博客地址，后面是博客被访问次数
        HashMap<Integer, AtomicInteger> localBlogUrlCount = new HashMap<Integer, AtomicInteger>();
        for (int i = 0; i < localBlogUrl.size(); i++) {
            localBlogUrlCount.put(i, new AtomicInteger());
        }
        // 初始化总计数器，使用原子类，直接调用incr方法，防止线程间同时写入，导致的ABA问题
        AtomicInteger count = new AtomicInteger();

        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        for (int i = 1; i <= threadSize; i++) {
            executorService.execute(new Thread(new RefreshBlogThreadNew(i * 10000, localBlogUrl, localBlogUrlCount, count, proxyOrderId), "thread-refresh-" + i));
        }
    }


    //获取本地BlogUrl.txt文本中的博客地址，并装入hashMap中，key=Integer,value=博客地址
    public static HashMap<Integer, String> getLocalBlogUrl() {

        HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
        int id = 1;
        // ----------------------------指定位置
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106524423");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106524423");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106524423");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106527760");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106527760");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106527760");
        // ----------------------------指定位置

        return hashMap;
    }

}
