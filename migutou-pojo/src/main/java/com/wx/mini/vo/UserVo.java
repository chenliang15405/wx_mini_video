package com.wx.mini.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(value = "用户注册对象")
public class UserVo {

    @ApiModelProperty(hidden = true)
    private String id;

    @ApiModelProperty(value = "用户名",name = "username", example = "wxuser",required = true)
    private String username;

    @ApiModelProperty(value = "密码",name = "password", example = "123456",required = true)
    @JsonIgnore
    private String password;

    @ApiModelProperty(hidden = true)
    private String token;

    @ApiModelProperty(hidden = true)
    private String faceImage;

    private String nickname;

    @ApiModelProperty(hidden = true)
    private Integer fansCounts;

    @ApiModelProperty(hidden = true)
    private Integer followCounts;

    @ApiModelProperty(hidden = true)
    private Integer receiveLikeCounts;

    @ApiModelProperty(hidden = true)
    private boolean isFollow;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public boolean getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(boolean follow) {
        isFollow = follow;
    }
}