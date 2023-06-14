package com.yuanma.module.security.handler;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import com.yuanma.auth.bean.UserDataScope;
import com.yuanma.module.security.config.bean.SecurityProperties;
import com.yuanma.module.security.mode.dto.JwtUserDto;
import com.yuanma.module.security.mode.dto.SimpleUserDto;
import com.yuanma.webmvc.util.RedisUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    public static final String AUTHORITIES_KEY = "auth";
    private JwtParser jwtParser;
    private JwtBuilder jwtBuilder;

    public TokenProvider(SecurityProperties properties, RedisUtils redisUtils) {
        this.properties = properties;
        this.redisUtils = redisUtils;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getBase64Secret());
        Key key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        jwtBuilder = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512);
    }

    public Long getUid(Authentication authentication){
        Object principal = authentication.getPrincipal();
        if(principal instanceof SimpleUserDto){
            return ((SimpleUserDto)principal).getUserId();
        } else if(principal instanceof JwtUserDto){
            return ((JwtUserDto)principal).getUser().getId();
        }
        return null;
    }

    /**
     * 创建Token 设置永不过期，
     * Token 的时间有效性转到Redis 维护
     *
     * @param authentication /
     * @return /
     */
    public String createToken(Authentication authentication) {
        /*
         * 获取权限列表
         */
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Long uid = ((JwtUserDto)authentication.getPrincipal()).getUser().getId();
        UserDataScope userDataScope = ((JwtUserDto) authentication.getPrincipal()).getUserDataScope();

        JwtBuilder claim = jwtBuilder
                // 加入ID确保生成的 Token 都不一致
                .setId(IdUtil.simpleUUID())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("uid", uid);

        if(null != userDataScope){
            PropertyPreFilters filters = new PropertyPreFilters();
            PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filters.addFilter();
            excludefilter.addExcludes(new String[]{"data"});
            String strUserDataScope = JSONObject.toJSONString(userDataScope,excludefilter, SerializerFeature.PrettyFormat);
            claim.claim("userDataScope",strUserDataScope);
        }
        return claim.setSubject(authentication.getName())
                .compact();
    }



    /**
     * 依据Token 获取鉴权信息
     *
     * @param token /
     * @return /
     */
    Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        // fix bug: 当前用户如果没有任何权限时，在输入用户名后，刷新验证码会抛IllegalArgumentException
        Object authoritiesStr = claims.get(AUTHORITIES_KEY);
        Collection<? extends GrantedAuthority> authorities =
                ObjectUtil.isNotEmpty(authoritiesStr) ?
                        Arrays.stream(authoritiesStr.toString().split(","))
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList()) : Collections.emptyList();
        Long uid = Long.valueOf(claims.get("uid").toString());
        User principal = new SimpleUserDto(uid,claims.getSubject(), "******", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public Claims getClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * @param token 需要检查的token
     */
    public void checkRenewal(String token) {
        // 判断是否续期token,计算token的过期时间
        long time = redisUtils.getExpire(properties.getOnlineKey() + token) * 1000;
        Date expireDate = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) time);
        // 判断当前时间与过期时间的时间差
        long differ = expireDate.getTime() - System.currentTimeMillis();
        // 如果在续期检查的范围内，则续期
        if (differ <= properties.getDetect()) {
            long renew = time + properties.getRenew();
            redisUtils.expire(properties.getOnlineKey() + token, renew, TimeUnit.MILLISECONDS);
        }
    }

    public String getToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(properties.getHeader());
        if (requestHeader != null && requestHeader.startsWith(properties.getTokenStartWith())) {
            return requestHeader.substring(7);
        }
        return null;
    }

    public static void main(String[] args) {
        String base64Secret="d2lubmVyNjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=";
        String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJiY2VkOWZmN2YwMjk0NWZkOTVhMGYxMTg5ZTI4M2Y5NiIsImF1dGgiOiJyZXBvcnRfd2Vla19kYXksd29ya3NwYWNlLHBhc3NhZ2Usd2VhdGhlcjpsaXN0LG1ldGFkYXRhOmxheWVyLHJlcG9ydF95ZWFyX21vbnRoLG1ldGFkYXRhOmZvbGRlcix3b3JrZmxvd19kZWYscmVzb3VyY2Vfc2NoZWR1bGluZ19pbmRleCxtZXRhZGF0YTpzdG9yZWhvdXNlLHdvcmtmbG93X3Rhc2ssZmFjaWxpdHk6bGlzdCxyZXBvcnRfeWVhcl9kYXksZXJyb3Jfc3RhdGlzdGljcyx3b3JrZmxvd19pbnN0YW5jZSxkYXRhSm9iOmNvbmZpZyxkYXRhX2FsYXlfbW9kdWxlLGVuZ2luZV9saXN0LGRpc3Bvc2l0aW9uOmxpc3QscGVybWlzc2lvbnM6bGlzdCxkYXRhc291cmNlX21vZHVsZSxmaWxlSm9iOmFkZCxkYXRhSm9iOmFkZCxwb3NpdGlvbjpsaXN0LG1ldGFkYXRhOnRhZ19saXN0LHJlcG9ydF9kYXksZGF0YUpvYjplZGl0LHN0YW5kYXJkX21hbmFnZXIsZGF0YXNvdXJjZSxhbmFseXNpczpkYXRhYmFzZSxhbmFseXNpczpIREZTLGRhdGFfcXVhbGl0eV9tb2R1bGUsY29sbGVjdF9kYXRhX21vZHVsZSxtZXRhZGF0YTplbmNyeXB0aW9uLG90aGVyLG1ldGFkYXRhOmNhdGFsb2d1ZV9saXN0LGNyZWF0ZV93b3Jrc3BhY2UsbGFiZWw6bGlzdCxiYXNpY1BhcmFtcyxkYXRhSm9iOmRlbGV0ZSxkYXRhc291cmNlOmVkaXQsYmFzaWNJbmZvLGRhdGFzb3VyY2U6YWRkLGRhdGFfc2NoZWR1bGVyX21vZHVsZSxzdHVkeUp1ZGdlLGZpbGVKb2I6ZWRpdCxmaWxlSm9iOmRlbGV0ZSxkYXRhX2dvdmVyX21vZHVsZSxzaXRlOmxpc3QsYWRtaW5pc3RyYXRpdmU6bGlzdCxkYXRhc291cmNlOmNoZWNrLGJ1c2luZXNzZGF0ZTpsaXN0LGNhbWVyYURhdGE6bGlzdCxkYXRhUGxhdGZvcm0sam9iX21hbmFnZSxwYXJ0eVJlcG9ydDpsaXN0LGRpc3BsYXksYW5hbHlzaXM6d29ya3NwYWNlLHBhcnR5QmFjayxtZXRhZGF0YV9tYW5hZ2VyX21vZHVsZSx3aW5uZXJjYW1lcmE6bGlzdCxmaWxlSm9iOmNvbmZpZyxtZXRhZGF0YTp0YWJsZSxyZXBvcnRfbW9udGhfZGF5LGRhdGU6bGlzdCxmaWxlX3dvcmssam9iX2FjY2VzcyxjYW1lcmE6bGlzdCxtZXRhZGF0YS1tYW5hZ2VyLGRhdGFzb3VyY2U6dGVzdCIsInVpZCI6MTAwOSwidXNlckRhdGFTY29wZSI6Intcblx0XCJkYXRhRmxhZ1wiOnRydWUsXG5cdFwidXNlckZsYWdcIjpcIjBcIixcblx0XCJ1c2VySWRcIjpcIjEwMDlcIixcblx0XCJ1c2VyTmFtZVwiOlwiZ2dmd1wiLFxuXHRcInVzZXJOaWNrTmFtZVwiOlwiZ2dmd1wiXG59Iiwic3ViIjoiZ2dmdyJ9.zEI360OPz97RIjM7r-7HGAGFN7Vmmd20iM13gvI_D_0bcJSBYqiB4JUD5OYLAA_PPGhX9ZdYzRsSsMmQX17wuQ";


        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();


        System.out.println(jwtParser.parseClaimsJws(token).getBody());



    }
}
