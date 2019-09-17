package com.wx.mini.controller;

import com.wx.mini.pojo.Users;
import com.wx.mini.service.UserService;
import com.wx.mini.utils.IMoocJSONResult;
import com.wx.mini.vo.PublisherVo;
import com.wx.mini.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @auther alan.chen
 * @time 2019/9/6 9:52 AM
 */
@Api(value = "用户相关业务接口", tags = {"用户业务相关Controller"})
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${user.upload.face.fileSpace}")
    private String fileSpace;


    @ApiOperation(value = "用户上传头像", notes = "用户上传头像接口")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
    @PostMapping("/uploadFace")
    public IMoocJSONResult uploadFace(String userId, @RequestParam("file") MultipartFile[] file) {
        FileOutputStream out = null;
        InputStream in = null;
        String filePath = null;
        // 前端页面显示路径和数据库保存路径
        String mvcShowPath = File.separator + userId + "/face";;
        try {
            if(file != null && file.length > 0) {
                String fileName = file[0].getOriginalFilename();

                if(StringUtils.isNotBlank(fileName)) {
                    // 文件上传的路径
                    filePath = fileSpace + mvcShowPath + File.separator + fileName;
                    mvcShowPath = mvcShowPath + File.separator + fileName;
                    File outFile = new File(filePath);

                    if(outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        outFile.getParentFile().mkdirs();
                    }
                    out = new FileOutputStream(outFile);
                    in = file[0].getInputStream();

                    IOUtils.copy(in, out);
                }
            } else {
                return IMoocJSONResult.errorMsg("上传失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传失败");
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }

        // 更新用户信息
        Users user = new Users();
        user.setId(userId);
        user.setFaceImage(mvcShowPath);
        userService.update(user);

        return IMoocJSONResult.ok(mvcShowPath);
    }


    @ApiOperation(value = "查询用户信息", notes = "查询用户信息接口")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
    @GetMapping("/query")
    public IMoocJSONResult query(String userId) {
        if(StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户id为空");
        }
        Users user = userService.findById(userId);

        UserVo vo = new UserVo();
        BeanUtils.copyProperties(user, vo);
        return IMoocJSONResult.ok(vo);
    }

    /**
     * 查询视频发布者信息
     *
     * @param loginUserId
     * @param publisherId
     * @param videoId
     * @return
     */
    @ApiImplicitParams({ // query表示该参数是在url上面拼接的，path是路径上面的， form是表单中的参数
            @ApiImplicitParam(name = "videoId", value = "视频id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "publisherId", value = "视频发布者用户id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "loginUserId", value = "登录用户ID", required = true, dataType = "String", paramType = "form"),
    })
    @GetMapping("/queryPublisher")
    public IMoocJSONResult queryPublisher(@RequestParam("loginUserId") String loginUserId,
                                          @RequestParam("publisherId") String publisherId,@RequestParam("videoId") String videoId) {
        if(StringUtils.isBlank(videoId) || StringUtils.isBlank(publisherId) || StringUtils.isBlank(loginUserId)) {
            return IMoocJSONResult.errorMsg("用户数据异常");
        }
        // 查询发布者用户数据
        Users user = userService.findById(publisherId);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);

        // 查询登录用户和视频发布者是否点赞
        boolean isLike = userService.isLikeUserVideo(loginUserId, videoId);

        PublisherVo vo = new PublisherVo();
        vo.setUserVo(userVo);
        vo.setLikeVideo(isLike);

        return IMoocJSONResult.ok(vo);
    }


}
