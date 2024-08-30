package cn.apimix.service.impl;

import cn.apimix.core.core.model.PageRequest;
import cn.apimix.mapper.ApiInfoMapper;
import cn.apimix.mapper.PackageMapper;
import cn.apimix.mapper.ProductOrderMapper;
import cn.apimix.model.entity.ApiInfo;
import cn.apimix.model.entity.Package;
import cn.apimix.model.entity.ProductOrder;
import cn.apimix.model.entity.table.ProductOrderTableDef;
import cn.apimix.model.vo.ProductOrderVo;
import cn.apimix.service.IProductOrderService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/6/24 下午2:19
 * @Version: 1.0
 */
@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrder> implements IProductOrderService {

    @Resource
    private PackageMapper packageMapper;

    @Resource
    private ApiInfoMapper apiInfoMapper;

    public Page<ProductOrderVo> selectProductOrderByUserId(PageRequest pageRequest, Long userId) {
        Page<ProductOrder> paginate = getMapper().paginate(pageRequest.getPageNumber(), pageRequest.getPageSize(),
                query().where(ProductOrderTableDef.PRODUCT_ORDER.USER_ID.eq(userId))
        );

        Page<ProductOrderVo> pageVo = new Page<>();
        pageVo.setPageNumber(paginate.getPageNumber());
        pageVo.setPageSize(paginate.getPageSize());
        pageVo.setTotalPage(paginate.getTotalPage());
        pageVo.setTotalRow(paginate.getTotalRow());

        List<ProductOrderVo> productOrderVos = new ArrayList<>();
        paginate.getRecords().forEach(item -> {
            Package aPackage = packageMapper.selectOneById(item.getPackageId());

            ApiInfo apiInfo = apiInfoMapper.selectOneById(aPackage.getApiId());

            ProductOrderVo productOrderVo = ProductOrderVo.builder()
                    .type(item.getType())
                    .userId(item.getUserId())
                    .price(item.getPrice())
                    .payType(item.getPayType())
                    .count(item.getCount())
                    .invoice(item.getInvoice())
                    .packageInfo(aPackage)
                    .createTime(item.getCreateTime())
                    .productName(apiInfo.getName())
                    .build();
            productOrderVos.add(productOrderVo);
        });
        pageVo.setRecords(productOrderVos);
        return pageVo;
    }
}
