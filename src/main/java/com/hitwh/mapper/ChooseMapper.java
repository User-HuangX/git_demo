package com.hitwh.mapper;

import com.hitwh.model.CourseModel;
import com.hitwh.model.UserModel;
import com.hitwh.request.ChooseRequest;
import com.hitwh.request.PositionRequest;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 选择课程模型接口，用于定义与课程选择相关的数据库操作
 */
@Mapper
public interface ChooseMapper {

    /**
     * 从学生课程表中删除指定学生和课程的记录
     */
    @Delete("delete from student_course where my_id=#{myId} and course_id=#{courseId}")
    void deleteChooseByMyIdAndCourseId(int myId,int courseId);

    /**
     * 更新课程表，增加指定课程的容量（即增加一个座位）
     */
    @Update("update course set capacity=capacity+#{changeNum} where course_id=#{courseId}")
    void updateCourseCapacityByCourseId(int courseId, Integer changeNum);

    /**
     * 在学生课程表中插入一条新记录，表示学生选择了一门课程
     */
    @Insert("insert into student_course (my_id, course_id) VALUES (#{myId},#{courseId})")
    void addChooseByMyIdAndCourseId(int myId, int courseId);

    /**
     * 检查学生是否已经选择过指定课程
     *
     * @return 如果学生选择了课程，则返回包含该课程的列表；否则返回空列表
     */
    @Select("select * from student_course where my_id=#{myId} and course_id=#{courseId}")
    List<CourseModel> getChooseByMyIdAndCourseId(int myId, int courseId);

    /**
     * 获取学生已选择的所有课程
     *
     * @return 学生已选择的课程列表
     */
    @Select("select course_name, capacity, study_time, category, target_grade, target_department, course_sort, target_major, teacher, remain, choose_time, end_choose_time from course where course_id in (select course_id from student_course where my_id=#{myId})")
    List<CourseModel> getCourseByMyId(int MyId);

    /**
     * 获取学生可以选的课程列表，排除已选课程，并根据学生专业、年级和系别筛选
     *
     * @param targetGrade
     * @param myId
     * @param targetMajor
     * @param targetDepartment
     * @return 学生可以选的课程列表
     */
    @Select("select course_name, capacity, study_time, category, target_grade, target_department, course_sort, target_major, teacher,choose_time, end_choose_time from course  where course_id not in (select course_id from student_course where my_id=#{myId}) and target_department=#{targetDepartment} and target_grade=#{targetGrade} and target_major=#{targetMajor} and status=1")
    List<CourseModel> getCourseCanChoose(int myId,String targetGrade,String targetMajor,String targetDepartment);

    /**
     * 获取已选择指定类型课程的学生人数
     * @return 已选择指定类型课程的学生人数
     */
    @Select("SELECT COUNT(*)\n" +
            "FROM student_course sc1, student_course sc2, course\n" +
            "WHERE sc1.my_id = #{myId}\n" +
            "  AND sc2.my_id = #{myId}\n" +
            "  AND sc1.course_id = sc2.course_id\n" +
            "  AND sc1.course_id = course.course_id\n" +
            "  AND course.target_major = #{targetMajor}\n "+
            "  AND course.target_department = #{targetDepartment}\n "+
            "  AND course.target_major = #{targetMajor}\n ")
    int getNumFormChooseByCourseIdDepartmentAndCategoryAndMajorAndGrade(int myId, int courseId, String targetMajor, String targetGrade, String targetDepartment, String category);

    /**
     * 设置课程的选课时间
     *
     * @param courseModel 包含课程ID、目标专业、目标年级、目标系别和课程类别的课程实体对象
     */
    @Update("update course set choose_time=#{chooseTime} ,end_choose_time=#{endChooseTime} where target_major=#{targetMajor} and target_grade=#{targetGrade} and target_department=#{targetDepartment} and category =#{category}")
    void updateChooseTimeFromCourseByMajorAndGradeAndDepartmentAndCategory(CourseModel courseModel);

    /**
     * 获取最大选课人数
     *
     * @param positionId 职位ID
     * @param category 课程类别
     * @return 最大选课人数
     */
    @Select("select max_num from max cross join position where  position.position_id =#{positionId} and max.category=#{category}")
    int getMaxNumFormMaxByPosition(int positionId,String category);

    /**
     * 添加最大选课人数记录
     *
     * @param category 课程类别
     * @param positionId 职位ID
     * @param maxNum 最大选课人数
     */
    @Insert("insert into max (position_id, max_num, category) VALUES(#{positionId},#{maxNum},#{category})")
    void addMaxNumByPosition(String category,Integer positionId, Integer maxNum);

    /**
     * 根据请求获取课程列表
     *
     * @param teacher 教师姓名
     * @param courseName 课程名称
     * @return 匹配的课程列表
     */
    @Select("select * from course where course_name like CONCAT('%', #{courseName}, '%') or teacher like CONCAT('%', #{teacher}, '%')")
    List<CourseModel> getCourseByReq(String courseName, String teacher);
}
