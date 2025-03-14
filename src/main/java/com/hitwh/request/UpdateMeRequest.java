package com.hitwh.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 员工实体类
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMeRequest {

    private String password;
    /**
     *工号或学号
     */
    private String email;
    /**
     * 电话号码，用于唯一标识用户的联系方式
     */

    private String phone;



}

