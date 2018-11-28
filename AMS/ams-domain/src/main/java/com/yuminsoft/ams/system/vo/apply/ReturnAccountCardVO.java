package com.yuminsoft.ams.system.vo.apply;

import java.math.BigDecimal;
import java.util.Date;

public class ReturnAccountCardVO {
	//	tradeDate	Date		交易日期，格式为yyyy-MM-dd，例如：2015-03-13。
	private Date tradeDate;
	//	tradeType	String		交易方式, TradeType{现金,转账,通联代扣,富友代扣,上海银联代扣,冲正补记,冲正,挂账,保证金,系统使用保证金,风险金},详见数据字典.
	private String tradeType;
	//	tradeCode	String    		交易类型
	private String tradeCode;
	//	tradeName	String		交易类型名称
	private String tradeName;
	//	beginBalance	BigDecimal		期初余额
	private BigDecimal beginBalance;
	//	income	BigDecimal		收入
	private BigDecimal income;
	//	outlay	BigDecimal		支出
	private BigDecimal outlay;
	//	endBalance	BigDecimal		期末余额
	private BigDecimal endBalance;
	//	memo	String	挂账日终自动处理	备注
	private String memo;
	//	tradeNo	String	已回盘	流水号
	private String tradeNo;
	//	List<FlowDetialsVO>	FlowDetialsVO		流水明细信息列表
//	private List<FlowDetialsVO> FlowDetialsVO;
	
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getTradeCode() {
		return tradeCode;
	}
	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public BigDecimal getBeginBalance() {
		return beginBalance;
	}
	public void setBeginBalance(BigDecimal beginBalance) {
		this.beginBalance = beginBalance;
	}
	public BigDecimal getIncome() {
		return income;
	}
	public void setIncome(BigDecimal income) {
		this.income = income;
	}
	public BigDecimal getOutlay() {
		return outlay;
	}
	public void setOutlay(BigDecimal outlay) {
		this.outlay = outlay;
	}
	public BigDecimal getEndBalance() {
		return endBalance;
	}
	public void setEndBalance(BigDecimal endBalance) {
		this.endBalance = endBalance;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	
	
}
