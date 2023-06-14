package com.yuanma.webmvc.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class SnowflakeUtils {

    private static long workerId = 0;
    private static long datacenterId = 1;
    private static Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);

    public static synchronized long snowflakeId() {
        return snowflake.nextId();
    }

}
