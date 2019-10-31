package com.wx.mini.controller;

import com.wx.mini.pojo.Bgm;
import com.wx.mini.service.BgmService;
import com.wx.mini.utils.IMoocJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @auther alan.chen
 * @time 2019/9/7 4:08 PM
 */
@Api(value = "背景音乐相关业务接口", tags = {"背景音乐业务相关Controller"})
@RestController
@RequestMapping("/bgm")
public class BgmController {


    @Autowired
    private BgmService bgmService;

    @ApiOperation(value = "背景音乐列表", notes = "背景音乐列表接口")
    @GetMapping("/list")
    public IMoocJSONResult queryList() {
        List<Bgm> list = bgmService.findList();
        return IMoocJSONResult.ok(list);
    }

}
