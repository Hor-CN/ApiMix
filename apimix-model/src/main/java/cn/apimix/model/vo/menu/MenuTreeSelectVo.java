package cn.apimix.model.vo.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/24 17:33
 * @Version: 1.0
 */
@Data
@Builder
public class MenuTreeSelectVo {

    /**
     * 菜单ID
     */
    private Integer id;

    /**
     * 菜单名称
     */
    private String title;

    /**
     * 子菜单
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<MenuTreeSelectVo> children;
}
