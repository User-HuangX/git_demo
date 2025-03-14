/**
 * 评论回复的响应对象
 * 用于封装评论相关信息，便于在系统间传输
 */
package com.hitwh.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论回复的响应对象
 * 用于封装评论相关信息，便于在系统间传输
 *
 * 使用 Lombok 注解实现自动构造和简化代码：
 * - @Data: 自动生成 getter 和 setter 方法
 * - @NoArgsConstructor: 自动生成无参构造函数
 * - @AllArgsConstructor: 自动生成全参构造函数
 *
 * 使用 Jackson 注解实现自动序列化和反序列化：
 * - @JsonAutoDetect: 设置所有字段的可见性，确保所有字段都能被自动序列化和反序列化
 */
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    /**
     * 评论者
     * 表示发表评论的用户
     */
    String commenter;

    /**
     * 评论内容
     * 表示用户发表的具体评论信息
     */
    String msg;
}
