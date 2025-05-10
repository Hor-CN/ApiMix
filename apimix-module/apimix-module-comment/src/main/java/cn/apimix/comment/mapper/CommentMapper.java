package cn.apimix.comment.mapper;

import cn.apimix.comment.model.entity.Comment;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 *  映射层。
 *
 * @author Hor
 * @since 2025-01-30
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
