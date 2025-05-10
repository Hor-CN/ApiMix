package cn.apimix.controller;

import cn.apimix.api.model.entity.ApiVersion;
import cn.apimix.api.model.entity.Category;
import cn.apimix.api.model.req.ApiQueryRequest;
import cn.apimix.api.model.resp.ApiReleaseResp;
import cn.apimix.api.service.ApiReleaseService;
import cn.apimix.api.service.impl.CategoryServiceImpl;
import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.model.dto.api.ApiInfoQueryRequest;
import cn.apimix.model.entity.ApiInfo;
import cn.apimix.model.vo.api.ApiInfoVo;
import cn.apimix.model.vo.api.ApiItemVo;
import cn.apimix.service.impl.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/25 17:51
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/interface")
public class ApiController {

//    @Resource
//    private Apiservice apiService;
    @Resource
    private ApiReleaseService apiReleaseService;

    @Resource
    private CategoryServiceImpl categoryService;




//    /**
//     * 获取当前开发者贡献的接口
//     */
//    @SaCheckLogin
//    @GetMapping("count")
//    public Long getUserDevApiByCount() {
//        Long currentUserId = StpUtil.getLoginIdAsLong();
//        return apiService.selectApiBycCount(currentUserId);
//    }

//
//    /**
//     * 根据主键获取详细信息。
//     *
//     * @param apiId 主键
//     * @return 详情
//     */
//    @GetMapping("/{apiId}")
//    public ApiInfoVo getApiInfo(@PathVariable Long apiId) {
//        return apiService.selectApiInfo(apiId);
//    }

    /**
     * 分页查询所有接口根据分类ID
     *
     * @param categoryId 分类ID
     * @return 分页对象
     */
    @GetMapping("list")
    public Page<ApiReleaseResp> getApiLists(@Valid ApiQueryRequest page, Long categoryId) {
        return apiReleaseService.selectInterfaceByCategory(page, categoryId);
    }

    /**
     * 获取开发者上传的接口列表
     *
     * @param page 分页
     * @return 结果
     */
    @SaCheckLogin
    @GetMapping("getDevApiList")
    public Page<ApiReleaseResp> getDevApiList(@Valid ApiQueryRequest page) {
        // 获取当前用户ID
        Long loginId = StpUtil.getLoginIdAsLong();
        return apiReleaseService.getDevInterfaceByPage(page, loginId);
    }


    /**
     * 获取接口分类列表
     */
    @GetMapping("category")
    public List<Category> getCategoryList() {
        return categoryService.selectCategoryByList();
    }




}
