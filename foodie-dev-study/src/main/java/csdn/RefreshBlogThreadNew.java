package csdn;

import com.qyk.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class RefreshBlogThreadNew implements Runnable {

    //本地博客地址文本中的文章数量
    private int blogUrlSize = 0;
    //本地博客地址文本装入HashMap中
    private static HashMap<Integer, String> LocalBlogUrl = null;
    //本地博客地址访问统计
    private HashMap<Integer, AtomicInteger> LocalBlogUrlCount = null;
    private RedisOperator redisOperator;
    //访问总量统计
    private AtomicInteger count = null;
    private int sleepSec = 0;
    private String proxyOrderId = "";

    public RefreshBlogThreadNew(int sleepSec, RedisOperator redisOperator, HashMap<Integer, String> localBlogUrl, HashMap<Integer, AtomicInteger> localBlogUrlCount, AtomicInteger count) {
        this.redisOperator = redisOperator;
        this.LocalBlogUrl = localBlogUrl;
        this.sleepSec = sleepSec;
        this.blogUrlSize = LocalBlogUrl.size();
        this.LocalBlogUrlCount = localBlogUrlCount;
        this.count = count;

    }

    public RefreshBlogThreadNew(int sleepSec, HashMap<Integer, String> localBlogUrl, HashMap<Integer, AtomicInteger> localBlogUrlCount, AtomicInteger count, String proxyOrderId) {
        this.proxyOrderId = proxyOrderId;
        this.LocalBlogUrl = localBlogUrl;
        this.sleepSec = sleepSec;
        this.blogUrlSize = LocalBlogUrl.size();
        this.LocalBlogUrlCount = localBlogUrlCount;
        this.count = count;

    }

    @Override
    public void run() {

        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "----sleep" + (sleepSec));
        try {
            Thread.sleep(sleepSec);
            System.out.println(threadName + "请求代理");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            //比如你的订单号是123456789，每次你想提取200个代理进行使用，就应该是
            String url = "http://tvp.daxiangdaili.com/ip/?tid="+proxyOrderId+"&num=200&delay=5";
            List<MyIp> ipList = getIp(url);
            for (MyIp myIp : ipList) {
                System.setProperty("http.maxRedirects", "50");
                System.getProperties().setProperty("proxySet", "true");
                System.getProperties().setProperty("http.proxyHost", myIp.getAddress());
                System.getProperties().setProperty("http.proxyPort", myIp.getPort());
                while (true) {
                    try {
                        int id = 0;
                        String urlStr = null;
                        while (StringUtils.isBlank(urlStr)) {
                            id = randomBlogUrl();
                            urlStr = LocalBlogUrl.get(id);
                        }
                        Document doc = Jsoup.connect(LocalBlogUrl.get(id))
                                .userAgent("Mozilla")
                                .cookie("auth", "token")
                                .timeout(3000)
                                .get();
                        if (doc != null) {
                            count.incrementAndGet();
                            LocalBlogUrlCount.get(id).incrementAndGet();
//                            LocalBlogUrlCount.put(id, LocalBlogUrlCount.get(id) + 1);
                            System.out.print("ID： " + id + "\tAddress： " + (LocalBlogUrl.get(id) + "\t成功刷新次数: " + count + "\t") + "Proxy： " + myIp.toString() + "\t");
                        }
                    } catch (IOException e) {
                    }

                    try {
                        sleepThread(randomClick());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    show();
                }
            }
        }
    }

    //访问文章的随机函数，用来模拟真实的访问量操作，以免所有的文章访问量都是一样的，很明显是刷的，此操作随机访问文章，制造访问假象
    public int randomBlogUrl() {
        int id = new Random().nextInt(blogUrlSize);
        return id;
    }

    //时间的随机函数，用来模拟真实的访问量操作，以防被博客后台识别，模拟操作60-200秒内的随机秒数,
    public int randomClick() {
        int time = (new Random().nextInt(200)) + 60;
        return time;
    }

    //获取在【大象代理】中购买的IP，装入ArrayList<MyIp>中
    public List<MyIp> getIp(String url) {
        List<MyIp> ipList = null;
        while (ipList == null) {
            try {
                //1.向ip代理地址发起get请求，拿到代理的ip
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla")
                        .cookie("auth", "token")
                        .timeout(3000)
                        .get();

                System.out.println(doc.body().text());
                //2,将得到的ip地址解析除字符串
                String ipStr = doc.body().text().trim().toString();
                System.out.println("当前使用ipStr----------" + ipStr);
                //3.用正则表达式去切割所有的ip
                String[] ips = ipStr.split("\\s+");
                if (redisOperator != null) {
                    redisOperator.lpush("ip-list", ipStr);
                }
                //4.循环遍历得到的ip字符串，封装成MyIp的bean
                ipList = new ArrayList<MyIp>();
                for (final String ip : ips) {
                    MyIp myIp = new MyIp();
                    String[] temp = ip.split(":");
                    myIp.setAddress(temp[0].trim());
                    myIp.setPort(temp[1].trim());
                    ipList.add(myIp);
                }
            } catch (IOException e) {
                System.out.println("加载文档出错,等待5s后重试");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return ipList;
    }

    //休眠进程，单位是分钟,CSDN的规则好像是：每个IP访问一个博客地址的时间间隔是5-15分钟，计数一次
    public void sleepThread(int s) throws InterruptedException {
        long ms = s * 1000;
        Thread.sleep(ms);
        System.out.println("睡眠： " + s + "s");
    }

    //展示访问统计总量
    public void show() {
        System.out.println("访问量统计：");
        for (int i = 0; i < LocalBlogUrlCount.size(); i++) {
            System.out.print("博客【" + i + "】:" + LocalBlogUrlCount.get(i) + "次\t");
        }
        System.out.println();
        System.out.println("总计：" + count + "次");
        System.out.println();
    }

}
