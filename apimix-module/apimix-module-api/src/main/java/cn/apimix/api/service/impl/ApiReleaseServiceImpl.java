package cn.apimix.api.service.impl;


import cn.apimix.api.mapper.ApiReleaseMapper;
import cn.apimix.api.model.entity.ApiExample;
import cn.apimix.api.model.entity.ApiParam;
import cn.apimix.api.model.entity.ApiRelease;
import cn.apimix.api.model.entity.ApiVersion;
import cn.apimix.api.model.entity.table.ApiExampleTableDef;
import cn.apimix.api.model.entity.table.ApiReleaseTableDef;
import cn.apimix.api.model.entity.table.ApiVersionTableDef;
import cn.apimix.api.model.entity.table.CategoryApiTableDef;
import cn.apimix.api.model.mapstruct.ApiMapping;
import cn.apimix.api.model.req.ApiAddRequest;
import cn.apimix.api.model.req.ApiEditRequest;
import cn.apimix.api.model.req.ApiQueryRequest;
import cn.apimix.api.model.resp.ApiReleaseResp;
import cn.apimix.api.model.resp.RequestParamsVo;
import cn.apimix.api.model.resp.ResponseParamsVo;
import cn.apimix.api.service.*;
import cn.apimix.audit.model.entity.table.AuditTableDef;
import cn.apimix.audit.service.AuditService;
import cn.apimix.api.model.entity.CategoryApi;
import cn.apimix.model.entity.Package;
import cn.apimix.model.enums.ApiParamInEnum;
import cn.apimix.api.model.enums.ApiParamPartEnum;
import cn.apimix.model.vo.sku.SkuVo;
import cn.hutool.core.comparator.VersionComparator;
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
 *  服务层实现。
 *
 * @author Hor
 * @since 2025-02-17
 */
@Service
public class ApiReleaseServiceImpl extends ServiceImpl<ApiReleaseMapper, ApiRelease> implements ApiReleaseService {

    @Resource
    private ApiVersionService apiVersionService;

    @Resource
    private AuditService auditService;

    @Resource
    private ApiExampleService exampleService;

    @Resource
    private CategoryApiService categoryApiService;

    @Resource
    private ApiParamService paramService;

    @Resource
    private PackageService packageService;

    @Resource
    private ApiMapping apiMapping;

