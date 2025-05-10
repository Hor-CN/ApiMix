package cn.apimix.api.model.entity;

import cn.apimix.audit.model.entity.Audit;
import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 *  实体类。
 *
 * @author Hor
 * @since 2025-02-18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("api_version")
public class ApiVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    private Long id;

    /**
     * 接口ID
     */
    private Long apiId;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 是否代理
     */
    private Boolean proxy;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 接口图标地址
     */
    private String logo;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口介绍 Markdown
     */
    private String content;

    /**
     * 返回类型
     */
    private String returnType;

    /**
     * 请求类型：GET,PUT,POST,DELETE
     */
    private String method;

    /**
     * 状态：1-草稿，2-审核中，3-已发布，4-已下线
     */
    private Integer status;

    /**
     * 是否收费
     */
    private Boolean isPaid;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 逻辑删除标志（0代表存在 1代表删除）
     */
    @Column(isLogicDelete = true)
    private Boolean isDelete;

    /**
     * 版本号
     */
    private String version;

    /**
     * 版本介绍
     */
    private String versionDescription;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @RelationOneToOne(selfField = "id", targetField = "flowNo", extraCondition = "(type=1)")
    private Audit audit;

    /**
     * 分类列表
     */
    @RelationOneToMany(
            joinTable = "category_api",
            selfField = "id",
            joinSelfColumn = "api_id",
            targetField = "id",
            joinTargetColumn = "category_id")
    private List<Category> categories;

}
