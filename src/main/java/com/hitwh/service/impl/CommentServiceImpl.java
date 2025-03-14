package com.hitwh.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hitwh.mapper.CommonMapper;
import com.hitwh.request.CommentRequest;
import com.hitwh.request.PageBean;
import com.hitwh.mapper.CommentMapper;
import com.hitwh.model.UserModel;
import com.hitwh.response.CommentResponse;
import com.hitwh.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

/**
 * CommentService的实现类，负责处理评论相关的业务逻辑
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final CommonMapper commonMapper;

    /**
     * 审核评论
     *
     * @param commentRequest 评论请求对象，包含评论的详细信息
     */
    @Override
    public void checkComment(CommentRequest commentRequest) {
        commentMapper.checkComment(commentRequest);
    }

    /**
     * 发表评论
     *
     * @param commentRequest 评论请求对象，包含评论的详细信息
     * @param userId 用户ID，用于确定评论者
     */
    @Override
    public void commentTo(CommentRequest commentRequest, String userId) {
        UserModel userModel=commonMapper.getMyModelByUserId(userId);
        commentRequest.setCommentator(userModel.getMyId());
        commentMapper.commentTo(commentRequest);
    }

    /**
     * 分页显示所有评论
     *
     * @param page 当前页码
     * @param pageSize 每页大小
     * @return 返回包含评论列表的PageBean对象
     */
    @Override
    public PageBean showAll(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<CommentRequest> list= commentMapper.showAll();
        PageInfo<CommentRequest> pageInfo = new PageInfo<>(list);
        // 返回分页结果
        return new PageBean(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 分页显示所有已审核的评论
     * @param type 评论类型，用于筛选评论类型
     * @param commenter 评论者ID，用于筛选评论类型
     * @param page 当前页码
     * @param pageSize 每页大小
     * @return 返回包含评论列表的PageBean对象
     */
    @Override
    public PageBean showPublicComment(int commenter,String type, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<CommentResponse> list = commentMapper.showPublicComment(commenter,type);
        PageInfo<CommentResponse> pageInfo = new PageInfo<>(list);
        // 返回分页结果
        return new PageBean(pageInfo.getTotal(), pageInfo.getList());
    }
}
