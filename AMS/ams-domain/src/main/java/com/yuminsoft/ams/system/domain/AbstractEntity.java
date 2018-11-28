package com.yuminsoft.ams.system.domain;

import com.ymkj.pms.biz.api.vo.response.ResEmployeeVO;
import com.ymkj.sso.client.ShiroUtils;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 基类
 * 
 * @author fuhongxing
 * @modified wulj
 */
@Data
@ToString
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 5583035582769043365L;

	private Long id;// 主键id

	private Date createdDate = new Date();// 创建时间

	private Date lastModifiedDate = new Date();// 修改时间

	private String createdBy = "anonymous";// 创建人

	private String lastModifiedBy = "anonymous";// 修改人

	public String getCreatedBy() {

		if(StringUtils.isNotEmpty(createdBy) && !"anonymous".equals(this.createdBy)){
			return createdBy;
		}

		try {
			ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
			if (null != currentUser) {
				this.createdBy = currentUser.getUsercode();
			}
		} catch (Exception e) {
			this.createdBy = "anonymous";
		}
		return createdBy;
	}

	public String getLastModifiedBy() {
		if(StringUtils.isNotEmpty(lastModifiedBy) && !"anonymous".equals(this.lastModifiedBy)){
			return lastModifiedBy;
		}
		try {
			ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
			if (null != currentUser) {
				this.lastModifiedBy = currentUser.getUsercode();
			}
		} catch (Exception e) {
			this.lastModifiedBy = "anonymous";
		}

		return lastModifiedBy;
	}

}
