package com.wx.mini.service;

import com.wx.mini.pojo.Bgm;

import java.util.List;

/**
 * @auther alan.chen
 * @time 2019/9/7 4:16 PM
 */
public interface BgmService {

    List<Bgm> findList();

    Bgm findById(String bgmId);
}
