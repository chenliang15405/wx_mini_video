package com.wx.mini.service.pipeline;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @auther alan.chen
 * @time 2019/10/12 2:04 PM
 */
@Component
public class CsdnArticlePipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {
        String titles = resultItems.get("titles");
        String content = resultItems.get("content");

        System.out.println(titles);
        System.out.println(content);

    }

}
