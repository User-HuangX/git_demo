/**
 * CommentMapper接口用于定义与评论相关操作的映射，包括添加评论、审核评论以及展示评论
 */
package com.hitwh.mapper;

import com.hitwh.request.CommentRequest;
import com.hitwh.response.CommentResponse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CommentMapper {
    /**
     * 向数据库中添加一条评论
     *
     * @param commentRequest 包含评论信息的实体对象，包括评论内容、评论者、被评论者和评论类型
     */
    @Insert("insert into student_comment (comment, commentator, commenter,type) VALUES (#{comment},#{commentator},#{commenter},#{type})")
    void commentTo(CommentRequest commentRequest);

    /**
     * 审核评论通过更新评论的审核状态
     *
     * @param commentRequest 包含评论ID和审核状态的实体对象，用于确定要审核的评论和其新状态
     */
    @Update("update student_comment set status=#{status} where comment_id=#{id} ")
    void checkComment(CommentRequest commentRequest);

    /**
     * 获取所有未审核的评论
     *
     * @return 未审核评论的列表
     */
    @Select("select * from student_comment ")
    List<CommentRequest> showAll();

    /**
     * 获取所有已审核的评论，可以根据评论类型和被评论者进行筛选
     * @param commenter 被评论者的ID
     * @param type     评论类型，可选值为"TEACHER"或"COURSE"
     * @return 已审核评论的列表，根据指定的类型和被评论者筛选
     */
    @Select("select comment,commentator from student_comment where type=#{type} and commenter=#{commenter} and status=1")
    List<CommentResponse> showPublicComment(int commenter,String type);
}
