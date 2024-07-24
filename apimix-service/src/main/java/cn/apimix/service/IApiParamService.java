package cn.apimix.service;

import cn.apimix.model.entity.ApiParam;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/25 17:19
 * @Version: 1.0
 */
public interface IApiParamService extends IService<ApiParam> {

    /**
     * 获取此API接口的全部参数
     *
     * @param apiId 接口ID
     * @return 接口参数列表
     */
    List<ApiParam> selectApiParamByApiId(Long apiId);

    /**
     * 批量插入接口参数
     *
     * @param list 待插入的参数列表
     * @return 是否插入成功
     */
    Boolean insertApiParamBatch(List<ApiParam> list);

    /**
     * 更新接口参数
     *
     * @param apiParam 接口参数
     * @return 是否更新成功
     */
    Boolean updateApiParam(ApiParam apiParam);

    /**
     * 批量更新接口参数
     *
     * @param apiParams 参数列表
     * @return 是否更新成功
     */
    Boolean updateApiParamBatch(List<ApiParam> apiParams);


    /**
     * 根据参数ID删除
     *
     * @param id 参数ID
     * @return boolean
     */
    Boolean deleteApiParamById(Long id);


    /**
     * 根据API的Id删除该APi的全部参数
     * @param apiId ApiID
     * @return boolean
     */
    Boolean deleteApiParamAllByApiId(Long apiId);


}
