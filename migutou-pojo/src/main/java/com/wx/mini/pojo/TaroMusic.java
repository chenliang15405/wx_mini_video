package com.wx.mini.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author alan.chen
 * @date 2019/12/11 10:21 AM
 */
@Data
@Table(name = "taro_music")
public class TaroMusic {

    /**
     * 禁止播放
     */
    public static final Integer BAN_STATUS = 0;

    /**
     * 正常状态
     */
    public static final Integer NORMAL_STATUS = 1;

    /**
     * 未发布状态
     */
    public static final Integer NOTPUB_STATUS = 2;



    @Id
    private String id;

    private String name;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "music_desc")
    private String musicDesc;

    /**
     * music路径 展示路径
     */
    @Column(name = "music_path")
    private String musicPath;

    /**
     * 保存路径
     */
    @Column(name = "save_path")
    private String savePath;

    /**
     * 关联的视频id
     */
    @Column(name = "video_id")
    private String videoId;

    /**
     * 封面图
     */
    @Column(name = "cover_path")
    private String coverPath;

    /**
     * 喜欢/赞美的数量
     */
    @Column(name = "like_counts")
    private Long likeCounts;

    /**
     * 状态 0 禁止播放 1 正常状态 2 用户已上传未发布状态
     */
    private Integer status;

    @Column(name = "create_date")
    private Date createDate;

}
