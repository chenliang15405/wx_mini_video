package com.wx.mini.service.impl;

import com.wx.mini.idworker.Sid;
import com.wx.mini.mapper.VideosMapper;
import com.wx.mini.pojo.Bgm;
import com.wx.mini.pojo.Videos;
import com.wx.mini.service.BgmService;
import com.wx.mini.service.VideoService;
import com.wx.mini.utils.FFMpegCommon;
import com.wx.mini.vo.VideoStatusEnum;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * @auther alan.chen
 * @time 2019/9/10 9:50 AM
 */
@Slf4j
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideosMapper videosMapper;

    @Autowired
    private BgmService bgmService;

    @Value("${user.upload.face.fileSpace}")
    private String fileSpace;
    @Value("${ffmpeg.path}")
    private String FFMPEG_EXE;



    /**
     * 合成视频和音频
     *
     * @param bgmId
     * @param userId
     * @param mvcShowPath
     * @param uploadFilePath
     * @param videoSeconds
     * @return
     */
    @Override
    public String mergeBgmAndVideo(String bgmId, String userId, String mvcShowPath, String uploadFilePath, double videoSeconds) {
        String tempVideoPath = File.separator + userId + "/video/noSoundTemp";
        // 根据bgmid查询bgm信息
        Bgm bgm = bgmService.findById(bgmId);
        String mp3Path = fileSpace + bgm.getPath();
        String videoOutputName = UUID.randomUUID().toString() + ".mp4";
        String noSoundVideoName = UUID.randomUUID().toString() + ".mp4";

        mvcShowPath = mvcShowPath + File.separator + "bgmVideo" +File.separator + videoOutputName;
        String videoOuputPath = fileSpace + mvcShowPath;
        String noSoundVideoPath = fileSpace + tempVideoPath + File.separator + noSoundVideoName;
        File noSoundFile = new File(noSoundVideoPath);
        File videoOuputPathFile = new File(videoOuputPath);
        if(noSoundFile.getParentFile() != null || !noSoundFile.getParentFile().isDirectory()) {
            noSoundFile.getParentFile().mkdirs();
        }
        if(videoOuputPathFile.getParentFile() != null || !videoOuputPathFile.getParentFile().isDirectory()) {
            videoOuputPathFile.getParentFile().mkdirs();
        }

//        mp3Path = mp3Path.replace("\\", "/");
        // 合并视频
        FFMpegCommon common = new FFMpegCommon(FFMPEG_EXE);
        common.convertorWithBgmForMac(uploadFilePath, videoOuputPath, noSoundVideoPath, mp3Path, videoSeconds);

        return mvcShowPath;
    }

    /**
     * 保存video
     * @param bgmId
     * @param userId
     * @param videoSeconds
     * @param videoHeight
     * @param videoWidth
     * @param desc
     * @param mvcShowPath
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveVideo(String bgmId, String userId, double videoSeconds, int videoHeight, int videoWidth, String desc, String mvcShowPath) {
        Videos videos = new Videos();
        String id = Sid.next();
        videos.setId(id);
        videos.setAudioId(bgmId);
        videos.setUserId(userId);
        videos.setCreateTime(new Date());
        videos.setVideoHeight(videoHeight);
        videos.setVideoSeconds((float) videoSeconds);
        videos.setVideoWidth(videoWidth);
        videos.setVideoDesc(desc);
        videos.setVideoPath(mvcShowPath);
        videos.setLikeCounts(0L);
        videos.setStatus(VideoStatusEnum.SUCCESS.value);
        videosMapper.insertSelective(videos);
        return id;
    }

    /**
     * 更新video的封面图
     * @param videoId
     * @param mvcShowPath
     */
    @Override
    public void updateVideoCoverUrl(String videoId, String mvcShowPath) {
        Videos videos = new Videos();
        videos.setId(videoId);
        videos.setCoverPath(mvcShowPath);
        videosMapper.updateByPrimaryKeySelective(videos);
    }


}
