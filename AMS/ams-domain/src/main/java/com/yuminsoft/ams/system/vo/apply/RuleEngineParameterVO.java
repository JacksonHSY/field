package com.yuminsoft.ams.system.vo.apply;

import com.yuminsoft.ams.system.vo.AbstractEntityVo;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by YM10106 on 2018/6/11.
 */
@ToString
@Data
public class RuleEngineParameterVO extends AbstractEntityVo{

    /**借款编号**/
    private String loanNo;

    /**建议核实收入**/
    private Integer adviceVerifyIncome;

    /**建议审批额度 -建议到手金额 **/
    private Integer adviceAuditLines;

    /**内部负债率**/
    private BigDecimal internalDebtRatio;

    /**总负债率**/
    private BigDecimal totalDebtRatio;

    /**评分卡分数2.1**/
    private Integer scorecardScore;

    /*评分卡分数1.1*/
    private Integer scorecardScoreOnePointOne;

    /**经过的CC规则集的名称**/
    private String ccRuleSet;

    /**规则引擎拒绝原因码**/
    private String rejectCode;

    /**欺诈可疑**/
    private String isAntifraud;

    /**ZDSC提示级别**/

    private String scorecardCreditLevel;

    /**评分等级**/
    private String scorecardRanking;

    /**风险评级**/
    private String riskRating;

    /**ZDSC综合信用评级**/
    private String comCreditRating;

    /**近一个月查询**/
    private Integer oneMonthsCount;

    /**近三个月查询**/
    private Integer threeMonthsCount;

    /**有信用记录(Y有,N无)**/
    private String ifCreditRecord;

    /**规则引擎返操作类型**/
    private String engineType;

    /**执行环节**/
    private String executeType;

    /***规则引擎提示信息(数组)***/
    private String engineHints;

    // Add Version 3.0.06
    /**央行征信提示信息(数组)**/
    private String creditReportHints;

    /**外部征信提示信审(数组)**/
    private String externalCreditHints;
}
