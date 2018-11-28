package com.yuminsoft.ams.system.exception;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Throwables;
import com.yuminsoft.ams.system.common.AmsConstants;
/**
 * 全局异常处理
 * @author fuhongxing
 */
@ControllerAdvice
public class GlobalExceptionHandler{    
	
   private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
   
   //异常会被这个方法捕获
   @ExceptionHandler(Exception.class)    
   public ModelAndView handleError(HttpServletRequest req, HttpServletResponse rsp, Exception e) throws Exception {
	   	LOGGER.error("=======全局异常处理=======");
        return handleError(req, rsp, e, "/error/error", HttpStatus.NOT_FOUND);    
   } 
   
	public ModelAndView handleError(HttpServletRequest req, HttpServletResponse rsp, Exception e, String viewName, HttpStatus status) throws Exception {    
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {      
       	 	throw e;
        }
        //判断是否为自定义异常
//        String errorMsg = e instanceof BusinessException ? e.getMessage() : AmsConstants.DEFAULT_ERROR_MESSAGE;    
        String errorMsg = "";
        if(e instanceof BusinessException || e instanceof FileException){
        	errorMsg = e.getMessage();
        }else{
        	errorMsg = AmsConstants.DEFAULT_ERROR_MESSAGE;
        }
        //获取异常具体信息
        String errorStack = Throwables.getStackTraceAsString(e);   
        LOGGER.error("Request Url: {} ,ErrorInfo {}", req.getRequestURI(), errorStack);
        //判断是否为ajax请求
        if (isAjax(req)) { 
             return handleAjaxError(rsp, errorMsg, status);   
        }        
        return handleViewError(req.getRequestURL().toString(), errorStack, errorMsg, viewName);  
    }   
	
    /**
     * 页面跳转请求错误信息输出
     * @param url
     * @param errorStack
     * @param errorMessage
     * @param viewName
     * @return
     */
    public ModelAndView handleViewError(String url, String errorStack, String errorMessage, String viewName) {        
         ModelAndView mav = new ModelAndView();       
         mav.addObject("exception", errorStack);        
         mav.addObject("url", url);
         //消息内容
         mav.addObject("message", errorMessage);  
         mav.addObject("timestamp", new Date());
         //跳转页面
         mav.setViewName(viewName);    
         return mav;   
    }
    
    /**
     * ajax请求错误信息输出
     * @param rsp
     * @param errorMessage
     * @param status
     * @return
     * @throws IOException
     */
    public ModelAndView handleAjaxError(HttpServletResponse rsp, String errorMessage, HttpStatus status) throws IOException {        
       rsp.setCharacterEncoding("UTF-8");       
       rsp.setStatus(status.value());      
       PrintWriter writer = rsp.getWriter();
       writer.write(errorMessage);        
       writer.flush();
       return null;    
     }
    
    /**
     * 判断ajax请求
     * @param request
     * @return
     */
    public boolean isAjax(HttpServletRequest request){
        return  (request.getHeader("X-Requested-With") != null  && "XMLHttpRequest".equals( request.getHeader("X-Requested-With").toString())) ;
    }

      //500的异常会被这个方法捕获
//      @ExceptionHandler(Exception.class) 
//      @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) 
//      public ModelAndView handle500Error(HttpServletRequest req, HttpServletResponse rsp, Exception e) throws Exception { 
//    	  LOGGER.error("500异常..."); 
//    	  return handleError(req, rsp, e, "/WEB-INF/views/error/error", HttpStatus.INTERNAL_SERVER_ERROR);  
//      }    

  }