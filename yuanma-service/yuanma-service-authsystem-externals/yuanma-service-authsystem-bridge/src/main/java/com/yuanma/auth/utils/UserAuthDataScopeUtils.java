package com.yuanma.auth.utils;

import com.alibaba.fastjson.JSONObject;
import com.yuanma.auth.bean.UserDataScope;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class UserAuthDataScopeUtils {

    private static String HS512KEY="d2lubmVyNjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=";

    public static UserDataScope acquireByTokenBase64(String token){
        token = token.split(" ")[0];
        Claims claims = parseToken(token);
        if(claims.containsKey("userDataScope")){
            return JSONObject.parseObject(claims.get("userDataScope").toString(),UserDataScope.class);
        }
        return null;
    }

    public static <T> UserDataScope acquireByTokenBase64(String token,Class<T> clz){
        UserDataScope userDataScope = acquireByTokenBase64(token);
        if(null != userDataScope && null != userDataScope.getData()){
           T bean = JSONObject.parseObject(JSONObject.toJSONString(userDataScope.getData()),clz);
           userDataScope.setData(bean);
        }
        return userDataScope;
    }

    public static UserDataScope convert(String strUserDataScope){
        if(null != strUserDataScope){
            return JSONObject.parseObject(strUserDataScope,UserDataScope.class);
        }
        return null;
    }

    public static <T> UserDataScope convert(String strUserDataScope,Class<T> clz){
        UserDataScope userDataScope = convert(strUserDataScope);
        if(null != userDataScope && null != userDataScope.getData()){
            T bean = JSONObject.parseObject(JSONObject.toJSONString(userDataScope.getData()),clz);
            userDataScope.setData(bean);
        }
        return userDataScope;
    }

    private static Claims parseToken(String token) {
        String tokenSecret = System.getProperty("tokenBase64Secret");
        if(null == tokenSecret){
            tokenSecret = HS512KEY;
        }
        return Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJlZjI5NjVjZDhlMjk0OTdjOWNhZGM3MzY0N2UzOWYyZCIsImF1dGgiOiJhZG1pbiIsInVpZCI6MSwidXNlckRhdGFTY29wZSI6IntcImRhdGFcIjpcIlVzZXJBdXRob3JpdHlTZXJ2aWNlSW1wbFVzZXJBdXRob3JpdHlTZXJ2aWNlSW1wbFwiLFwidXNlcklkXCI6XCIxXCJ9Iiwic3ViIjoicm9vdCJ9.lI94Gkkp5K2M04A2KUr12cX85oQQSg8Kto1k4MQlmZIvcpiP8cuPimVfsYu3Ezei4uMSyHPxnQBeievUB7ZpWw";
        UserDataScope userDataScope = UserAuthDataScopeUtils.acquireByTokenBase64(token);
        System.out.println(userDataScope);
    }

}
