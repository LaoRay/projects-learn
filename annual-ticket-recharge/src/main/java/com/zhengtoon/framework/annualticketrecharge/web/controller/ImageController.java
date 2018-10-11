package com.zhengtoon.framework.annualticketrecharge.web.controller;

import com.baomidou.kaptcha.Kaptcha;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登陆验证码生成
 */
@Controller
@Slf4j
@RequestMapping(value = "/image")
@Api(value = "登陆验证码生成", description = "登陆验证码生成")
public class ImageController {

	@Autowired
	private Kaptcha kaptcha;

	/**
	 * 生成验证码
	 */
	@GetMapping("/render")
	public void render() {
		kaptcha.render();
	}
}
