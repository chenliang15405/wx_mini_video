package com.wx.mini.service.pipeline;

import com.wx.mini.pojo.Comment;
import com.wx.mini.pojo.Music;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

/**
 * @auther alan.chen
 * @time 2019/10/10 5:58 PM
 */
public class NetEaseMusicPipeline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {

        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getKey().equals("music")) {
                Music music = (Music) entry.getValue();
//                System.out.println("mMusicDao--->null" + mMusicDao == null);
//                if (mMusicDao.countBySongId(music.getSongId()) == 0) {
//                    mMusicDao.save(music);
//                }
            } else {
                Comment comment = (Comment) entry.getValue();
//                System.out.println("mCommentDao--->null" + mCommentDao == null);
//                if (mCommentDao.countByCommentId(comment.getCommentId()) == 0) {
//                    mCommentDao.save(comment);
//                }
            }

        }

    }
}
