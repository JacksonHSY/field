package com.yuminsoft.ams.system.service.pic.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ymkj.sso.client.ShiroUtils;
import com.yuminsoft.ams.system.service.pic.PicService;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pic.AttachmentVo;
import com.yuminsoft.ams.system.vo.pic.PapersTypeVo;
import com.yuminsoft.ams.system.vo.pic.PicRequestVo;
import com.yuminsoft.ams.system.vo.pic.PicResponseVo;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

/**
 *
 * Created by YM10195 on 2017/7/19.
 */
@Service
public class PicServiceImpl implements PicService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PicServiceImpl.class);

    @Value("${ams.pic.image.url}")
    private String picImgUrl;   // pic文件操作地址
    /** pic信审权限 */
    @Value("${ams.pic.image.picApproval}")
    private String picApproval;

    private static final String URL_PAPRES_TYPE = "/api/paperstype/list";    // 获取文件类型集合（文件夹、目录）接口
    private static final String URL_PICTURE_LIST = "/api/picture/list";      // 获取文件集合接口
    private static final String URL_PICTURE_DELETE = "/api/picture/delete";  // 删除附件接口

    /**
     * 根据申请件编号， 查询所有附件列表
     * @param requestVo 查询VO
     * @author wulinjie
     * @return 附件列表
     */
    @Override
    public List<AttachmentVo> getAllAttachment(PicRequestVo requestVo) {
        LOGGER.info("查询所有附件列表");
        List<AttachmentVo> attachmentVoList = Lists.newArrayList();
        List<PapersTypeVo> papersTypeList = getPapersTypeList(requestVo); // 查询该申请件所有附件的文件目录
        if(CollectionUtils.isNotEmpty(papersTypeList)){
            // 查询每个目录下的附件列表
            for (PapersTypeVo papersTypeVo : papersTypeList) {
                requestVo.setSubclass_sort(papersTypeVo.getCode()); // 设置附件所属目录
                PicResponseVo responseVo = execute(URL_PICTURE_LIST, requestVo);
                LOGGER.info("查询所有附件列表, Resquest:{}, Response:{}", JSONObject.toJSON(requestVo), JSONObject.toJSON(responseVo));
                if(responseVo.isSuccess()){// 添加到总附件列表里
                    List<AttachmentVo> subAttachmentList = JSONArray.parseArray(responseVo.getResult(), AttachmentVo.class);
                    attachmentVoList.addAll(subAttachmentList);
                }
            }
        }
        return attachmentVoList;
    }

    /**
     * 获取文件类型集合（文件夹、目录）接口
     * @param requestVo 查询VO
     * @author wulinjie
     * @return 文件类型集合（含目录）
     */
    @Override
    public List<PapersTypeVo> getPapersTypeList(PicRequestVo requestVo){
        LOGGER.info("获取文件类型集合（文件夹、目录）接口, queryVo:{}", JSON.toJSON(requestVo));
        List<PapersTypeVo> papersTypeVoList = Lists.newArrayList();
        PicResponseVo responseVo = execute(URL_PAPRES_TYPE, requestVo);
        if(responseVo.isSuccess()){
            papersTypeVoList = JSONArray.parseArray(responseVo.getResult(), PapersTypeVo.class);
        }
        return papersTypeVoList;
    }

    /**
     * 删除附件
     * @param requestVo
     */
    @Override
    public void deleteAttachment(PicRequestVo requestVo){
        LOGGER.info("删除附件, queryVo:{}", JSONObject.toJSON(requestVo));
        PicResponseVo responseVo = execute(URL_PICTURE_DELETE, requestVo);
    }

    /**
     * 调用PIC接口
     * @param url       接口地址
     * @param requestVo 查询VO
     * @author wulinjie
     * @return 响应报文
     */
    private PicResponseVo execute(String url, PicRequestVo requestVo){
        LOGGER.info("开始调用PIC接口");
        try{
            String result = Request.Post(picImgUrl + url).bodyForm(buildQueryParams(requestVo), Charset.forName("UTF-8")).execute().returnContent().asString();
            LOGGER.info("PIC接口 URL:{},Request:{},Response:{}", picImgUrl + url, JSONObject.toJSON(requestVo), result);
            if(StringUtils.isNotEmpty(result)){
                PicResponseVo responseVo = JSONObject.parseObject(result, PicResponseVo.class);
                return responseVo;
            }
        }catch (Exception e){
            LOGGER.error("调用PIC接口异常", e);
        }
        return PicResponseVo.failure();
    }

    /**
     * 构建Form Body查询参数
     * @param queryVo
     * @return Form Body查询参数
     */
    @SneakyThrows
    private List<BasicNameValuePair> buildQueryParams(PicRequestVo queryVo){
        List<BasicNameValuePair> params = Lists.newArrayList();
        Field[] fields = queryVo.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if(!"serialVersionUID".equals(fieldName)){
                //获取方法
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Class<? extends Object> clazz = queryVo.getClass();
                Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                // 执行get方法
                Object value = getMethod.invoke(queryVo, new Object[]{});
                if(null != value){
                    if(value instanceof Collection){
                        String join = StringUtils.join(((Collection) value).toArray(), ",");
                        params.add(new BasicNameValuePair(fieldName, join));
                    }else{
                        params.add(new BasicNameValuePair(fieldName, value.toString()));
                    }
                }
            }
        }
        return params;
    }

    /**
     * 判断两个申请件是否都有附件
     * @param loanNo1
     * @param loanNo2
     * @return
     */
    @Override
    public Result<String> getCompareAttachment(String loanNo1, String loanNo2) {
        Result<String> result = new Result<String>(Result.Type.SUCCESS);
        if (StringUtils.isNotEmpty(loanNo1) && StringUtils.isNotEmpty(loanNo2)) {
            PicRequestVo picRequestVo = new PicRequestVo();
            picRequestVo.setNodeKey(picApproval);
            picRequestVo.setJobNumber(ShiroUtils.getAccount());
            picRequestVo.setOperator(ShiroUtils.getCurrentUser().getName());

            //查询第一个单子的附件列表信息
            picRequestVo.setAppNo(loanNo1);
            List<AttachmentVo> attachmentVoList1 = getAllAttachment(picRequestVo);

            //查询第一个单子的附件列表信息
            picRequestVo.setAppNo(loanNo2);
            List<AttachmentVo> attachmentVoList2 = getAllAttachment(picRequestVo);
            if (CollectionUtils.isEmpty(attachmentVoList1) && CollectionUtils.isEmpty(attachmentVoList2)) {
                result.setType(Result.Type.FAILURE);
                result.addMessage("所选申请件无影像资料！");
            }
        } else {
            result.setType(Result.Type.FAILURE);
            result.addMessage("单号错误！！");
        }
        return result;
    }
}
