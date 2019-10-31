package com.wx.mini.pojo;

import lombok.Data;

/**
 * @auther alan.chen
 * @time 2019/10/10 5:42 PM
 */
@Data
public class Comment {

    private Integer	id;
    private String	songId;
    private String	nickname;
    private Integer	likedCount;
    private String	content;
    private String time;
    private int commentId;

}
