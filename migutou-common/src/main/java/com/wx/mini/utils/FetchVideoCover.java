package com.wx.mini.utils;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @auther alan.chen
 * @time 2019/9/10 11:18 PM
 */
public class FetchVideoCover {

    private String ffmpegEXE; // ffmpeg文件路径

    public FetchVideoCover(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public static FetchVideoCover builder(String ffmpegEXE) {
        return new FetchVideoCover(ffmpegEXE);
    }


    /**
     * 对视频进行截取，获取视频封面图
     *
     * @param videoInputPath
     * @param coverOutPath
     * @return
     */
    public boolean getVideoCover(String videoInputPath, String coverOutPath) {
//        ffmpeg -ss 00:00:01 -y -i input.mp4 -vframes 1 out.jpg
//        png比较大
        try {
            List<String> commands = Lists.newArrayList();
            commands.add(ffmpegEXE);

            commands.add("-ss");
            commands.add("00:00:01");

            commands.add("-y");
            commands.add("-i");
            commands.add(videoInputPath);

            commands.add("-vframes");
            commands.add("1");

            commands.add(coverOutPath);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            // 获取到error流
            InputStream errorStream = process.getErrorStream();

            releaseErrorStream(errorStream);

            return true;
        } catch (IOException e) {
            System.out.println("错误信息" + e);
        }
        return false;
    }


    private void releaseErrorStream(InputStream errorStream) throws IOException {
        InputStreamReader reader = new InputStreamReader(errorStream);
        BufferedReader br = new BufferedReader(reader);
        // 读取error stream，就标示将该流释放，防止堵塞
        String line = "";
        while ((line = br.readLine())!= null) {
        }

        IOUtils.closeQuietly(errorStream);
        IOUtils.closeQuietly(reader);
        IOUtils.closeQuietly(br);
    }


}
