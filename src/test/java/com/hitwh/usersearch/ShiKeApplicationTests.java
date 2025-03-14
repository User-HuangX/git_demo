package com.hitwh.usersearch;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class ShiKeApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     * 生产JWT令牌
     */
    @Test
    public void jwtTest() {
        Map<String,Object > claims= new HashMap<>();//生成一个哈希map
        claims.put("id",1);
        claims.put("name","Tom");
        String jwt=Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,"hitwh666666666666666666666666666666666666666")//签名算法,其中字符串长度大于43位数
                .setClaims(claims)//自定义内容（载荷部分）
                .setExpiration(new Date(System.currentTimeMillis()+1000*3600))//设置过期时间,一小时后会过期
                .compact();
        System.out.println(jwt);//吧jwt令牌打印出来
    }

    /**
     * 校验JWT令牌
     */
 @Test
    public void jwtParse() {
        Claims claims = Jwts.parser().setSigningKey("hitwh666666666666666666666666666666666666666").build().parseSignedClaims("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiVG9tIiwiaWQiOjEsImV4cCI6MTczODQwMjc3MX0.AVxSC2u-S9NJcwVcs3JnPP0k3zBUgok5aJfKJJqJ8G0").getBody();
     System.out.println(claims);
    }
}
