package com.yuminsoft.ams.system;

import org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer;

import com.yuminsoft.ams.system.common.Environment;


/***
 * 系统环境变更
 * @author fuhongxing
 *
 */
public class SystemConfig extends PreferencesPlaceholderConfigurer {
	private static SystemConfig this_;
	public Environment environment = Environment.DEVELOP;
	
	public SystemConfig(String environment) {
		this_ = this;
		this.environment = Environment.valueOf(environment);
	}
	public static SystemConfig get() {
		return this_;
	}

}
