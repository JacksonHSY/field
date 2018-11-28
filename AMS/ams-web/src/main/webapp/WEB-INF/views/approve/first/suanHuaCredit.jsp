<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>${name}-算话征信</title>
	<jsp:include page="../../common/commonJS.jsp"></jsp:include>
	<jsp:include page="../../common/theme.jsp"></jsp:include>
	<style>
		#first_suanHuaCredit_parent  thead td{
			font-size: 15px;
		}
		#first_suanHuaCredit_parent td{
			font-size: 14px;
			text-align: center;
			height: 25px;
			line-height: 25px;
		}
		#first_suanHuaCredit_parent h3 {
			background-color: lightskyblue;
			font-size: 16px;
		}
	</style>
</head>
<body class="padding_20 ms-controller" ms-controller="page">
    <div class="padding_20">
		<br>
		<div id="first_suanHuaCredit_parent" >
				<div id = "first_suanHuaQueryTimes">
					<div class="W90 center_m center_text"><h3 style="font-size: 18px">查询记录信息</h3></div>
					<div ms-if="suanHuanTimesMessage.length == 0">
						<table class="table_list W90 center_m">
							<thead>
								<tr>
									<td class="W10">查询机构数</td>
									<td>查询次数</td>
									<td>近3个月查询机构数</td>
									<td>近6个月查询机构数</td>
									<td>近3个月查询次数</td>
									<td class="W15">近6个月查询次数</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>{{suanHuanQueryTimes.orgCount}}</td>
									<td>{{suanHuanQueryTimes.queryCount}}</td>
									<td>{{suanHuanQueryTimes.orgCountM3}}</td>
									<td>{{suanHuanQueryTimes.orgCountM6}}</td>
									<td>{{suanHuanQueryTimes.queryCountM3}}</td>
									<td>{{suanHuanQueryTimes.queryCountM6}}</td>
								</tr>
							</tbody>
						</table>
						<br>
						<div class="W90 center_m center_text"><h3 style="font-size: 18px">查询记录明细列表</h3></div>
						<table class="table_list W90 center_m">
							<thead>
								<tr>
									<td class="W10">序号</td>
									<td>查询原因</td>
									<td class="W40">查询机构</td>
									<td class="W15">查询时间</td>
								</tr>
							</thead>
							<tbody>
								<tr ms-for="($index,item) in suanHuanQueryTimes.details">
									<td>{{$index + 1}}</td>
									<td>{{getQueryReasonType(item.reason)}}</td>
									<td>{{item.org}}</td>
									<td>{{@item.datetime | strToDate}}</td>
								</tr>
							</tbody>
						</table>
						<br>
					</div>
					<div ms-if="suanHuanTimesMessage.length >0">
						<table class="table_list W90 center_m">
							<thead>
								<tr>
									<td class="center_text">{{suanHuanTimesMessage || '查无结果'}}</td>
								</tr>
							</thead>
						</table>
					</div>
				</div>
				<br>
				<dvi id = "first_suanHuanCreditReport">
					<div ms-if="suanHuanObjMessage.length == 0">
						<div class="W90 center_m center_text"><h3 style="font-size: 18px">身份信息概要</h3></div>
						<table class="table_list W90 center_m" ms-if="!isEmpty(suanHuanCreditObj.Person)">
							<thead>
								<tr>
									<td colspan="6" class="center_text">身份信息</td>
								</tr>
								<tr>
									<td class="W10">性别</td>
									<td>出生日期</td>
									<td>证件类型</td>
									<td>证件号码</td>
									<td class="W20">婚姻状态</td>
									<td class="W15">最高学历</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>{{getSexType(suanHuanCreditObj.Person.identification.gender)}}</td>
									<td>{{@suanHuanCreditObj.Person.identification.birthday|strToDate}}</td>
									<td>{{getCardIdType(suanHuanCreditObj.Person.identification.idType)}}</td>
									<td>{{suanHuanCreditObj.Person.identification.idCard}}</td>
									<td>{{getMarriageType(suanHuanCreditObj.Person.identification.marriage)}}</td>
									<td>{{getEducationType(suanHuanCreditObj.Person.identification.eduLevel)}}</td>
								</tr>
							</tbody>
							<thead>
								<tr>
									<td colspan="6" class="center_text">职业信息</td>
								</tr>
								<tr>
									<td class="W10">编号</td>
									<td colspan="2" class="center_text">工作单位</td>
									<td colspan="2" class="center_text">单位地址</td>
									<td>信息更新日期</td>
								</tr>
							</thead>
							<tbody>
								<tr ms-for="($index, item) in suanHuanCreditObj.Person.occupations">
									<td>{{ $index +1 }}</td>
									<td colspan="2">{{item.company}}</td>
									<td colspan="2">{{item.companyAddress}}</td>
									<td colspan="2">{{@item.updateTime|strToDate}}</td>
								</tr>
							</tbody>
							<thead>
								<tr><td colspan="6" class="center_text">通讯地址信息</td></tr>
								<tr><td class="W10">编号</td><td colspan="4" class="center_text W70">通讯地址</td><td>信息更新日期</td></tr>
							</thead>
							<tbody>
								<tr ms-for="($index,item) in suanHuanCreditObj.Person.contactAddresses">
									<td>{{$index + 1}}</td>
									<td colspan="4">{{item.contactAddress}}</td>
									<td>{{@item.updateTime|strToDate}}</td>
								</tr>
							</tbody>
							<thead>
								<tr><td colspan="6" class="center_text">居住地址信息</td></tr>
								<tr><td class="W10">编号</td><td colspan="2" class="center_text">居住地址信息</td><td colspan="2">居住状况</td><td>信息更新日期</td></tr>
							</thead>
							<tbody>
								<tr ms-for="($index,item) in suanHuanCreditObj.Person.estates">
									<td>{{$index + 1}}</td>
									<td colspan="2">{{item.address}}</td>
									<td colspan="2">{{getLiveType(item.condition)}}</td>
									<td>{{@item.updateTime|strToDate}}</td>
								</tr>
							</tbody>
						</table>
						<table class="table_list W90 center_m" ms-if="isEmpty(suanHuanCreditObj.Person)">
							<thead>
								<tr>
									<td class="center_text">查无结果</td>
								</tr>
							</thead>
						</table>
						<br>
						<div class="W90 center_m center_text"><h3 style="font-size: 18px">信贷信息概要(信用提示)</h3></div>
						<div ms-if="!isEmpty(suanHuanCreditObj.Credit)">
							<table class="table_list W90 center_m">
								<thead>
									<tr>
										<td class="W10">业务类型</td>
										<td class="W30">总笔数</td>
										<td class="W30">未结清笔数</td>
										<td class="W30">为他人担保笔数</td>
									</tr>
								</thead>
								<tbody>
									<tr ms-for="(key,val) in indicator">
										<td>{{val}}</td>
										<td>{{suanHuanCreditObj.Credit.indicator[key].total}}</td>
										<td>{{suanHuanCreditObj.Credit.indicator[key].unSettledTotal}}</td>
										<td>{{suanHuanCreditObj.Credit.indicator[key].guaranteeTotal}}</td>
									</tr>
								</tbody>
							</table>
							<br>
							<div class="W90 center_m center_text"><h3 style="font-size: 18px">逾期及违约概要</h3></div>
							<table class="table_list W90 center_m">
								<thead>
									<tr><td>呆账笔数</td><td>呆账总负债</td><td>核销笔数</td><td>核销总负债</td><td>代偿笔数</td><td>代偿总负债</td></tr>
								</thead>
								<tbody>
									<tr>
										<td>{{suanHuanCreditObj.Credit.overdueInfo.badDebtNum}}</td>
										<td>{{suanHuanCreditObj.Credit.overdueInfo.badDebtBalance}}</td>
										<td>{{suanHuanCreditObj.Credit.overdueInfo.offNum}}</td>
										<td>{{suanHuanCreditObj.Credit.overdueInfo.offBalance}}</td>
										<td>{{suanHuanCreditObj.Credit.overdueInfo.agentNum}}</td>
										<td>{{suanHuanCreditObj.Credit.overdueInfo.agentBalance}}</td>
									</tr>
								</tbody>
								<thead>
									<tr><td>逾期笔数</td><td>最大逾期期数</td><td>最大逾期金额</td><td></td><td></td><td></td></tr>
								</thead>
								<tbody>
									<tr>
										<td>{{suanHuanCreditObj.Credit.overdueInfo.overdueNum}}</td>
										<td>{{suanHuanCreditObj.Credit.overdueInfo.maxOverdueTimes}}</td>
										<td>{{suanHuanCreditObj.Credit.overdueInfo.maxOverdueAmount}}</td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
								</tbody>
							</table>
							<br>
							<div class="W90 center_m center_text"><h3 style="font-size: 18px">授信及负债概要(未结清)</h3></div>
							<table class="table_list W90 center_m">
								<thead>
									<tr><td class="W20">业务类型</td><td>机构数</td><td>笔数</td><td>合同金额</td><td>总负债</td></tr>
								</thead>
								<tbody>
									<tr ms-for="(key,val) in indicator">
										<td>{{val}}</td>
										<td>{{suanHuanCreditObj.Credit.debtInfo.unSettled[key].orgNum}}</td>
										<td>{{suanHuanCreditObj.Credit.debtInfo.unSettled[key].total}}</td>
										<td>{{suanHuanCreditObj.Credit.debtInfo.unSettled[key].creditLimit}}</td>
										<td>{{suanHuanCreditObj.Credit.debtInfo.unSettled[key].balance}}</td>
									</tr>
									<tr>
										<td>对外保证汇总</td>
										<td>{{suanHuanCreditObj.Credit.debtInfo.unSettled.guarantee.orgNum}}</td>
										<td>{{suanHuanCreditObj.Credit.debtInfo.unSettled.guarantee.total}}</td>
										<td>{{suanHuanCreditObj.Credit.debtInfo.unSettled.guarantee.creditLimit}}</td>
										<td>{{suanHuanCreditObj.Credit.debtInfo.unSettled.guarantee.balance}}</td>
									</tr>
								</tbody>
							</table>
							<br>
							<div class="W90 center_m center_text"><h3 style="font-size: 18px">长期未更新贷款概要</h3></div>
							<table class="table_list W90 center_m">
								<thead>
								<tr><td>呆账笔数(未结清笔数)</td><td>呆账总负债</td><td>对外担保笔数</td><td>对外担保总负债</td><td>代偿笔数</td><td>代偿总负债</td></tr>
								</thead>
								<tbody>
									<tr>
										<td>{{suanHuanCreditObj.Credit.remoteCreditInfo.unSettledNum}}</td>
										<td>{{suanHuanCreditObj.Credit.remoteCreditInfo.unSettledBalance}}</td>
										<td>{{suanHuanCreditObj.Credit.remoteCreditInfo.guaranteeNum}}</td>
										<td>{{suanHuanCreditObj.Credit.remoteCreditInfo.guaranteeBalance}}</td>
										<td>{{suanHuanCreditObj.Credit.remoteCreditInfo.agentNum}}</td>
										<td>{{suanHuanCreditObj.Credit.remoteCreditInfo.agentBalance}}</td>
									</tr>
								</tbody>
								<thead>
									<tr><td>逾期笔数</td><td>最大逾期期数</td><td>最大逾期金额</td><td></td><td></td><td></td></tr>
								</thead>
								<tbody>
									<tr>
										<td>{{suanHuanCreditObj.Credit.remoteCreditInfo.overdueNum}}</td>
										<td>{{suanHuanCreditObj.Credit.remoteCreditInfo.maxOverdueTimes}}</td>
										<td>{{suanHuanCreditObj.Credit.remoteCreditInfo.maxOverdueAmount}}</td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
								</tbody>
							</table>
							<br>
							<div class="W90 center_m center_text" ms-if="suanHuanCreditObj.Credit.creditInfos"><h3 style="font-size: 18px">信贷信息明细</h3></div>
							<table class="table_list W90 center_m" ms-for="($index, item) in suanHuanCreditObj.Credit.creditInfos">
								<thead>
									<tr>
										<td class="W20" colspan="4">业务类型</td>
										<td colspan="4">合同状态</td>
										<td colspan="4">担保方式</td>
										<td colspan="4">合同金额</td>
										<td colspan="4">还款频率</td>
										<td colspan="4">机构号</td>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td colspan="4">{{getBusinessType(item.creditDetail.creditType)}}</td>
										<td colspan="4">{{getCompactType(item.creditDetail.accountStatus)}}</td>
										<td colspan="4">{{getGuaranteeType(item.creditDetail.guaranteeWay)}}</td>
										<td colspan="4">{{item.creditDetail.creditLimit}}</td>
										<td colspan="4">{{getRepayFreqType(item.creditDetail.repayFreq)}}</td>
										<td colspan="4">{{item.creditDetail.orgCode}}</td>
									</tr>
								</tbody>
								<thead>
									<tr>
										<td colspan="4">当前负债金额</td>
										<td colspan="4">贷款期数</td>
										<td colspan="4">剩余还款期数</td>
										<td colspan="4">本期应还款</td>
										<td colspan="4">本期实还款</td>
										<td colspan="4"></td>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td colspan="4">{{item.creditDetail.balance}}</td>
										<td colspan="4">{{item.creditDetail.loanTerm}}</td>
										<td colspan="4">{{item.creditDetail.periodNumber}}</td>
										<td colspan="4">{{item.creditDetail.scheduledAmount}}</td>
										<td colspan="4">{{item.creditDetail.actualPayAmount}}</td>
										<td colspan="4"></td>
									</tr>
								</tbody>
								<thead>
									<tr>
										<td colspan="4">合同生效日期</td>
										<td colspan="4">合同到期日期</td>
										<td colspan="4">本期应还款日</td>
										<td colspan="4">最近一次还款日</td>
										<td colspan="4"></td>
										<td colspan="4"></td>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td colspan="4">{{@item.creditDetail.dateOpened|strToDate}}</td>
										<td colspan="4">{{@item.creditDetail.dateClosed|strToDate}}</td>
										<td colspan="4">{{@item.creditDetail.billingDate|strToDate}}</td>
										<td colspan="4">{{@item.creditDetail.actualPayDate|strToDate}}</td>
										<td colspan="4"></td>
										<td colspan="4"></td>
									</tr>
								</tbody>
								<thead>
									<tr>
										<td colspan="4">最高逾期期数</td>
										<td colspan="4">最高逾期金额</td>
										<td colspan="4">当前逾期期数</td>
										<td colspan="4">当前逾期金额</td>
										<td colspan="4"></td>
										<td colspan="4"></td>
									</tr>
									</thead>
									<tbody>
									<tr>
										<td colspan="4">{{item.creditDetail.maxOverdueTimes}}</td>
										<td colspan="4">{{item.creditDetail.maxOverdueAmount}}</td>
										<td colspan="4">{{item.creditDetail.nowOverdueTimes}}</td>
										<td colspan="4">{{item.creditDetail.nowOverdueAmount}}</td>
										<td colspan="4"></td>
										<td colspan="4"></td>
									</tr>
									<tr><td colspan="24">24个月还款状态</td></tr>
									<tr><td colspan="6">开始日期</td><td colspan="6">{{@item.repayStatus.start|strToDate('YYYY-MM','YYYYDD')}}</td><td colspan="6">结束日期</td><td colspan="6">{{@item.repayStatus.end|strToDate('YYYY-MM','YYYYDD')}}</td></tr>
									<tr><td>{{item.repayStatus.M1}}</td><td>{{item.repayStatus.M2}}</td><td>{{item.repayStatus.M3}}</td><td>{{item.repayStatus.M4}}</td><td>{{item.repayStatus.M5}}</td><td>{{item.repayStatus.M6}}</td>
									<td>{{item.repayStatus.M7}}</td><td>{{item.repayStatus.M8}}</td><td>{{item.repayStatus.M9}}</td><td>{{item.repayStatus.M10}}</td><td>{{item.repayStatus.M11}}</td><td>{{item.repayStatus.M12}}</td>
									<td>{{item.repayStatus.M13}}</td><td>{{item.repayStatus.M14}}</td><td>{{item.repayStatus.M15}}</td><td>{{item.repayStatus.M16}}</td><td>{{item.repayStatus.M17}}</td><td>{{item.repayStatus.M18}}</td>
									<td>{{item.repayStatus.M19}}</td><td>{{item.repayStatus.M20}}</td><td>{{item.repayStatus.M21}}</td><td>{{item.repayStatus.M22}}</td><td>{{item.repayStatus.M23}}</td><td>{{item.repayStatus.M24}}</td></tr>
									<tr><td colspan="8">逾期期数</td><td colspan="8">逾期金额</td><td colspan="8">本期应还款日</td></tr>
									<tr ms-for="($ind,ite) in item.overdues">
										<td colspan="8">{{ite.nowOverdueTimes}}</td>
										<td colspan="8">{{ite.nowOverdueAmount}}</td>
										<td colspan="8">{{@ite.billingDate|strToDate}}</td>
									</tr>
									<tr><td colspan="24" style="background-color: #fcffa9"></td></tr>
								</tbody>
							</table>
							<br>
							<div class="W90 center_m center_text"><h3 style="font-size: 18px">对外担保信息明细</h3></div>
							<table class="table_list W90 center_m">
								<thead>
									<tr>
										<td>发放机构</td><td>担保贷款合同</td><td>担保金额</td><td>担保状态</td><td>总负债</td><td>担保贷款发放日期</td><td>担保贷款到期日期</td><td>信息更新日期</td>
									</tr>
								</thead>
								<tbody>
									<tr ms-for="($index,item) in suanHuanCreditObj.Credit.guaranteeInfos">
										<td>{{item.orgCode}}</td>
										<td>{{item.creditLimit}}</td>
										<td>{{item.occurSum}}</td>
										<td>{{getGuaranteeStatusType(item.guaranteeType)}}</td>
										<td>{{item.balance}}</td>
										<td>{{@item.dateOpened|strToDate}}</td>
										<td>{{@item.dateClosedstrToDate}}</td>
										<td>{{@item.updateDate|strToDate}}</td>
									</tr>
								</tbody>
							</table>
						</div>
						<div ms-if="isEmpty(suanHuanCreditObj.Credit)">
							<table class="table_list W90 center_m">
								<thead>
								<tr>
									<td class="center_text">查无结果</td>
								</tr>
								</thead>
							</table>
						</div>
						<br>
						<div class="W90 center_m center_text"><h3 style="font-size: 18px">异常交易信息列表</h3></div>
						<table class="table_list W90 center_m" ms-if="!isEmpty(suanHuanCreditObj.SpecTrade)">
							<thead>
								<tr>
									<td>编号</td>
									<td>异常类型</td>
									<td>合同生效日期</td>
									<td>机构</td>
									<td>发生金额</td
									><td>发生日期</td>
									<td>明细</td>
									<td>信息获取时间</td>
								</tr>
							</thead>
							<tbody ms-for="(key,val) in specTrade">
								<tr ms-for="($index,item) in suanHuanCreditObj.SpecTrade[key]">
									<td>{{$index + 1}}</td>
									<td>{{val}}</td>
									<td>{{@item.dateOpened|strToDate}}</td>
									<td>{{item.orgCode}}</td>
									<td>{{item.tradeSum}}</td>
									<td>{{@item.tradeDate|strToDate}}</td>
									<td>{{item.details}}</td>
									<td>{{@item.createTime|strToDate}}</td>
								</tr>
							</tbody>
						</table>
						<table class="table_list W90 center_m" ms-if="isEmpty(suanHuanCreditObj.SpecTrade)">
							<thead>
								<tr>
									<td class="center_text">查无结果</td>
								</tr>
							</thead>
						</table>
					</div>
					<div ms-if="suanHuanObjMessage.length >0">
						<br>
						<div class="W90 center_m center_text"><h3 style="font-size: 18px">信贷信息</h3></div>
						<table class="table_list W90 center_m">
							<thead>
								<tr>
									<td class="center_text">{{suanHuanObjMessage || '查无结果'}}</td>
								</tr>
							</thead>
						</table>
					</div>
					<br>
					<br>
				</dvi>
			</div>
	</div>
  <script type="text/javascript" src="${ctx}/resources/js/approve/first/suanHuaCredit.js"></script>
  <script type="text/javascript">
	  // 定义页面VM
	  pageInit({
		  $id: 'page',
		  loanNo: '${loanNo != null ? loanNo : ''}',
	  });
  </script>
</body>
</html>