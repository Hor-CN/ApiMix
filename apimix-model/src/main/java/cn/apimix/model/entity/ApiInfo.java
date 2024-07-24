package cn.apimix.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口实体
 *
 * @Author: Hor
 * @Date: 2024/5/20 19:50
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "api_info")
public class ApiInfo implements Serializable {

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 是否代理
     */
    private Boolean proxy;

    /**
     * 接口 logo 图标
     */
    private String logo;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口介绍 MarkDown
     */
    private String content;

    /**
     * 请求类型：GET、PUT、POST
     */
    private String method;

    /**
     * 状态： 0-下线，1-上线
     */
    private Boolean status;

    /**
     * 返回值类型 JSON|TEXT...
     */
    private String returnType;

    /**
     * 是否收费
     */
    private Boolean isPaid;

    @RelationOneToOne(
            selfField = "id",
            targetField = "flowNo",
            extraCondition = "(type=1)"
    )
    private Audit audit;

    /**
     * 用户ID（作者ID）
     */
    private Long userId;

    /**
     * 逻辑删除标志（0代表存在 1代表删除）
     */
    @Column(isLogicDelete = true)
    private Boolean isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}