package com.wx.mini.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author alan.chen
 * @date 2019/12/24 10:47 PM
 */
@Data
public class TaroMusicVo {


    private String id;

    /**
     * 音乐名称
     */
    private String name;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 音乐描述
     */
    private String musicDesc;

    /**
     * music路径 展示路径
     */
    private String musicPath;

    /**
     * 保存路径
     */
    private String savePath;

    /**
     * 关联的视频id
     */
    private String videoId;

    /**
     * 封面图
     */
    private String coverPath;

    /**
     * 喜欢/赞美的数量
     */
    private Long likeCounts;

    /**
     * 状态 0 禁止播放 1 正常状态 2 用户已上传未发布状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 头像图片路径
     */
    private String faceImage;

    /**
     * 用户昵称
     */
    private String nickname;

}
