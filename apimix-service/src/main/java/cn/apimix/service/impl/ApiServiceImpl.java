package cn.apimix.service.impl;

import cn.apimix.mapper.ApiExampleMapper;
import cn.apimix.mapper.ApiInfoMapper;
import cn.apimix.model.dto.api.ApiAddRequest;
import cn.apimix.model.dto.api.ApiEditRequest;
import cn.apimix.model.dto.api.ApiInfoQueryRequest;
import cn.apimix.model.entity.Package;
import cn.apimix.model.entity.*;
import cn.apimix.model.entity.table.ApiExampleTableDef;
import cn.apimix.model.entity.table.ApiInfoTableDef;
import cn.apimix.model.entity.table.AuditTableDef;
import cn.apimix.model.entity.table.CategoryApiTableDef;
import cn.apimix.model.enums.ApiParamInEnum;
import cn.apimix.model.enums.ApiParamPartEnum;
import cn.apimix.model.mapstruct.ApiMapping;
import cn.apimix.model.vo.api.ApiItemVo;
import cn.apimix.model.vo.api.RequestParamsVo;
import cn.apimix.model.vo.api.ResponseParamsVo;
import cn.apimix.model.vo.api.SkuVo;
import cn.apimix.service.IApiService;
import cn.hutool.core.lang.Assert;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.StringUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: Hor
 * @Date: 2024/5/25 16:49
 * @Version: 1.0
 */
@Service
public class ApiServiceImpl extends ServiceImpl<ApiInfoMapper, ApiInfo> implements IApiService {

    @Resource
    private ApiParamServiceImpl paramService;

    @Resource
    private ApiExampleMapper apiExampleMapper;

    @Resource
    private ApiMapping apiMapping;

    @Resource
    private AuditServiceImpl auditService;

    @Resource
    private CategoryApiServiceImpl categoryApiService;


    @Resource
    private PackageServiceImpl packageService;

