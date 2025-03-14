package com.hitwh.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

/**
 * 分页查询结果的封装类
 * 用于封装分页查询的结果，包括总记录数和具体的查询结果列表
 */
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
@AllArgsConstructor
public class PageBean {
    /**
     * 记录总数
     * 用于存储某个整体的数量或大小
     */
    private long total;
    /**
     * 内容
     * 用于表示表格、列表等结构中的行数量
     */
    private Object rows;
}
