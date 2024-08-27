package cn.apimix.controller;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.model.dto.api.ApiInfoQueryRequest;
import cn.apimix.model.entity.ApiInfo;
import cn.apimix.model.entity.table.ApiInfoTableDef;
import cn.apimix.model.vo.SearchCountInfoVo;
import cn.apimix.service.impl.ApiServiceImpl;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.StringUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author: Hor
 * @Date: 2024/8/26 下午12:16
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/search")
public class SearchController {


    @Resource
    private ApiServiceImpl apiService;


    @GetMapping("count")
    public SearchCountInfoVo getCount() {
        return SearchCountInfoVo.builder()
                .build();
    }

    @GetMapping("api")
    public Page<ApiInfo> getApis(@Valid ApiInfoQueryRequest page) {

        if (page.getName().isEmpty()) {
            return new Page<>();
        }

//        apiService.
        return apiService.getMapper().paginate(
                Page.of(page.getPageNumber(), page.getPageSize()),
                new QueryWrapper().where(ApiInfoTableDef.API_INFO.STATUS.eq(true))
                        .and(ApiInfoTableDef.API_INFO.NAME.like(page.getName(), StringUtil::isNotBlank))
        );
    }


}
