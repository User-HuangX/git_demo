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
     * @param claims
     * @return
     */
    public static String generateJwt(Map<String, Object> claims) {
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SIGN_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .compact();
        return jwt;
    }

    /**
     * 解析JWT令牌
     *
     * @param jwt
     * @return
     */
    public static Claims parseJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(SIGN_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }



}