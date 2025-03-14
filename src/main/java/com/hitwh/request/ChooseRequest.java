/**
 * 选课请求类
 * 用于表示学生选择课程的请求信息
 */
package com.hitwh.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ChooseRequest 类用于封装选课请求的数据
 * 它包含了课程名和教师两个属性，通过 Lombok 注解自动生成构造函数和 getter/setter 方法
 * 使用 Jackson 注解来控制对象与 JSON 之间的序列化和反序列化过程
 */
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
@AllArgsConstructor
public class ChooseRequest {
    /**
     * 课程名
     * 用于标识选课请求中的课程名称
     */
    String courseName;

    /**
     * 授课教师
     * 用于标识选课请求中的授课教师姓名
     */
    String teacher;
}
