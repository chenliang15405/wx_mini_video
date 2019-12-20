package com.java8;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author alan.chen
 * @date 2019/11/21 12:18 PM
 */
public class Test1 {

    public static void main(String[] args) {
        while(true){
            int startIndex = (int)(Math.random()*23977);
            Test1.start(startIndex);
        }
    }

    public static void start(int startIndex) {
        String urlStr = "https://www.duitang.com/napi/blog/list/by_filter_id/?include_fields=top_comments%2Cis_root%2Csource_link%2Citem%2Cbuyable%2Croot_id%2Cstatus%2Clike_count%2Csender%2Calbum%2Creply_count&filter_id=%E5%A4%B4%E5%83%8F&start=" + startIndex;
        String charset = "utf8";
        String content = getURLContent(urlStr, charset);

        String regex = "https://c-ssl.duitang.com/uploads/item/([\\s\\S]+?)g";
        List<String> result = getMatcherContent(content, regex );
        download(result);
    }

    /**
     * 指定url网页字符集，并返回网页源代码
     *
     * @param urlStr
     * @param charset
     * @return
     * @throws IOException
     */
    public static String getURLContent(String urlStr, String charset) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            URL url = new URL(urlStr);
            br = new BufferedReader(new InputStreamReader(url.openStream(), charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeAll(br);
        }
        return sb.toString();
    }

    /**
     * 网页源代码通过匹配正则表达式获取符合条件的信息 并封装成list返回
     *
     * @param content
     * @param regex
     * @return
     */
    public static List<String> getMatcherContent(String content, String regex) {
        List<String> result = new ArrayList<>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            result.add(m.group());
        }
        return result;
    }

    /**
     * 下载集合中的图片
     *
     * @param result
     * @throws IOException
     */
    public static void download(List<String> result) {
        for (String temp : result) {
            download(temp);
        }
    }

    /**
     * 下载图片详细方法
     *
     * @param imgURL
     */
    public static void download(String imgURL) {
        // 测试中发现有个结果不符合要求，过滤掉
        if (!(imgURL.endsWith(".jpg") || imgURL.endsWith(".jpeg") || imgURL.endsWith(".png")))
            return;
        System.out.println("堆糖--正在下载：" + imgURL);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            File dest = new File("/Users/alan.chen/Desktop/dmsData/堆糖头像");// 图片下载在d盘下的spider文件夹下
            if (!dest.exists())
                dest.mkdirs();
            // 图片名
            String imgName = imgURL.substring(imgURL.lastIndexOf("/") + 1);
            dest = new File(dest, imgName); // 构建子文件夹
            // 开始下载图片到本地
            URL url = new URL(imgURL);
            bis = new BufferedInputStream(url.openStream());
            bos = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] flush = new byte[1024];
            int len = 0;
            while ((len = bis.read(flush)) != -1) {
                bos.write(flush, 0, len);
            }
            bos.flush(); // 强制刷新
        } catch (IOException e) {
            e.printStackTrace();
            closeAll(bis, bos);
        }
    }

    /**
     * 关闭流的方法
     *
     * @param io
     */
    public static void closeAll(Closeable... io) {
        for (Closeable temp : io) {
            try {
                if (temp != null)
                    temp.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
