package com.yuminsoft.ams.system.vo.pic;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * PIC 响应报文
 * Created by YM10195 on 2017/7/20.
 */
@Data
@ToString
public class PicResponseVo implements Serializable {

    private static final long serialVersionUID = 6000490337436354539L;

    private Boolean isFail; // true or false
    private Boolean isOk;   // true or false
    private String errorcode;   // 错误代码
    private String result;      // 数据

    public PicResponseVo(){

    }

    public PicResponseVo(Type type){
        if(type.equals(Type.SUCCESS)){
            this.isFail = false;
            this.isOk = true;
            this.errorcode = "000000";
            this.result = "";
        }else{
            this.isFail = true;
            this.isOk = false;
            this.errorcode = "111111";
            this.result = "";
        }
    }

    /**
     * 调用成功
     * @return
     */
    public static PicResponseVo success(){
        return new PicResponseVo(Type.SUCCESS);
    }

    /**
     * 调用失败
     * @return
     */
    public static PicResponseVo failure(){
        return new PicResponseVo(Type.FAILURE);
    }

    /**
     * 接口是否调用成功
     * @return
     */
    public boolean isSuccess(){
        return "000000".equals(this.errorcode);
    }

    public enum Type {
        /**
         * 成功
         */
        SUCCESS,
        /**
         * 失败
         */
        FAILURE
    }
}
