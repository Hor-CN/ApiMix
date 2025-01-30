package cn.apimix.model.dto.token;

import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2024/12/22 16:58
 * @Version: 1.0
 */
@Data
public class AllocationTokenAddRequest {



    private Long apiId;

    private Long tokenId;

    private Long totalQuota;

}
