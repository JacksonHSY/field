<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- 标记联系人下标 -->
<c:set var ="contactsIndex" value="0"></c:set>
<div id="first_contactInfoVOList_div">
    <c:forEach var="item" items="${applyInfo.contactInfoVOList}" varStatus="status">
        <form  class="contactForm">
            <c:if test="${item.contactRelation=='00013'}">
            <div class="easyui-panel" data-option="height:50" title="配偶信息">
            </c:if>
            <c:if test="${item.contactRelation!='00013'}">
            <c:set value="${contactsIndex +1}" var="contactsIndex"></c:set>
            <div class="easyui-panel" data-option="height:50" title="第<span class='first_contactsIndex'>${contactsIndex}</span>联系人信息  &nbsp;<a class='easyui-linkbutton_ok04 l-btn l-btn-small' href='javaScript:void(0);' onclick='deleteContact(this)'>删 除</a>">
            </c:if>
                    <table class="table_ui W100" id="firstCustomerInfo_sequenceNum_${item.sequenceNum}">
                        <tr>
                            <th width="10%" class="${oldapplyInfo.contactInfoVOList[status.index].contactName} requiredFontWeight">姓名:</th>
                            <td width="12%"><input class="easyui-textbox input peopleName" value="${item.contactName}" name="contactName"></td>
                            <td width="18%"></td>
                            <th width="8%" class="${oldapplyInfo.contactInfoVOList[status.index].contactRelation} requiredFontWeight">与申请人关系:</th>
                            <td width="12%">
                                <!-- 判断当前联系人是否是配偶 --->
                                <c:if test="${item.contactRelation =='00013'}">
                                    <input type="hidden" value="00013" name="contactRelation" />配偶
                                </c:if>
                                <c:if test="${item.contactRelation!='00013'}">
                                    <input class="easyui-combobox maritalRelation input rlstionsp" value="${item.contactRelation}" name="contactRelation" />
                                </c:if>
                            </td>
                            <th width="18%" class="${oldapplyInfo.contactInfoVOList[status.index].ifKnowLoan} requiredFontWeight">是否知晓贷款:</th>
                            <td width="12%">
                                是<input type="radio" name="ifKnowLoan" <c:if test="${item.ifKnowLoan=='Y'}">checked="checked"</c:if> value="Y" />
                                否<input type="radio" name="ifKnowLoan" value="N" <c:if test="${item.ifKnowLoan=='N'}">checked="checked"</c:if> />
                            </td>
                        </tr>

                        <!-- 如果是配偶需要填写证件信息  -->
                        <c:if test="${item.contactRelation =='00013'}">
                            <tr>
                                <th width="10%" class="${oldapplyInfo.contactInfoVOList[status.index].ifForeignPenple} requiredFontWeight">外籍人士:</th>
                                <td width="12%">
                                    是<input type="radio" name="ifForeignPenple" id="firstCustomerInfo_spouse_ifForeignY" value="Y"
                                            <c:if test="${item.ifForeignPenple=='Y'}"> checked="checked" </c:if> />
                                    否<input type="radio" name="ifForeignPenple" id="firstCustomerInfo_spouse_ifForeignN" value="N"
                                            <c:if test="${item.ifForeignPenple!='Y'}"> checked="checked" </c:if> />
                                </td>
                                <td></td>
                                <th class="${oldapplyInfo.contactInfoVOList[status.index].contactIdNo} requiredFontWeight">身份证号码:</th>
                                <td colspan="3">
                                    <input id="first_contactInfoVOList_div_spouseIdCard" name="contactIdNo" class="easyui-textbox input" value="${item.contactIdNo}"
                                    	<c:if test="${isDirectApp && item.ifForeignPenple!='Y'}">
                                    		data-options="validType:['consortIDCard','checkSexForContacts[\'${applyInfo.applyInfoVO.idNo}\']'],width:435"
                                    	</c:if>
                                        <c:if test="${isDirectApp && item.ifForeignPenple=='Y'}">
                                            data-options="validType:'consortInfo[1,100]',width:435"
                                        </c:if>
                                    	<c:if test="${!isDirectApp && item.ifForeignPenple=='Y'}">
                                        	data-options="validType:'consortInfo[1,100]',width:435"
                                       	</c:if>
                                        <c:if test="${!isDirectApp && item.ifForeignPenple!='Y'}">
	                                        data-options="required:true,
	                                            missingMessage:'客户配偶非外籍人士，请填写身份证！',
	                                            validType:['consortIDCard','checkSexForContacts[\'${applyInfo.applyInfoVO.idNo}\']'],
	                                            width:435"
                                        </c:if>
                                    />
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <th class="${oldapplyInfo.contactInfoVOList[status.index].contactCellPhone} requiredFontWeight">手机:</th>
                            <td><input class="easyui-textbox input cellPhoneCheck firstPhone" value="${item.contactCellPhone}" name="contactCellPhone"></td>
                            <c:if test="${empty item.contactCellPhone_1}">
                                <td><a href="javaScript:void(0);" onclick="addCellPhone(this,'cellPhone')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                            </c:if>
                            <c:if test="${not empty item.contactCellPhone_1}">
                                <td><input class="easyui-textbox input cellPhoneCheck secondPhone" data-options="width:180" value="${item.contactCellPhone_1}" name="contactCellPhone_1"><a style="margin-left: 15px" href="javaScript:void(0);" onclick="deleteCellPhone(this,'cellPhone')"><i class="fa fa-minus" aria-hidden="true"></i></a></td>
                            </c:if>
                            <th class="${oldapplyInfo.contactInfoVOList[status.index].contactCorpPhone}">单电:</th>
                            <td><input class="easyui-textbox input contactCorpPhone" value="${item.contactCorpPhone}" name="contactCorpPhone"></td>
                            <c:if test="${empty item.contactCorpPhone_1}">
                                <td><a href="javaScript:void(0);" onclick="addCellPhone(this,'corpPhone')"><i class="fa fa-plus" aria-hidden="true"></i></a></td>
                            </c:if>
                            <c:if test="${not empty item.contactCorpPhone_1}">
                                <td><input class="easyui-textbox input contactCorpPhone" data-options="validType:'telNum',width:180" value="${item.contactCorpPhone_1}" name="contactCorpPhone_1"><a style="margin-left: 15px" href="javaScript:void(0);" onclick="deleteCellPhone(this,'corpPhone')"><i class="fa fa-minus" aria-hidden="true"></i></a></td>
                            </c:if>
                        </tr>
                        <tr>
                            <th class="${oldapplyInfo.contactInfoVOList[status.index].contactEmpName}">单位名称:</th>
                            <td colspan="5"><input class="easyui-textbox input contactEmpName" value="${item.contactEmpName}" name="contactEmpName"></td>
                        </tr>
                    </table>
                </div>
        </form>
    </c:forEach>
</div>
<a class="easyui-linkbutton_ok05 l-btn l-btn-small " id="first_customerInfo_addPeople" onclick="customerAddContacts()">新增联系人</a>&nbsp;&nbsp;