    /**
     * 上传接口
     *
     * @param addRequest 添加接口信息
     * @param userId     用户Id
     */
    @Transactional
    @Override
    public Boolean insertApi(ApiAddRequest addRequest, Long userId) {
        // 查看接口是否存在
        boolean exists = queryChain().where(ApiInfoTableDef.API_INFO.URL.eq(addRequest.getUrl())).exists();
        Assert.isFalse(exists, "接口已存在");

        Assert.isTrue(addRequest.getIsPaid() && addRequest.getProxy(), "收费接口必须代理");

        // 构建API信息
        ApiInfo apiInfo = apiMapping.apiAddRequestToApi(addRequest);
        apiInfo.setUserId(userId);
        apiInfo.setReturnType(addRequest.getResponse().getType());
        apiInfo.setProxy(addRequest.getProxy());
        apiInfo.setIsPaid(addRequest.getIsPaid());
        apiInfo.setStatus(false);

        // 插入API信息
        boolean insertApiInfo = save(apiInfo);
        // 获取插入的API主键
        Long apiId = apiInfo.getId();

        // 构建API参数信息
        List<ApiParam> requestQuery = addRequest.getRequest().getQuery()
                .stream()
                .map(item -> ApiParam.builder()
                        .parentId(0L)
                        .apiId(apiId)
                        .in(ApiParamInEnum.QUERY)
                        .part(ApiParamPartEnum.REQUEST)
                        .explain(item.getExplain())
                        .name(item.getName())
                        .example(item.getExample())
                        .type(item.getType())
                        .isRequired(item.getIsRequired())
                        .build()
                ).collect(Collectors.toList());

        List<ApiParam> requestHeader = addRequest.getRequest().getHeader()
                .stream()
                .map(item -> ApiParam.builder()
                        .parentId(0L)
                        .apiId(apiId)
                        .in(ApiParamInEnum.HEADER)
                        .part(ApiParamPartEnum.REQUEST)
                        .explain(item.getExplain())
                        .name(item.getName())
                        .example(item.getExample())
                        .type(item.getType())
                        .isRequired(item.getIsRequired())
                        .build()
                ).collect(Collectors.toList());

        // 请求体
        List<ApiParam> requestBody = addRequest.getRequest().getBody()
                .stream()
                .map(item -> ApiParam.builder()
                        .parentId(0L)
                        .apiId(apiId)
                        .in(ApiParamInEnum.BODY)
                        .part(ApiParamPartEnum.REQUEST)
                        .explain(item.getExplain())
                        .name(item.getName())
                        .example(item.getExample())
                        .type(item.getType())
                        .isRequired(item.getIsRequired())
                        .build()
                ).collect(Collectors.toList());

        List<ApiParam> responseHeader = addRequest.getResponse().getHeader()
                .stream()
                .map(item -> ApiParam.builder()
                        .parentId(0L)
                        .apiId(apiId)
                        .in(ApiParamInEnum.HEADER)
                        .part(ApiParamPartEnum.RESPONSE)
                        .explain(item.getExplain())
                        .name(item.getName())
                        .example(item.getExample())
                        .type(item.getType())
                        .isRequired(item.getIsRequired())
                        .build()
                ).collect(Collectors.toList());

        List<ApiParam> responseBody = addRequest.getResponse().getBody()
                .stream()
                .map(item -> ApiParam.builder()
                        .parentId(0L)
                        .apiId(apiId)
                        .in(ApiParamInEnum.HEADER)
                        .part(ApiParamPartEnum.RESPONSE)
                        .explain(item.getExplain())
                        .name(item.getName())
                        .example(item.getExample())
                        .type(item.getType())
                        .isRequired(item.getIsRequired())
                        .build()
                ).collect(Collectors.toList());


        requestQuery.addAll(requestHeader);
        requestQuery.addAll(requestBody);
        requestQuery.addAll(responseHeader);
        requestQuery.addAll(responseBody);


        // 获取响应示例
        List<ApiExample> result = addRequest.getResult().stream()
                .map(item -> ApiExample.builder()
                        .code(item.getCode())
                        .content(item.getContent())
                        .apiId(apiId)
                        .name(item.getName())
                        .type(item.getType())
                        .build())
                .collect(Collectors.toList());

        // 新增响应示例
        apiExampleMapper.insertBatch(result);

        // 新增参数
        paramService.getMapper().insertBatch(requestQuery);

        // 新增待审核
        auditService.insertAudit(Audit.builder()
                .flowNo(apiId)
                .type(1)
                .status(1)
                .build());

        // 关联分类
        categoryApiService.save(CategoryApi.builder()
                .apiId(apiId)
                .categoryId(addRequest.getCategory())
                .build());

        // 新增套餐
        addRequest.getPackages().forEach(item -> {
            List<Package> list = item.getList().stream().peek(
                    pk -> {
                        pk.setApiId(apiId);
                        pk.setPackageType(item.getId());
                    }
            ).collect(Collectors.toList());
            packageService.saveBatch(list);
        });


        return insertApiInfo;
    }

