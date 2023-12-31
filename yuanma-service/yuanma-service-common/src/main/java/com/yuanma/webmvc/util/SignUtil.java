package com.yuanma.webmvc.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.SortedMap;

@Slf4j
public class SignUtil {

    /**
     * 验证签名
     *
     * @param params
     * @param sign
     * @return
     */
    public static boolean verifySign(SortedMap<String, String> params, String sign, Long timestamp) {
        String paramsJsonStr = "Timestamp" + timestamp + JSONObject.toJSONString(params);
        return verifySign(paramsJsonStr, sign);
    }

    /**
     * 验证签名
     *
     * @param params
     * @param sign
     * @return
     */
    public static boolean verifySign(String params, String sign) {
        log.info("Header Sign : {}", sign);
        if (StringUtils.isEmpty(params)) {
            return false;
        }
        log.info("Param : {}", params);
        String paramsSign = getParamsSign(params);
        log.info("Param Sign : {}", paramsSign);
        return sign.equals(paramsSign);
    }

    /**
     * @return 得到签名
     */
    public static String getParamsSign(String params) {
        return DigestUtils.md5DigestAsHex(params.getBytes()).toUpperCase();
    }


    /**
     * @param params 所有的请求参数都会在这里进行排序加密
     * @return 验证签名结果
     */
    public static boolean verifySign(SortedMap<String, String> params) {

        String urlSign = params.get("sign");
        log.info("Url Sign : {}", urlSign);
        if (params == null || StringUtils.isEmpty(urlSign)) {
            return false;
        }
        //把参数加密
        String paramsSign = getParamsSign(params);
        log.info("Param Sign : {}", paramsSign);
        return !StringUtils.isEmpty(paramsSign) && urlSign.equals(paramsSign);
    }

    /**
     * @param params 所有的请求参数都会在这里进行排序加密
     * @return 得到签名
     */
    public static String getParamsSign(SortedMap<String, String> params) {
        //要先去掉 Url 里的 Sign
        params.remove("sign");
        String paramsJsonStr = JSONObject.toJSONString(params);
        return DigestUtils.md5DigestAsHex(paramsJsonStr.getBytes()).toUpperCase();
    }

}
