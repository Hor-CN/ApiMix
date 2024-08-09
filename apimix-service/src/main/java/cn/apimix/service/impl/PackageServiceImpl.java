package cn.apimix.service.impl;

import cn.apimix.core.core.model.PageRequest;
import cn.apimix.mapper.PackageMapper;
import cn.apimix.model.entity.Package;
import cn.apimix.model.entity.*;
import cn.apimix.model.entity.table.PackageTableDef;
import cn.apimix.model.entity.table.UserApiRelationTableDef;
import cn.apimix.model.entity.table.UserPackageTableDef;
import cn.apimix.model.vo.api.SkuVo;
import cn.apimix.service.IPackageService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 服务层实现。
 *
 * @author Hor
 * @since 2024-06-17
 */
@Slf4j
@Service
public class PackageServiceImpl extends ServiceImpl<PackageMapper, Package> implements IPackageService {


    @Resource
    private PackageTypeServiceImpl packageTypeService;

    @Resource
    private UserPackageServiceImpl userPackageService;

    @Resource
    private AccountServiceImpl accountService;

    @Resource
    private ProductOrderServiceImpl productOrderService;

    @Resource
    private UserApiRelationServiceImpl userApiRelationService;

    /**
     * 根据接口ID获取套餐列表和套餐类型
     *
     * @param id 接口ID
     */
    @Override
    public List<SkuVo> getSkuList(Long id) {

        // 根据接口ID获取套餐，根据获取的套餐获取套餐类别
        List<Package> packages = list(query().where(
                PackageTableDef.PACKAGE.API_ID.eq(id)
        ));

        Map<Integer, List<Package>> listMap = packages.stream()
                .collect(Collectors.groupingBy(Package::getPackageType));


        List<SkuVo> skuVos = new ArrayList<>();

        listMap.forEach((item, v) -> {
            PackageType packageType = packageTypeService.getById(item);
            SkuVo skuVo = SkuVo.builder()
                    .id(packageType.getId())
                    .name(packageType.getName())
                    .list(v)
                    .build();

            skuVos.add(skuVo);
        });


        return skuVos;
    }

    /**
     * 查询所有的套餐
     *
     * @param apiId       接口ID
     * @param packageType 套餐类型
     * @return 套餐列表
     */
    @Override
    public List<Package> selectAllPackage(Long apiId, Long packageType) {
        return mapper.selectListByQuery(query().where(
                PackageTableDef.PACKAGE.API_ID.eq(apiId).and(
                        PackageTableDef.PACKAGE.PACKAGE_TYPE.eq(packageType)
                )
        ));
    }

    /**
     * 获取套餐详情
     *
     * @param id 套餐ID
     */
    @Override
    public Package selectPackage(Long id) {
        return getById(id);
    }

    /**
     * 新增套餐
     *
     * @param pkg 套餐
     */
    @Override
    public Boolean addPackage(Package pkg) {
        return save(pkg);
    }

    /**
     * 删除套餐
     *
     * @param id 套餐ID
     */
    @Override
    public Boolean deletePackage(Long id) {
        return removeById(id);
    }

    /**
     * 购买套餐
     *
     * @param packageId 套餐ID
     * @param userId    用户ID
     * @param count     购买数量
     */
    @Override
    public Boolean purchasePackage(Long packageId, Long userId, Integer count) {
        // 获取套餐详细
        Package packageInfo = getById(packageId);

        // 判断该套餐是否限购，如限购判断购买次数
        if (packageInfo.getLimitQuota() != 0) {
            Assert.isTrue(
                    userPackageService.getPackageCount(
                            packageInfo.getId(),
                            userId
                    ) + count <= packageInfo.getLimitQuota(),
                    "购买次数不能超过限制"
            );
        }

        Long amount = packageInfo.getPrice() * count;

        // 如果套餐不免费时，扣款
        if (packageInfo.getPrice() != 0) {
            // 判断金额是否足够
            Account account = accountService.getAmount(userId);
            Assert.isTrue(amount < account.getAmount(), "账户余额积分不足");
            // 扣积分
            Assert.isTrue(accountService.feDeduction(userId, amount), "扣费失败");
        }

        // 在用户订单表order中添加，在同一次订单中商品会叠加
        productOrderService.save(
                ProductOrder.builder()
                        .type(0)
                        .userId(userId)
                        .packageId(packageId)
                        .payType(0)
                        .price(amount)
                        .count(count)
                        .invoice(false)
                        .build()
        );


        // 套餐过期时间
        Date expiredTime = DateUtil.offsetDay(DateUtil.date(), packageInfo.getExpired());

        // 请求次数
        Long totalQuota = packageInfo.getQuota() * count;


        // 在用户购买套餐表中添加
        userPackageService.save(UserPackage.builder()
                .userId(userId)
                .packageId(packageId)
                .totalQuota(totalQuota)
                .apiId(packageInfo.getApiId())
                .count(count)
                .usedQuota(0L)
                .status(0)
                .expiredTime(expiredTime)
                .build());
        // 在用户调用接口关系表user_api_relation中添加
        UserApiRelation userApiRelation = userApiRelationService.getOne(query().where(
                UserApiRelationTableDef.USER_API_RELATION.API_ID.eq(packageInfo.getApiId())
                        .and(UserApiRelationTableDef.USER_API_RELATION.USER_ID.eq(userId))
        ));

        if (userApiRelation != null) {
            totalQuota = userApiRelation.getTotalQuota() + totalQuota;
        }

        return userApiRelationService.save(UserApiRelation.builder()
                .userId(userId)
                .apiId(packageInfo.getApiId())
                .totalQuota(totalQuota)
                .build());
    }


    /**
     * 获取该用户的某接口的流量包
     */
    public Page<UserPackage> getUserPackageList(PageRequest pageRequest, Long userId, Long apiId) {
        return userPackageService.getMapper().paginateWithRelations(
                pageRequest.getPageNumber(), pageRequest.getPageSize(),
                query().where(UserPackageTableDef.USER_PACKAGE.USER_ID.eq(userId)).and(UserPackageTableDef.USER_PACKAGE.API_ID.eq(apiId))
        );
    }

}
