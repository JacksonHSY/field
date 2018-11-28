package com.yuminsoft.ams.system.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 字典管理
 * @author zhouwq
 *
 */
@Controller
@RequestMapping("/dictionary")
public class DictionaryController {
	
	/**
	 * 字典管理页面
	 * @return
	 */
	@RequestMapping("/index")
	public String index() {
		return "system/dictionary/dictionary";
	}
}
