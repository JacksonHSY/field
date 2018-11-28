<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="h20"></div>
<div class="easyui-tabs">
	<div title="基本信息" class="padding_20">
		<div class="easyui-panel" title="个人信息">
			<form id="quality_customerInfo_form">
				<table class="table_ui W100">
					<tr>
						<th class="${oldapplyInfo.applyInfoVO.name} requiredFontWeight">客户姓名:</th>
						<td>${applyInfo.applyInfoVO.name}</td>
						<th class="${oldIdNo} requiredFontWeight">身份证号码:</th>
						<td>${oldIdNo}</td>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.gender} requiredFontWeight">性别:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.gender}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.age} requiredFontWeight">年龄:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.age}</td>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.maritalStatus} requiredFontWeight">婚姻状况:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.maritalStatus}</td>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.childrenNum}">子女数:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.childrenNum}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.qualification} requiredFontWeight">最高学历:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.qualification}</td>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.graduationDate}">毕业时间:</th>
						<td>${graduationDate}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.issuerStateId} requiredFontWeight">户籍所在省:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.issuerState}</td>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.issuerCityId}">户籍所在市:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.issuerCity}</td>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.issuerZoneId}">户籍所在区:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.issuerZone}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.idIssuerAddress} requiredFontWeight">户籍地址:</th>
						<td colspan="3">${applyInfo.basicInfoVO.personInfoVO.idIssuerAddress}</td>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.issuerPostcode}">户籍邮编:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.issuerPostcode}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.homeStateId} requiredFontWeight">家庭所在省:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.homeState}</td>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.homeCityId} requiredFontWeight">家庭所在市:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.homeCity}</td>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.homeZoneId} requiredFontWeight">家庭所在区县:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.homeZone}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.homeAddress} requiredFontWeight">家庭地址:</th>
						<td colspan="3">${applyInfo.basicInfoVO.personInfoVO.homeAddress}</td>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.homePostcode}">家庭住宅邮编:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.issuerPostcode}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.houseType} requiredFontWeight">住宅类型:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.houseType}</td>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.houseRent}">租金:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.houseRent}元</td>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.familyMonthPay} requiredFontWeight">每月家庭支出:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.familyMonthPay}元</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.monthMaxRepay} requiredFontWeight">可接受的月最高还款:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.monthMaxRepay}元</td>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.cellphone} requiredFontWeight">常用手机:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.cellphone}</td>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.cellphoneSec}">备用手机:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.cellphoneSec}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.homePhone1}">宅电:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.homePhone1}</td>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.qqNum}">QQ号:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.qqNum}</td>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.weChatNum}">微信号:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.weChatNum}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.personInfoVO.email}">电子邮箱:</th>
						<td>${applyInfo.basicInfoVO.personInfoVO.email}</td>
					</tr>
				</table>
			</form>
		</div>
		<hr>
		<div class="easyui-panel" title="工作信息">
			<form id="quality_customerWork_form">
				<table class="table_ui W100">
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpName} requiredFontWeight">单位名称:</th>
						<td colspan="3">${applyInfo.basicInfoVO.workInfoVO.corpName}</td>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.cusWorkType} requiredFontWeight">客户工作类型:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.cusWorkType}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpProvinceId} requiredFontWeight">单位所在省:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpProvince}</td>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpCityId} requiredFontWeight">单位所在市:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpCity}</td>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpZoneId} requiredFontWeight">单位所在区/县:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpZone}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpAddress} requiredFontWeight">单位地址:</th>
						<td colspan="3">${applyInfo.basicInfoVO.workInfoVO.corpAddress}</td>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpPostcode}">单位邮编:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpPostcode}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.businessNetWork} requiredFontWeight">工商网信息:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.businessNetWork}</td>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpStructure} requiredFontWeight">单位性质:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpStructure}</td>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpType} requiredFontWeight">单位行业类别:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpType}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpDepapment} requiredFontWeight">任职部门:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpDepapment}</td>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpPost} requiredFontWeight">职务:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpPost}</td>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.occupation} requiredFontWeight">职业:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.occupation}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpPhone} requiredFontWeight">单电:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpPhone}</td>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpPhoneSec}">单电2:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpPhoneSec}</td>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpStandFrom} requiredFontWeight">入职时间:</th>
						<td>${corpStandFrom}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.corpPayWay} requiredFontWeight">发薪方式:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.corpPayWay}</td>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.monthSalary} requiredFontWeight">单位月收入:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.monthSalary}元</td>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.otherIncome}">其他月收入:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.otherIncome}元</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.basicInfoVO.workInfoVO.totalMonthSalary} requiredFontWeight">月总收入:</th>
						<td>${applyInfo.basicInfoVO.workInfoVO.totalMonthSalary}元</td>
					</tr>
				</table>
			</form>
		</div>
		<hr>
		<c:if test="${applyInfo.basicInfoVO.workInfoVO.cusWorkType == '私营业主'}">
			<c:if test="${not empty applyInfo.basicInfoVO.privateOwnerInfoVO.priEnterpriseType}">
				<div class="easyui-panel" title="私营业主信息">
					<table class="table_ui W100">
						<tr>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.setupDate} requiredFontWeight">成立时间:</th>
							<td>${setupDate}</td>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.sharesScale} requiredFontWeight">占股比例:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.sharesScale}%</td>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.registerFunds} requiredFontWeight">注册资本:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.registerFunds}元</td>
						</tr>
						<tr>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.priEnterpriseType} requiredFontWeight">私营企业类型:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.priEnterpriseType}</td>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.businessPlace} requiredFontWeight">经营场所:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.businessPlace}</td>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.monthRent}">月租金:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.monthAmt}元</td>
						</tr>
						<tr>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.employeeNum} requiredFontWeight">员工人数:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.employeeNum}人</td>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.enterpriseRate} requiredFontWeight">企业净利润率:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.enterpriseRate}%</td>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.monthAmt} requiredFontWeight">每月净利润额:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.monthAmt}元</td>
						</tr>
						<tr>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.sharesName}">股东姓名(除客户外最大股东):</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.sharesName}</td>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.sharesIdNO}">股东身份证:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.sharesIdNO}</td>
						</tr>
					</table>
				</div>
			</c:if>
		</c:if>
		<c:if test="${applyInfo.basicInfoVO.workInfoVO.cusWorkType == '自雇人士'}">
			<c:if test="${not empty applyInfo.basicInfoVO.privateOwnerInfoVO.priEnterpriseType}">
				<div class="easyui-panel" title="私营业主信息">
					<table class="table_ui W100">
						<tr>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.setupDate} requiredFontWeight">成立时间:</th>
							<td>${setupDate}</td>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.sharesScale} requiredFontWeight">占股比例:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.sharesScale}%</td>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.registerFunds} requiredFontWeight">注册资本:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.registerFunds}元</td>
						</tr>
						<tr>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.priEnterpriseType} requiredFontWeight">私营企业类型:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.priEnterpriseType}</td>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.businessPlace} requiredFontWeight">经营场所:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.businessPlace}</td>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.monthRent}">月租金:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.monthAmt}元</td>
						</tr>
						<tr>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.employeeNum} requiredFontWeight">员工人数:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.employeeNum}人</td>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.enterpriseRate} requiredFontWeight">企业净利润率:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.enterpriseRate}%</td>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.monthAmt} requiredFontWeight">每月净利润额:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.monthAmt}元</td>
						</tr>
						<tr>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.sharesName}">股东姓名(除客户外最大股东):</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.sharesName}</td>
							<th class="${oldapplyInfo.basicInfoVO.privateOwnerInfoVO.sharesIdNO}">股东身份证:</th>
							<td>${applyInfo.basicInfoVO.privateOwnerInfoVO.sharesIdNO}</td>
						</tr>
					</table>
				</div>
			</c:if>
		</c:if>
	</div>
	<div title="联系人信息" class="padding_20">
		<c:forEach var="item" items="${applyInfo.contactInfoVOList}" varStatus="status">
			<div class="easyui-panel" title="第${status.index+1}联系人信息">
				<table class="table_ui W100">
					<tr>
						<th class="${oldapplyInfo.contactInfoVOList[status.index].contactName} requiredFontWeight">姓名:</th>
						<td>${item.contactName}</td>
						<th class="${oldapplyInfo.contactInfoVOList[status.index].contactRelation} requiredFontWeight">与申请人关系:</th>
						<td>${item.contactRelation}</td>
						<th class="${oldapplyInfo.contactInfoVOList[status.index].ifKnowLoan} requiredFontWeight">是否知晓贷款:</th>
						<td>${item.ifKnowLoan}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.contactInfoVOList[status.index].contactEmpName}">单位名称:</th>
						<td>${item.contactEmpName}</td>
						<th class="${oldapplyInfo.contactInfoVOList[status.index].contactCorpPost}">职务:</th>
						<td>${item.contactCorpPost}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.contactInfoVOList[status.index].contactCellPhone} requiredFontWeight">手机号:</th>
						<td>${item.contactCellPhone}</td>
						<th class="${oldapplyInfo.contactInfoVOList[status.index].contactCellPhone_1}">备用手机:</th>
						<td>${item.contactCellPhone_1}</td>
					</tr>
					<tr>
						<th class="${oldapplyInfo.contactInfoVOList[status.index].contactCorpPhone}">单位电话号码:</th>
						<td>${item.contactCorpPhone}></td>
						<th class="${oldapplyInfo.contactInfoVOList[status.index].contactCorpPhone_1}">备用电话:</th>
						<td>${item.contactCorpPhone_1}</td>
					</tr>
				</table>
			</div>
			<hr>
		</c:forEach>
		<hr>
	</div>
	<div title="资产信息" class="padding_20">
		<div class="easyui-panel" title="保单信息" <c:if test="${applyInfo.assetsInfoVO.policyInfoVO.ifEmpty==1}"> data-options="collapsible:true" </c:if> <c:if test="${applyInfo.assetsInfoVO.policyInfoVO.ifEmpty==0}"> data-options="collapsible:true,collapsed:true" </c:if>>
			<table class="table_ui W100">
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.policyInfoVO.insuranceAmt} requiredFontWeight">保险金额:</th>
					<td>${applyInfo.assetsInfoVO.policyInfoVO.insuranceAmt}元</td>
					<th class="${oldapplyInfo.assetsInfoVO.policyInfoVO.insuranceTerm} requiredFontWeight">保险年限:</th>
					<td>${applyInfo.assetsInfoVO.policyInfoVO.insuranceTerm == 999 ? "终身": applyInfo.assetsInfoVO.policyInfoVO.insuranceTerm.toString().concat(' 年')}</td>
					<th class="${oldapplyInfo.assetsInfoVO.policyInfoVO.paidTerm} requiredFontWeight">已缴年限:</th>
					<td>${applyInfo.assetsInfoVO.policyInfoVO.paidTerm}年</td>
				</tr>
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.policyInfoVO.lastPaymentDate} requiredFontWeight">最近一次缴纳时间:</th>
					<td>${lastPaymentDate}</td>
					<th class="${oldapplyInfo.assetsInfoVO.policyInfoVO.paymentMethod} requiredFontWeight">缴费方式:</th>
					<td>${applyInfo.assetsInfoVO.policyInfoVO.paymentMethod}</td>
					<th class="${oldapplyInfo.assetsInfoVO.policyInfoVO.policyRelation} requiredFontWeight">与被保险人关系:</th>
					<td>${applyInfo.assetsInfoVO.policyInfoVO.policyRelation}</td>
				</tr>
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.policyInfoVO.yearPaymentAmt} requiredFontWeight">年缴金额:</th>
					<td>${applyInfo.assetsInfoVO.policyInfoVO.yearPaymentAmt}元</td>
					<th class="${oldapplyInfo.assetsInfoVO.policyInfoVO.policyCheck} requiredFontWeight">保单真伪核实方式:</th>
					<td>${applyInfo.assetsInfoVO.policyInfoVO.policyCheck}</td>
				</tr>
			</table>
		</div>
		<hr>
		<div class="easyui-panel" title="车辆信息" <c:if test="${applyInfo.assetsInfoVO.carInfoVO.ifEmpty==1}"> data-options="collapsible:true" </c:if> <c:if test="${applyInfo.assetsInfoVO.carInfoVO.ifEmpty==0}"> data-options="collapsible:true,collapsed:true" </c:if>>
			<table class="table_ui W100">
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.carInfoVO.carType} requiredFontWeight">车辆类型:</th>
					<td>${applyInfo.assetsInfoVO.carInfoVO.carType}</td>
					<th class="${oldapplyInfo.assetsInfoVO.carInfoVO.localPlate} requiredFontWeight">本地车牌:</th>
					<td>${applyInfo.assetsInfoVO.carInfoVO.localPlate}</td>
					<th class="${oldapplyInfo.assetsInfoVO.carInfoVO.carBuyDate}">购买时间:</th>
					<td>${carBuyDate}</td>
				</tr>
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.carInfoVO.carLoan} requiredFontWeight">是否有车贷:</th>
					<td>${applyInfo.assetsInfoVO.carInfoVO.carLoan}</td>
					<th class="${oldapplyInfo.assetsInfoVO.carInfoVO.monthPaymentAmt}">月供:</th>
					<td>${applyInfo.assetsInfoVO.carInfoVO.monthPaymentAmt}元</td>
					<th class="${oldapplyInfo.assetsInfoVO.carInfoVO.nakedCarPrice} requiredFontWeight">裸车价:</th>
					<td>${applyInfo.assetsInfoVO.carInfoVO.nakedCarPrice}元</td>
				</tr>
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.carInfoVO.carBuyPrice} requiredFontWeight">购买价:</th>
					<td>${applyInfo.assetsInfoVO.carInfoVO.carBuyPrice}元</td>
					<th class="${oldapplyInfo.assetsInfoVO.carInfoVO.carLoanTerm}">贷款剩余期数:</th>
					<td>${applyInfo.assetsInfoVO.carInfoVO.carLoanTerm}</td>
					<th class="${oldapplyInfo.assetsInfoVO.carInfoVO.plateNum} requiredFontWeight">车牌号:</th>
					<td>${applyInfo.assetsInfoVO.carInfoVO.plateNum}</td>
				</tr>
			</table>
		</div>
		<hr>
		<div class="easyui-panel" title="公积金信息" <c:if test="${applyInfo.assetsInfoVO.providentInfoVO.ifEmpty==1}"> data-options="collapsible:true" </c:if> <c:if test="${applyInfo.assetsInfoVO.providentInfoVO.ifEmpty==0}"> data-options="collapsible:true,collapsed:true" </c:if>>
			<table class="table_ui W100">
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.providentInfoVO.openAccountDate} requiredFontWeight">开户时间:</th>
					<td>${openAccountDate}</td>
					<th class="${oldapplyInfo.assetsInfoVO.providentInfoVO.depositRate} requiredFontWeight">续存比例:</th>
					<td>${applyInfo.assetsInfoVO.providentInfoVO.depositRate}%</td>
					<th class="${oldapplyInfo.assetsInfoVO.providentInfoVO.monthDepositAmt} requiredFontWeight">月缴存额:</th>
					<td>${applyInfo.assetsInfoVO.providentInfoVO.monthDepositAmt}元</td>
				</tr>
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.providentInfoVO.depositBase} requiredFontWeight">缴存基数:</th>
					<td>${applyInfo.assetsInfoVO.providentInfoVO.depositBase}元</td>
					<th class="${oldapplyInfo.assetsInfoVO.providentInfoVO.providentInfo} requiredFontWeight">公积金材料:</th>
					<td>${applyInfo.assetsInfoVO.providentInfoVO.providentInfo}</td>
					<th class="${oldapplyInfo.assetsInfoVO.providentInfoVO.paymentUnit} requiredFontWeight">缴纳单位同申请单位:</th>
					<td>${applyInfo.assetsInfoVO.providentInfoVO.paymentUnit}</td>
				</tr>
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.providentInfoVO.paymentMonthNum} requiredFontWeight">申请单位已缴月数:</th>
					<td>${applyInfo.assetsInfoVO.providentInfoVO.paymentMonthNum}月</td>
				</tr>
			</table>
		</div>
		<hr>
		<div class="easyui-panel" title="卡友贷信息" <c:if test="${applyInfo.assetsInfoVO.cardLoanInfoVO.ifEmpty==1}"> data-options="collapsible:true" </c:if> <c:if test="${applyInfo.assetsInfoVO.cardLoanInfoVO.ifEmpty==0}"> data-options="collapsible:true,collapsed:true" </c:if>>
			<table class="table_ui W100">
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.cardLoanInfoVO.issuerDate} requiredFontWeight">发卡时间:</th>
					<td>${issuerDate}</td>
					<th class="${oldapplyInfo.assetsInfoVO.cardLoanInfoVO.creditLimit} requiredFontWeight">额度:</th>
					<td>${applyInfo.assetsInfoVO.cardLoanInfoVO.creditLimit}元</td>
				</tr>
				<tr>
					<th class="requiredFontWeight">近4个月账单金额依次为:</th>
					<td colspan="2">${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt1}元${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt2}元${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt3}元 ${applyInfo.assetsInfoVO.cardLoanInfoVO.billAmt4}元</td>
				</tr>
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.cardLoanInfoVO.payMonthAmt}">月均:</th>
					<td colspan="2">${applyInfo.assetsInfoVO.cardLoanInfoVO.payMonthAmt}元</td>
				</tr>
			</table>
		</div>
		<hr>
		<div class="easyui-panel" title="房产信息" <c:if test="${applyInfo.assetsInfoVO.estateInfoVO.ifEmpty==1}"> data-options="collapsible:true" </c:if> <c:if test="${applyInfo.assetsInfoVO.estateInfoVO.ifEmpty==0}"> data-options="collapsible:true,collapsed:true" </c:if>>
			<table class="table_ui W100">
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.estateType} requiredFontWeight">房产类型:</th>
					<td>${applyInfo.assetsInfoVO.estateInfoVO.estateType}</td>
					<th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.ifMe} requiredFontWeight">单据户名为本人:</th>
					<td>${applyInfo.assetsInfoVO.estateInfoVO.ifMe}</td>
					<th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.builtupArea} requiredFontWeight">建筑面积:</th>
					<td>${applyInfo.assetsInfoVO.estateInfoVO.builtupArea}平方米</td>
				</tr>
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.estateStateId} requiredFontWeight">房产所在省:</th>
					<td>${applyInfo.assetsInfoVO.estateInfoVO.estateStateId}</td>
					<th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.estateCityId} requiredFontWeight">房产所在市:</th>
					<td>${applyInfo.assetsInfoVO.estateInfoVO.estateCityId}</td>
					<th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.estateZoneId} requiredFontWeight">房产所在区:</th>
					<td>${applyInfo.assetsInfoVO.estateInfoVO.estateZoneId}</td>
				</tr>
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.estateAddress} requiredFontWeight">房产地址:</th>
					<td colspan="2">${applyInfo.assetsInfoVO.estateInfoVO.estateAddress}</td>
					<th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.estateLoan} requiredFontWeight">房贷情况:</th>
					<td>${applyInfo.assetsInfoVO.estateInfoVO.estateLoan}</td>
				</tr>
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.estateBuyDate} requiredFontWeight">购买时间:</th>
					<td>${estateBuyDate}</td>
					<th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.estateAmt}">购买总价值:</th>
					<td>${applyInfo.assetsInfoVO.estateInfoVO.estateAmt}元</td>
					<th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.referenceAmt} requiredFontWeight">市值参考价:</th>
					<td>${applyInfo.assetsInfoVO.estateInfoVO.referenceAmt}元</td>
				</tr>
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.estateLoanAmt} requiredFontWeight">房贷金额:</th>
					<td>${applyInfo.assetsInfoVO.estateInfoVO.estateLoanAmt}元</td>
					<th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.monthPaymentAmt} requiredFontWeight">月供:</th>
					<td>${applyInfo.assetsInfoVO.estateInfoVO.monthPaymentAmt}元</td>
					<th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.hasRepaymentNum}">已还期数:</th>
					<td>${applyInfo.assetsInfoVO.estateInfoVO.hasRepaymentNum}</td>
				</tr>
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.houseOwnership} requiredFontWeight">房产所有权:</th>
					<td colspan="2">${applyInfo.assetsInfoVO.estateInfoVO.houseOwnership}</td>
					<th class="${oldapplyInfo.assetsInfoVO.estateInfoVO.equityRate} requiredFontWeight">产权比例:</th>
					<td>${applyInfo.assetsInfoVO.estateInfoVO.equityRate}%</td>
				</tr>
			</table>
		</div>
		<hr>
		<div class="easyui-panel" title="随薪贷信息" <c:if test="${applyInfo.assetsInfoVO.salaryLoanInfoVO.ifEmpty==1}"> data-options="collapsible:true" </c:if> <c:if test="${applyInfo.assetsInfoVO.salaryLoanInfoVO.ifEmpty==0}"> data-options="collapsible:true,collapsed:true" </c:if>>
			<table class="table_ui W100">
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.salaryLoanInfoVO.conditionType} requiredFontWeight">条件类型:</th>
					<td>${applyInfo.assetsInfoVO.salaryLoanInfoVO.conditionType}</td>
				</tr>
			</table>
		</div>
		<hr>
		<div class="easyui-panel" title="网购达人贷B信息" <c:if test="${applyInfo.assetsInfoVO.masterLoanInfoVO.ifEmpty==1}"> data-options="collapsible:true" </c:if> <c:if test="${applyInfo.assetsInfoVO.masterLoanInfoVO.ifEmpty==0}"> data-options="collapsible:true,collapsed:true" </c:if>>
			<table class="table_ui W100">
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.jiDongUserLevel} requiredFontWeight">京东用户等级:</th>
					<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.jiDongUserLevel}</td>
					<th class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.whiteCreditValue} requiredFontWeight">小白信用分:</th>
					<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoVO.whiteCreditValue}" maxFractionDigits="1" /></td>
					<th class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.pastYearShoppingAmount} requiredFontWeight">近一年实际消费金额:</th>
					<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.pastYearShoppingAmount}元</td>
				</tr>
			</table>
		</div>
		<hr>
		<div class="easyui-panel" title="网购达人贷A信息" <c:if test="${applyInfo.assetsInfoVO.masterLoanInfoVO.ifEmpty==1}"> data-options="collapsible:true" </c:if> <c:if test="${applyInfo.assetsInfoVO.masterLoanInfoVO.ifEmpty==0}"> data-options="collapsible:true,collapsed:true" </c:if>>
			<table class="table_ui W100">
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.acctRegisterDate} requiredFontWeight">账户注册时间:</th>
					<td>${acctRegisterDate}</td>
					<th class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.buyerCreditLevel} requiredFontWeight">买家信用等级:</th>
					<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.buyerCreditLevel}</td>
					<th class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.buyerCreditType} requiredFontWeight">买家信用类型:</th>
					<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.buyerCreditType}</td>
				</tr>
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.goodRate} requiredFontWeight">好评率:</th>
					<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.goodRate}%</td>
					<th class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.lastYearPayAmt} requiredFontWeight">上一年度支出额:</th>
					<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.lastYearPayAmt}元</td>
					<th class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.sesameCreditValue} requiredFontWeight">芝麻信用分:</th>
					<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.sesameCreditValue}</td>
				</tr>
				<tr>
					<th class="requiredFontWeight">近三个月支出总额:</th>
					<td colspan="2">${applyInfo.assetsInfoVO.masterLoanInfoVO.payAmt1}元${applyInfo.assetsInfoVO.masterLoanInfoVO.payAmt2}元${applyInfo.assetsInfoVO.masterLoanInfoVO.payAmt3}元</td>
					<th class="${oldapplyInfo.assetsInfoVO.masterLoanInfoVO.payMonthAmt}">月均:</th>
					<td>${applyInfo.assetsInfoVO.masterLoanInfoVO.payMonthAmt}元</td>
					<th>淘气值:</th>
					<td><fmt:formatNumber type="number" value="${applyInfo.assetsInfoVO.masterLoanInfoAVO.naughtyValue}" maxFractionDigits="0" /></td>
				</tr>
			</table>
		</div>
		<hr>
		<div class="easyui-panel" title="淘宝商户信贷" <c:if test="${applyInfo.assetsInfoVO.merchantLoanInfoVO.ifEmpty==1}"> data-options="collapsible:true" </c:if> <c:if test="${applyInfo.assetsInfoVO.merchantLoanInfoVO.ifEmpty==0}"> data-options="collapsible:true,collapsed:true" </c:if>>
			<table class="table_ui W100">
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.merchantLoanInfoVO.setupShopDate} requiredFontWeight">开店时间:</th>
					<td>${setupShopDate}</td>
					<th class="${oldapplyInfo.assetsInfoVO.merchantLoanInfoVO.sellerCreditLevel} requiredFontWeight">卖家信息等级:</th>
					<td>${applyInfo.assetsInfoVO.merchantLoanInfoVO.sellerCreditLevel}</td>
					<th class="${oldapplyInfo.assetsInfoVO.merchantLoanInfoVO.sellerCreditType} requiredFontWeight">卖家信息类型:</th>
					<td>${applyInfo.assetsInfoVO.merchantLoanInfoVO.sellerCreditType}</td>
				</tr>
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.merchantLoanInfoVO.regardedNum} requiredFontWeight">近半年好评数:</th>
					<td colspan="3">${applyInfo.assetsInfoVO.merchantLoanInfoVO.regardedNum}</td>
				</tr>
				<tr>
					<th class="requiredFontWeight">近6个月账单金额依次为:</th>
					<td colspan="3">${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt1}元${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt2}元${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt3}元 ${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt4}元 ${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt5}元 ${applyInfo.assetsInfoVO.merchantLoanInfoVO.billAmt6}元</td>
				</tr>
				<tr>
					<th class="${oldapplyInfo.assetsInfoVO.merchantLoanInfoVO.payMonthAmt}">月均:</th>
					<td colspan="3">${applyInfo.assetsInfoVO.merchantLoanInfoVO.payMonthAmt}元</td>
				</tr>
			</table>
		</div>
	</div>
</div>                              r
<script type="text/javascript" src="${ctx}/resources/js/quality/qualityControlDesk/qualityReceive/customerInfo.js"></script>