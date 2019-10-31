package com.wx.mini.service.processor;

import com.wx.mini.pojo.Do.CsdnBlog;
import com.wx.mini.service.pipeline.CsdnArticlePipeline;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * @auther alan.chen
 * @time 2019/10/11 10:43 AM
 */
public class CsdnArticlePageProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
    //            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
    private static String username; // 设置的Csdn用户名
    private static int size = 0; // 共抓取到的文章数量




    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    @Override
    public void process(Page page) {

        //设置爬虫url中子连接的爬取的url规则
        page.addTargetRequests(page.getHtml().links().regex("https://blog.csdn.net/[a-z 0-9 -]+/article/details/[0-9]{8}").all());

        //将爬取的内容保存
        String title = page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div/div/div[1]/h1").get();
        String content = page.getHtml().xpath("//*[@id=\"content_views\"]").get();
        if(StringUtils.isNotEmpty(title) && StringUtils.isNotEmpty(content)){
            page.putField("title",title);
            page.putField("content",content);
        } else {
            page.setSkip(true);//跳过
        }

    }

    @Override
    public Site getSite() {
        return site;
    }


    public static void main(String[] args) {
        long startTime, endTime;
//        System.out.print("请输入要爬取的CSDN博主用户名：");
//        Scanner scanner = new Scanner(System.in);
//        username = scanner.next();
        username = "dyc87112";
        System.out.println("-----------启动爬虫程序-----------");
        startTime = System.currentTimeMillis();
        Spider.create(new CsdnArticlePageProcessor())
                .addUrl("https://www.csdn.net/nav/ai")
                .addPipeline(new CsdnArticlePipeline())
                //开启5个线程抓取
                //启动爬虫
                .start();
        endTime = System.currentTimeMillis();
        System.out.println("结束爬虫程序，共抓取 " + size + " 篇文章，耗时约 " + ((endTime - startTime) / 1000) + " 秒！");
    }


}
