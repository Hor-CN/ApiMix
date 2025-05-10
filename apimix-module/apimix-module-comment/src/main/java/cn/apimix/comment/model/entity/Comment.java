package cn.apimix.comment.model.entity;

import cn.apimix.user.model.entity.User;
import com.mybatisflex.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  实体类。
 *
 * @author Hor
 * @since 2025-01-30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    private Long id;

    /**
     * 父节点
     */
    private Long parentId;

    /**
     * 目标ID
     */
    private Long postId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 回复给谁
     */
    private Long replyUserId;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 地址
     */
    private String address;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    /**
     * 审核状态（0-待审核，1-已通过，2-未通过，3-人工复核）
     */
    private Integer status;

    /**
     * 审核结果详情（如命中标签、置信度）
     */
    private String auditResult;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 操作人ID（人工复核时记录）
     */
    private Long operatorId;

    /**
     * 触发方式（0-自动审核，1-人工触发）
     */
    private Integer auditTrigger;

    /**
     * 审核服务类型（如阿里云/腾讯云/自建规则）
     */
    private String auditService;


    @RelationOneToOne(selfField = "userId", targetField = "id")
    private User user;

}
