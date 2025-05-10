package cn.apimix.service.impl;

import cn.apimix.common.model.InterfaceLog;
import cn.apimix.core.constant.StringConstants;
import cn.apimix.mapper.ApiLogMapper;
import cn.apimix.model.entity.ApiLog;
import cn.apimix.model.entity.table.ApiLogTableDef;
import cn.apimix.model.mapstruct.ApiLogMapping;
import cn.apimix.model.vo.api.ApiStatistics;
import cn.apimix.model.vo.console.ChartDataVo;
import cn.apimix.service.ApiLogService;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import com.mybatisflex.core.row.RowUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Author: Hor
 * @Date: 2024/8/10 下午1:32
 * @Version: 1.0
 */
@Service
public class ApiLogServiceImpl extends ServiceImpl<ApiLogMapper, ApiLog> implements ApiLogService {


    @Resource
    private ApiLogMapping apiLogMapping;

    /**
     * 新增接口日志
     *
     * @param apiLog 日志
     * @return boolean
     */
    @Override
    public Boolean insertApiLog(InterfaceLog apiLog) {
        return save(apiLogMapping.interfaceLogToApiLog(apiLog));
    }

    /**
     * 根据请求唯一ID获取日志信息
     *
     * @param requestId 请求id
     */
    @Override
    public ApiLog getApiLogByRequestId(String requestId) {
        return getOne(query().where(ApiLogTableDef.API_LOG.REQUEST_ID.eq(requestId)));
    }


    public List<ChartDataVo> getCountByUserIdAndYearMonth(Long userId) {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 定义日期格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // 生成从当前月往前12个月的列表
        List<ChartDataVo> fullYearXys = IntStream.rangeClosed(currentDate.getMonthValue() - 11, currentDate.getMonthValue())
                .mapToObj(i -> {
                    int month = i < 1? i + 12 : i;
                    int year = currentDate.getYear() - (i < 1? 1 : 0);
                    return new ChartDataVo(LocalDate.of(year, month, 1).format(formatter), 0L);
                })
                .collect(Collectors.toList());

        Map<String, Long> map = Collections.singletonMap("user_id", userId);
        List<Row> rows = Db.selectListBySql("SELECT  DATE_FORMAT(request_time, '%Y-%m') as name,count(*) as value FROM api_log WHERE user_id = #{user_id} \n" +
                    "AND request_time BETWEEN SUBDATE(CURRENT_TIMESTAMP, INTERVAL 12 MONTH) AND CURRENT_TIMESTAMP\n" +
                    "GROUP BY DATE_FORMAT(request_time, '%Y-%m')", map);


        List<ChartDataVo> xys = RowUtil.toEntityList(rows, ChartDataVo.class);

        // 将原始查询到的数据填充到完整列表中
        xys.forEach(xy -> fullYearXys.stream()
                .filter(f -> f.getName().equals(xy.getName()))
                .findFirst()
                .ifPresent(f -> f.setValue(xy.getValue())));

        return fullYearXys;

    }

    public List<ChartDataVo> getActiveApiByUserId(Long userId) {

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 定义日期格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // 生成从当前月往前12个月的列表
        List<ChartDataVo> fullYearXys = IntStream.rangeClosed(currentDate.getMonthValue() - 11, currentDate.getMonthValue())
                .mapToObj(i -> {
                    int month = i < 1? i + 12 : i;
                    int year = currentDate.getYear() - (i < 1? 1 : 0);
                    return new ChartDataVo(LocalDate.of(year, month, 1).format(formatter), 0L);
                })
                .collect(Collectors.toList());


        List<ChartDataVo> chartDataVos = listAs(query()
                .select(
                        QueryMethods.dateFormat(ApiLogTableDef.API_LOG.REQUEST_TIME, "%Y-%m").as(ChartDataVo::getName),
                        QueryMethods.count(QueryMethods.distinct(ApiLogTableDef.API_LOG.TARGET_SERVER)).as(ChartDataVo::getValue)
                ).where(ApiLogTableDef.API_LOG.USER_ID.eq(userId))
                .groupBy(QueryMethods.dateFormat(ApiLogTableDef.API_LOG.REQUEST_TIME, "%Y-%m")),
                ChartDataVo.class
        );

        // 将原始查询到的数据填充到完整列表中
        chartDataVos.forEach(xy -> fullYearXys.stream()
                .filter(f -> f.getName().equals(xy.getName()))
                .findFirst()
                .ifPresent(f -> f.setValue(xy.getValue())));

        return fullYearXys;
    }

