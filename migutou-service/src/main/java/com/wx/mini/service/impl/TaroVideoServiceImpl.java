package com.wx.mini.service.impl;

import com.wx.mini.mapper.TaroVideoMapper;
import com.wx.mini.pojo.TaroVideo;
import com.wx.mini.service.TaroVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author alan.chen
 * @date 2019/12/11 6:53 PM
 */
@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class TaroVideoServiceImpl implements TaroVideoService {

    @Autowired
    private TaroVideoMapper taroVideoMapper;


    @Override
    public void save(TaroVideo video) {
        // insertSelective 会选择性插入有值的数据，insert会插入所有的数据，即使没有值，也会插入null
        taroVideoMapper.insertSelective(video);
    }

}
