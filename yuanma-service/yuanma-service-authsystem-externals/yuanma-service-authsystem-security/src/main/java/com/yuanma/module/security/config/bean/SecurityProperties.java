
package com.yuanma.module.security.config.bean;

import lombok.Data;

/**
 * Jwt参数配置
 */
@Data
public class SecurityProperties {
    /**
     * Request Headers ： Authorization
     */
    private String header = "Authorization";

    /**
     * 令牌前缀，最后留个空格 Bearer
     */
    private String tokenStartWith = "Bearer";

    /**
     * 必须使用最少88位的Base64对该令牌进行编码
     */
    private String base64Secret="d2lubmVyNjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=";

    /**
     * 令牌过期时间 此处单位/毫秒 ，默认4小时，
     */
    private Long tokenValidityInSeconds = 14400000L;

    /**
     * 在线用户key
     */
    private String onlineKey="online-token-";

    /**
     * 验证码
     */
    private String codeKey="code-key-";

    /**
     * token 续期检查时间范围（默认30分钟，单位毫秒），在token即将过期的一段时间内用户操作了，则给用户的token续期
     */
    private Long detect=1800000L;

    /**
     * 续期时间范围，默认1小时，单位毫秒
     */
    private Long renew=3600000L;

    public String getTokenStartWith() {
        return tokenStartWith + " ";
    }
}
