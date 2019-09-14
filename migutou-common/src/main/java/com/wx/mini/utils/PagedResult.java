package com.wx.mini.utils;

import lombok.Data;

import java.util.List;

/**
 * 封装分页后的数据格式
 *
 * @auther alan.chen
 * @time 2019/9/14 2:26 PM
 */
@Data
public class PagedResult {

    private int page; // 当前页数

    private int totalPages; // 总页数

    private long records; // 总记录数

    private List<?> rows; // 每页显示的内容

}
