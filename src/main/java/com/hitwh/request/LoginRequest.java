/**
 * 登录请求类
 * 该类用于封装用户登录时的请求数据，包括用户ID、手机号和密码
 * 使用Lombok注解简化getter和setter的编写，并使用Jackson注解控制序列化行为
 */
package com.hitwh.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录请求实体类
 *
 * 该类通过@Data、@NoArgsConstructor和@AllArgsConstructor注解自动生成getter、setter、无参构造器和全参构造器
 * 使用@JsonAutoDetect注解设置所有字段可见，以便于JSON序列化和反序列化
 *
 * 字段说明：
 * - userId: 用户ID，用于标识用户
 * - phoneNum: 手机号，用户的联系方式，也是登录的一种方式
 * - password: 用户密码，用于验证用户身份
 */
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String userId;
    private String phone;
    private String password;
}
