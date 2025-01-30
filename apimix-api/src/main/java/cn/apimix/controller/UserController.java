package cn.apimix.controller;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.model.dto.system.user.SysUserAddRequest;
import cn.apimix.model.dto.user.*;
import cn.apimix.model.mapstruct.UserMapping;
import cn.apimix.service.impl.CaptchaServiceImpl;
import cn.apimix.service.impl.UserServiceImpl;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户控制层
 *
 * @Author: Hor
 * @Date: 2024/5/22 22:40
 * @Version: 1.0
 */
@Slf4j
@EnableAsync
@RestController
@ResponseResult
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserServiceImpl userService;

    @Resource
    private UserMapping userMapping;

    @Resource
    private CaptchaServiceImpl captchaService;




    @PostMapping("/register")
    public Boolean register(@Validated @RequestBody UserRegisterRequest registerReq) {
        // 校验验证码
        captchaService.checkEmailCode(registerReq.getEmail(), registerReq.getCaptcha());
        // 校验密码
        Assert.isTrue(registerReq.getPassWord().equals(registerReq.getRepeatPassword()), "两次密码不一致");

        return userService.insertUser(SysUserAddRequest.builder()
                .username(registerReq.getUserName())
                .nickname(registerReq.getUserName())
                .email(registerReq.getEmail())
                .password(registerReq.getPassWord())
                .status(1)
                .description("还没有个性签名呢")
//                .roleIds(Collections.singletonList("user"))
                .build());
    }


}
