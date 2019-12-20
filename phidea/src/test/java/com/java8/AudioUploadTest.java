package com.java8;

import com.ffmpeg.common.response.Result;
import com.ffmpeg.common.video.VideoOperation;
import com.wx.mini.service.VideoService;
import com.wx.mini.service.impl.VideoServiceImpl;
import com.wx.mini.utils.PathUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * @author alan.chen
 * @date 2019/12/10 10:42 AM
 */
public class AudioUploadTest {

    private VideoService videoService;
//    private final VideoOperation videoOperator = VideoOperation.builder("/Users/alan.chen/Documents/notes/ffmpeg");


    @Before
    public void setup() {
        videoService = new VideoServiceImpl();
    }

    @Test
    public void getCoverImg() {
        VideoOperation videoOperator = VideoOperation.builder("/Users/alan.chen/Documents/notes/ffmpeg");
        String videoUploadPath = "/Users/alan.chen/Desktop/dmsData/1001/video/1.mp4";
        String mvcShowPath = "/Users/alan.chen/Desktop/dmsData/1001/video";
        String filename = videoUploadPath.substring(videoUploadPath.lastIndexOf(File.separator) + 1);
        String imgPath = mvcShowPath + File.separator +  filename.substring(0, filename.lastIndexOf(".")) + ".png";
        Result result = videoOperator.getVideoCoverImg(videoUploadPath, imgPath);
//        System.out.println(result.getErrMessage());
        System.out.println(result.getCode());
    }

    @Test
    public void getPath() {
        String videoUploadPath = "/Users/alan.chen/Desktop/dmsData/1001/video/1.mp4";
        String mvcShowPath = "/1001/video";
        String s = generatePath(videoUploadPath, mvcShowPath, ".mp3");
        System.out.println(s);
        Assert.assertEquals("/Users/alan.chen/Desktop/dmsData/1001/video/1.mp3", s);
    }


    private String generatePath(String sourcePath, String showPath, String targetSuffix) {
        String filename = sourcePath.substring(sourcePath.lastIndexOf(File.separator) + 1);
        sourcePath = sourcePath.substring(0, sourcePath.lastIndexOf(File.separator));
        filename = filename.split("\\.")[0];
        String targetpath = sourcePath + File.separator +  filename + targetSuffix;
        return targetpath;
    }

    @Test
    public void pathUtilTest() {
        String videoUploadPath = "/Users/alan.chen/Desktop/dmsData/1001/video/1.mp4";
        String newfilename = "12323932013012";
        String newPath = PathUtil.generateTargetPath(videoUploadPath, newfilename);
        System.out.println(newPath);
        Assert.assertEquals("/Users/alan.chen/Desktop/dmsData/1001/video/" + newfilename + ".mp4",newPath);
    }

}
