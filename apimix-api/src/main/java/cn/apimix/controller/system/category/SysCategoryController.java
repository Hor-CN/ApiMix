package cn.apimix.controller.system.category;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.core.model.IdRequest;
import cn.apimix.model.dto.system.category.SysCategoryAddRequest;
import cn.apimix.model.dto.system.category.SysCategoryEditRequest;
import cn.apimix.model.entity.Category;
import cn.apimix.model.mapstruct.CategoryMapping;
import cn.apimix.service.impl.CategoryServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/31 17:40
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/system/category")
public class SysCategoryController {

    @Resource
    private CategoryServiceImpl categoryService;

    @Resource
    private CategoryMapping categoryMapping;


    /**
     * 获取全部分类列表
     */
    @GetMapping()
    public List<Category> getlist() {
        return categoryService.selectCategoryByList();
    }


    /**
     * 新增分类
     */
    @SaCheckLogin
    @SaCheckPermission("sys:category:add")
    @PostMapping("save")
    public Boolean addCategory(@RequestBody @Valid SysCategoryAddRequest addRequest) {
        return categoryService.insertCategory(categoryMapping.sysCategoryAddRequestToCategory(addRequest));
    }


    /**
     * 修改分类
     */
    @SaCheckLogin
    @SaCheckPermission("sys:category:edit")
    @PostMapping("edit")
    public Boolean editCategory(@RequestBody SysCategoryEditRequest editRequest) {
        return categoryService.updateCategory(categoryMapping.sysCategoryEditRequestToCategory(editRequest));
    }


    /**
     * 删除分类
     */
    @SaCheckLogin
    @SaCheckPermission("sys:category:del")
    @PostMapping("delete")
    public Boolean delCategory(@RequestBody @Valid IdRequest idRequest) {
        return categoryService.removeById(idRequest.getId());
    }

    /**
     * 根据通知编号获取详细信息
     */
    @SaCheckLogin
    @GetMapping(value = "/{categoryId}")
    public Category getInfo(@PathVariable Long categoryId) {
        return categoryService.selectCategoryById(categoryId);
    }
}
