package com.yuminsoft.ams.system.vo.engine;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 规则引擎返回值
 *
 * @author dmz
 * @date 2017年9月4日
 */
@Data
@ToString
public class RuleEngineVO implements Serializable {
    private static final long serialVersionUID = -9207410525644620646L;
    private String loanNo;// 借款编号
    private Integer adviceVerifyIncome;// 建议核实收入
    private Integer adviceAuditLines;// 建议审批额度-建议到手金额
    private BigDecimal internalDebtRatio;// 内部负债率
    private BigDecimal totalDebtRatio;// 总负债率
    private Integer scorecardScore;// 评分卡分数2.1
    private Integer scorecardScoreOnePointOne;  // 评分卡分数1.1
    private String ccRuleSet;// 经过的CC规则集的名称
    private String rejectCode;// 规则引擎拒绝原因码
    private String isAntifraud;// 欺诈可疑
    private String scorecardCreditLevel;//ZDSC提示级别
    private String scorecardRanking;// 评分卡2.1评分等级
    private String riskRating;//风险评级(规则返回枚举值：1、2、3、4,对应1星2星3星4星)
    private String comCreditRating;//ZDSC综合信用评级
    private String hasCreditRecord;
    private Integer oneMonthsCount;//近一个月查询
    private Integer threeMonthsCount;//近三个月查询
    private String ifCreditRecord;//有信用记录(Y有,N无)
    // Add Version 3.0.06
    private String creditReportHints;// 央行征信提示信息(数组)
    private String externalCreditHints;// 外部征信提示信审(数组)
    //Add Version 3.0.08
    private BigDecimal handsAmount;//到时金额
    // Add Vession 4.0.02
    private BigDecimal estimatedCost;//预估评级费
}
