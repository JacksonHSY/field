package com.yuminsoft.ams.system.service.uflo.process.provider;

import com.bstek.uflo.env.EnvironmentProvider;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
/**
 * EnvironmentProvider接口实现类
 * @author fuhongxing
 *
 */
@Component
public class AMSEnvironmentProvider implements EnvironmentProvider{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private PlatformTransactionManager platformTransactionManager;
	
	/**
     * @return 返回流程引擎需要使用的Hibernate SessionFactory
     */
	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	 /**
     * @return 返回与当前SessionFactory绑定的PlatformTransactionManager对象
     */
	 @Override
	public PlatformTransactionManager getPlatformTransactionManager() {
		return platformTransactionManager;
	}
	
	public void setPlatformTransactionManager(
	PlatformTransactionManager platformTransactionManager) {
		this.platformTransactionManager = platformTransactionManager;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	 /**
     * @return 返回当前系统的登录用户
     */
	 @Override
	public String getLoginUser() {
		return "anonymous";
	}

	  /**
     * @return 返回当前系统分类ID
     */
	  @Override
	public String getCategoryId() {
		return null;
	}
	
}
