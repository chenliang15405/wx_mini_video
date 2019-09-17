package com.wx.mini.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wx.mini.idworker.Sid;
import com.wx.mini.mapper.*;
import com.wx.mini.pojo.*;
import com.wx.mini.service.BgmService;
import com.wx.mini.service.VideoService;
import com.wx.mini.utils.FFMpegCommon;
import com.wx.mini.utils.FetchVideoCover;
import com.wx.mini.utils.PagedResult;
import com.wx.mini.vo.VideoStatusEnum;
import com.wx.mini.vo.VideoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @auther alan.chen
 * @time 2019/9/10 9:50 AM
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideosMapper videosMapper;
    @Autowired
    private VideosMapperCustomer videosMapperCustomer;
    @Autowired
    private SearchRecordsMapper searchRecordsMapper;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private BgmService bgmService;

    @Autowired
    private Sid sid;

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
    public String saveVideo(String bgmId, String userId, double videoSeconds, int videoHeight, int videoWidth, String desc, String mvcShowPath, String videoCoverPath) {
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
        videos.setCoverPath(videoCoverPath);
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

    /**
     * 获取视频封面截图
     *
     * @param mvcVideoShowPath
     * @return
     */
    @Override
    public String uploadVideoCoverPath(String mvcVideoShowPath) {
        String videoFilePath = fileSpace + mvcVideoShowPath;
        String videoFileName = mvcVideoShowPath.split("\\.")[0];
        String videoCoverDBPath = videoFileName + ".jpg";
        String videoCoverPath = fileSpace + videoCoverDBPath;

        FetchVideoCover ffmpeg = FetchVideoCover.builder(FFMPEG_EXE);
        ffmpeg.getVideoCover(videoFilePath, videoCoverPath);

        return videoCoverDBPath;
    }

    /**
     * 分页查询，通过pageHelper和封装的pagedResult
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult getAllVideosByPage(Videos video, Integer isSaveKeyboard, int page, int pageSize) {
        String videoDesc = video.getVideoDesc();

        if(isSaveKeyboard != null && isSaveKeyboard == 1) {
            SearchRecords records = new SearchRecords();
            String id = sid.nextShort();
            records.setId(id);
            records.setContent(videoDesc);
            searchRecordsMapper.insert(records);
        }

        // pageHelper的分页，是通过在执行Sql的时候，进行拦截，对不同的数据库执行不同的分页方法，相当于aop,
        // 然后查询的时候正常查询，查询全部数据即可，会通过pageHelper拦截封装之后，返回当前分页数据
        PageHelper.startPage(page, pageSize);
        List<VideoVo> list = videosMapperCustomer.queryAllVideos(videoDesc);

        PageInfo<VideoVo> info = new PageInfo<>(list);
        PagedResult result = new PagedResult();

        result.setPage(page);
        result.setTotalPages(info.getPages());
        result.setRecords(info.getTotal());
        result.setRows(list);

        return result;
    }

    /**
     * 搜索关键词列表
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<String> getHotList() {
        return searchRecordsMapper.getHotList();
    }

    /**
     * 视频点赞操作
     *
     * @param videoId
     * @param publisherId
     * @param userId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void likeVideo(String videoId, String publisherId, String userId) {
        // user_like_video 关系表插入数据
        UsersLikeVideos ulv = new UsersLikeVideos();
        String id = sid.nextShort();
        ulv.setId(id);
        ulv.setUserId(userId);
        ulv.setVideoId(videoId);
        usersLikeVideosMapper.insert(ulv);
        // user表增加like count
        usersMapper.addReceiveLikeCount(publisherId);
        // video表增加 like count
        videosMapper.addLikeCount(videoId);
    }

    /**
     * 视频取消点赞操作
     * @param videoId
     * @param publisherId
     * @param userId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void unLikeVideo(String videoId, String publisherId, String userId) {
        Example example = new Example(UsersLikeVideos.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("videoId", videoId);
        usersLikeVideosMapper.deleteByExample(example);

        usersMapper.reduceReceiveLikeCount(publisherId);
        videosMapper.reduceLikeCount(videoId);
    }


}