    /**
     * 根据接口ID获取接口信息
     *
     * @param apiId 接口ID
     * @return 结果
     */
    @Override
    public ApiItemVo selectApiInfoByApiId(Long apiId) {
        // 判断API是否存在
        boolean exists = queryChain().where(ApiInfoTableDef.API_INFO.ID.eq(apiId)).exists();
        Assert.isTrue(exists, "接口不存在");

        // 获取接口的参数列表
        List<ApiParam> apiParams = paramService.selectApiParamByApiId(apiId);
        // 取出请求参数和返回参数
        Map<ApiParamPartEnum, List<ApiParam>> paramMap = apiParams.stream()
                .collect(Collectors.groupingBy(ApiParam::getPart));
        List<ApiParam> requestParams = paramMap.getOrDefault(ApiParamPartEnum.REQUEST, Collections.emptyList());
        List<ApiParam> responseParams = paramMap.getOrDefault(ApiParamPartEnum.RESPONSE, Collections.emptyList());
        // 分出Query|BODY|HEADER
        Map<ApiParamInEnum, List<ApiParam>> requestinEnumListMap = requestParams.stream()
                .collect(Collectors.groupingBy(ApiParam::getIn));
        List<ApiParam> requestQueryParams = requestinEnumListMap.getOrDefault(ApiParamInEnum.QUERY, Collections.emptyList());
        List<ApiParam> requestBodyParams = requestinEnumListMap.getOrDefault(ApiParamInEnum.BODY, Collections.emptyList());
        List<ApiParam> requestHeaderParams = requestinEnumListMap.getOrDefault(ApiParamInEnum.HEADER, Collections.emptyList());
        // 组合RequestParams
        RequestParamsVo requestParamsVo = RequestParamsVo.builder()
                .query(requestQueryParams)
                .body(requestBodyParams)
                .header(requestHeaderParams)
                .build();
        // 分出响应的Header|Body
        Map<ApiParamInEnum, List<ApiParam>> responseinEnumListMap = responseParams.stream()
                .collect(Collectors.groupingBy(ApiParam::getIn));
        List<ApiParam> responseHeaderParams = responseinEnumListMap.getOrDefault(ApiParamInEnum.HEADER, Collections.emptyList());
        List<ApiParam> responseBodyParams = responseinEnumListMap.getOrDefault(ApiParamInEnum.BODY, Collections.emptyList());


        // 获取API的示例结果
        QueryWrapper exampleQueryWrapper = QueryWrapper.create();
        exampleQueryWrapper.where(ApiExampleTableDef.API_EXAMPLE.API_ID.eq(apiId));
        List<ApiExample> apiExamples = apiExampleMapper.selectListByQuery(exampleQueryWrapper);

        // 查询API信息
        ApiInfo apiInfo = getById(apiId);

        // 获取分类信息ID
        Long categoryId = categoryApiService.getOne(query().where(CategoryApiTableDef.CATEGORY_API.API_ID.eq(apiId))).getCategoryId();


        // 组和ResponseParams
        ResponseParamsVo responseParamsVo = ResponseParamsVo.builder()
                .header(responseHeaderParams)
                .body(responseBodyParams)
                .type(apiInfo.getReturnType())
                .build();

        // 获取套餐信息
        List<SkuVo> skuList = packageService.getSkuList(apiId);


        ApiItemVo build = ApiItemVo.builder()
                .id(apiInfo.getId())
                .name(apiInfo.getName())
                .logo(apiInfo.getLogo())
                .method(apiInfo.getMethod())
                .isPaid(apiInfo.getIsPaid())
                .proxy(apiInfo.getProxy())
                .content(apiInfo.getContent())
                .description(apiInfo.getDescription())
                .category(categoryId)
                .status(apiInfo.getStatus())
                .packages(skuList)
                .request(requestParamsVo)
                .response(responseParamsVo)
                .result(apiExamples)
                .userId(apiInfo.getUserId())
                .createTime(apiInfo.getCreateTime())
                .updateTime(apiInfo.getUpdateTime())
                .build();


        if (!apiInfo.getProxy()) {
            build.setUrl(apiInfo.getUrl());
        }

        return build;
    }

    /**
     * 根据接口ID删除接口
     *
     * @param apiId 接口ID
     * @return 结果
     */
    @Override
    public Boolean deleteApiInfoByApiId(Long apiId) {
        return removeById(apiId);
    }

    /**
     * 更新接口信息
     *
     * @param editRequest 接口信息
     * @return 结果
     */
    @Override
    public Boolean updateApiInfo(ApiEditRequest editRequest) {


        return null;
    }


