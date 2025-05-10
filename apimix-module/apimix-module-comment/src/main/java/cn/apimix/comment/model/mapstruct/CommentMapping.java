package cn.apimix.comment.model.mapstruct;

import cn.apimix.comment.model.entity.Comment;
import cn.apimix.comment.model.req.CommentReq;
import cn.apimix.comment.model.resp.CommentResp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @Author: Hor
 * @Date: 2025/1/31 00:13
 * @Version: 1.0
 */

@Mapper(componentModel = "spring")
public interface CommentMapping {

    Comment commentReqToComment(CommentReq commentReq);


    @Mapping(target = "reply", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createTime", source = "createTime")
    CommentResp commentToCommentResp(Comment comment);

}
