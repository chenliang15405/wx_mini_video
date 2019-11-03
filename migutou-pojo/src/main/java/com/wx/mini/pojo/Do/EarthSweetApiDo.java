package com.wx.mini.pojo.Do;

import lombok.Data;

import java.util.List;

/**
 * @author alan.chen
 * @date 2019/11/2 10:36 AM
 */
@Data
public class EarthSweetApiDo {

    private String code;

    private String message;

    private List<String> returnObj;

}
