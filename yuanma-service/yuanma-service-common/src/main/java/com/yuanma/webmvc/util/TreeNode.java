package com.yuanma.webmvc.util;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TreeNode {

    String value() default "";

    String id() default "id";
    String pid() default "pid";
    String children() default "children";

}
