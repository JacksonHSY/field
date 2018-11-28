package com.yuminsoft.ams.system.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dictionaryType")
public class DictionaryTypeController {
	
	@RequestMapping("/index")
	public String Index(){
		return "system/dictionaryType/dictionaryType";
	}
}
