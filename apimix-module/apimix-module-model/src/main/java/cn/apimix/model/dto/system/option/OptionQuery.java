package cn.apimix.model.dto.system.option;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
/**
 * @Author: Hor
 * @Date: 2024/10/22 11:55
 * @Version: 1.0
 */
@Data
public class OptionQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 键列表
     */
    private List<String> code;

    /**
     * 类别
     */
    private String category;
}