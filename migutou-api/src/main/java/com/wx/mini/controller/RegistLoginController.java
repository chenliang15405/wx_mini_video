package com.wx.mini.controller;

import com.wx.mini.pojo.Users;
import com.wx.mini.service.UserService;
import com.wx.mini.utils.IMoocJSONResult;
import com.wx.mini.utils.RedisOperator;
import com.wx.mini.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @auther alan.chen
 * @time 2019/8/31 5:10 PM
 */
@Api(value = "用户注册接口", tags = {"注册和登录时的Controller"})
@RestController
public class RegistLoginController {

	@Autowired
	private UserService userservice;

	@Autowired
	private RedisOperator redisOperator;


	private static final String REDIS_USER_SESSION_TOKEN = "user-session-token";

	private static final Long REDIS_USER_SESSION_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;


	@ApiOperation(value = "用户注册", notes = "用户注册接口")
	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	public IMoocJSONResult regist(@RequestBody Users users) {
		if(StringUtils.isBlank(users.getUsername()) || StringUtils.isBlank(users.getPassword())) {
			return IMoocJSONResult.errorMsg("用户名密码不能为空");
		}
		boolean isExist = userservice.queryUserNameIsExist(users.getUsername());
		if(isExist) {
            return IMoocJSONResult.errorMsg("用户名已存在");
        }
        userservice.saveUserInfo(users);

		users.setPassword("");

		UserVo userVo = setUserToken(users);

		return IMoocJSONResult.ok(userVo);
	}

	@ApiOperation(value = "用户登录", notes = "用户登录接口")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public IMoocJSONResult login(@RequestBody Users users) {
		if(StringUtils.isBlank(users.getUsername()) || StringUtils.isBlank(users.getPassword())) {
			return IMoocJSONResult.errorMsg("用户名密码不能为空");
		}
		Users user = userservice.loginWithUsernamePassword(users.getUsername(),users.getPassword());
		if(user == null) {
			return IMoocJSONResult.errorMsg("用户名或密码错误");
		}
		users.setPassword("");
		UserVo userVo = setUserToken(users);

		return IMoocJSONResult.ok(userVo);
	}



	public UserVo setUserToken(Users users) {
		String token = UUID.randomUUID().toString();
		redisOperator.set(REDIS_USER_SESSION_TOKEN + ":" + users.getId(), token, REDIS_USER_SESSION_TOKEN_EXPIRE_TIME);

		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(users, userVo);
		userVo.setUserToken(token);
		return userVo;
	}


}
