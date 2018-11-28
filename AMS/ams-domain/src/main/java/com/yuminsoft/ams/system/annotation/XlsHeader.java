package com.yuminsoft.ams.system.annotation;

import java.lang.annotation.*;

/**
 * Created by wulinjie on 2017/6/20.
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XlsHeader {

    String value() default "";
}
