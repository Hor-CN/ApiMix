package cn.apimix.model.dto.token;

import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2024/12/22 16:50
 * @Version: 1.0
 */
@Data
public class AllocationTokenEditRequest {

    /**
     * 主键
     */
    private Long id;

    /**
     * TokenId
     */
    private Long tokenId;

    /**
     * 接口ID
     */
    private Long apiId;

    /**
     * 修改的额度数量
     */
    private Long totalQuota;
}
