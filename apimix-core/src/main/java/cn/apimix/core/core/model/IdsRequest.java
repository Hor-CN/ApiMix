package cn.apimix.core.core.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/24 20:28
 * @Version: 1.0
 */
@Data
public class IdsRequest {

    @NotNull(message = "id 不能为空")
    private List<Integer> ids;
}
