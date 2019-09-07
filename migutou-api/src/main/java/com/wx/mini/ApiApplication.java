package com.wx.mini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @auther alan.chen
 * @time 2019/8/31 4:16 PM
 */
@MapperScan(basePackages="com.wx.mini.mapper")
@ComponentScan(basePackages= {"com.wx.mini"})
@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}
