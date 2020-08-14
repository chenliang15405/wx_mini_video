package com.wx.mini.controller;

import com.wx.mini.idworker.Sid;
import com.wx.mini.pojo.TaroMusic;
import com.wx.mini.service.AudioService;
import com.wx.mini.utils.IMoocJSONResult;
import com.wx.mini.utils.PagedResult;
import com.wx.mini.utils.PathUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author alan.chen
 * @date 2019/12/9 6:15 PM
 */
@Slf4j
@Api(value = "Taro视频相关业务接口", tags = {"Taro视频业务相关Controller"})
@RestController
@RequestMapping("/taro/audio")
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
    @RequestMapping(value = "/save", method = RequestMethod.PUT)
    public IMoocJSONResult saveMusic(@RequestBody Map<String, Object> map) {
        if(map == null) {
            return IMoocJSONResult.errorMsg("参数不能为空");
        }
        String userId = (String) map.get("userId");
        String musicId = (String) map.get("musicId");
        if(StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(musicId)) {
            // 确认保存视频
            audioService.saveMusic(userId, musicId);
            return IMoocJSONResult.ok();
        }
        return IMoocJSONResult.errorMsg("参数不能为空");
    }

    /**
     * 如果是GET请求使用@RequestParam，并且需要获取到参数，那么请求必须是query类型，就是说在url？xx=yy 这种
     * 如果是是接收form表单类型，那么可以使用post请求，对应使用@RequestParam
     * 如果是接收表单数据是一个对象，可以直接使用对接收即可，相当于加个@ModelAttribute注解，不加也行
     *
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "首页热门列表数据", notes = "首页热门列表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页数", required = true, dataType = "Integer", paramType = "query"), // get请求需要通过@RequestParam获取参数，需要使用query
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = false, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value = "/index/list", method = RequestMethod.GET)
    public IMoocJSONResult index(@RequestParam("page") Integer page, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if(pageSize == null) {
            pageSize = 8;
        }
        PagedResult pagedResult = audioService.getAudioListByPage(page, pageSize);
        return IMoocJSONResult.ok(pagedResult);
    }


    @ApiOperation(value = "用户列表数据", notes = "用户列表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页数", required = true, dataType = "Integer", paramType = "query"), // get请求需要通过@RequestParam获取参数，需要使用query
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = false, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public IMoocJSONResult getUserAudioList(@RequestParam("userId") String userId, @RequestParam("page") Integer page, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if(pageSize == null) {
            pageSize = 9;
        }
        if(StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("参数异常");
        }
        PagedResult pagedResult = audioService.getUserAudioListByPage(userId, page, pageSize);
        return IMoocJSONResult.ok(pagedResult);
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

    @RequestMapping("export")
    public void export(HttpServletResponse response){

        //模拟从数据库获取需要导出的数据
        List<Person> personList = new ArrayList<>();
        Person person1 = new Person("路飞","1",new Date());
        Person person2 = new Person("娜美","2", new Date());
        Person person3 = new Person("索隆","1", new Date());
        Person person4 = new Person("小狸猫","1", new Date());
        personList.add(person1);
        personList.add(person2);
        personList.add(person3);
        personList.add(person4);

        //导出操作
        ExcelUtil.exportExcel(personList,"花名册","草帽一伙",Person.class,"海贼王.xls",response);
    }

}
