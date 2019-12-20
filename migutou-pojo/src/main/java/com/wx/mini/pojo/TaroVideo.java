package com.wx.mini.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class TaroVideo {
    @Id
    private String id;

    /**
     * 发布者id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 视频存放的路径
     */
    @Column(name = "video_path")
    private String videoPath;

    /**
     * 视频秒数
     */
    @Column(name = "video_seconds")
    private Float videoSeconds;

    /**
     * 视频宽度
     */
    @Column(name = "video_width")
    private Integer videoWidth;

    /**
     * 视频高度
     */
    @Column(name = "video_height")
    private Integer videoHeight;


    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取发布者id
     *
     * @return user_id - 发布者id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置发布者id
     *
     * @param userId 发布者id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }


    /**
     * 获取视频存放的路径
     *
     * @return video_path - 视频存放的路径
     */
    public String getVideoPath() {
        return videoPath;
    }

    /**
     * 设置视频存放的路径
     *
     * @param videoPath 视频存放的路径
     */
    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    /**
     * 获取视频秒数
     *
     * @return video_seconds - 视频秒数
     */
    public Float getVideoSeconds() {
        return videoSeconds;
    }

    /**
     * 设置视频秒数
     *
     * @param videoSeconds 视频秒数
     */
    public void setVideoSeconds(Float videoSeconds) {
        this.videoSeconds = videoSeconds;
    }

    /**
     * 获取视频宽度
     *
     * @return video_width - 视频宽度
     */
    public Integer getVideoWidth() {
        return videoWidth;
    }

    /**
     * 设置视频宽度
     *
     * @param videoWidth 视频宽度
     */
    public void setVideoWidth(Integer videoWidth) {
        this.videoWidth = videoWidth;
    }

    /**
     * 获取视频高度
     *
     * @return video_height - 视频高度
     */
    public Integer getVideoHeight() {
        return videoHeight;
    }

    /**
     * 设置视频高度
     *
     * @param videoHeight 视频高度
     */
    public void setVideoHeight(Integer videoHeight) {
        this.videoHeight = videoHeight;
    }


    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}