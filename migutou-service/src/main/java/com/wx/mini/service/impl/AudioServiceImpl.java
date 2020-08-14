package com.wx.mini.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wx.mini.idworker.Sid;
import com.wx.mini.mapper.TaroMusicMapper;
import com.wx.mini.mapper.TaroMusicMapperCustomer;
import com.wx.mini.pojo.TaroMusic;
import com.wx.mini.pojo.TaroVideo;
import com.wx.mini.service.AudioService;
import com.wx.mini.service.TaroVideoService;
import com.wx.mini.service.VideoService;
import com.wx.mini.utils.PagedResult;
import com.wx.mini.utils.PathUtil;
import com.wx.mini.vo.TaroMusicVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author alan.chen
 * @date 2019/12/10 11:12 AM
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AudioServiceImpl implements AudioService {

    @Autowired
    private VideoService videoService;

    @Autowired
    private TaroVideoService taroVideoService;
    @Autowired
    private TaroMusicMapper taroMusicMapper;
    @Autowired
    private TaroMusicMapperCustomer taroMusicMapperCustomer;


    @Override
    public TaroMusic handlerDiyMusic(String videoUploadPath, String mvcShowPath, String userId, String name, String desc, String randomFilename) {
        // 获取视图封面图
        String coverImgPath = videoService.getVideoCovertImg(videoUploadPath, mvcShowPath);
        // 分离视频中音频
        String musicPath = videoService.getAudioFromVideo(videoUploadPath, mvcShowPath);
        log.info("上传视频路径：[{}], 生成封面图路径：[{}] , 生成音频路径：[{}], 文件名称：[{}]", videoUploadPath, coverImgPath, musicPath, randomFilename);
        TaroMusic music = null;
        // 保存视频到视频表
        TaroVideo video = saveVideo(videoUploadPath, userId, randomFilename);
        if(video != null && StringUtils.isNotBlank(video.getId())) {
            String fileName = randomFilename.split("\\.")[0];
            // 保存音频到音频表
            music = new TaroMusic();
            music.setId(Sid.next());
            music.setCoverPath(mvcShowPath + File.separator + fileName + ".png");
            music.setSavePath(musicPath);
            music.setMusicPath(mvcShowPath + File.separator + fileName + ".mp3");
            /// 确认时再更新用户的userId
            // music.setUserId(userId);
            music.setVideoId(video.getId());
            music.setMusicDesc(desc);
            music.setLikeCounts(0L);
            music.setName(name);
            music.setCreateDate(new Date());
            music.setStatus(TaroMusic.NOTPUB_STATUS);
            taroMusicMapper.insert(music);
        }
        return music;
    }

    /**
     * 确认并保存音乐
     *
     * @param userId
     * @param musicId
     */
    @Override
    public void saveMusic(String userId, String musicId) {
        // 音乐已经生成，根据id查询music，并更新userId
        TaroMusic taroMusic = taroMusicMapper.selectByPrimaryKey(musicId);
        if(taroMusic != null) {
            taroMusic.setUserId(userId);
            taroMusic.setStatus(TaroMusic.NORMAL_STATUS);
        }
        taroMusicMapper.updateByPrimaryKeySelective(taroMusic);
    }

    /**
     * 分页查询数据
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PagedResult getAudioListByPage(Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);
        List<TaroMusicVo> list = taroMusicMapperCustomer.getAudioListByPage();

        PageInfo<TaroMusicVo> info = new PageInfo<>(list);
        PagedResult result = new PagedResult();

        result.setPage(page);
        result.setTotalPages(info.getPages());
        result.setRecords(info.getTotal());
        result.setRows(list);

        return result;
    }

    /**
     * 查询user对应的audio List
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PagedResult getUserAudioListByPage(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        List<TaroMusicVo> list = taroMusicMapperCustomer.getUserAudioListByPage(userId);
        PageInfo<TaroMusicVo> info = new PageInfo<>(list);
        PagedResult result = new PagedResult();

        result.setPage(page);
        result.setTotalPages(info.getPages());
        result.setRecords(info.getTotal());
        result.setRows(list);

        return result;
    }


    /**
     * 保存taro_video
     * @param videoUploadPath
     */
    private TaroVideo saveVideo(String videoUploadPath, String userId, String randomFilename) {
        TaroVideo video = new TaroVideo();
        String id = Sid.next();
        video.setId(id);
        video.setUserId(userId);
        video.setVideoPath(videoUploadPath);
        video.setCreateTime(new Date());
        taroVideoService.save(video);
        return video;
    }

}
