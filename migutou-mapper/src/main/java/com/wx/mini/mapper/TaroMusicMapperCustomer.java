package com.wx.mini.mapper;

import com.wx.mini.pojo.TaroMusic;
import com.wx.mini.utils.MyMapper;
import com.wx.mini.vo.TaroMusicVo;

import java.util.List;

/**
 * @author alan.chen
 * @date 2019/12/11 6:55 PM
 */
public interface TaroMusicMapperCustomer extends MyMapper<TaroMusic> {

    List<TaroMusicVo> getAudioListByPage();

    List<TaroMusicVo> getUserAudioListByPage(String userId);
}
