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
 * @time 2019/9/8 9:17 PM
 */
public class FFMpegCommon {


    private String ffmpegEXE; // ffmpeg文件路径

    public FFMpegCommon(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    /**
     * ffmgpeg执行转换视频方法
     * TODO 后面可以做一个jar依赖
     *
     *
     * @param videoInputPath
     * @param videoOutPath
     * @return
     */
    public boolean convertor(String videoInputPath, String videoOutPath) {
//        ffmpeg -i input.mp4 out.mp4
        try {
            List<String> commands = Lists.newArrayList();
            commands.add(ffmpegEXE);
            commands.add("-i");
            commands.add(videoInputPath);
            commands.add("-y");
            commands.add(videoOutPath);

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


    /**
     * ffmpeg 转换视频，并加上bgm
     *
     * @param videoInputPath
     * @param videoOutPath
     * @param bgmInputPath
     * @param seconds
     * @return
     */
    public boolean convertorWithBgm(String videoInputPath, String videoOutPath, String bgmInputPath, double seconds) {
//        ffmpeg -i input.mp4 -i bgm.mp3 -t 7 -y out.mp4
        try {
            List<String> commands = Lists.newArrayList();
            commands.add(ffmpegEXE);

            commands.add("-i");
            commands.add(videoInputPath);

            commands.add("-i");
            commands.add(bgmInputPath);

            commands.add("-t");
            commands.add(String.valueOf(seconds));

            commands.add("-y");
            commands.add(videoOutPath);

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

    /**
     * 合并音频和视频的另一种方式，Mac可以使用
     * @param videoInputPath
     * @param videoOutPath
     * @param bgmInputPath
     * @param seconds
     * @return
     */
    public boolean convertorWithBgmForMac(String videoInputPath, String videoOutPath, String noSoundVideoPath,String bgmInputPath, double seconds) {
//        ffmpeg -i G:\hi.mp4 -c:v copy -an G:\nosound.mp4
//        ffmpeg -i G:\nosound.mp4 -i G:\songs.mp3 -t 7.1 -c copy G:\output.mp4
        try {
            System.out.println("111111-----------");
            List<String> commands = Lists.newArrayList();
            commands.add(ffmpegEXE);

            commands.add("-i");
            commands.add(videoInputPath);

            commands.add("-c:v");
            commands.add("copy");
            commands.add("-an");
            commands.add(noSoundVideoPath);

            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();

            // 获取到error流
            InputStream errorStream = process.getErrorStream();
            releaseErrorStream(errorStream);

//            Thread.sleep(3000);
            System.out.println("睡眠启动-----------");
            // 转换nosound.mp4 为背景音乐的mp4
            convertNoSoundVideoToBgmVideo(videoOutPath,noSoundVideoPath,bgmInputPath,seconds);

            return true;
        } catch (IOException e) {
            System.out.println("错误信息" + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 真正将没有背景音乐的video转换为有背景音乐video
     *
     * @param videoOutPath
     * @param noSoundVideoPath
     * @param bgmInputPath
     * @param seconds
     * @throws IOException
     */
    public void convertNoSoundVideoToBgmVideo(String videoOutPath, String noSoundVideoPath,String bgmInputPath, double seconds) throws IOException {
        List<String> commands2 = Lists.newArrayList();
        commands2.add(ffmpegEXE);

        commands2.add("-i");
        commands2.add(noSoundVideoPath);

        commands2.add("-i");
        commands2.add(bgmInputPath);

        commands2.add("-t");
        commands2.add(String.valueOf(seconds));

        commands2.add("-y");
        commands2.add(videoOutPath);

        ProcessBuilder builder1 = new ProcessBuilder(commands2);
        Process process1 = builder1.start();

        // 获取到error流
        InputStream errorStream1 = process1.getErrorStream();
        releaseErrorStream(errorStream1);
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



    public static void main(String[] args) {
            /*
            * 去掉原视频音轨
                E:\anzhuangbao\ffmpeg\bin\ffmpeg -i G:\hi.mp4 -c:v copy -an G:\nosound.mp4
                添加背景音乐
                E:\anzhuangbao\ffmpeg\bin\ffmpeg -i G:\nosound.mp4 -i G:\songs.mp3 -t 7.1 -c copy G:\output.mp4

             */
            /*
            * 链接mp3重复
            * ffmpeg -f concat -i list.txt -c copy out.mp3
            *
            * 截取mp3长度为指定长度
            *  ffmpeg -i out.mp3 -ss 00:00:00.0 -t 00:06:38 -acodec copy love3.mp3

                合成yin音频和视频
               ffmpeg -i angle.mp4 -i love3.mp3 -c copy output.mp4

            */

            /*
            * 合成图片为视频带音频
            * ffmpeg -threads2 -y -r 10 -i /tmpdir/image%04d.jpg -i audio.mp3 -absf aac_adtstoasc output.mp4
            *
            * 合成图片为视频不带音频
            *
            *   ffmpeg -loop 1 -f image2 -i /tmpdir/image%04d.jpg -vcodec libx264 -r 10 -t 10 test.mp4
            */

            /*
                使用ffmpeg AVfilter 中的amix实现混音
            *ffmpeg -i take.mp3 -i pingfan.mp3 -filter_complex amix=inputs=2:duration=first:dropout_transition=2 outputnew2.mp3
            *
            */

        String ffmpeg = "/Users/alan.chen/Documents/notes/ffmpeg";
        FFMpegCommon ffMpegCommon = new FFMpegCommon(ffmpeg);

        String videoInputPath = "/Users/alan.chen/Documents/notes/2222.mp4";
        String nosoundPath = "/Users/alan.chen/Documents/notes/nosound.mp4";
        String videoOutPath = "/Users/alan.chen/Documents/notes/3333.mp4";
        String bgmPath = "/Users/alan.chen/Documents/notes/young.mp3";


        ffMpegCommon.convertorWithBgmForMac(videoInputPath, videoOutPath,nosoundPath,bgmPath,5.0) ;
    }

}
