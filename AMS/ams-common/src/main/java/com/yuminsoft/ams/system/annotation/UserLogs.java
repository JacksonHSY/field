package com.yuminsoft.ams.system.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.yuminsoft.ams.system.domain.system.UserLog.Type;

/**
 * 
 * 用户操作日志注解，方法设置后，围绕方法会记录结果
 * @author fuhongxing
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserLogs {
	/**环节*/
	String link() default "";
	/**操作*/
	String operation();
	/**备注*/
	String remark() default "";
	/**类型*/
	Type type() default Type.初审;

}
