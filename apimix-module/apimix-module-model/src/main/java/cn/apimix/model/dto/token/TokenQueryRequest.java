package cn.apimix.model.dto.token;

import cn.apimix.core.model.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: Hor
 * @Date: 2024/12/20 20:25
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TokenQueryRequest extends PageRequest {

    private String name;

}
