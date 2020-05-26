package csdn;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RefreshBlogThread implements Runnable {

    private Logger logger = Logger.getLogger(RefreshBlogThread.class);
    //本地博客地址文本中的文章数量
    private int blogUrlSize = 0;
    //本地博客地址文本装入HashMap中
    private static HashMap<Integer, String> LocalBlogUrl = null;
    //本地博客地址访问统计
    private HashMap<Integer, Integer> LocalBlogUrlCount = null;
    //访问总量统计
    private int count = 0;
    private int sleepSec = 0;

    public RefreshBlogThread(int sleepSec) {
        if (LocalBlogUrl == null) {
            LocalBlogUrl = getLocalBlogUrl();
        }
        this.sleepSec = sleepSec;
        blogUrlSize = LocalBlogUrl.size();
        LocalBlogUrlCount = initLocalBlogUrlCount();

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
            //http://tvp.daxiangdaili.com/ip/?tid=123456789&num=200&delay=5
            String url = "http://tvp.daxiangdaili.com/ip/?tid=559921805593255&num=500&delay=5";
            List<MyIp> ipList = getIp(url);
            for (MyIp myIp : ipList) {
                System.setProperty("http.maxRedirects", "50");
                System.getProperties().setProperty("proxySet", "true");
                System.getProperties().setProperty("http.proxyHost", myIp.getAddress());
                System.getProperties().setProperty("http.proxyPort", myIp.getPort());
                while (true) {
                    try {
                        int id = randomBlogUrl();
                        Document doc = Jsoup.connect(LocalBlogUrl.get(id))
                                .userAgent("Mozilla")
                                .cookie("auth", "token")
                                .timeout(3000)
                                .get();
                        if (doc != null) {
                            count++;
                            LocalBlogUrlCount.put(id, LocalBlogUrlCount.get(id) + 1);
                            System.out.print("ID： " + id + "\tAddress： " + (LocalBlogUrl.get(id) + "\t成功刷新次数: " + count + "\t") + "Proxy： " + myIp.toString() + "\t");
                        }
                    } catch (IOException e) {
                        logger.error(myIp.getAddress() + ":" + myIp.getPort() + "报错");
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
        try {
            //1.向ip代理地址发起get请求，拿到代理的ip
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .get();

            //2,将得到的ip地址解析除字符串
            String ipStr = doc.body().text().trim().toString();

            //3.用正则表达式去切割所有的ip
            String[] ips = ipStr.split("\\s+");

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
            System.out.println("加载文档出错");
        }
        return ipList;
    }

    //获取本地BlogUrl.txt文本中的博客地址，并装入hashMap中，key=Integer,value=博客地址
    public static HashMap<Integer, String> getLocalBlogUrl() {

        HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
        int id = 1;
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106245934");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106178717");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106242794");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106298365");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/80622506");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106319166");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/83149050");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106298394");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106298383");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106174047");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/99590533");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/80449107");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106171001");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106300644");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/81743216");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/88639291");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/80533631");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/105280200");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/80533552");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106174196");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/80449534");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/106102567");
        hashMap.put(id++, "https://blog.csdn.net/Mrkaizi/article/details/105286124");
        return hashMap;
    }

    //休眠进程，单位是分钟,CSDN的规则好像是：每个IP访问一个博客地址的时间间隔是5-15分钟，计数一次
    public void sleepThread(int s) throws InterruptedException {
        long ms = s * 1000;
        Thread.sleep(ms);
        System.out.println("睡眠： " + s + "s");
    }

    //访问统计
    public HashMap<Integer, Integer> initLocalBlogUrlCount() {
        HashMap<Integer, Integer> temp = new HashMap<Integer, Integer>();
        for (int i = 0; i < blogUrlSize; i++) {
            temp.put(i, 0);
        }
        return temp;
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
