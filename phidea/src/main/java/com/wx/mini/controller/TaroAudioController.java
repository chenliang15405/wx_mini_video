package com.wx.mini.controller;

import com.wx.mini.idworker.Sid;
import com.wx.mini.pojo.TaroMusic;
import com.wx.mini.service.AudioService;
import com.wx.mini.utils.IMoocJSONResult;
import com.wx.mini.utils.PathUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author alan.chen
 * @date 2019/12/9 6:15 PM
 */
@Slf4j
@Api(value = "Taro视频相关业务接口", tags = {"Taro视频业务相关Controller"})
@RestController
@RequestMapping("/taro/video")
public class TaroAudioController {

    @Value("${user.upload.face.fileSpace}")
    private String fileSpace;

    @Autowired
    private AudioService audioService;



    @ApiOperation(value = "视频上传生成音频", notes = "视频上传接口生成音频")
    @ApiImplicitParams({ // query表示该参数是在url上面拼接的，path是路径上面的， form是表单中的参数
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "音频名称", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "desc", value = "视频描述", required = false, dataType = "String", paramType = "form")
    })
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public IMoocJSONResult handlerAudio(String userId, String name, String desc, @ApiParam(value = "视频文件", required = true) MultipartFile file) {

        String filename = null;
        String mvcShowPath = File.separator + userId + "/video";

        if(file != null) {
            // filename = file.getOriginalFilename();
            filename = PathUtil.replaceFileName(file.getOriginalFilename(), Sid.next());
            if(StringUtils.isNotBlank(filename)) {
                String videoUploadPath = fileSpace + mvcShowPath + File.separator + filename;
                boolean flag = saveVideoFile(videoUploadPath, filename, file);
                if(!flag) {
                    return IMoocJSONResult.errorMsg("服务器异常，上传失败");
                }
                log.info("开始处理视频文件 {}", filename);
                TaroMusic music = audioService.handlerDiyMusic(videoUploadPath, mvcShowPath, userId, name, desc, filename);
                log.info("视频文件处理完成: {}", music);
                return IMoocJSONResult.ok(music);
            }
        }

        return IMoocJSONResult.errorMsg("参数异常");
    }


    @ApiOperation(value = "确认生成音频接口", notes = "确认音频并发布")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "musicId", value = "音频ID", required = true, dataType = "String", paramType = "form")
    })
    public IMoocJSONResult saveMusic(@RequestParam("userId") String userId, @RequestParam("musicId") String musicId) {
        if(StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(musicId)) {
            // 确认保存视频
            audioService.saveMusic(userId, musicId);
            return IMoocJSONResult.ok();
        }
        return IMoocJSONResult.errorMsg("参数不能为空");
    }


    private boolean saveVideoFile(String videoUploadPath, String filename, MultipartFile file) {
        try {
            FileOutputStream out = null;
            InputStream in = null;
            // 文件上传的路径
            File videUploadFile = new File(videoUploadPath);

            if(videUploadFile.getParentFile() != null || !videUploadFile.getParentFile().isDirectory()) {
                videUploadFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(videUploadFile);
            in = file.getInputStream();

            IOUtils.copy(in, out);

            return true;
        } catch (IOException e) {
            log.error("上传视频报错：", e);
        }
        return false;
    }

}
