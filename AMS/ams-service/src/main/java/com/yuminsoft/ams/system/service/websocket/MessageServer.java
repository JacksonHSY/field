package com.yuminsoft.ams.system.service.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.ymkj.base.core.biz.api.message.Response;
import com.ymkj.base.core.web.result.JsonResult;
import com.ymkj.pms.biz.api.service.IMessageExecuter;
import com.ymkj.pms.biz.api.vo.request.ReqMessageVO;
import com.yuminsoft.ams.system.common.EnumUtils;

/**
 * 站内消息
 * @author fuhongxing
 */
@Service
@ServerEndpoint("/api/MessageServer/{usercode}")
public class MessageServer {
    private static Map<Session,String> messageClients = new ConcurrentHashMap<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageServer.class);
   
	private static IMessageExecuter messageExecuter;
    
	@Autowired
    public void setMessageService(IMessageExecuter messageExecuter){
        MessageServer.messageExecuter=messageExecuter;
    }
    
    @Value("${sys.code}")
	private String sysCode;
    /**
     * 发送消息数量
     * @param usercodes
     */
    public void sendUnreadCountToEmployees(List<String> usercodes){
    	LOGGER.info("=========调用平台发送站内消息数量=========");
        if (usercodes!=null && usercodes.size()>0){
            for (String usercode:usercodes){
            	LOGGER.info("==============发送给："+usercode);
                List<Session> sessions=getSessionsByUsercode(usercode);
                for (Session session:sessions){
                    onMessage(usercode,session);
                }
            }
        }else {
            onMessage(null,null);
        }
    }
    /**
     * 保存站内消息
     * @param usercodes
     */
    public boolean sendMessages(ReqMessageVO vo){
    	LOGGER.info("========调用平台保存站内消息========");
    	boolean flag = false;
    	Response<Boolean> response = messageExecuter.sendMessageToEmployee(vo);
    	if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
    		flag = response.getData();
		}
    	return flag;
    }

    /**
     * 更新用户未读消息条数并发送到客户端
     * @param usercode
     * @param session
     */
    private void updateAndSend(String usercode, Session session){
    	ReqMessageVO reqMessageVO = new ReqMessageVO();
        reqMessageVO.setUsercode(usercode);
        reqMessageVO.setSysCode(sysCode);
        Response<Integer> response = messageExecuter.getUnreadCount(reqMessageVO);
        JsonResult<Integer> result = new JsonResult<Integer>();
        if (null != response && EnumUtils.ResponseCodeEnum.SUCCESS.getValue().equals(response.getRepCode())) {
        	result.setData(response.getData());
		}
        try {
            session.getBasicRemote().sendText(JSON.toJSONString(result));
        } catch (IOException e) {
        	LOGGER.error("更新用户未读站内消息条数并发送到客户端异常!!!", e);
        }
    }

    /**
     * 向客户端发送消息函数
     * @param session
     */
    @OnMessage
    public void onMessage(String usercode, Session session) {
        if (session!=null && !StringUtils.isEmpty(usercode)){
            updateAndSend(usercode,session);
        }else {
            /*向所有已连接的客户端发送最新的未读消息条数*/
            for (Session session1:messageClients.keySet()){
                updateAndSend(messageClients.get(session1),session1);
            }
        }
    }

    /**
     * 客户端连接响应函数
     * @param usercode
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("usercode")String usercode,Session session){
        messageClients.put(session,usercode);
    }

    /**
     * 客户端断开响应
     * @param session
     */
    @OnClose
    public void onClose(Session session){
        messageClients.remove(session);
    }

    public List<Session> getSessionsByUsercode(String usercode){
        List<Session> res=new ArrayList<Session>();
        if (StringUtils.isEmpty(usercode)){
            return res;
        }
        Set<Session> keys=messageClients.keySet();
        for (Session session:keys){
            if (usercode.equals(messageClients.get(session))){
                res.add(session);
            }
        }
        return res;
    }
}
