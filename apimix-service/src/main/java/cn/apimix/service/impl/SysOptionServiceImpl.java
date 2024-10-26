package cn.apimix.service.impl;

import cn.apimix.mapper.SysOptionMapper;
import cn.apimix.model.dto.system.option.OptionQuery;
import cn.apimix.model.dto.system.option.OptionReq;
import cn.apimix.model.dto.system.option.OptionResetValueReq;
import cn.apimix.model.dto.system.option.OptionResp;
import cn.apimix.model.entity.SysOption;
import cn.apimix.model.entity.table.SysOptionTableDef;
import cn.apimix.service.SysOptionService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 服务层实现。
 *
 * @author Hor
 * @since 2024-10-22
 */
@Service
public class SysOptionServiceImpl extends ServiceImpl<SysOptionMapper, SysOption> implements SysOptionService {

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return 列表信息
     */
    @Override
    public List<OptionResp> list(OptionQuery query) {
        return listAs(query()
                        .where(SysOptionTableDef.SYS_OPTION.CODE.in(query.getCode()))
                        .and(SysOptionTableDef.SYS_OPTION.CATEGORY.eq(query.getCategory())),
                OptionResp.class);
    }

    /**
     * 根据类别查询
     *
     * @param category 类别
     * @return 参数信息
     */
    @Override
    public Map<String, String> getByCategory(String category) {
        return list(query().where(SysOptionTableDef.SYS_OPTION.CATEGORY.eq(category)))
                .stream()
                .collect(Collectors.toMap(SysOption::getCode, o -> StrUtil.emptyIfNull(ObjectUtil.defaultIfNull(o
                        .getValue(), o.getDefaultValue())), (oldVal, newVal) -> oldVal));
    }

    /**
     * 修改参数
     *
     * @param options 参数列表
     */
    @Override
    public void update(List<OptionReq> options) {
        // 非空校验
        List<Long> idList = options.stream().map(OptionReq::getId).collect(Collectors.toList());
        List<SysOption> optionList = list(query().where(SysOptionTableDef.SYS_OPTION.CODE.in(idList)));
        Map<String, SysOption> optionMap = optionList.stream()
                .collect(Collectors.toMap(SysOption::getCode, Function.identity(), (existing, replacement) -> existing));
        for (OptionReq req : options) {
            SysOption option = optionMap.get(req.getCode());
            Assert.notNull(option, "参数 [{}] 不存在", req.getCode());
            if (StrUtil.isNotBlank(option.getDefaultValue())) {
                Assert.notBlank(req.getValue(), "参数 [{}] 的值不能为空", option.getName());
            }
        }

        updateBatch(BeanUtil.copyToList(options, SysOption.class));
    }

    /**
     * 重置参数
     *
     * @param req 重置信息
     */
    @Override
    public void resetValue(OptionResetValueReq req) {
        String category = req.getCategory();
        List<String> codeList = req.getCode();
        Assert.isFalse(StrUtil.isBlank(category) && CollUtil.isEmpty(codeList), "键列表不能为空");

        List<SysOption> list;

        if (StrUtil.isNotBlank(category)) {
            list = list(query().where(SysOptionTableDef.SYS_OPTION.CATEGORY.eq(category)));
        } else {
            list = list(query().where(SysOptionTableDef.SYS_OPTION.CODE.in(codeList)));
        }
        list.forEach((item) -> item.setValue(null));
        updateBatch(list);
    }

    /**
     * 根据编码查询参数值
     *
     * @param code 编码
     * @return 参数值（自动转换为 int 类型）
     */
    @Override
    public int getValueByCode2Int(String code) {
        return this.getValueByCode(code, Integer::parseInt);
    }

    /**
     * 根据编码查询参数值
     *
     * @param code   编码
     * @param mapper 转换方法 e.g.：value -> Integer.parseInt(value)
     * @return 参数值
     */
    @Override
    public <T> T getValueByCode(String code, Function<String, T> mapper) {
        SysOption one = getOne(query()
                .select(SysOptionTableDef.SYS_OPTION.VALUE, SysOptionTableDef.SYS_OPTION.DEFAULT_VALUE)
                .where(SysOptionTableDef.SYS_OPTION.CODE.eq(code))
        );

        Assert.notNull(one, "参数 [{}] 不存在", code);
        String value = StrUtil.nullToDefault(one.getValue(), one.getDefaultValue());
        Assert.notBlank(value, "参数 [{}] 数据错误", code);

        return mapper.apply(value);
    }
}
