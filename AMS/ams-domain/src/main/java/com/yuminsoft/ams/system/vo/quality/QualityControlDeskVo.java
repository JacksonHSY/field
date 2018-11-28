package com.yuminsoft.ams.system.vo.quality;

import com.yuminsoft.ams.system.annotation.XlsHeader;
import com.yuminsoft.ams.system.vo.AbstractEntityVo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Desc: 质检工作台vo
 * @Author: phb
 * @Modified wulinjie
 * @Date: 2017/5/12 15:40
 */
@Data
public class QualityControlDeskVo extends AbstractEntityVo {
    private static final long serialVersionUID = 6001752806210375897L;

    /*以下字段来自ams_quality_check_info表*/
    @XlsHeader("申请件编号")
    private String loanNo;                    // 申请件编号
    private Integer approvalStatus;            // 申请件状态 {@link com.yuminsoft.ams.system.common.QualityEnum.ApprovalStatus}

    @XlsHeader("审批结果")
    private String approvalStatusText;


    @XlsHeader("分派日期")
    private Date assignDate;                // 质检派单日期
    private String checkStatus;            // 质检状态
    private String checkUser;                // 质检员
    @XlsHeader("质检人员")
    private String checkUserName;            // 质检员姓名

    @XlsHeader("完成日期")
    private Date endDate;                    // 质检完成时间
    private String source;                    // 申请来源

    @XlsHeader("申请来源")
    private String sourceText;                // 申请来源(文字)
    private String isClosed;                // 是否关闭
    private String isRegular;                // 抽单方式（0-自动抽单，1-手工导入）

    @XlsHeader("客户姓名")
    private String customerName;            // 客户姓名

    @XlsHeader("身份证号")
    private String idNo;                    // 身份证号
    private String owningBranceId;            // 进件营业部ID

    @XlsHeader("进件营业部")
    private String owningBrance;            // 进件营业部名称

    @XlsHeader("申请日期")
    private Date applyDate;                    // 申请日期

    @XlsHeader("贷款类型")
    private String productTypeName;            // 产品类型
    private String approveState;            // 工作流节点

    private String checkPerson;                // 初审人员工号
    @XlsHeader("初审姓名")
    private String checkPersonName;            // 初审人员姓名

    private String finalPerson;                // 终审人员工号
    @XlsHeader("终审姓名")
    private String finalPersonName;            // 终审人员姓名

    @XlsHeader("审批日期")
    private Date approveDate;                // 审批日期
    private String assignType;                // 派单方式（0-自动派单，1-手动派单）
    private String refuseLevelOne;            // 一级拒绝原因Code
    private String refuseLevelOneName;        // 二级拒绝原因名称
    private String refuseLevelTwo;            // 二级拒绝原因Code
    private String refuseLevelTwoName;        // 二级拒绝原因名称

    @XlsHeader("拒绝原因")
    private String refuseReasonEmbed;        // 拒绝原因（拼接）

    private String levelName;                // 拒绝原因

    /* 查询条件字段 */
    private Date assignDateStart;            // 分派日期-开始时间
    private Date assignDateEnd;                // 分派日期-结束时间
    private Date endDateStart;                // 完成日期-开始时间
    private Date endDateEnd;                // 完成日期-结束时间
    private Date lastModifiedDateStart;        // 最后修改时间-开始时间
    private Date lastModifiedDateEnd;        // 最后修改时间-结束时间

    @XlsHeader("客户类型")
    private String customerType;            // 客户类型
    private String approvePerson;            // 审核人员
    //TODO approval_leader 字段命名不规范
    private String approvalLeader;            // 组长/主管

    /* 以下字段来自ams_quality_check_result表 */
    private String checkError;                // 质检差错环节
    private String checkResult;                // 质检差错结论
    @XlsHeader("差错类型")
    private String checkResultText;            // 质检差错结论(文本)
    private String checkView;                // 质检意见
    private String errorCode;                // 差错代码
    private Long qualityCheckId;            // 质检申请件ID  重复的ID
    private Long feedbackCode;                // 质检反馈流程节点(0-未进入反馈，1-进入反馈流程，2-质检主管核对完成，3-流程结束)
    private String status;                    // 质检结论-状态
    private String recheckPerson;            // 复核处理人
    /*质检来源名称ams_quality_source_info*/
    private String qualitySource;            //申请来源

