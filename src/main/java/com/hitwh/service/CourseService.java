package com.hitwh.service;

import com.hitwh.model.CourseModel;
import com.hitwh.request.PageBean;
import com.hitwh.model.UserModel;

import java.util.List;


public interface CourseService {


    PageBean searchByPage(Integer page, Integer pageSize);

    void add(CourseModel courseModel,String userId);

    void update(CourseModel courseModel, String userId);

    void delete(CourseModel courseModel);

    PageBean getTeacherCourse(Integer page, Integer pageSize,String userId);

    void addExcel(List<CourseModel> list, String userId);
}