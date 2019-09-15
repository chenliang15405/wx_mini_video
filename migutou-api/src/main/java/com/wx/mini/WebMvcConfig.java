package com.wx.mini;

import com.wx.mini.interceptor.MiniInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;

/**
 * @auther alan.chen
 * @time 2019/9/7 12:55 PM
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Value("${user.upload.face.fileSpace}")
    private String fileSpace;

    @Autowired
    private MiniInterceptor miniInterceptor;

    /**
     * 添加资源路径，可以直接访问对应的静态资源,例如 图片、html等
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")// 设置swagger-ui.html可以访问
                .addResourceLocations("file:" + fileSpace + File.separator);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(miniInterceptor)
                .addPathPatterns("/user/**")
                .addPathPatterns("/bgm/**")
                .addPathPatterns("/video/upload","/video/uploadCover");
    }
}
