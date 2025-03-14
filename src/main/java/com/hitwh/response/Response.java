package com.hitwh.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@NoArgsConstructor
public class Response {
    /**
     * 返回1成功2是失败
     */
    private Integer code;
    /**
     * 对上方的解释
     */
    private String msg;
    /**
     * 传入的数据
     */
    private Object data;


    public Response(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 不传参时使用的方法
     */
    public static Response success(String msg) {
        return new Response(200, msg, null);
    }

    /**
     * 传参时使用的方法
     */
    public static Response success(String msg,Object data) {
        return new Response(200, msg, data);
    }

    /**
     * 失败时的方法
     */
    public static Response error(String msg) {
        return new Response(500, msg, null);
    }
}