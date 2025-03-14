package com.hitwh.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.*;

public class JwtUtils {

    private final static String SIGN_KEY = "hitwhhitwhhitwhhitwhhitwhhitwhhitwhhitwhhitwh";
    private final static Long EXPIRE = 43200000L;

    /**
     * 生成JWT令牌
     *
     */
    public static String generateJwt(Map<String, Object> claims) {
//        return Jwts.builder()
//                .addClaims(claims)
//                .signWith(SignatureAlgorithm.HS256, SIGN_KEY)
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
//                .compact();
        return Jwts.builder()
                .claims(claims)
                .signWith(SignatureAlgorithm.HS256,SIGN_KEY)
                .expiration(new Date(System.currentTimeMillis() + EXPIRE))
                .compact();
    }

    /**
     * 解析JWT令牌
     *
     */
    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(SIGN_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }



}