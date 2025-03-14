/**
 * CommentEntity类用于表示评论信息
 * 它是一个实体类，通常用于数据传输或ORM映射
 * 使用Lombok和Jackson注解来简化代码和自动处理JSON序列化/反序列化
 */
package com.hitwh.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 使用@Data注解自动生成getter和setter方法，以及hashCode、equals和toString方法
 * 使用@JsonAutoDetect注解设置所有字段对Jackson可见，以便自动序列化/反序列化
 * 使用@NoArgsConstructor和@AllArgsConstructor注解自动生成无参和全参构造函数
 */
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    /**
     * 评论的唯一标识符
     */
    private Integer id;

    /**
     * 评论者的用户ID
     */
    private Integer commenter;

    /**
     * 评论内容
     */
    private String comment;

    /**
     * 被评论者的用户ID
     */
    private Integer commentator;

    /**
     * 评论的状态，通常用于表示评论是否被审核或是否可见
     */
    private Integer status;

    /**
     * 评论的类型，用于区分不同的评论场景或来源
     */
    private String type;
}
