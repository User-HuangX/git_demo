package com.hitwh.service;
import com.hitwh.model.*;
import com.hitwh.request.LoginRequest;
import com.hitwh.request.PageBean;
import com.hitwh.request.UpdateMeRequest;

import java.util.List;


public interface UserService {


    /**
     * 展示表格
     */

    PageBean show(Integer page, Integer pageSize);


    /**
     * 生成唯一学号
     */
    String generateUniqueUserId();


    void add(UserModel userModel);

    void update(UserModel userModel);

    void delete(UserModel userModel);

    UserModel getMe(String userId);

    void upDateMe(UpdateMeRequest updateMeRequest,String userId);


    void addExcel(List<UserModel> list);

    PageBean getCourseTimeAndCategory(String userId, Integer page, Integer pageSize);



    void register(UserModel userModel);}

