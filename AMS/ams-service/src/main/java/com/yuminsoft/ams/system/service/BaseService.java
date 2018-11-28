package com.yuminsoft.ams.system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Service基础类
 * @author wulj
 */
public abstract class BaseService {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${sys.code}")
    protected String sysCode;

    /** pic文件管理地址 */
    @Value("${ams.pic.image.url}")
    protected String picImageUrl;

    /** pic信审权限 */
    @Value("${ams.pic.image.picApproval}")
    protected String picApproval;

    @Value("${sys.credit.zx}")
    protected String sysCreditZX;

    @Value("${ams.pms.url}")
    protected String pmsUrl;
}
