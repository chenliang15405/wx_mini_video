package com.wx.mini.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Optional;

/**
 * @author alan.chen
 * @date 2019/11/16 7:30 PM
 */
public class ApplicationContextHodler implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 根据类型获取容器中的bean
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> Optional<T> getBean(Class<T> type) {
        if(context == null) {
            return Optional.empty();
        }
        return Optional.of(context.getBean(type));
    }

    /**
     * 根据名字和类型获取容器中的bean
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> Optional<T> getBean(String name, Class<T> type) {
        if(context == null) {
            return Optional.empty();
        }
        return Optional.of(context.getBean(name, type));
    }


}
