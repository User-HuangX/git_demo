package com.hitwh.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * CourseModel类用于表示课程的相关信息，包括课程的基本属性、目标学生群体、学习时间和课程容量等
 * 该类使用Lombok注解来简化getter和setter的编写，并使用Jackson注解来控制序列化和反序列化过程
 */
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
@AllArgsConstructor
public class CourseModel {
    /**
     * 课程ID，用于唯一标识一门课程
     */
    private Integer courseId;

    /**
     * 课程名称，描述课程的主题
     */
    private String courseName;

    /**
     * 授课教师姓名，指示谁是这门课程的讲师
     */
    private String teacher;

    /**
     * 表示课程的类型,如设计、编程、音乐等
     */
    private String courseSort;

    /**
     * 目标年级，指定该课程适合哪个年级的学生学习
     */
    private String targetGrade;

    /**
     * 目标学院，限定课程面向的特定学院学生
     */
    private String targetDepartment;

    /**
     * 目标专业，明确课程适用的专业领域
     */
    private String targetMajor;

    /**
     * 学习时间，说明课程安排的学习时间段或周期
     */
    private String studyTime;

    /**
     * 课程容量，定义可以选修该课程的学生人数上限
     */
    private Integer capacity;

    /**
     * 课程类别，对课程进行分类以便管理和选择，例如必修、选修等
     */
    private String category;

    /**
     * 课程状态，用于表示课程的当前状态，如已选、未选、已满等
     */
    private int status;

    /**
     * 剩余名额，表示当前课程还可以被多少学生选修
     */
    private int remain;

    /**
     * 选课时间，记录学生选修该课程的时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date chooseTime;

    /**
     * 结束选课时间，表示该课程选修截止的时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endChooseTime;

}
