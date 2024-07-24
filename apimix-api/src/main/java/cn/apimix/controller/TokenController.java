package cn.apimix.controller;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.core.model.IdRequest;
import cn.apimix.core.core.model.PageRequest;
import cn.apimix.model.dto.token.TokenAddRequest;
import cn.apimix.model.dto.token.TokenEditRequest;
import cn.apimix.model.entity.UserToken;
import cn.apimix.service.impl.UserTokenServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author: Hor
 * @Date: 2024/5/23 14:21
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/token")
public class TokenController {

    @Resource
    private UserTokenServiceImpl tokenService;

    /**
     * 添加 Token
     *
     * @param addRequest 添加Token信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @SaCheckLogin
    @PostMapping("addToken")
    public boolean addToken(@RequestBody TokenAddRequest addRequest) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        // 保存
        return tokenService.saveToken(addRequest, userId);
    }


    /**
     * 删除Token
     *
     * @param idRequest Token Id
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @SaCheckLogin
    @PostMapping("delToken")
    public boolean delToken(@RequestBody @Valid IdRequest idRequest) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        // 删除
        return tokenService.delToken(idRequest.getId(), userId);
    }


    /**
     * 修改 Token
     *
     * @param editRequest token更新信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @SaCheckLogin
    @PostMapping("edit")
    public boolean editToken(@RequestBody TokenEditRequest editRequest) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        // 修改
        return tokenService.updateToken(editRequest, userId);
    }


    @SaCheckLogin
    @GetMapping("detail")
    public UserToken getToken(@RequestParam Long id) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        // 返回Token详情
        return tokenService.selectTokenByIdAndUserId(id, userId);
    }


    /**
     * 分页查询当前用户的所有Token
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @SaCheckLogin
    @GetMapping("")
    public Page<UserToken> getTokenList(@Valid PageRequest page) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        // 返回结果
        return tokenService.selectTokenList(page, userId);
    }


}
