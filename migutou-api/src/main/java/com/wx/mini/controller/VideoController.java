package com.wx.mini.controller;

import com.wx.mini.pojo.Users;
import com.wx.mini.pojo.Videos;
import com.wx.mini.service.UserService;
import com.wx.mini.service.VideoService;
import com.wx.mini.utils.IMoocJSONResult;
import com.wx.mini.utils.PagedResult;
import com.wx.mini.vo.PublisherVo;
import com.wx.mini.vo.UserVo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
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
        String videoCoverPath = null;
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

            //上传视频封面
            videoCoverPath = videoService.uploadVideoCoverPath(mvcShowPath);

        }
        // 保存video信息到数据库
        String videoId = videoService.saveVideo(bgmId, userId, videoSeconds, videoHeight, videoWidth, desc, mvcShowPath, videoCoverPath);

        return IMoocJSONResult.ok(videoId);
    }


    /**
     * 获取分页video列表，并通过关键字搜索时分页查询且保存关键词
     *
     * @param video
     * @param isSaveKeyboard
     * @param page
     * @return
     */
    @ApiImplicitParams({ // query表示该参数是在url上面拼接的，path是路径上面的， form是表单中的参数
            @ApiImplicitParam(name = "isSaveKeyboard", value = "是否需要保存关键词", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "page", value = "当前页数", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/showAll")
    public IMoocJSONResult pageVideos(@RequestBody Videos video, Integer isSaveKeyboard, Integer page) {
        if(page == null) {
            page = 1;
        }
        Integer pageSize = 5;
        PagedResult pagedResult = videoService.getAllVideosByPage(video, isSaveKeyboard, page, pageSize);
        return IMoocJSONResult.ok(pagedResult);
    }

    /**
     * 查询热门搜索关键词列表
     * @return
     */
    @ApiOperation(value = "查询热门搜索列表", notes = "查询热门搜索列表接口")
    @GetMapping("/hot/search")
    public IMoocJSONResult hotList() {
        return IMoocJSONResult.ok(videoService.getHotList());
    }


    /**
     * 视频点赞
     *
     * @param videoId 点赞的视频id
     * @param publisherId 发布视频用户id
     * @param userId 当前操作用户id
     * @return
     */
    @ApiOperation(value = "查询热门搜索列表", notes = "查询热门搜索列表接口")
    @ApiImplicitParams({ // query表示该参数是在url上面拼接的，path是路径上面的， form是表单中的参数
            @ApiImplicitParam(name = "videoId", value = "视频id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "publisherId", value = "视频发布者用户id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "userId", value = "登录用户ID", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/like")
    public IMoocJSONResult likeVideo(@RequestParam("videoId") String videoId,
                                     @RequestParam("publisherId") String publisherId,@RequestParam("userId") String userId) {
        if(StringUtils.isBlank(videoId) || StringUtils.isBlank(publisherId) || StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户数据异常");
        }
        videoService.likeVideo(videoId, publisherId, userId);

        return IMoocJSONResult.ok();
    }

    /**
     * 视频取消点赞
     *
     * @param videoId 点赞的视频id
     * @param publisherId 发布视频用户id
     * @param userId 当前操作用户id
     * @return
     */
    @ApiImplicitParams({ // query表示该参数是在url上面拼接的，path是路径上面的， form是表单中的参数
            @ApiImplicitParam(name = "videoId", value = "视频id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "publisherId", value = "视频发布者用户id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "userId", value = "登录用户ID", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/unlike")
    public IMoocJSONResult unLikeVideo(@RequestParam("videoId") String videoId,
                                     @RequestParam("publisherId") String publisherId,@RequestParam("userId") String userId) {
        if(StringUtils.isBlank(videoId) || StringUtils.isBlank(publisherId) || StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户数据异常");
        }
        videoService.unLikeVideo(videoId, publisherId, userId);

        return IMoocJSONResult.ok();
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
