package com.wx.mini.service.apiprocessor.impl;

import com.google.common.collect.Lists;
import com.wx.mini.mapper.apiprocess.ApiCollDataMapper;
import com.wx.mini.pojo.Do.EarthSweetApiDo;
import com.wx.mini.pojo.apiprocessor.ApiCollData;
import com.wx.mini.service.apiprocessor.ApiService;
import com.wx.mini.utils.ApiCollDataUtil;
import com.wx.mini.utils.JsonUtils;
import com.wx.mini.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

/**
 * @author alan.chen
 * @date 2019/10/31 6:09 PM
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class EarthlySweetApiServiceImpl implements ApiService {

    @Autowired
    private ApiCollDataMapper apiCollDataMapper;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ApiCollDataUtil apiCollDataUtil;

    @Value("${api.url.earthlySweetApi}")
    private String EARTHLY_SWEET_API;

    private static final String EARTHLY_SWEET_API_KEY_PREFIX = "API:EARTHLY_SWEET:KEY:PREFIX:";
    private static Integer count = 50;

    @Override
    public void handlerApi() {
        String url = EARTHLY_SWEET_API.replace(":serializationType", "json").replace(":count", String.valueOf(count));
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (responseEntity != null && responseEntity.getStatusCodeValue() == 200) {
            String body = responseEntity.getBody();
            EarthSweetApiDo apiDo = JsonUtils.jsonToPojo(body, EarthSweetApiDo.class);
            System.out.println(apiDo);
            assert apiDo != null;
            List<ApiCollData> list = Lists.newArrayList();
            apiDo.getReturnObj().forEach(str -> {
                boolean flag = apiCollDataUtil.duplicateRemove(EARTHLY_SWEET_API_KEY_PREFIX, str);
                if (flag) {
                    ApiCollData api = new ApiCollData();
                    api.setContent(str);
                    api.setCreateDate(new Date());
                    api.setSource("土味情话");
                    list.add(api);
                }
            });
            if(list.size() > 0) {
                apiCollDataMapper.insertList(list);
            }
        }

    }


}
