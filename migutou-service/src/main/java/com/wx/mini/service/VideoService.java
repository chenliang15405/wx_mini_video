package com.wx.mini.service;

/**
 * @auther alan.chen
 * @time 2019/9/10 9:50 AM
 */
public interface VideoService {

    String mergeBgmAndVideo(String bgmId, String userId, String mvcShowPath, String uploadFilePath, double videoSeconds);

    String saveVideo(String bgmId, String userId, double videoSeconds, int videoHeight, int videoWidth, String desc, String mvcShowPath, String videoCoverPath);

    void updateVideoCoverUrl(String videoId, String mvcShowPath);

    String uploadVideoCoverPath(String mvcVideoShowPath);
}
