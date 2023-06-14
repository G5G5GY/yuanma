package com.yuanma.webmvc.util;

import com.yuanma.exception.ExceptionMessage;
import com.yuanma.exception.LoginException;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {

    public static String HS512KEY = "abcdefghijklmnopqrstuvwxyz";

    public static String getJWTString(String uid) {
        String tokenSecret = System.getProperty("tokenSecret");
        if(null == tokenSecret){
            tokenSecret = HS512KEY;
        }
        long nowMillis = System.currentTimeMillis();
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", uid);
        //claims.put(Claims.EXPIRATION, new Date(nowMillis + (30 * 60 * 1000)));
        claims.put(Claims.ISSUED_AT, createDate(nowMillis));

        JwtBuilder jwtBuilder = Jwts.builder().setClaims(claims);
        jwtBuilder.setExpiration(createDate(nowMillis + (2 * 24 * 60 * 60 * 1000)));
        jwtBuilder.signWith(SignatureAlgorithm.HS512, tokenSecret);
        return jwtBuilder.compact();
    }

    public static Claims parseToken(String token) {
        String tokenSecret = System.getProperty("tokenSecret");
        if(null == tokenSecret){
            tokenSecret = HS512KEY;
        }
        return Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();
    }


    /**
     * 判断token是否已经失效
     */
    public static boolean isTokenExpired(String token) {
        try {
            Date expiredDate = getExpiredDateFromToken(token);
            return expiredDate.before(new Date());
        } catch (ExpiredJwtException e){
            throw new LoginException(ExceptionMessage.ERR_E90003);
        }
    }

    /**
     * 从token中获取过期时间
     */
    private static Date getExpiredDateFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }

    public static String getUID(String token){
        Claims claims = TokenUtils.parseToken(token);
        return String.valueOf(claims.get("uid"));
    }


    public static Date createDate(long nowMillis){
        return new Date(nowMillis) ;
        //return DateUtils.addDays(new Date(nowMillis),-5) ;
    }

    public static void main(String[] args) {

       String token = TokenUtils.getJWTString("327EB609-363E-4893-870F-E60F457C190E");
        System.out.println(token);
        boolean result = TokenUtils.isTokenExpired(token);
        System.out.println(result);
        System.out.println(new Date( System.currentTimeMillis() + (2 * 24 * 60 * 60 * 1000))); ;;
    }



}
