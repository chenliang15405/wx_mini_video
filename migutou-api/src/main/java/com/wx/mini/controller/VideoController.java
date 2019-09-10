package com.wx.mini.controller;

import com.wx.mini.service.BgmService;
import com.wx.mini.service.VideoService;
import com.wx.mini.utils.IMoocJSONResult;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @auther alan.chen
 * @time 2019/9/7 5:27 PM
 */
@Slf4j
@Api(value = "视频相关业务接口", tags = {"视频业务相关Controller"})
@RestController
@RequestMapping("/video")
public class VideoController {


    @Value("${user.upload.face.fileSpace}")
    private String fileSpace;

    @Autowired
    private VideoService videoService;


    @ApiOperation(value = "视频上传", notes = "视频上传接口")
    @ApiImplicitParams({ // query表示该参数是在url上面拼接的，path是路径上面的， form是表单中的参数
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "form"),
        @ApiImplicitParam(name = "bgmId", value = "背景音乐id", required = true, dataType = "String", paramType = "form"),
        @ApiImplicitParam(name = "videoSeconds", value = "视频播放长度", required = true, dataType = "double", paramType = "form"),
        @ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true, dataType = "int", paramType = "form"),
        @ApiImplicitParam(name = "videoHeight", value = "视频长度", required = true, dataType = "int", paramType = "form"),
        @ApiImplicitParam(name = "desc", value = "视频描述", required = false, dataType = "String", paramType = "form")
    })
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public IMoocJSONResult uploadFace(String userId, String bgmId,double videoSeconds,int videoWidth,
                                      int videoHeight,String desc,@ApiParam(value = "段视频", required = true) MultipartFile file) {
        FileOutputStream out = null;
        InputStream in = null;
        String uploadFilePath = null;
        // 前端页面显示路径和数据库保存路径
        String mvcShowPath = File.separator + userId + "/video";
        try {
            if(file != null) {
                String fileName = file.getOriginalFilename();

                if(StringUtils.isNotBlank(fileName)) {
                    // 文件上传的路径
                    uploadFilePath = fileSpace + mvcShowPath + File.separator + fileName;
                    File outFile = new File(uploadFilePath);

                    if(outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        outFile.getParentFile().mkdirs();
                    }
                    out = new FileOutputStream(outFile);
                    in = file.getInputStream();

                    IOUtils.copy(in, out);
                }
            } else {
                return IMoocJSONResult.errorMsg("视频上传失败");
            }
        } catch (IOException e) {
            log.error("视频上传异常：", e);
            return IMoocJSONResult.errorMsg("视频上传失败");
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }

        if(StringUtils.isNotBlank(bgmId)) {
            mvcShowPath = videoService.mergeBgmAndVideo(bgmId, userId, mvcShowPath, uploadFilePath, videoSeconds);

            log.info("视频上传完成  ", mvcShowPath);

        }
        // 保存video信息到数据库
        String videoId = videoService.saveVideo(bgmId, userId, videoSeconds, videoHeight, videoWidth, desc, mvcShowPath);

        return IMoocJSONResult.ok(videoId);
    }



    @ApiOperation(value = "视频封面上传", notes = "视频封面上传接口")
    @ApiImplicitParams({ // query表示该参数是在url上面拼接的，path是路径上面的， form是表单中的参数
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoId", value = "视频id", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping(value = "/uploadCover", headers = "content-type=multipart/form-data")
    public IMoocJSONResult uploadFace(String userId, String videoId,
                                      @ApiParam(value = "视频封面", required = true) MultipartFile file) {
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(videoId)) {
            return IMoocJSONResult.errorMsg("系统异常");
        }

        FileOutputStream out = null;
        InputStream in = null;
        String uploadFilePath = null;
        // 前端页面显示路径和数据库保存路径
        String mvcShowPath = File.separator + userId + "/video/bgmVideo";
        try {
            if(file != null) {
                String fileName = file.getOriginalFilename();

                if(StringUtils.isNotBlank(fileName)) {
                    // 文件上传的路径
                    uploadFilePath = fileSpace + mvcShowPath + File.separator + fileName;
                    mvcShowPath = mvcShowPath + File.separator + fileName;
                    File outFile = new File(uploadFilePath);

                    out = new FileOutputStream(outFile);
                    in = file.getInputStream();

                    IOUtils.copy(in, out);
                }
            } else {
                return IMoocJSONResult.errorMsg("视频封面上传失败");
            }
        } catch (IOException e) {
            log.error("视频封面上传异常：", e);
            return IMoocJSONResult.errorMsg("视频封面上传失败");
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }

        // 更新video封面图url
        videoService.updateVideoCoverUrl(videoId, mvcShowPath);

        return IMoocJSONResult.ok();
    }


}
