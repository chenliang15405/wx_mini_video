package com.wx.mini.service;

import com.wx.mini.pojo.Videos;
import com.wx.mini.utils.PagedResult;

import java.util.List;

/**
 * @auther alan.chen
 * @time 2019/9/10 9:50 AM
 */
public interface VideoService {

    String mergeBgmAndVideo(String bgmId, String userId, String mvcShowPath, String uploadFilePath, double videoSeconds);

    String saveVideo(String bgmId, String userId, double videoSeconds, int videoHeight, int videoWidth, String desc, String mvcShowPath, String videoCoverPath);

    void updateVideoCoverUrl(String videoId, String mvcShowPath);

    String uploadVideoCoverPath(String mvcVideoShowPath);

    PagedResult getAllVideosByPage(Videos video, Integer isSaveKeyboard, int page, int pageSize);

    List<String> getHotList();

    void likeVideo(String videoId, String publisherId, String userId);

    void unLikeVideo(String videoId, String publisherId, String userId);

    PagedResult getAllLikeVideoByPage(Videos video, Integer page, Integer pageSize);

    PagedResult getAllFollowVideoByPage(Videos video, Integer page, Integer pageSize);
}
