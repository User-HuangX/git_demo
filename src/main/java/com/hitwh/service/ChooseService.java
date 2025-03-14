package com.hitwh.service;

import com.hitwh.model.CourseModel;
import com.hitwh.request.ChooseRequest;
import com.hitwh.request.PageBean;
import com.hitwh.request.PositionRequest;

public interface ChooseService {
    void chooseCourse(Integer courseId,String userId);

    PageBean getStudentCourse(String UserId, Integer page, Integer pageSize);

    PageBean getCourseCanChoose(String UserId, Integer page, Integer pageSize,String courseName,String teacher);

    void setChooseTime(CourseModel courseModel);

    void setMax(PositionRequest positionRequest, Integer max,String category);

    void dropCourse(Integer courseId,String userId);
}
