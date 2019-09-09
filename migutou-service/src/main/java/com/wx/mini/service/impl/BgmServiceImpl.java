package com.wx.mini.service.impl;

import com.wx.mini.mapper.BgmMapper;
import com.wx.mini.pojo.Bgm;
import com.wx.mini.service.BgmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @auther alan.chen
 * @time 2019/9/7 4:16 PM
 */
@Transactional
@Service
public class BgmServiceImpl implements BgmService {


    @Autowired
    private BgmMapper bgmMapper;

    @Override
    public List<Bgm> findList() {
        return bgmMapper.selectAll();
    }

    @Override
    public Bgm findById(String bgmId) {
        return bgmMapper.selectByPrimaryKey(bgmId);
    }
}