    public Long getCountActiveApiByTotal(Long userId) {
        return count(queryChain().select(QueryMethods.distinct(ApiLogTableDef.API_LOG.TARGET_SERVER)).where(ApiLogTableDef.API_LOG.USER_ID.eq(userId)));
    }

    public Long getCountActiveApiByToday(Long userId) {
        return count(
                queryChain().select(QueryMethods.distinct(ApiLogTableDef.API_LOG.TARGET_SERVER))
                        .where(ApiLogTableDef.API_LOG.USER_ID.eq(userId))
                        .and(QueryMethods.dateFormat(ApiLogTableDef.API_LOG.REQUEST_TIME, "%Y-%m-%d").eq(
                                DateTime.now().toString("yyyy-MM-dd"))
                        )
        );
    }

    public Long getCountActiveApiByYesterday(Long userId) {
        return count(
                queryChain().select(QueryMethods.distinct(ApiLogTableDef.API_LOG.TARGET_SERVER))
                        .where(ApiLogTableDef.API_LOG.USER_ID.eq(userId))
                        .and(QueryMethods.dateFormat(ApiLogTableDef.API_LOG.REQUEST_TIME, "%Y-%m-%d")
                                .eq(DateUtil.yesterday().toString("yyyy-MM-dd"))
                        )
        );
    }

    public List<ChartDataVo> getHotCountByUserId(Long userId,Integer rows) {
        return listAs(query().select(ApiLogTableDef.API_LOG.TARGET_NAME.as(ChartDataVo::getName),
                                QueryMethods.count().as(ChartDataVo::getValue)
                        ).where(ApiLogTableDef.API_LOG.USER_ID.eq(userId)).groupBy(ApiLogTableDef.API_LOG.TARGET_NAME)
                        .orderBy(QueryMethods.count(), false).limit(rows)
                , ChartDataVo.class);
    }

