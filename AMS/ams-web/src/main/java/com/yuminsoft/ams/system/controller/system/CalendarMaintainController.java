package com.yuminsoft.ams.system.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 日历维护
 * @author YM10103
 *
 */
@Controller
@RequestMapping("/calendarMaintain")
public class CalendarMaintainController {

	/**
	 * 首页
	 * @return
	 */
	@RequestMapping("index")
	public String index(){
		return "system/calendarMaintain/calendarMaintain";
	}
}
