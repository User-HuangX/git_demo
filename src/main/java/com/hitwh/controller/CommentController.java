package com.hitwh.controller;

import com.hitwh.request.CommentRequest;
import com.hitwh.request.PageBean;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import com.hitwh.response.Response;
import com.hitwh.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * CommentController类负责处理与评论相关的HTTP请求，
 * 包括学生对课程的评论、管理员对评论的审核以及评论的查看等操作。
 */
@Slf4j
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@SecurityRequirement(name = "token")
public class CommentController {
    private final CommentService commentService;

    /**
     * 学生对课程进行评论的操作。
     *
     * @param commentRequest 包含评论信息的实体对象。
     * @return 返回评论操作的结果。
     */
    @PostMapping("/student/commentTo")
    public Response commentTo(@RequestBody CommentRequest commentRequest, @RequestAttribute String userId) {
        log.info("进行评论操作");

        commentService.commentTo(commentRequest,userId);
        return Response.success("评论成功");
    }

    /**
     * 管理员对课程评论进行审核的操作。
     *
     * @param commentRequest 包含评论信息的实体对象。
     * @return 返回审核操作的结果。
     */
    @PatchMapping("/admin/checkComment")
    public Response checkComment(@RequestBody CommentRequest commentRequest) {
        log.info("进行审查课程评论操作");

        commentService.checkComment(commentRequest);
        return Response.success("完成修改");
    }

    /**
     * 查看课程的所有评论。
     *
     * @param page 当前页码，默认为1。
     * @param pageSize 每页大小，默认为10。
     * @return 返回分页后的评论信息。
     */
    @GetMapping("/student/showAll")
    public Response showAll( @RequestParam(defaultValue = "1")Integer page,
                             @RequestParam(defaultValue = "10")Integer pageSize) {
        log.info("进行查看评论操作");

        PageBean pageBean= commentService.showAll(page,pageSize);
        return Response.success("查看完成",pageBean);
    }

    /**
     * 查看所有未审核的课程评论。
     *
     * @param type 评论类型。
     * @param commenter 被评论者的用户ID。
     * @param page 当前页码，默认为1。
     * @param pageSize 每页大小，默认为10。
     * @return 返回分页后的未审核评论信息。
     */
    @GetMapping("/admin/showPublicComment")
    public Response showPublicComment(String type,int commenter, @RequestParam(defaultValue = "1")Integer page,
                                      @RequestParam(defaultValue = "10")Integer pageSize) {
        log.info("查看未审查的课程评论操作");

        PageBean pageBean= commentService.showPublicComment(commenter,type,page,pageSize);
        return Response.success("查看完成",pageBean);
    }

}
