package csdn.hp;

import csdn.MyIp;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class HPRefreshBlogThread implements Runnable {

    private Logger logger = Logger.getLogger(HPRefreshBlogThread.class);
    //本地博客地址文本中的文章数量
    private int blogUrlSize = 0;
    //本地博客地址文本装入HashMap中
    private static HashMap<Integer, String> LocalBlogUrl = null;
    //本地博客地址访问统计
    private HashMap<Integer, Integer> LocalBlogUrlCount = null;
    //访问总量统计
    private int count = 0;
    private int sleepSec = 0;

    public HPRefreshBlogThread(int sleepSec) {
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
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93486963");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89467638");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89398097");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/106096766");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/94431147");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93467008");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93869852");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/98307110");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93488503");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89381131");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90439185");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93209110");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93209622");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90436863");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90409133");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89177148");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/92799288");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/53404586");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/105768849");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90416215");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/94442955");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/66971134");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/105203299");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90436907");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/106094115");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/88972261");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93714842");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/88948670");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90439050");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/97391848");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/100516029");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93871365");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/88974683");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93760065");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/105653052");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/106140622");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90712786");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90440712");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90408850");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90415379");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/98941975");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/88973917");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/106049148");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89378634");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/105638875");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90710806");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/106020784");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/104246317");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/98846571");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93872842");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93203104");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90177255");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93469050");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89214110");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/88975464");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89468909");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90477279");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90438305");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/88988373");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89357240");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90258070");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93192617");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93873924");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/92829739");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/88975073");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/106098779");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89951274");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89378691");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/80597183");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90416374");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/98964375");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89234342");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93212104");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/53841687");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90520310");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/95471226");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90519850");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90574414");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90484591");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90440451");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/88948567");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90746435");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/99327773");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/95317761");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/94023774");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93518167");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90746493");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89215140");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/106070692");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/77933436");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89215265");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/53378976");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93203684");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90712245");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90519659");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90439389");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89213761");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/98616052");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89498852");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90439416");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89552183");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/97395154");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89357062");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90746408");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90258053");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/98851187");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/97388224");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/94721399");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90573451");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90409055");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89494409");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/53390209");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/105628638");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/99301133");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/98574735");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/95473343");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/92796928");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/47947425");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90473846");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90439012");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/104242482");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/106058977");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93468031");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90416273");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/98578250");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/92833016");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90440357");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/98726880");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90436810");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93208869");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90574311");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90746534");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90482036");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89226788");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/88949038");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/88948983");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90478969");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/106097998");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89707492");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/104628752");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/106096542");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93870464");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90257970");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/80762360");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/98496765");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/88973366");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/80754057");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90439288");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90377424");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89215801");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89212414");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93196325");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/88572803");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93210472");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90043717");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89357043");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89209639");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90258033");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/98615419");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/94017067");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90744860");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90473820");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89356923");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/94435419");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89183257");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89378770");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/89216201");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90414551");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/94735534");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/92741553");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90574332");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/93210089");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90745306");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/90040718");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/88572511");
        hashMap.put(id++, "https://blog.csdn.net/haponchang/article/details/91046889");
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
