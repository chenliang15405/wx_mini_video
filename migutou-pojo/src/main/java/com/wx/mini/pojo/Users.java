package com.wx.mini.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;

@ApiModel(value = "用户注册对象")
public class Users {

    @ApiModelProperty(hidden = true)
    @Id
    private String id;

    @ApiModelProperty(value = "用户名",name = "username", example = "wxuser",required = true)
    private String username;

    @ApiModelProperty(value = "密码",name = "password", example = "123456",required = true)
    private String password;

    @ApiModelProperty(hidden = true)
    @Column(name = "FACE_IMAGE")
    private String faceImage;

    private String nickname;

    @ApiModelProperty(hidden = true)
    @Column(name = "FANS_COUNTS")
    private Integer fansCounts;

    @ApiModelProperty(hidden = true)
    @Column(name = "FOLLOW_COUNTS")
    private Integer followCounts;

    @ApiModelProperty(hidden = true)
    @Column(name = "RECEIVE_LIKE_COUNTS")
    private Integer receiveLikeCounts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImages) {
        this.faceImage = faceImages;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickName) {
        this.nickname = nickName;
    }

    public Integer getFansCounts() {
        return fansCounts;
    }

    public void setFansCounts(Integer fansCounts) {
        this.fansCounts = fansCounts;
    }

    public Integer getFollowCounts() {
        return followCounts;
    }

    public void setFollowCounts(Integer followCounts) {
        this.followCounts = followCounts;
    }

    public Integer getReceiveLikeCounts() {
        return receiveLikeCounts;
    }

    public void setReceiveLikeCounts(Integer receiveLikeCounts) {
        this.receiveLikeCounts = receiveLikeCounts;
    }
}