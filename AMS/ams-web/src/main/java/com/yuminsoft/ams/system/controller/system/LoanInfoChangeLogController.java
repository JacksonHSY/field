package com.yuminsoft.ams.system.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/loanInfoChangeLog")
public class LoanInfoChangeLogController {

	@RequestMapping("index")
	public String index(){
		return "system/loanInfoChangeLog/loanInfoChangeLog";
	}
}
