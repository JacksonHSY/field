package com.yuminsoft.ams.system.util;

import com.yuminsoft.ams.system.common.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * redis cache 工具类 
 * @author fuhongxing
 */
@Service
public final class RedisUtil {

	@Value("${sys.code}")
	private String sysCode;	// 系统编号

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private RedisTemplate<Serializable, Object> redisTemplate;  
  
	/**
	 * 批量删除对应的value
	 * 
	 * @param keys
	 */
	public void remove(final String... keys) {
		LOGGER.info("批量删除...");
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 批量删除key
	 * 
	 * @param pattern
	 */
	public void removePattern(final String pattern) {
		Set<Serializable> keys = redisTemplate.keys(pattern);
		if (!keys.isEmpty()){
			redisTemplate.delete(keys);
		}
	}

	/**
	 * 删除对应的value
	 * 
	 * @param key
	 */
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 根据类型删除缓存
	 * @param type	缓存类型
	 */
	public void removeByCacheType(final EnumUtils.CacheType type){
		LOGGER.info("根据类型删除缓存, cacheType:{}", type.getPrefix());
		redisTemplate.delete(redisTemplate.keys(sysCode + "_" + type.getPrefix() + "_" + "*"));
	}

	/**
	 * 删除所有value
	 *
	 * @author wulj
	 */
	public void removeAll(){
		LOGGER.info("清空redis");
		redisTemplate.delete(redisTemplate.keys("*"));
	}

	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	public Object get(final String key) {
		Object result = null;
		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
		result = operations.get(key);
		return result;
	}

	/**
	 * 读取缓存
	 * @param type	缓存类型
	 * @param key	缓存键
	 * @return
	 */
	public Object get(EnumUtils.CacheType type, final String key) {
		Object result = null;
		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();

		result = operations.get(sysCode + "_" + type.getPrefix() + "_" + key);

		return result;
	}


	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			LOGGER.error("redis缓存写入异常...", e);
		}

		return result;
	}

	/**
	 * 写入缓存
	 * @param key 
	 * @param value
	 * @param expireTime 有效时间
	 * @return
	 */
	public boolean set(final String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			LOGGER.error("redis缓存写入异常...", e);
		}
		return result;
	}

	/**
	 * 写入缓存
	 * @param type	缓存类型
	 * @param key	缓存键
	 * @param value	缓存值
	 * @author wulinjie
	 * @return
	 */
	public boolean set(EnumUtils.CacheType type, String key, Object value){
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = this.redisTemplate.opsForValue();
			operations.set(this.sysCode + "_" + type.getPrefix() + "_" + key, value, type.getExpire(), type.getTimeUnit());
			result = true;
		}catch (Exception e){
			LOGGER.error("redis缓存写入异常...", e);
		}

		return result;
	}

}  