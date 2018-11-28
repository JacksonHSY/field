package com.yuminsoft.ams.system.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.yuminsoft.ams.system.editor.DateEditor;

/**
 * Created by wulinjie on 2017/6/17.
 */
public class BaseController {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired(required=false)
    protected HttpServletRequest request;

    /** pic文件管理地址 */
    @Value("${ams.pic.image.url}")
    protected String picImageUrl;

    /** pic信审权限 */
    @Value("${ams.pic.image.picApproval}")
    protected String picApproval;
    
    /** pic信审查询 */
	@Value("${ams.pic.image.picApprovalQuery}")
	protected String picApprovalQuery;

    @Value("${sys.credit.zx}")
    protected String sysCreditZX;

    @Value("${sys.code}")
    protected String sysCode;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new DateEditor(true));
    }

}
