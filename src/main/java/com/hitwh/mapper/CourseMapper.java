package com.hitwh.mapper;

import com.hitwh.model.CourseModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 课程映射器接口
 * 用于定义与课程相关的数据库操作
 */
@Mapper
public interface CourseMapper {
    /**
     * 查询所有课程信息
     * 此方法用于从数据库中查询所有课程的信息
     *
     * @return 课程列表
     */
    @Select("select * from course")
    List<CourseModel> getAllByPage();

    /**
     * 添加新课程
     * 此方法用于向数据库中添加新的课程信息
     *
     * @param courseModel 课程对象，包含课程的详细信息
     */
    @Insert("insert into course (course_name, capacity, study_time, category, target_grade, target_department, course_sort, target_major, teacher,remain) VALUES ( #{courseName}, #{capacity}, #{studyTime}, #{category}, #{targetGrade}, #{targetDepartment},#{courseSort},#{targetMajor}, #{teacher},#{capacity})")
    void add(CourseModel courseModel);

    /**
     * 更新课程信息
     * 此方法用于更新数据库中的课程信息
     *
     * @param courseModel 课程对象，包含更新后的课程信息
     */
    @Update("update course set course_name=#{courseName},capacity=#{capacity},study_time=#{studyTime},category=#{category},target_grade=#{targetGrade},target_department=#{targetDepartment},course_sort=#{courseSort},target_major=#{targetMajor},teacher=#{teacher} where course_id=#{courseId}")
    void update(CourseModel courseModel);

    /**
     * 删除课程
     * 此方法用于从数据库中删除课程通过将课程的状态设置为0
     *
     * @param courseModel 课程对象，包含要删除的课程的ID
     */
    @Delete("update course set status=0 where course_id=#{courseId}")
    void delete(CourseModel courseModel);

}
