package com.wx.mini.mapper;

import com.wx.mini.pojo.SearchRecords;
import com.wx.mini.utils.MyMapper;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
    List<String> getHotList();

}