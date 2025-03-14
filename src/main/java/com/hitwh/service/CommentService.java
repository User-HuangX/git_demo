package com.hitwh.service;

import com.hitwh.request.CommentRequest;
import com.hitwh.request.PageBean;


public interface CommentService {


    void checkComment(CommentRequest commentRequest);

    void commentTo(CommentRequest commentRequest, String userId);

    PageBean showAll( Integer page, Integer pageSize);

    PageBean showPublicComment(int commenter,String type, Integer page, Integer pageSize);
}

