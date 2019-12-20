package com.wx.mini.interceptor;

import com.wx.mini.utils.IMoocJSONResult;
import com.wx.mini.utils.JsonUtils;
import com.wx.mini.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 用户登录拦截器
 *
 * @auther alan.chen
 * @time 2019/9/15 4:33 PM
 */
@Slf4j
@Component
public class MiniInterceptor implements HandlerInterceptor {


    @Autowired
    private RedisOperator redisOperator;

    private static final String REDIS_USER_SESSION_TOKEN = "user-session-token";



    /**
     * 判断用户是否登录
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String userId = httpServletRequest.getHeader("userId");
        String token = httpServletRequest.getHeader("token");
        log.info("interceptor userId: {}  token: {}", userId, token);
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(token)) {
            msgResponse(httpServletResponse, IMoocJSONResult.errorMsg("请登录"));
            return false;
        }
        String redisToken = redisOperator.get(REDIS_USER_SESSION_TOKEN + ":" + userId);
        log.info("MiniInterceptor preHandle....  token: ", redisToken);
        if(StringUtils.isBlank(redisToken)) {
            msgResponse(httpServletResponse, IMoocJSONResult.errorTokenMsg("请登录"));
            return false;
        }
        if(!redisToken.equals(token)) {
            msgResponse(httpServletResponse, IMoocJSONResult.errorTokenMsg("登录已过期，请重新登录"));
            return false;
        }
        return true;
    }

    public void msgResponse(HttpServletResponse response, IMoocJSONResult result) {
        OutputStream out = null;

        try {
            response.setContentType("application/json; charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("UTF-8"));
            out.flush();
        } catch (IOException e) {
            log.error("msgResponse interceptor error ", e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }



    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