    public Boolean updateStatusByOnlineOrOffLine(Long apiId, Long userId, Boolean status) {
        // 获取要修改接口的作者
        Long apiUserId = getById(apiId).getUserId();
        // 判断此操作是不是接口作者
        Assert.isTrue(Objects.equals(userId, apiUserId), "无权限");

        Assert.isTrue(
                auditService.getOne(query().where(AuditTableDef.AUDIT.FLOW_NO.eq(apiId))).getStatus() == 2,
                "未通过审核，无法上线"
        );

        return updateById(ApiInfo.builder()
                .id(apiId)
                .status(status)
                .build());
    }

    public Boolean onLineOrOffLine(Long apiId, Boolean status) {
        return updateById(ApiInfo.builder()
                .id(apiId)
                .status(status)
                .build());
    }

    /**
     * 分页获取API接口列表
     *
     * @param queryRequest 查询
     */
    @Override
    public Page<ApiInfo> selectApiInfoByPage(ApiInfoQueryRequest queryRequest) {
        return null;
    }


    @Resource
    private CategoryServiceImpl categoryService;

    /**
     * 分页获取API接口列表
     *
     * @param categoryId 分类ID
     */
    @Override
    public Page<ApiInfo> selectApiInfoByCategory(ApiInfoQueryRequest queryRequest, Long categoryId) {

        // 根据分类ID获取API列表
        List<Long> apiIds = categoryApiService.getMapper().selectListByQuery(
                query().where(CategoryApiTableDef.CATEGORY_API.CATEGORY_ID.eq(categoryId))
        ).stream().map(CategoryApi::getApiId).collect(Collectors.toList());

        if (apiIds.isEmpty() && categoryId != -1) {
            return new Page<>();
        }

        return mapper.paginate(
                Page.of(queryRequest.getPageNumber(), queryRequest.getPageSize()),
                query().where(ApiInfoTableDef.API_INFO.ID.in(apiIds))
                        .and(ApiInfoTableDef.API_INFO.STATUS.eq(true))
                        .and(ApiInfoTableDef.API_INFO.NAME.like(queryRequest.getName(), StringUtil::isNotBlank))
        );
    }

    /**
     * 分页获取API接口列表当前开发者
     *
     * @param queryRequest 查询
     * @param userId       用户ID
     */
    @Override
    public Page<ApiInfo> selectDevApiInfoByPage(ApiInfoQueryRequest queryRequest, Long userId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(ApiInfoTableDef.API_INFO.USER_ID.eq(userId))
                .and(ApiInfoTableDef.API_INFO.NAME.like(queryRequest.getName(), StringUtil::isNotBlank));
        return mapper.paginateWithRelations(
                Page.of(queryRequest.getPageNumber(), queryRequest.getPageSize()),
                queryWrapper
        );
    }

    /**
     * 分页获取待审核的接口列表
     *
     * @param queryRequest 请求
     */
    @Override
    public Page<ApiInfo> selectAuditApiInfoByPage(ApiInfoQueryRequest queryRequest) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(ApiInfoTableDef.API_INFO.ALL_COLUMNS)
                .leftJoin(AuditTableDef.AUDIT)
                .on(ApiInfoTableDef.API_INFO.ID.eq(AuditTableDef.AUDIT.FLOW_NO))
                .where(AuditTableDef.AUDIT.STATUS.eq(1))
                .and(ApiInfoTableDef.API_INFO.NAME.like(queryRequest.getName(), StringUtil::isNotBlank));
        return page(Page.of(queryRequest.getPageNumber(), queryRequest.getPageSize()),
                queryWrapper);
    }

    /**
     * 统计该开发者上传已通过审核的接口
     */
    @Override
    public Long selectApiBycCount(Long userId) {
        return count(query().where(
                ApiInfoTableDef.API_INFO.USER_ID.eq(userId)
        ).and(ApiInfoTableDef.API_INFO.STATUS.eq(true)));
    }

}
