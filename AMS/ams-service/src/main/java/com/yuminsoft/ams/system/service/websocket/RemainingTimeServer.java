package com.yuminsoft.ams.system.service.websocket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yuminsoft.ams.system.service.system.TimeManageService;
import com.yuminsoft.ams.system.vo.system.TimeManageRemainVO;

@Service
@ServerEndpoint("/socket/remainingTime/{usercode}")
public class RemainingTimeServer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RemainingTimeServer.class);
   
	private static TimeManageService timeManageService;
    
	@Autowired
    public void setTimeManageService(TimeManageService timeManageService){
		RemainingTimeServer.timeManageService=timeManageService;
    }

    /**
     * 向客户端发送消息函数
     * @param session
     */
    @OnMessage
    public void onMessage(String usercode, Session session) {
    	TimeManageRemainVO vo = timeManageService.getRoleAndReaminTime(usercode);
    	vo.setRemainLoginTime(timeManageService.getRemainingTime(usercode));
    	
    	 try {
             session.getBasicRemote().sendText(JSON.toJSONString(vo));
         } catch (IOException e) {
         	LOGGER.error("向客户端发送消息异常!!!", e);
         }
    }

    /**
     * 客户端连接响应函数
     * @param usercode
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("usercode")String usercode,Session session){
    	
    }

    /**
     * 客户端断开响应
     * @param session
     */
    @OnClose
    public void onClose(Session session){
    	
    }
}