    public List<ChartDataVo> getCountAddressByUserId(Long userId) {
        Map<String, Long> map = Collections.singletonMap("user_id", userId);
        List<Row> rows = Db.selectListBySql("SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(address, '|', 2), '|', -1) AS name,COUNT(ip) AS value\n" +
                "FROM api_log WHERE user_id = #{user_id}\n" +
                "GROUP BY SUBSTRING_INDEX(SUBSTRING_INDEX(address, '|', 2), '|', -1)", map);
        List<ChartDataVo> entityList = RowUtil.toEntityList(rows, ChartDataVo.class);

        List<ChartDataVo> list = new ArrayList<>(34);
        // 获取省份数据
        String chinaJson = null;
        try {
            chinaJson = IoUtil.readUtf8(new ClassPathResource("china.json").getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONArray jsonArr = JSONUtil.parseObj(chinaJson).getJSONArray("children");
        List<String> provinceList = jsonArr.stream().map(item -> {
            JSONObject itemJsonObj = JSONUtil.parseObj(item);
            return itemJsonObj.getStr("name")+":" +itemJsonObj.getStr("fullname");
        }).collect(Collectors.toList());
        // 汇总各省份访问数据
        for (String province : provinceList) {
            String[] split = province.split(StringConstants.COLON);
            String name = split[0];
            String fullName = split[1];
            long sum = entityList.stream()
                    .filter(item -> item.getName().contains(name))
                    .mapToLong(ChartDataVo::getValue)
                    .sum();
            list.add(new ChartDataVo(fullName, sum));
        }

        return list;
    }

    public List<ChartDataVo> getCountIpByUserId(Long userId) {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 定义日期格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // 生成从当前月往前12个月的列表
        List<ChartDataVo> fullYearXys = IntStream.rangeClosed(currentDate.getMonthValue() - 11, currentDate.getMonthValue())
                .mapToObj(i -> {
                    int month = i < 1? i + 12 : i;
                    int year = currentDate.getYear() - (i < 1? 1 : 0);
                    return new ChartDataVo(LocalDate.of(year, month, 1).format(formatter), 0L);
                })
                .collect(Collectors.toList());

        List<ChartDataVo> xys = listAs(query().select(
                QueryMethods.dateFormat(ApiLogTableDef.API_LOG.REQUEST_TIME, "%Y-%m").as(ChartDataVo::getName),
                QueryMethods.count(QueryMethods.distinct(ApiLogTableDef.API_LOG.IP)).as(ChartDataVo::getValue)
        ).where(ApiLogTableDef.API_LOG.USER_ID.eq(userId)).groupBy(QueryMethods.dateFormat(ApiLogTableDef.API_LOG.REQUEST_TIME, "%Y-%m")), ChartDataVo.class);

        // 将原始查询到的数据填充到完整列表中
        xys.forEach(xy -> fullYearXys.stream()
                .filter(f -> f.getName().equals(xy.getName()))
                .findFirst()
                .ifPresent(f -> f.setValue(xy.getValue())));
        return fullYearXys;
    }

    public Long getCountIpByTotal(Long userId) {
        return count(queryChain().select(QueryMethods.distinct(ApiLogTableDef.API_LOG.IP)).where(ApiLogTableDef.API_LOG.USER_ID.eq(userId)));
    }

    public Long getCountIpByToday(Long userId) {
        return count(
                queryChain().select(QueryMethods.distinct(ApiLogTableDef.API_LOG.IP))
                        .where(ApiLogTableDef.API_LOG.USER_ID.eq(userId))
                        .and(QueryMethods.dateFormat(ApiLogTableDef.API_LOG.REQUEST_TIME, "%Y-%m-%d").eq(
                                DateTime.now().toString("yyyy-MM-dd"))
                        )
        );
    }

    public Long getCountIpByYesterday(Long userId) {
        return count(
                queryChain().select(QueryMethods.distinct(ApiLogTableDef.API_LOG.IP))
                        .where(ApiLogTableDef.API_LOG.USER_ID.eq(userId))
                        .and(QueryMethods.dateFormat(ApiLogTableDef.API_LOG.REQUEST_TIME, "%Y-%m-%d")
                                .eq(DateUtil.yesterday().toString("yyyy-MM-dd"))
                        )
        );
    }

    public Long getCountMethodGetByUserId(Long userId) {
        return count(query().where(ApiLogTableDef.API_LOG.USER_ID.eq(userId)).and(ApiLogTableDef.API_LOG.REQUEST_METHOD.eq("GET")));
    }

    public Long getCountMethodPostByUserId(Long userId) {
        return count(query().where(ApiLogTableDef.API_LOG.USER_ID.eq(userId)).and(ApiLogTableDef.API_LOG.REQUEST_METHOD.eq("POST")));
    }

    public Long getCountMethodPutByUserId(Long userId) {
        return count(query().where(ApiLogTableDef.API_LOG.USER_ID.eq(userId)).and(ApiLogTableDef.API_LOG.REQUEST_METHOD.eq("PUT")));
    }

    public Long getCountMethodDelByUserId(Long userId) {
        return count(query().where(ApiLogTableDef.API_LOG.USER_ID.eq(userId)).and(ApiLogTableDef.API_LOG.REQUEST_METHOD.eq("DELETE")));
    }

    public Long getCountMethodOtherByUserId(Long userId) {
        return count(query().where(ApiLogTableDef.API_LOG.USER_ID.eq(userId))
                .and(ApiLogTableDef.API_LOG.REQUEST_METHOD.notIn("GET","POST","PUT","DELETE")));
    }

    /**
     * 获取总日志统计
     * {总次数，总成功，总失败}
     */
    @Override
    public ApiStatistics getApiStatisticsByAll() {
        Long totalNumber = count();
        Long successNumber = count(query().where(ApiLogTableDef.API_LOG.STATUS.eq("成功")));
        return ApiStatistics.builder().totalNumber(totalNumber).successNumber(successNumber).failedNumber(totalNumber - successNumber).build();
    }

    /**
     * 获取总日志统计
     * {总次数，总成功，总失败}
     */
    public ApiStatistics getApiStatisticsByUserId(Long userId) {
        Long totalNumber = count(query().where(ApiLogTableDef.API_LOG.USER_ID.eq(userId)));
        Long successNumber = count(
                query().where(ApiLogTableDef.API_LOG.STATUS.eq("成功"))
                        .and(ApiLogTableDef.API_LOG.USER_ID.eq(userId))
        );
        return ApiStatistics.builder().totalNumber(totalNumber).successNumber(successNumber).failedNumber(totalNumber - successNumber).build();
    }


    public Long getCountByToday(Long userId) {
        return count(query().where(
                        ApiLogTableDef.API_LOG.USER_ID.eq(userId))
                .and(QueryMethods.dateFormat(ApiLogTableDef.API_LOG.REQUEST_TIME, "%Y-%m-%d").eq(
                        DateTime.now().toString("yyyy-MM-dd"))
                ));
    }

    public Long getCountByYesterday(Long userId) {
        return count(query()
                .where(ApiLogTableDef.API_LOG.USER_ID.eq(userId))
                .and(QueryMethods.dateFormat(ApiLogTableDef.API_LOG.REQUEST_TIME, "%Y-%m-%d")
                        .eq(DateUtil.yesterday().toString("yyyy-MM-dd"))
        ));
    }

    public ApiStatistics getApiStatisticsByUser(Long apiId, Long userId, Long startTime, Long endTime) {
        Long totalNumber = count(query()
                .where(ApiLogTableDef.API_LOG.TARGET_SERVER.eq(apiId))
                .and(ApiLogTableDef.API_LOG.USER_ID.eq(userId))
                .and(ApiLogTableDef.API_LOG.TARGET_SERVER.eq(apiId)));
        Long successNumber = count(query()
                .where(ApiLogTableDef.API_LOG.TARGET_SERVER.eq(apiId))
                .and(ApiLogTableDef.API_LOG.STATUS.eq("成功"))
                .and(ApiLogTableDef.API_LOG.USER_ID.eq(userId)));
        return ApiStatistics.builder().totalNumber(totalNumber).successNumber(successNumber).failedNumber(totalNumber - successNumber).build();
    }

    @Override
    public List<ChartDataVo> getMonitorLine(Long apiId, Long userId, Long startTime, Long endTime) {

        List<DateTime> ranged = DateUtil.rangeToList(DateUtil.date(startTime), DateUtil.date(endTime), DateField.DAY_OF_YEAR);

        List<ChartDataVo> chartDataVoList = ranged.stream()
                .map(dateTime -> new ChartDataVo(dateTime.toString("yyyy-MM-dd"),0L))
                .collect(Collectors.toList());

        List<ChartDataVo> xys = listAs(
                query().select(
                    QueryMethods.dateFormat(ApiLogTableDef.API_LOG.REQUEST_TIME, "%Y-%m-%d").as(ChartDataVo::getName),
                    QueryMethods.count().as(ChartDataVo::getValue)
                ).where(ApiLogTableDef.API_LOG.USER_ID.eq(userId))
                .and(ApiLogTableDef.API_LOG.TARGET_SERVER.eq(apiId))
                .and(ApiLogTableDef.API_LOG.END_TIME.gt(DateTime.of(startTime)))
                .and(ApiLogTableDef.API_LOG.END_TIME.lt(DateTime.of(endTime)))
                .groupBy(QueryMethods.dateFormat(ApiLogTableDef.API_LOG.REQUEST_TIME, "%Y-%m-%d")), ChartDataVo.class);


        // 将原始查询到的数据填充到完整列表中
        xys.forEach(xy -> chartDataVoList.stream()
                .filter(f -> f.getName().equals(xy.getName()))
                .findFirst()
                .ifPresent(f -> f.setValue(xy.getValue())));


        return chartDataVoList;
    }
}
