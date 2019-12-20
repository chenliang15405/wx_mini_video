package com.wx.mini.service;

import com.wx.mini.pojo.TaroMusic;

/**
 * @author alan.chen
 * @date 2019/12/10 11:12 AM
 */
public interface AudioService {

    TaroMusic handlerDiyMusic(String videoUploadPath, String mvcShowPath, String userId, String name, String desc, String filename);

    void saveMusic(String userId, String musicId);

}
