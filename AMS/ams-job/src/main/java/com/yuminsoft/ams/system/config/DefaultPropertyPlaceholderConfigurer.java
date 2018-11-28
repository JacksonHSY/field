package com.yuminsoft.ams.system.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.spring.config.PropertySourcesProcessor;
import com.ctrip.framework.foundation.Foundation;
import com.yuminsoft.ams.system.util.ThreeDes;
import com.zdmoney.credit.util.ConfigUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.IOException;

/**
 * 读取配置文件（properties） 做二次翻译工作
 *
 * @author Ivan
 *
 */
public class DefaultPropertyPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPropertyPlaceholderConfigurer.class);
	/**
	 * 数据库用户名Key
	 */
	private final static String USER_NAME_KEY = "jdbc.username";

	/**
	 * 数据库密码Key
	 */
	private final static String PASS_WORD_KEY = "jdbc.password";

	/**
	 * 数据库加密解密私钥key
	 */
	private final static String ENCRYPT_KEY = "encrypt.private.key";
	/**
	 * 读取哪个namespace
	 */
	private static final String NAMESPACE = "ams-job-application";


	public void loadProperties() throws IOException {
		//读取apollo中心的配置文件，解密数据库用户名密码，并重新塞到配置中
		String env = Foundation.server().getEnvType();
		LOGGER.info("=====加载配置文件信息完毕，当前系统环境：{}=====",env);

		Config config = ConfigService.getConfig(PropertySourcesProcessor.getNamespaces());
		String username = config.getProperty(USER_NAME_KEY, "ams");
		String password = config.getProperty(PASS_WORD_KEY, "ams");

		if(Env.FAT.name().equals(env) || Env.UAT.name().equals(env)|| Env.PRO.name().equals(env)) {
			LOGGER.info(env + "环境，进行数据库用户名密码解密操作开始......");
			System.setProperty(USER_NAME_KEY, new String(ThreeDes.decryptMode(ThreeDes.keyBytes, ThreeDes.hex2byte(username.getBytes()))));
			System.setProperty(PASS_WORD_KEY, new String(ThreeDes.decryptMode(ThreeDes.keyBytes, ThreeDes.hex2byte(password.getBytes()))));
			LOGGER.info(env + "环境，进行数据库用户名密码解密操作结束......");
		}

		// 加载明感字段加密解密公钥
		String encrypt = config.getProperty(ENCRYPT_KEY, "2c39f184fd42605f");
		if (StringUtils.isNotEmpty(encrypt)) {
			LOGGER.info(env + "环境，设置数据库字段加密解密的私钥为:" + encrypt);
			ConfigUtil.privateKey = encrypt;
		}

	}

}
