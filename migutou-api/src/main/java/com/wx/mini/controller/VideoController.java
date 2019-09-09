package com.wx.mini.controller;

import com.wx.mini.pojo.Bgm;
import com.wx.mini.service.BgmService;
import com.wx.mini.utils.FFMpegCommon;
import com.wx.mini.utils.IMoocJSONResult;
import io.swagger.annotations.*;
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
import java.util.UUID;

/**
 * @auther alan.chen
 * @time 2019/9/7 5:27 PM
 */
@Api(value = "视频相关业务接口", tags = {"视频业务相关Controller"})
@RestController
@RequestMapping("/video")
public class VideoController {


    @Value("${user.upload.face.fileSpace}")
    private String fileSpace;
    @Value("${ffmpeg.path}")
    private String FFMPEG_EXE;

    @Autowired
    private BgmService bgmService;


    @ApiOperation(value = "视频上传头像", notes = "视频上传头像接口")
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
                                      int videoHeight,String desc,@ApiParam(value = "段视频", required = true) MultipartFile file) throws IOException {
        FileOutputStream out = null;
        InputStream in = null;
        String filePath = null;
        // 前端页面显示路径和数据库保存路径
        String mvcShowPath = File.separator + userId + "/video";
        String tempVideoPath = File.separator + userId + "/video/noSoundTemp";
        try {
            if(file != null) {
                String fileName = file.getOriginalFilename();

                if(StringUtils.isNotBlank(fileName)) {
                    // 文件上传的路径
                    filePath = fileSpace + mvcShowPath + File.separator + fileName;
                    File outFile = new File(filePath);

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
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("视频上传失败");
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }

        if(StringUtils.isNotBlank(bgmId)) {
            // 根据bgmid查询bgm信息
            Bgm bgm = bgmService.findById(bgmId);
            String mp3Path = fileSpace + bgm.getPath();
            String videoOutputName = UUID.randomUUID().toString() + ".mp4";
            String noSoundVideoName = UUID.randomUUID().toString() + ".mp4";

            mvcShowPath = mvcShowPath + File.separator + videoOutputName;
            String videoOuputPath = fileSpace + mvcShowPath;
            String noSoundVideoPath = fileSpace + tempVideoPath + File.separator + noSoundVideoName;
            File noSoundFile = new File(noSoundVideoPath);
            if(noSoundFile.getParentFile() != null || !noSoundFile.getParentFile().isDirectory()) {
                noSoundFile.getParentFile().mkdirs();
            }

            mp3Path = mp3Path.replace("\\", "/");
            // 合并视频
            FFMpegCommon common = new FFMpegCommon(FFMPEG_EXE);
            // TODO bgmPath有问题
            common.convertorWithBgmForMac(filePath, videoOuputPath, noSoundVideoPath, mp3Path, videoSeconds);

            System.out.println("mp3: " + mp3Path);
            System.out.println("videoOuputPath" +videoOuputPath);
            System.out.println("mvcShowPath" +mvcShowPath);

        }

        return IMoocJSONResult.ok(mvcShowPath);
    }



}
