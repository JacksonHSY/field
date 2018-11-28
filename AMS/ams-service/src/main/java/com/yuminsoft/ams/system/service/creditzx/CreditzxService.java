package com.yuminsoft.ams.system.service.creditzx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ymkj.ams.api.vo.request.apply.ReqInformationVO;
import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.creditzx.huazheng.HzRequest;
import com.yuminsoft.ams.system.vo.creditzx.huazheng.HzResponse;
import com.yuminsoft.ams.system.vo.creditzx.huazheng.IdCardCheckInfoVo;
import com.yuminsoft.ams.system.vo.creditzx.huazheng.MobileOnlineInfoVo;

import java.util.Map;

public interface CreditzxService {

    /**
     * 信用不良查询
     *
     * @param reqInformationVO
     * @return
     */
    public Result<JSONObject> getCreditReportInfoService(ReqInformationVO reqInformationVO);

    /**
     * 信用卡贷款自动填充
     *
     * @param map
     * @return
     */
    public Result<JSONArray> getLoanLimitInfoService(Map<String, Object> map);

    /**
     * 其他贷款自动填充(信用贷款、房贷、车贷、其他)
     *
     * @param reqInformationVO
     * @return
     */
    public Result<JSONObject> getLoanInfoService(ReqInformationVO reqInformationVO);

    /**
     * 查询手机在网时长
     *
     * @param request
     */
    public HzResponse<MobileOnlineInfoVo> getMobileOnlineInfoService(HzRequest request);

    /**
     * 查询手机三要素实名认证信息
     *
     * @param request
     */
    public HzResponse<IdCardCheckInfoVo> getIdCardCheckInfo(HzRequest request);

    /**
     * 学历查询
     *
     * @param map
     * @return
     */
    public Result<JSONArray> getEducationInfoService(Map<String, Object> map);

    /**
     * 车辆信息查询
     *
     * @param map
     * @return
     */
    public Result<JSONArray> getCarInfoService(Map<String, Object> map);

    /**
     * 获取上海资信报告
     * @param loanNo 借款编号
     * @param nfcsId 资信报告ID
     * @author dmz
     * @return 上海资信报告
     */
    public Result<JSONObject> getNfcsCreditById(String loanNo, String nfcsId);


    /**
     * 算话反欺诈评分查询
     *
     * @param loanNo
     * @return
     */
    public Result<JSONObject> getSuanHuaAntifraud(String loanNo);

    /**
     * 算话征信报告
     *
     * @param applyBasiceInfo
     * @return
     */
    Result<JSONObject> getSuanHuaCredit(ReqInformationVO applyBasiceInfo);

}
