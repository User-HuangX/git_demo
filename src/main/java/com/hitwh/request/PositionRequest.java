package com.hitwh.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PositionRequest 类用于封装用户查询职位时的请求参数
 * 它包括位置ID、年级、专业和系别等信息，用于指定用户在查询职位时的偏好或条件
 *
 * @author [Your Name]
 * @date [Today's Date]
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionRequest {
    /**
     * 位置ID，用于唯一标识一个职位信息
     */
    private int positionId;
    /**
     * 年级，表示用户当前的学业年级，如2024级，2023级等
     */
    private String targetGrade;

    /**
     * 专业，描述用户所学的专业领域
     */
    private String targetMajor;

    /**
     * 系别，标识用户所属的学院或系
     */
    private String targetDepartment;
}
