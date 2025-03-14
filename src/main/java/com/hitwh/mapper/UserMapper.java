package com.hitwh.mapper;

import com.hitwh.model.CourseModel;
import com.hitwh.model.UserModel;
import com.hitwh.request.LoginRequest;
import com.hitwh.request.PositionRequest;
import com.hitwh.request.UpdateMeRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 根据用户名或学号和密码查询学生
     */
    @Select("select * from user where  password = #{password} and ((phone = #{phone})or(user_id=#{userId}))")
    UserModel getUserByPhoneOrUserIdAndPassword(LoginRequest loginRequest);

    /**
     * 分页查询所有学生
     */
    @Select("select my_id, user_id, password, role, user_name, phone, email, grade, department, major, gender, status from user")
    List<UserModel> getAllByPage();

    /**
     * 查询所有非超级管理员用户
     */
    @Select("select * from user where role !='SUPER_ADMIN'")
    List<UserModel> getAll();

    /**
     * 添加新用户
     */
    @Insert("insert into user (user_id, password, user_name) VALUES (#{userId},#{password},#{userName})")
    void addIdPasswordUserNameInUser(UserModel userModel);

    /**
     * 更新用户信息
     */
    @Update("update user set user_name=#{userName},password=#{password},user_id=#{userId},role=#{role},email=#{email},phone=#{phone},gender=#{gender},grade=#{grade},department=#{department},major=#{major},status=#{status} where my_id=#{myId}")
    void update(UserModel userModel);

    /**
     * 删除用户
     */
    @Delete("delete from user where my_id=#{myId}")
    void delete(UserModel userModel);

    /**
     * 根据用户ID获取用户信息
     */
    @Select("select user_id, user_name, phone, email, grade, department, major, gender from user where user_id=#{userId}")
    UserModel getMeByUserId(String userId);

    /**
     * 更新用户的密码、邮箱和电话号码
     */
    @Update("update user set password=#{updateMeRequest.password},email=#{updateMeRequest.email},phone=#{updateMeRequest.phone} where user_id=#{userId}")
    void updateMe(UpdateMeRequest updateMeRequest,String userId);

    /**
     * 通过Excel添加新用户
     */
    @Insert("insert into user (user_id, password, role, user_name, phone, email, grade, department, major, gender) VALUES (#{userId},#{password},#{role},#{userName},#{phone},#{email},#{grade},#{department},#{major},#{gender})")
    void addByExcel(UserModel userModel);

    /**
     * 根据用户模型获取课程时间和类别
     */
    @Select("select category,choose_time ,course_name from course where target_department=#{targetDepartment} and target_major=#{targetMajor} and target_grade=#{targetGrade}")
    List<CourseModel> getCourseTimeAndCategory(PositionRequest positionRequest);
}
