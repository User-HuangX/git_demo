package com.hitwh.mapper;

import com.hitwh.model.CourseModel;
import com.hitwh.model.UserModel;
import com.hitwh.request.PositionRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 通用Mapper接口，用于定义获取用户和课程信息的方法
 */
@Mapper
public interface CommonMapper {
    /**
     * 根据用户ID获取用户模型
     *
     * @param userId 用户ID
     * @return 用户模型对象，包含用户的详细信息
     */
    @Select("select my_id, user_id, password, role, user_name, phone, email, grade, department, major, gender, status from user where user_id=#{userId}")
    UserModel getMyModelByUserId(String userId);

    /**
     * 根据用户ID获取用户教授的课程列表
     *
     * @param userId 用户ID
     * @return 课程模型列表，包含用户教授的所有课程信息
     */
    @Select("select * from course where teacher=(select user_name from user where user_id=#{userId})")
    List<CourseModel> getCourseModelByUserId(String userId);

    /**
     * 根据课程模型获取职位请求信息
     *
     * @param courseModel 课程模型，包含课程详细信息
     * @return 职位请求对象，包含针对特定课程的职位信息
     */
    @Select("SELECT * from position where target_department=#{targetDepartment} and target_major=#{targetMajor} and target_grade=#{targetGrade}")
    PositionRequest getPositionRequestByChooseModel(CourseModel courseModel);

    /**
     * 根据课程ID获取教师用户名
     *
     * @param courseId 课程ID
     * @return 教师用户名
     */
    @Select("select teacher from course where course_id=#{courseId}")
    String getTeacherById(Integer courseId);

    /**
     * 根据课程ID获取课程名称
     *
     * @param courseId 课程ID
     * @return 课程名称
     */
    @Select("select course_name from course where course_id=#{courseId}")
    String getCourseById(Integer courseId);
    /**
     * 根据课程ID获取课程
     *
     * @param courseId 课程ID
     * @return 课程名称
     */

    @Select("select * from course where course_id=#{courseId}")
    CourseModel getCourseModelByCourseId(Integer courseId);
    @Select("select user.user_name from user where user_id =#{userId}")
    String getTeacherByUserId(String userId);
}