    /**
     * 发布接口
     *
     * @param addRequest 请求信息
     * @param userId 用户
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean publishInterFace(ApiAddRequest addRequest, Long userId) {

        Assert.isFalse(
                addRequest.getIsPaid() && addRequest.getProxy(),
                "收费接口必须代理"
        );

        // 发行版本
        ApiRelease apiRelease = ApiRelease.builder()
                .userId(userId)
                .status(false)
                .build();
        // 插入API信息
        boolean insertApiInfo = save(apiRelease);

        // 获取插入的API主键
        Long apiId = apiRelease.getId();
        // 构建 API版本 信息
        ApiVersion apiVersion = apiMapping.apiAddRequestToApiVersion(addRequest);
        apiVersion.setApiId(apiId);
        apiVersion.setReturnType(addRequest.getResponse().getType());
        apiVersion.setProxy(addRequest.getProxy());
        apiVersion.setIsPaid(addRequest.getIsPaid());
        apiVersion.setUserId(userId);
        // 状态：1-草稿，2-审核中，3-已发布，4-已下线
        apiVersion.setStatus(1);
        apiVersion.setVersion(addRequest.getVersion());
        apiVersion.setVersionDescription(addRequest.getVersionDescription());
        apiVersionService.save(apiVersion);
        Long versionId = apiVersion.getId();
        apiRelease.setCurrentVersionId(versionId);
        updateById(apiRelease);

        // 构建参数信息
        List<ApiParam> requestQuery = addRequest.getRequest().getQuery()
                .stream()
                .map(item -> ApiParam.builder()
                        .parentId(0L)
                        .apiVersionId(versionId)
                        .in(ApiParamInEnum.QUERY)
                        .part(ApiParamPartEnum.REQUEST)
                        .explain(item.getExplain())
                        .name(item.getName())
                        .example(item.getExample())
                        .type(item.getType())
                        .isRequired(item.getIsRequired())
                        .build()
                ).collect(Collectors.toList());

        // 请求头信息
        List<ApiParam> requestHeader = addRequest.getRequest().getHeader()
                .stream()
                .map(item -> ApiParam.builder()
                        .parentId(0L)
                        .apiVersionId(versionId)
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
                        .apiVersionId(versionId)
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
                        .apiVersionId(versionId)
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
                        .apiVersionId(versionId)
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
                        .apiVersionId(versionId)
                        .name(item.getName())
                        .type(item.getType())
                        .build())
                .collect(Collectors.toList());

        // 新增响应示例
        exampleService.saveBatch(result);

        // 新增参数
        if(!requestQuery.isEmpty()) {
            paramService.saveBatch(requestQuery);
        }



        // 新增待审核
        auditService.insertApiAudit(versionId);

        // 关联分类
        categoryApiService.save(CategoryApi.builder()
                .apiId(versionId)
                .categoryId(addRequest.getCategory())
                .build());


        // 新增套餐
        addRequest.getPackages().forEach(item -> {
            List<Package> list = item.getList().stream().peek(
                    pk -> {
                        pk.setApiId(versionId);
                        pk.setPackageType(item.getId());
                    }
            ).collect(Collectors.toList());
            packageService.saveBatch(list);
        });


        return insertApiInfo;
    }

    /**
     * 更新接口
     *
     * @param editRequest 更新信息
     * @param userId      用户ID
     * @return Boolean
     */
    @Override
    public Boolean updateInterface(ApiEditRequest editRequest, Long userId) {
        ApiRelease apiRelease = getById(editRequest.getId());
        Assert.notNull(apiRelease, "此接口不存在");

        // 老版本
        ApiVersion oldVersion = apiVersionService.getById(apiRelease.getCurrentVersionId());

        // 版本比较
        int versionCompare = VersionComparator.INSTANCE.compare(editRequest.getVersion(), oldVersion.getVersion());
        Assert.equals(versionCompare,1,"版本号填写错误，应该比旧版本高");

        // 保存新版本
        ApiVersion newVersion = apiMapping.apiEditRequestToApiVersion(editRequest);
        newVersion.setId(null);
        // 设置为草稿
        newVersion.setStatus(1);
        boolean save = apiVersionService.save(newVersion);

        // 构建参数信息
        List<ApiParam> requestQuery = editRequest.getRequest().getQuery()
                .stream()
                .map(item -> ApiParam.builder()
                        .parentId(0L)
                        .apiVersionId(newVersion.getId())
                        .in(ApiParamInEnum.QUERY)
                        .part(ApiParamPartEnum.REQUEST)
                        .explain(item.getExplain())
                        .name(item.getName())
                        .example(item.getExample())
                        .type(item.getType())
                        .isRequired(item.getIsRequired())
                        .build()
                ).collect(Collectors.toList());

        // 请求头信息
        List<ApiParam> requestHeader = editRequest.getRequest().getHeader()
                .stream()
                .map(item -> ApiParam.builder()
                        .parentId(0L)
                        .apiVersionId(newVersion.getId())
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
        List<ApiParam> requestBody = editRequest.getRequest().getBody()
                .stream()
                .map(item -> ApiParam.builder()
                        .parentId(0L)
                        .apiVersionId(newVersion.getId())
                        .in(ApiParamInEnum.BODY)
                        .part(ApiParamPartEnum.REQUEST)
                        .explain(item.getExplain())
                        .name(item.getName())
                        .example(item.getExample())
                        .type(item.getType())
                        .isRequired(item.getIsRequired())
                        .build()
                ).collect(Collectors.toList());

        List<ApiParam> responseHeader = editRequest.getResponse().getHeader()
                .stream()
                .map(item -> ApiParam.builder()
                        .parentId(0L)
                        .apiVersionId(newVersion.getId())
                        .in(ApiParamInEnum.HEADER)
                        .part(ApiParamPartEnum.RESPONSE)
                        .explain(item.getExplain())
                        .name(item.getName())
                        .example(item.getExample())
                        .type(item.getType())
                        .isRequired(item.getIsRequired())
                        .build()
                ).collect(Collectors.toList());

        List<ApiParam> responseBody = editRequest.getResponse().getBody()
                .stream()
                .map(item -> ApiParam.builder()
                        .parentId(0L)
                        .apiVersionId(newVersion.getId())
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
        List<ApiExample> result = editRequest.getResult().stream()
                .map(item -> ApiExample.builder()
                        .code(item.getCode())
                        .content(item.getContent())
                        .apiVersionId(newVersion.getId())
                        .name(item.getName())
                        .type(item.getType())
                        .build())
                .collect(Collectors.toList());

        // 新增响应示例
        exampleService.saveBatch(result);

        // 新增参数
        if(!requestQuery.isEmpty()) {
            paramService.saveBatch(requestQuery);
        }

//        // 新增待审核
//        auditService.insertApiAudit(versionId);

        // 关联分类
        categoryApiService.save(CategoryApi.builder()
                .apiId(newVersion.getId())
                .categoryId(editRequest.getCategory())
                .build());


        // 新增套餐
        editRequest.getPackages().forEach(item -> {
            List<Package> list = item.getList().stream().peek(
                    pk -> {
                        pk.setApiId(newVersion.getId());
                        pk.setPackageType(item.getId());
                    }
            ).collect(Collectors.toList());
            packageService.saveBatch(list);
        });

        return save;
    }

    /**
     * 根据接口ID获取接口信息
     *
     * @param id 接口ID
     */
    @Override
    public ApiReleaseResp getInterfaceById(Long id) {
        // 判断API是否存在
        ApiRelease apiRelease = getById(id);
        Assert.notNull(apiRelease, "接口不存在");

        // 获取接口的参数列表
        List<ApiParam> apiParams = paramService.selectApiParamByApiId(apiRelease.getCurrentVersionId());

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
        exampleQueryWrapper.where(ApiExampleTableDef.API_EXAMPLE.API_VERSION_ID.eq(apiRelease.getCurrentVersionId()));
        List<ApiExample> apiExamples = exampleService.list(exampleQueryWrapper);

        // 查询API信息
        ApiVersion apiVersion = apiVersionService.getById(apiRelease.getCurrentVersionId());

        // 获取分类信息ID
        Long categoryId = categoryApiService.getOne(
                query().where(CategoryApiTableDef.CATEGORY_API.API_ID.eq(apiRelease.getCurrentVersionId())))
                .getCategoryId();

        // 组和ResponseParams
        ResponseParamsVo responseParamsVo = ResponseParamsVo.builder()
                .header(responseHeaderParams)
                .body(responseBodyParams)
                .type(apiVersion.getReturnType())
                .build();

        List<SkuVo> skuList = packageService.getSkuList(apiRelease.getCurrentVersionId());

        return ApiReleaseResp.builder()
                .id(apiRelease.getId())
                .name(apiVersion.getName())
                .logo(apiVersion.getLogo())
                .url(apiVersion.getUrl())
                .method(apiVersion.getMethod())
                .isPaid(apiVersion.getIsPaid())
                .proxy(apiVersion.getProxy())
                .content(apiVersion.getContent())
                .description(apiVersion.getDescription())
                .packages(skuList)
                .category(categoryId)
                .status(apiVersion.getStatus())
                .releaseStatus(apiRelease.getStatus())
                .request(requestParamsVo)
                .response(responseParamsVo)
                .result(apiExamples)
                .userId(apiRelease.getUserId())
                .version(apiVersion.getVersion())
                .versionDescription(apiVersion.getVersionDescription())
                .createTime(apiVersion.getCreateTime())
                .updateTime(apiRelease.getUpdateTime())
                .build();
    }

    /**
     * 根据接口ID删除发行接口
     *
     * @param id 接口ID
     * @return 结果
     */
    @Override
    public Boolean deleteInterfaceById(Long id) {
        return removeById(id);
    }

    /**
     * 上线或下线接口
     *
     * @param id     接口ID
     * @param userId 用户
     * @param status 状态
     * @return Boolean
     */
    @Override
    public Boolean updateStatusByOnlineOrOffLine(Long id, Long userId, Boolean status) {
        // 获取要修改接口的作者
        ApiRelease apiRelease = getById(id);
        // 判断此操作是不是接口作者
        Assert.isTrue(Objects.equals(userId, apiRelease.getUserId()), "无权限");

        Assert.isTrue(
                auditService.getOne(query()
                                .where(AuditTableDef.AUDIT.FLOW_NO.eq(apiRelease.getCurrentVersionId())))
                        .getStatus() == 2,
                "未通过审核，无法上线"
        );

        apiVersionService.updateById(ApiVersion.builder()
                .id(apiRelease.getCurrentVersionId())
                        .status(status ? 3 : 4)
                .build());

        return updateById(ApiRelease.builder()
                .id(id)
                .status(status)
                .build());
    }

    /**
     * 分页获取API接口列表当前开发者
     *
     * @param queryRequest 查询条件
     * @param userId       用户ID
     */
@Override
public Page<ApiReleaseResp> getDevInterfaceByPage(ApiQueryRequest queryRequest, Long userId) {
    QueryWrapper queryWrapper = QueryWrapper.create()
            .select(ApiVersionTableDef.API_VERSION.ALL_COLUMNS)
            .where(ApiReleaseTableDef.API_RELEASE.USER_ID.eq(userId))
            .and(ApiReleaseTableDef.API_RELEASE.STATUS.eq(queryRequest.getStatus()))
            .leftJoin(ApiVersionTableDef.API_VERSION)
            .on(ApiReleaseTableDef.API_RELEASE.CURRENT_VERSION_ID.eq(ApiVersionTableDef.API_VERSION.ID))
            .and(ApiVersionTableDef.API_VERSION.NAME.like(queryRequest.getName(), StringUtil::isNotBlank));

    Page<ApiVersion> apiVersionPage = mapper.paginateWithRelationsAs(
            Page.of(queryRequest.getPageNumber(), queryRequest.getPageSize()),
            queryWrapper, ApiVersion.class
    );
    return apiVersionPage.map(item -> {
        ApiReleaseResp apiReleaseResp = apiMapping.apiVersionToApiReleaseResp(item);
        ApiRelease release = getById(item.getApiId());
        apiReleaseResp.setReleaseStatus(release.getStatus());
        return apiReleaseResp;
    });
}

        /**
     * 分页获取API接口列表
     *
     * @param categoryId 分类ID
     */
    @Override
    public Page<ApiReleaseResp> selectInterfaceByCategory(ApiQueryRequest queryRequest, Long categoryId) {

        // 根据分类ID获取API列表
        List<Long> apiIds = categoryApiService.getMapper().selectListByQuery(
                query().where(CategoryApiTableDef.CATEGORY_API.CATEGORY_ID.eq(categoryId))
        ).stream().map(CategoryApi::getApiId).collect(Collectors.toList());

        if (apiIds.isEmpty() && categoryId != -1) {
            return new Page<>();
        }

        return apiVersionService.getMapper().paginate(
                Page.of(queryRequest.getPageNumber(), queryRequest.getPageSize()),
                query().where(ApiVersionTableDef.API_VERSION.ID.in(apiIds))
                        .leftJoin(ApiReleaseTableDef.API_RELEASE)
                        .on(ApiReleaseTableDef.API_RELEASE.CURRENT_VERSION_ID.eq(ApiVersionTableDef.API_VERSION.ID))
                        .and(ApiVersionTableDef.API_VERSION.STATUS.eq(true))
                        .and(ApiVersionTableDef.API_VERSION.NAME.like(queryRequest.getName(), StringUtil::isNotBlank))
        ).map(item ->  {
            ApiReleaseResp apiReleaseResp = apiMapping.apiVersionToApiReleaseResp(item);
            ApiRelease release = getById(item.getApiId());
            apiReleaseResp.setReleaseStatus(release.getStatus());
            return apiReleaseResp;
        });

    }

    /**
     * 统计该开发者上传已通过审核的上线接口数量
     *
     * @param userId 用户ID
     */
    @Override
    public Long getInterfaceByCount(Long userId) {
        return count(query().where(
                ApiReleaseTableDef.API_RELEASE.USER_ID.eq(userId)
        ).and(ApiReleaseTableDef.API_RELEASE.STATUS.eq(true)));
    }

    /**
     * 接口是否存在
     *
     * @param id 接口ID
     */
    @Override
    public Boolean existsInterfaceById(Long id) {
        return exists(query().where(ApiReleaseTableDef.API_RELEASE.ID.eq(id)));
    }

}
