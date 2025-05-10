package cn.apimix.controller;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.user.model.resp.user.UserInfoResp;
import cn.apimix.user.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户控制层
 *
 * @Author: Hor
 * @Date: 2024/5/22 22:40
 * @Version: 1.0
 */
@Slf4j
@RestController
@ResponseResult
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserServiceImpl userService;

    @GetMapping("/info/{id}")
    public UserInfoResp getUserInfo(@PathVariable Long id) {
        return userService.getUserInfo(id);
    }


}
