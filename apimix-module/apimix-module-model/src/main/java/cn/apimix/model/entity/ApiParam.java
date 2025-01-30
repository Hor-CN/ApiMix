package cn.apimix.model.entity;

import cn.apimix.model.enums.ApiParamInEnum;
import cn.apimix.model.enums.ApiParamPartEnum;
import cn.apimix.model.enums.ApiParamTypeEnum;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * API参数实体
 *
 * @Author: Hor
 * @Date: 2024/5/20 19:48
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "api_param")
public class ApiParam implements Serializable {

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 父参数ID
     */
    private Long parentId;

    /**
     * 接口ID
     */
    private Long apiId;

    /**
     * 参数属于的部分
     * query|body|header
     */
    private ApiParamInEnum in;

    /**
     * API参数属于那部分
     * response|request
     */
    private ApiParamPartEnum part;

    /**
     * 参数名
     */
    private String name;

    /**
     * 参数的类型
     */
    private ApiParamTypeEnum type;

    /**
     * 是否必须
     */
    private Boolean isRequired;

    /**
     * 参数排序
     */
    private Integer order;

    /**
     * 示例值
     */
    private String example;

    /**
     * 备注
     */
    private String explain;


    /*
    @RelationOneToMany(
            selfField = "id", targetField = "parentId"
    )
    private List<ApiParam> children;
    */


}
