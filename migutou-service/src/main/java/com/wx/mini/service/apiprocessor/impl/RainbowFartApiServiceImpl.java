package com.wx.mini.service.apiprocessor.impl;

import com.wx.mini.mapper.apiprocess.ApiCollDataMapper;
import com.wx.mini.pojo.apiprocessor.ApiCollData;
import com.wx.mini.service.apiprocessor.ApiService;
import com.wx.mini.service.apiprocessor.RainbowFartApiService;
import com.wx.mini.utils.ApiCollDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author alan.chen
 * @date 2019/11/4 10:00 AM
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class RainbowFartApiServiceImpl implements ApiService, RainbowFartApiService {

    @Autowired
    private ApiCollDataMapper apiCollDataMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ApiCollDataUtil apiCollDataUtil;

    @Value("${api.url.rainbowFartApi}")
    private String RAINBOW_FART_API;

    private static final String RAINBOW_FART_API_PREFIX = "API:RAINBOW_FART:KEY:PREFIX:";


    @Override
    public void handlerApi() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(RAINBOW_FART_API, String.class);
        if(responseEntity != null && responseEntity.getStatusCodeValue() == 200) {
            String body = responseEntity.getBody();
            log.info("rainbow fart response: {} ", body);
            boolean flag = apiCollDataUtil.duplicateRemove(RAINBOW_FART_API_PREFIX, body);
            if(flag) {
                ApiCollData data = new ApiCollData();
                data.setContent(body);
                data.setCreateDate(new Date());
                data.setSource("彩虹屁");
                apiCollDataMapper.insert(data);
            }
        }
    }


    @Override
    public String getRainbowFartWord() {
        Example example = new Example(ApiCollData.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("source", "彩虹屁");

        List<ApiCollData> list = apiCollDataMapper.selectByExample(example);
        if(list != null && list.size() > 0) {
            // 随机返回一条
            int index = (int) (Math.random() * list.size());
            return list.get(index).getContent();
        }
        return null;
    }

}
