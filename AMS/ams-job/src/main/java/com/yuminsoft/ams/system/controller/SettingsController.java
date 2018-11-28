package com.yuminsoft.ams.system.controller;

import com.yuminsoft.ams.system.util.RedisUtil;
import com.yuminsoft.ams.system.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 系统设置
 * Created by wulj on 2017/5/22.
 */

@Controller
@RequestMapping("settings")
public class SettingsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "", method= RequestMethod.GET)
    public String index(){
        LOGGER.info("系统设置");

        return "settings";
    }

    @RequestMapping(value="cleanRedis", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> cleanCache(){
        // 清空redis
        redisUtil.removeAll();

        return new Result<String>(Result.Type.SUCCESS, "操作成功");
    }

}
