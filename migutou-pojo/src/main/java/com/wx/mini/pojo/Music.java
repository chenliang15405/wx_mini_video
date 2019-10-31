package com.wx.mini.pojo;

import lombok.Data;

/**
 * @auther alan.chen
 * @time 2019/10/10 5:41 PM
 */
@Data
public class Music {

    private Integer	id;
    private String	songId;
    private String	title;
    private String	author;
    private String	album;
    private String	URL;
    private int		commentCount;

}
