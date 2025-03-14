package com.hitwh.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * 员工实体类
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    /**
     * 学生编号
     */
    @NotNull
    private Integer myId;
    /**
     * 登录的用户名
     */
    @NotNull
    private String userName;
    /**
     * 登录的密码
     */
    @NotBlank
    private String password;
    /**
     *工号或学号
     */
    private String userId;
    /**
     * 角色
     */
    private String role;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 电话号码，用于唯一标识用户的联系方式
     */
    private String phone;
    /**
     * 性别，描述用户的性别信息，可能的取值包括男、女等
     */
    private String gender;

    /**
     * 年级，表示用户当前的学业年级，如2024级，2023级等
     */
    private String grade;

    /**
     * 专业，描述用户所学的专业领域
     */
    private String major;

    /**
     * 系别，标识用户所属的学院或系
     */
    private String department;
    /**
     * 状态，表示用户的当前状态，可能的取值包括1(存在)、0(假删除)等
     */
    private String status;


}