    private Boolean ifTemporal = false;        // 是否为暂存件
    private Boolean ifRecheck = false;        // 是否为质检复核件

    private Boolean ifEditor = false;               //是否可编辑

    /**
     * 获取申请件状态(0-通过件,1-拒绝件,2-其他)
     *
     * @return
     */
    public String getApprovalStatusText() {
        // TODO VO放在domain模块，无法引用枚举类，应该把vo的package迁移到service层
        if (null != this.approvalStatus) {
            if (0 == this.approvalStatus) {
                this.approvalStatusText = "通过";
            } else if (1 == this.approvalStatus) {
                this.approvalStatusText = "拒绝";
            }
        } else {
            this.approvalStatusText = "其他";
        }

        return this.approvalStatusText;
    }

    /**
     * 获取申请件拒绝原因
     *
     * @return
     * @author wulinjie
     * @Desc 例如 客户要求取消申请-不合法
     */
    public String getRefuseReasonEmbed() {
        StringBuffer sb = new StringBuffer("");
        if (StringUtils.isNotEmpty(this.refuseLevelOneName)) {
            sb.append(this.refuseLevelOneName);
        }

        if (StringUtils.isNotEmpty(this.refuseLevelTwoName)) {
            sb.append("-").append(this.refuseLevelTwoName);
        }

        this.refuseReasonEmbed = sb.toString();

        return refuseReasonEmbed;
    }

    @Override
    public String toString() {
        return "QualityControlDeskVo{" +
                "loanNo='" + loanNo + '\'' +
                ", approvalStatus=" + approvalStatus +
                ", approvalStatusText='" + approvalStatusText + '\'' +
                ", assignDate=" + assignDate +
                ", checkStatus='" + checkStatus + '\'' +
                ", checkUser='" + checkUser + '\'' +
                ", checkUserName='" + checkUserName + '\'' +
                ", endDate=" + endDate +
                ", source='" + source + '\'' +
                ", sourceText='" + sourceText + '\'' +
                ", isClosed='" + isClosed + '\'' +
                ", isRegular='" + isRegular + '\'' +
                ", customerName='" + customerName + '\'' +
                ", idNo='" + idNo + '\'' +
                ", owningBranceId='" + owningBranceId + '\'' +
                ", owningBrance='" + owningBrance + '\'' +
                ", applyDate=" + applyDate +
                ", productTypeName='" + productTypeName + '\'' +
                ", approveState='" + approveState + '\'' +
                ", checkPerson='" + checkPerson + '\'' +
                ", checkPersonName='" + checkPersonName + '\'' +
                ", finalPerson='" + finalPerson + '\'' +
                ", finalPersonName='" + finalPersonName + '\'' +
                ", approveDate=" + approveDate +
                ", assignType='" + assignType + '\'' +
                ", refuseLevelOne='" + refuseLevelOne + '\'' +
                ", refuseLevelOneName='" + refuseLevelOneName + '\'' +
                ", refuseLevelTwo='" + refuseLevelTwo + '\'' +
                ", refuseLevelTwoName='" + refuseLevelTwoName + '\'' +
                ", refuseReasonEmbed='" + refuseReasonEmbed + '\'' +
                ", levelName='" + levelName + '\'' +
                ", assignDateStart=" + assignDateStart +
                ", assignDateEnd=" + assignDateEnd +
                ", endDateStart=" + endDateStart +
                ", endDateEnd=" + endDateEnd +
                ", lastModifiedDateStart=" + lastModifiedDateStart +
                ", lastModifiedDateEnd=" + lastModifiedDateEnd +
                ", customerType='" + customerType + '\'' +
                ", approvePerson='" + approvePerson + '\'' +
                ", approvalLeader='" + approvalLeader + '\'' +
                ", checkError='" + checkError + '\'' +
                ", checkResult='" + checkResult + '\'' +
                ", checkResultText='" + checkResultText + '\'' +
                ", checkView='" + checkView + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", qualityCheckId=" + qualityCheckId +
                ", feedbackCode=" + feedbackCode +
                ", status='" + status + '\'' +
                ", recheckPerson='" + recheckPerson + '\'' +
                ", qualitySource='" + qualitySource + '\'' +
                ", ifTemporal=" + ifTemporal +
                ", ifRecheck=" + ifRecheck +
                '}';
    }

}
