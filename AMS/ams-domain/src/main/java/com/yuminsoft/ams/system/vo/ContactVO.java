package com.yuminsoft.ams.system.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 联系人VO对象
 * @author fuhongxing
 */
@Data
@ToString
public class ContactVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 必须实现 序列化, 自动生成序列化值
	 */
	private Long id;
	private String loanBaseId; //借款主键
	private String loanNo;	   //借款编号
	private String contactName;         // 姓名	
	private String contactRelation;     //与申请人关系
	private String contacGender;         //性别
	private String phone;         // 手机
	private String ifKnowLoan;         // 是否知晓贷款
	private String contactEmpName;         //公司名称
	private String contactCorpPost;         // 职务
	private Integer sequenceNum;         // 排序号
	private String contactIdNo;//身份证号码
	private String ifForeignPenple;//是否外籍人士
	private String carrier;  //运营商
	private String phoneCity;  //归属地
//	private int phoneCount;   //所填电话数量
	private String remark; //备注
	private boolean matchBrach = false;//匹配营业部(电话号码与营业部是否为同一城市)
	
}
