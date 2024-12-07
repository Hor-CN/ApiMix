package cn.apimix.model.vo.console.home;

import cn.apimix.model.entity.Notice;
import cn.apimix.model.vo.api.ApiRelationVo;
import cn.apimix.model.vo.api.ApiVo;
import com.mybatisflex.core.paginate.Page;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 
 * @Author: Hor
 * @Date: 2024/11/24 11:05
 * @Version: 1.0
 */
@Data
@Builder
public class InfoVo {


    /**
     * 我的接口列表
     */
    private Page<ApiRelationVo> myApis;


    /**
     * 通知公告
     */
    private List<Notice> notices;


    /**
     * 报告接口列表
     */
    private List<ApiVo> reportApis;


}
