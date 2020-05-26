package csdn;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * --------------------自动刷CSDN博客访问量程序--------------------
 *
 * 将要刷访问量的博客id填写入24行的变量userId中，点击运行
 * 本程序访问该博主【用户ID】名下所有博客链接
 *
 * 仅供学习测试使用，不要真的用于刷访问量~
 */
public class UrlCrawBokePro {

    static String userId = "Mrkaizi";
    static int random_num = 0;


    public static void main(String urlstr[]) throws IOException, InterruptedException {

        Set<String> urls = new HashSet<String>();

        // ----------------------------------------------遍历每一页 获取文章链接----------------------------------------------
        final String homeUrl = "https://blog.csdn.net/" + userId + "/article/list/";// 后面加pageNum即可
        int totalPage = 0;
        InputStream is;
        String pageStr;
        StringBuilder curUrl = null;
        for (int i = 1; i < 100; i++) {
            Thread.sleep(1000);
            System.out.println("finding page " + i);
            curUrl = new StringBuilder(homeUrl);
            curUrl.append(i);
            System.out.println(curUrl);
            is = doGet(curUrl.toString());
            pageStr = inputStreamToString(is, "UTF-8");// 一整页的html源码

            List<String> list = getMatherSubstrs(pageStr, "(?<=href=\")https://blog.csdn.net/" + userId + "/article/details/[0-9]{8,9}(?=\")");
            urls.addAll(list);

            if (pageStr.lastIndexOf("空空如也") != -1) {
                System.out.println("No This Page!");
                break;
            } else {
                System.out.println("Success~");
            }
            totalPage = i;
        }
        System.out.println("总页数为: " + totalPage);

        // ---------------------------------------------------打印每个链接---------------------------------------------------
        System.out.println("打印每个链接");
        for (String s:urls) {
            System.out.println(s);
        }
        System.out.println("打印每个链接完毕");

        // ---------------------------------------------------访问每个链接---------------------------------------------------
        int i=0;
        for (String s:urls) {
            int num = 0;
            System.out.print("成功访问第" + (++i) + "个链接"+s+",共" );
            while (num++<100) {
                if(random_num++%2==0) {
                    Thread.sleep(1000+187);
                }else {
                    Thread.sleep(1000+243);
                }
                doGet(s);
            }
            System.out.println( --num+ "次:" );
        }

        // ---------------------------------------------------程序结束---------------------------------------------------
        System.out.println("运行完毕，成功增加访问数：" + urls.size()*100);
    }
    public static InputStream doGet(String urlstr) throws IOException {
        URL url = new URL(urlstr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (random_num++%2==0) {
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        }else {
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36");
        }
        conn.setRequestProperty("Referer",
                "https://blog.csdn.net");
        InputStream inputStream = conn.getInputStream();
        return inputStream;
    }

    public static String inputStreamToString(InputStream is, String charset) throws IOException {
        byte[] bytes = new byte[1024];
        int byteLength = 0;
        StringBuffer sb = new StringBuffer();
        while ((byteLength = is.read(bytes)) != -1) {
            sb.append(new String(bytes, 0, byteLength, charset));
        }
        return sb.toString();
    }

    // 正则匹配
    public static List<String> getMatherSubstrs(String str, String regex) {
        List<String> list = new ArrayList<String>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }
}