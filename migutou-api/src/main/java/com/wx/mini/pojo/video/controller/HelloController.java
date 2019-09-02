package com.wx.mini.pojo.video.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther alan.chen
 * @time 2019/8/31 5:10 PM
 */
@RestController
public class HelloController {


	@RequestMapping(value = "/hello")
	public String hello() {
		return "Hello  Spring boot...";
	}


}
