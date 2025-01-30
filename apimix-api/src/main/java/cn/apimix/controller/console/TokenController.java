package cn.apimix.controller.console;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.model.dto.token.TokenAddRequest;
import cn.apimix.model.dto.token.TokenEditRequest;
import cn.apimix.model.dto.token.TokenQueryRequest;
import cn.apimix.model.entity.UserToken;
import cn.apimix.service.impl.UserTokenServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/23 14:21
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/console/token")
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
    @PostMapping()
    public boolean addToken(@RequestBody TokenAddRequest addRequest) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        // 保存
        return tokenService.saveToken(addRequest, userId);
    }


    /**
     * 删除Token
     *
     * @param id Token Id
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @SaCheckLogin
    @DeleteMapping("/{id}")
    public boolean delToken(@PathVariable Long id) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        // 删除
        return tokenService.delToken(id, userId);
    }


    /**
     * 修改 Token
     *
     * @param editRequest token更新信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @SaCheckLogin
    @PutMapping()
    public boolean editToken(@RequestBody TokenEditRequest editRequest) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        // 修改
        return tokenService.updateToken(editRequest, userId);
    }


    @SaCheckLogin
    @GetMapping("/{id}")
    public UserToken getToken(@PathVariable Long id) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        // 返回Token详情
        return tokenService.selectTokenByIdAndUserId(id, userId);
    }


    /**
     * 分页查询当前用户的所有Token
     *
     * @param queryRequest 分页对象
     * @return 分页对象
     */
    @SaCheckLogin
    @GetMapping()
    public Page<UserToken> getTokenListByPage(@Valid TokenQueryRequest queryRequest) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        // 返回结果
        return tokenService.selectTokenList(queryRequest, userId);
    }

    /**
     * 当前用户的所有Token
     */
    @SaCheckLogin
    @GetMapping("/list/{id}")
    public List<UserToken> getTokenList(@PathVariable Long id) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        // 返回结果
        return tokenService.selectTokenByUserIdAndApiId(userId,id);
    }



}
