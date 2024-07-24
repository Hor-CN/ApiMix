package cn.apimix.service;

import cn.apimix.model.entity.Audit;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/27 22:56
 * @Version: 1.0
 */
public interface IAuditService extends IService<Audit> {

    /**
     * 根据类型和状态获取审核结果
     */
    List<Audit> selectAuditByTypeAndStatus(Integer type, Integer status);


    /**
     * 添加审核
     */
    Boolean insertAudit(Audit audit);

}
