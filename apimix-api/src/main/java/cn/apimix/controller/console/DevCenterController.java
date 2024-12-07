package cn.apimix.controller.console;

import cn.apimix.model.vo.api.ApiInfoVo;
import cn.apimix.service.impl.ApiServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 开发者中心
 *
 * @Author: Hor
 * @Date: 2024/12/4 15:29
 * @Version: 1.0
 */
@RequestMapping("/api/console/devCenter")
public class DevCenterController {

    @Resource
    private ApiServiceImpl apiService;


    /**
     * 根据接口ID获取详细信息。
     *
     * @param apiId 接口ID
     * @return 详情
     */
    @GetMapping("/{apiId}")
    public ApiInfoVo getApiInfo(@PathVariable Long apiId) {
        return apiService.selectApiInfoByApiId(apiId);
    }

}
