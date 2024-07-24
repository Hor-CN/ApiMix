package cn.apimix.service.impl;

import cn.apimix.mapper.ApiParamMapper;
import cn.apimix.model.entity.ApiParam;
import cn.apimix.model.entity.table.ApiParamTableDef;
import cn.apimix.service.IApiParamService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/25 17:23
 * @Version: 1.0
 */
@Service
public class ApiParamServiceImpl extends ServiceImpl<ApiParamMapper, ApiParam> implements IApiParamService {
    /**
     * 获取此API接口的全部参数
     *
     * @param apiId 接口ID
     * @return 接口参数列表
     */
    @Override
    public List<ApiParam> selectApiParamByApiId(Long apiId) {
        // 构建根据ApiId查询的QueryWrapper
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.where(ApiParamTableDef.API_PARAM.API_ID.eq(apiId));
        // 查询顶级ID
        queryWrapper.where(ApiParamTableDef.API_PARAM.PARENT_ID.eq(0));
        // 排序
        queryWrapper.orderBy(ApiParamTableDef.API_PARAM.ORDER.asc());
        // 返回父子关系查询结果
        return mapper.selectListWithRelationsByQuery(queryWrapper);
    }

    /**
     * 批量插入接口参数
     *
     * @param list 待插入的参数列表
     * @return 是否插入成功
     */
    @Override
    public Boolean insertApiParamBatch(List<ApiParam> list) {
        return null;
    }

    /**
     * 更新接口参数
     *
     * @param apiParam 接口参数
     * @return 是否更新成功
     */
    @Override
    public Boolean updateApiParam(ApiParam apiParam) {
        return null;
    }

    /**
     * 批量更新接口参数
     *
     * @param apiParams 参数列表
     * @return 是否更新成功
     */
    @Override
    public Boolean updateApiParamBatch(List<ApiParam> apiParams) {
        return null;
    }

    /**
     * 根据参数ID删除
     *
     * @param id 参数ID
     * @return boolean
     */
    @Override
    public Boolean deleteApiParamById(Long id) {
        return removeById(id);
    }

    /**
     * 根据API的Id删除该APi的全部参数
     *
     * @param apiId ApiID
     * @return boolean
     */
    @Override
    public Boolean deleteApiParamAllByApiId(Long apiId) {
        return null;
    }
}
