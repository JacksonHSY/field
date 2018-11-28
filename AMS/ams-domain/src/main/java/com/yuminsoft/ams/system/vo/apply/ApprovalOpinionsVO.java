package com.yuminsoft.ams.system.vo.apply;

import com.ymkj.ams.api.vo.request.audit.first.ReqAssetsInfoVO;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 审批意见对外接口VO(规则引擎调用而且必须有默认值)
 *
 * @author dmz
 * @date 2017年6月23日
 */
@Data
@ToString
public class ApprovalOpinionsVO implements Serializable {
    private static final long serialVersionUID = -9096725085322993544L;
    // Fields
    // private Integer incomeCertificate;// 收入证明金额
    private List<Integer> personalWater = new ArrayList<Integer>();// 个人流水
    private List<Integer> personMonthAverage = new ArrayList<Integer>();// 个人流水月均
    private List<Integer> publicWater = new ArrayList<Integer>();// 对公流水
    private List<Integer> publicMonthAverage = new ArrayList<Integer>();// 对公流水月均
    private Integer monthAverage = new Integer(-1); // 月平均金额
    // private String creditRecord = "N";// 是否有信用记录
    private Integer threeMonthsCount = new Integer(-1);// 近三个月查询
    private Integer oneMonthsCount = new Integer(-1);// 近一个月查询
    private Integer creditLoanDebt = new Integer(-1);// 外部信用负债总额
    private Integer outDebtTotal = new Integer(-1);// 外部负债总额
    private String approvalProductCd = "";// 审批产品
    private Integer approvalCheckIncome = new Integer(-1);// 核实收入
    private Integer approvalLimit = new Integer(-1);// 审批额度
    private Integer approvalTerm = new Integer(-1);// 审批期限
    // 前前进件
    private String estateAuthenticated = "N";//	房产信息已验证(Y/N)
    private String carAuthenticated = "N";// 车辆信息已验证(Y/N)
    private String policyAuthenticated = "N";//保单信息已验证(Y/N)
    private String educationAuthenticated = "N";//学历信息已验证(Y/N)
    private String cardAuthenticated = "N";// 信用卡信息已验证(Y/N)
    private String masterAuthenticated = "N";//淘宝账户信息已验证(Y/N)



    // 4.0.03 初审系统初判添加页面资产信息参数
    private String  assetsInfo; // 用于接受页面复杂资产信息对象后续转换成 saveAssetsVO;
    private ReqAssetsInfoVO saveAssetsVO;
    private String selectAssetsType;// 页面勾选审批资产信息
}
