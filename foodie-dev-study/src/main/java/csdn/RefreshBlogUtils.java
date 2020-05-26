package csdn;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RefreshBlogUtils {
    private static Logger logger = Logger.getLogger(RefreshBlogUtils.class);
    //本地博客地址文本中的文章数量
    private static int blogUrlSize = 0;
    //本地博客地址文本装入HashMap中
    private static HashMap<Integer, String> LocalBlogUrl = null;
    //本地博客地址访问统计
    private static HashMap<Integer, Integer> LocalBlogUrlCount = null;
    //访问总量统计
    private static int count = 0;

    public static void main(String[] args) throws InterruptedException, IOException {

        //比如你的订单号是123456789，每次你想提取200个代理进行使用，就应该是
        //http://tvp.daxiangdaili.com/ip/?tid=123456789&num=200&delay=5
        String url = "http://tvp.daxiangdaili.com/ip/?tid=559580690449621&num=200&delay=5";
        List<MyIp> ipList = getIp(url);
        while (true) {
            LocalBlogUrl = getLocalBlogUrl();
            blogUrlSize = LocalBlogUrl.size();
            LocalBlogUrlCount = initLocalBlogUrlCount();
            for (final MyIp myIp : ipList) {
                System.setProperty("http.maxRedirects", "50");
                System.getProperties().setProperty("proxySet", "true");
                System.getProperties().setProperty("http.proxyHost", myIp.getAddress());
                System.getProperties().setProperty("http.proxyPort", myIp.getPort());
                try {
                    Document doc = null;
                    int index = 0;
                    while (index++ < LocalBlogUrl.size()) {
                        getRequest(myIp,randomBlogUrl(),doc);
                    }

                } catch (IOException e) {
                    logger.error(myIp.getAddress() + ":" + myIp.getPort() + "报错");
                }
                sleepThread(randomClick());
                show();
            }
        }

    }

    private static void getRequest(MyIp myIp,int id,Document doc) throws IOException {
        doc = Jsoup.connect(LocalBlogUrl.get(id))
                .userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(3000)
                .get();
        if (doc != null) {
            count++;
            LocalBlogUrlCount.put(id, LocalBlogUrlCount.get(id) + 1);
            System.out.print("ID： " + id + "\tAddress： " + (LocalBlogUrl.get(id) + "\t成功刷新次数: " + count + "\t") + "Proxy： " + myIp.toString() + "\t");
        }
    }

    //访问文章的随机函数，用来模拟真实的访问量操作，以免所有的文章访问量都是一样的，很明显是刷的，此操作随机访问文章，制造访问假象
    public static int randomBlogUrl() {
        int id = new Random().nextInt(blogUrlSize);
        return id;
    }

    //时间的随机函数，用来模拟真实的访问量操作，以防被博客后台识别，模拟操作60-200秒内的随机秒数,
    public static int randomClick() {
        int time = (new Random().nextInt(30)) + 30;
        return time;
    }

    //获取在【大象代理】中购买的IP，装入ArrayList<MyIp>中
    public static List<MyIp> getIp(String url) {
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
    public static HashMap<Integer, String> getLocalBlogUrl() throws IOException {

        File filePath = new File(System.getProperty("user.dir"), "BlogUrl.txt");
        FileReader in = new FileReader(filePath);
        BufferedReader br = new BufferedReader(in);
        HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
        String line = null;
        int id = 0;
        while ((line = br.readLine()) != null) {
            hashMap.put(id++, line);
        }
        br.close();
        return hashMap;
    }

    //休眠进程，单位是分钟,CSDN的规则好像是：每个IP访问一个博客地址的时间间隔是5-15分钟，计数一次
    public static void sleepThread(int s) throws InterruptedException {
        long ms = s * 1000;
        Thread.sleep(ms);
        System.out.println("睡眠： " + s + "s");
    }

    //访问统计
    public static HashMap<Integer, Integer> initLocalBlogUrlCount() {
        HashMap<Integer, Integer> temp = new HashMap<Integer, Integer>();
        for (int i = 0; i < blogUrlSize; i++) {
            temp.put(i, 0);
        }
        return temp;
    }

    //展示访问统计总量
    public static void show() {
        System.out.println("访问量统计：");
        for (int i = 0; i < LocalBlogUrlCount.size(); i++) {
            System.out.print("博客【" + i + "】:" + LocalBlogUrlCount.get(i) + "次\t");
        }
        System.out.println();
        System.out.println("总计：" + count + "次");
        System.out.println();
    }

}