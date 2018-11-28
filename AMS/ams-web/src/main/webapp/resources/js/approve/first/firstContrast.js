$(function() {
	applyHistoryDataComparison();
});
window.onbeforeunload = function() {
	if (isNotNull(insideMatchImageContrastHTMLWindow)) {// 关闭影像比对
		insideMatchImageContrastHTMLWindow.close();
	}
	window.opener.insideMatchContrastHTMLWindow = null;
};
// add by zw at 2017-05-05 申请历史信息比对
function applyHistoryDataComparison() {
    var loanNo = $("#firstContrast_loanNo").val();
    post(ctx.rootPath() + '/firstInsideMatch/getCustomerKeyInformation/' + loanNo, null, "json", function (data) {
        if (data.type == "SUCCESS") {
            // 加载表单左边表头
            $('#firstInsideMatch_customerInfo_table').append('<thead><tr><td>客户信息</td></tr></thead><tr><td>申请时间</td></tr><tr><td>借款状态</td></tr><tr><td>常用手机</td></tr><tr><td>单位名称</td></tr><tr><td>单位地址</td></tr><tr><td>单位电话</td></tr><tr><td>家庭地址</td></tr>');
            //----------------------  加载 客户本人基本信息 模块--------------------------------------------------------------------------------------------------
            $.each(data.data.customerBasicInfos, function (key, v) {
                // 加载表单表头
                if (loanNo == v.loanNum) {
                    $('#firstInsideMatch_customerInfo_table tr:eq(0)').append('<td>本次申请&nbsp;<input name="Fruit" value="' + loanNo + '" type="checkbox" /></td>');
                } else {
                    $('#firstInsideMatch_customerInfo_table tr:eq(0)').append('<td>历史申请&nbsp;' + key + '<input name="Fruit" value="' + v.loanNum + '" type="checkbox" /></td>');
                }
                // 添加 申请时间 和申请状态
                $('#firstInsideMatch_customerInfo_table tr:eq(1)').append('<td>' + v.applyDate + '</td>');
                $('#firstInsideMatch_customerInfo_table tr:eq(2)').append('<td>' + v.applyStatus + '</td>');
                // 添加常用手机和备用手机
                if (isNotNull(v.phoneNumber2)) {
                    $('#firstInsideMatch_customerInfo_table tr:eq(3)').append('<td>'+v.phoneNumber1 +'<br>' + v.phoneNumber2 + '</td>');
                }else{
                    $('#firstInsideMatch_customerInfo_table tr:eq(3)').append('<td>' +v.phoneNumber1 + '</td>');
				}
                // 添加单位名称
                if (isNotNull(v.corpName)) {
                    $('#firstInsideMatch_customerInfo_table tr:eq(4)').append('<td>' + v.corpName + '</td>');
                } else {
                    $('#firstInsideMatch_customerInfo_table tr:eq(4)').append('<td></td>');
                }
                // 单位地址
                if (isNotNull(v.corpAddress)) {
                    $('#firstInsideMatch_customerInfo_table tr:eq(5)').append('<td>' + v.corpAddress + '</td>');
                } else {
                    $('#firstInsideMatch_customerInfo_table tr:eq(5)').append('<td></td>');
                }
                // 添加单位电话
                if (isNotNull(v.corpPhone2)) {
                    $('#firstInsideMatch_customerInfo_table tr:eq(6)').append('<td>'+v.corpPhone1 +"<br/>"+ v.corpPhone2+'</td>');
                }else{
                    $('#firstInsideMatch_customerInfo_table tr:eq(6)').append('<td>' + v.corpPhone1 + '</td>');
				}
                // 添加家庭地址
                if (isNotNull(v.homeAddress)) {
                    $('#firstInsideMatch_customerInfo_table tr:eq(7)').append('<td>' + v.homeAddress + '</td>');
                } else {
                    $('#firstInsideMatch_customerInfo_table tr:eq(7)').append('<td></td>');
                }
            });
            //----------------------客户本人基本信息  对比标红----------------------------------------------
            $('#firstInsideMatch_customerInfo_table tr').each(function (indexTr, trObj) {
                if (indexTr > 1) {                                  //剔除表头和申请时间
                    var firstValue = $(trObj).find("td:eq(1)").html();  //获取本次申请对比值
                    if (3 == indexTr || 6 == indexTr) {
                        $(trObj).find("td:gt(1)").each(function(indexTd,tdObj){
                            var otherPhone = $(tdObj).html();
                            if (isNotNull(firstValue)) {
                                if (isNotNull(otherPhone)) {
                                    var thisPhoneArray = firstValue.split('<br>');//如果是联系人手机 或 联系人电话 会出现两个
                                    if (otherPhone.indexOf(thisPhoneArray[0]) < 0) {
                                        // 判断是否有两个手机或电话
                                        if(thisPhoneArray.length == 2) {
                                            if (otherPhone.indexOf(thisPhoneArray[1]) < 0) {
                                                $(trObj).find("td:eq(0)").addClass("markRed");
                                                return false;
                                            } else {
                                                return false;
                                            }
                                        }else{
                                            $(trObj).find("td:eq(0)").addClass("markRed");
                                            return false;
                                        }
                                    }
                                } else {
                                    $(trObj).find("td:eq(0)").addClass("markRed");
                                    return false;
                                }
                            } else if (isNotNull(otherPhone)) {
                                $(trObj).find("td:eq(0)").addClass("markRed");
                                return false;
                            }
						});
				    } else {
                        $(trObj).find('td:gt(1)').each(function (indexTd, tdObj) {    //循环历史申请对比值
                            var otherValue = $(tdObj).html();
                            if (isNotNull(firstValue) && firstValue != otherValue) {
                                $(trObj).find("td:first").addClass("markRed");
                                return false;
                            } else if (!isNotNull(firstValue) && isNotNull(otherValue)) {
                                $(trObj).find("td:first").addClass("markRed");
                                return false;
                            }
                       });
                   }
                }
            });
            // 客户直系亲属联系人对比firstInsideMatch_customerRelativesInfos_table-------------------------------------------------------------------------------------------------------
            $("#firstInsideMatch_customerRelativesInfos_table").append('<thead><tr><td>联系人</td><td>相应字段</td></tr></thead>');
            var customerRelativesInfosList = data.data.customerRelativesInfos;
            // 循环创建直属联系人信息
            // 标识是否需要创建纵向标题
            var spuseNum = 0, parentNum = 0, childrenNum = 0;
            $.each(customerRelativesInfosList,function(key,valObj){
                // 获取配偶最大值最多一个
                if (isNotNull(valObj.spouse) && valObj.spouse.length > spuseNum) {
                    spuseNum = valObj.spouse.length;
                }
                // 获取父母最大值最多两个
                if (isNotNull(valObj.parent) && valObj.parent.length > parentNum) {
                    parentNum = valObj.parent.length;
                }
                // 获取子女最大值最多三个
                if (isNotNull(valObj.children) && valObj.children.length > childrenNum) {
                    childrenNum = valObj.children.length;
                }
            });
            // 创建直属联系人信息
           $.each(customerRelativesInfosList,function(key,valObj){
                // 创建横向标题
                if (key == 0) {
                    $("#firstInsideMatch_customerRelativesInfos_table tr:eq(0)").append('<td>本次申请&nbsp;</td>');
                } else {
                    $("#firstInsideMatch_customerRelativesInfos_table tr:eq(0)").append('<td>历史申请&nbsp;' + key + '</td>');
                }
                // 创建纵向标题
                if (0 == key) {
                    // 创建直属联系人配偶
                    if (0 != spuseNum) {
                        $("#firstInsideMatch_customerRelativesInfos_table").append("<tr><td  class='remarkRed' rowspan='3'>配偶</td><td class='remarkRed'>姓名</td></tr>")
                        $("#firstInsideMatch_customerRelativesInfos_table").append("<tr><td class='remarkRed'>手机</td></tr>")
                        $("#firstInsideMatch_customerRelativesInfos_table").append("<tr><td class='remarkRed'>工作单位</td></tr>");
                    }
                    // 创建直属联系人父母
                    for(var j =0; j < parentNum; j++) {
                        $("#firstInsideMatch_customerRelativesInfos_table").append("<tr><td class='remarkRed' rowspan='3'>父母</td><td class='remarkRed'>姓名</td></tr>")
                        $("#firstInsideMatch_customerRelativesInfos_table").append("<tr><td class='remarkRed'>手机</td></tr>")
                        $("#firstInsideMatch_customerRelativesInfos_table").append("<tr><td class='remarkRed'>工作单位</td></tr>");
                    }
                    // 创建直属联系人子女
                    for (var k = 0; k < childrenNum; k++) {
                        $("#firstInsideMatch_customerRelativesInfos_table").append("<tr><td class='remarkRed' rowspan='3'>子女</td><td class='remarkRed'>姓名</td></tr>")
                        $("#firstInsideMatch_customerRelativesInfos_table").append("<tr><td class='remarkRed'>手机</td></tr>")
                        $("#firstInsideMatch_customerRelativesInfos_table").append("<tr><td class='remarkRed'>工作单位</td></tr>");
                    }
                }
                // 创建配偶信息最大是一个
                if (0 != spuseNum) {// 如果不存在配偶
                    if (isNotNull(valObj.spouse)) {
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq(1)").append("<td>" + valObj.spouse[0].name + "</td>");
                        var phone2 = isNotNull(valObj.spouse[0].phoneNumber2) ? "<br>" + valObj.spouse[0].phoneNumber2 : "";
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq(2)").append("<td>" + valObj.spouse[0].phoneNumber1 + phone2 + "</td>");
                        var corpName = isNotNull( valObj.spouse[0].corpName) ?  valObj.spouse[0].corpName :"";
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq(3)").append("<td>" + corpName + "</td>");
                    } else {
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq(1)").append("<td></td>");
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq(2)").append("<td></td>");
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq(3)").append("<td></td>");
                    }
                }
                // 创建父母信息 最多是两个
                var currentParentNum = isNotNull(valObj.parent) ? valObj.parent.length : 0;
                for (var j = 0; j < (parentNum >= currentParentNum ? parentNum:currentParentNum); j++) {
                    var trIndex = 1 + spuseNum * 3 + j * 3;
                    if (j < currentParentNum) {
                        var parentObj = valObj.parent[j];
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq("+trIndex+")").append("<td>"+  parentObj.name +"</td>");
                        var phone2 = isNotNull(parentObj.phoneNumber2) ? "<br>" + parentObj.phoneNumber2 : "";
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq("+ (trIndex+1) +")").append("<td>"+ parentObj.phoneNumber1 + phone2 +"</td>");
                        var corpName = isNotNull( parentObj.corpName) ?  parentObj.corpName :"";
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq("+(trIndex+2) +")").append("<td>"+  corpName +"</td>");
                    } else {
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq("+trIndex+")").append("<td></td>");
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq("+ (trIndex+1) +")").append("<td></td>");
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq("+(trIndex+2) +")").append("<td></td>");
                    }
                }

                // 创建子女信息 最大三个
                var currentChildrenNum = isNotNull(valObj.children) ? valObj.children.length:0;
                for (var k = 0; k < (childrenNum >= currentChildrenNum ? childrenNum:currentChildrenNum ); k++){
                    var trcIndex = 1 + parentNum *3 + spuseNum *3 + k * 3;
                    if (k < currentChildrenNum) {
                        var childrenObj = valObj.children[k];
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq("+trcIndex+")").append("<td>"+  childrenObj.name +"</td>");
                        var phone2 = isNotNull(childrenObj.phoneNumber2) ? "<br>" + childrenObj.phoneNumber2 : "";
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq("+ (trcIndex+1) +")").append("<td>"+ childrenObj.phoneNumber1 + phone2 +"</td>");
                        var corpName = isNotNull( childrenObj.corpName) ?  childrenObj.corpName :"";
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq("+ (trcIndex+2) +")").append("<td>"+  corpName +"</td>");
                    } else {
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq("+trcIndex+")").append("<td></td>");
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq("+ (trcIndex+1) +")").append("<td></td>");
                        $("#firstInsideMatch_customerRelativesInfos_table tr:eq("+ (trcIndex+2) +")").append("<td></td>");
                    }
                }

            });
           // 直属联系人对比标红
            $('#firstInsideMatch_customerRelativesInfos_table tr').each(function (indexTr, trObj) {
                if (indexTr > 0) {//剔除表单头部
                    var tdIndexNum = 1;
                    if (indexTr % 3 == 1) {// 合并行后需要重新判断td的下标
                        tdIndexNum=2;
                    }
                    var  firstValue = $(trObj).find("td:eq("+tdIndexNum+")").html();  //获取本次申请对比值
                    if((indexTr-2)%3==0){// 过滤出手机信息
                        $(trObj).find("td:gt(1)").each(function(indexTd,tdObj){
                            var otherPhone = $(tdObj).html();
                            if (isNotNull(firstValue)) {
                                if (isNotNull(otherPhone)) {
                                    var thisPhoneArray = firstValue.split('<br>');//如果是联系人手机出现两个
                                    if (otherPhone.indexOf(thisPhoneArray[0]) < 0) {
                                        // 判断是否有两个手机或电话
                                        if(thisPhoneArray.length == 2) {
                                            if ( otherPhone.indexOf(thisPhoneArray[1]) < 0) {
                                                $(trObj).find(".remarkRed").addClass("markRed");
                                                $(trObj).prev().find("td:first").addClass("markRed");// 如果手机号标红需要把当前组关系标红
                                                return false;
                                            } else {
                                                return false;
                                            }
                                        }else{
                                            $(trObj).find(".remarkRed").addClass("markRed");
                                            $(trObj).prev().find("td:first").addClass("markRed");// 如果手机号标红需要把当前组关系标红
                                            return false;
                                        }
                                    }
                                } else {
                                    $(trObj).find(".remarkRed").addClass("markRed");
                                    $(trObj).prev().find("td:first").addClass("markRed");// 如果手机号标红需要把当前组关系标红
                                    return false;
                                }
                            } else if (isNotNull(otherPhone)) {
                                $(trObj).find(".remarkRed").addClass("markRed");
                                $(trObj).prev().find("td:first").addClass("markRed");// 如果手机号标红需要把当前组关系标红
                                return false;
                            }
                        });
                    }else {
                        $(trObj).find('td:gt('+tdIndexNum+')').each(function (indexTd, tdObj) {    //循环历史申请对比值
                            var otherValue = $(tdObj).html();
                            if (isNotNull(firstValue) && firstValue != otherValue) {
                                $(trObj).find(".remarkRed").addClass("markRed");
                                if (indexTr%3 == 0) {// 判断是否是工作单位
                                    $(trObj).prev().prev().find("td:first").addClass("markRed");// 如果手机号标红需要把当前组关系标红
                                }
                                return false;
                            } else if (!isNotNull(firstValue) && isNotNull(otherValue)) {
                                $(trObj).find(".remarkRed").addClass("markRed");
                                if (indexTr%3 == 0) {// 判断是否是工作单位
                                    $(trObj).prev().prev().find("td:first").addClass("markRed");// 如果手机号标红需要把当前组关系标红
                                }
                                return false;
                            }
                        });
                    }
                }
            });




            //-------------------------------------------- 加载模块-- 重名联系人信息比对--------------------------------------------------------------------------
            //定义重名联系人数组：联系人姓名，关系，手机
            var duplicateContactsInfosList = data.data.duplicateContactsInfos;
            $('#firstInsideMatch_duplicateContactsInfos_table').append('<thead><tr><td>联系人姓名</td><td>对应关系</td></tr></thead>');
            $.each(duplicateContactsInfosList, function (key, d) {
                //添加联系人表头姓名和关系、添加联系人表头姓名和手机
                $('#firstInsideMatch_duplicateContactsInfos_table').append('<tr><td>' + d.name + '</td><td>联系人关系</td></tr><tr><td>'+ d.name + '</td><td>联系人手机</td></tr>');
               //分割关系和电话
                var contact = d.contacts.split('|');
                var cellphone = d.cellPhones.split('|');
                var standbyCellPhones = d.standbyCellPhones.split('|');
                for (var i = 0;i < contact.length; i++) {
                    var v = contact[i] , cellphoneNum = cellphone[i],standbyCellPhone = standbyCellPhones[i];// 取出本次循环 关系 和 对应电话号码
                    //添加表头
                    if (0 == key) {
                        if (0 == i) {
                            $("#firstInsideMatch_duplicateContactsInfos_table tr:eq(0)").append('<td>本次申请&nbsp;</td>');
                        } else {
                            $("#firstInsideMatch_duplicateContactsInfos_table tr:eq(0)").append('<td>历史申请&nbsp;' + i + '</td>');
                        }
                    }
                    //添加联系人关系-不用判空
                     $("#firstInsideMatch_duplicateContactsInfos_table tr:eq(" + (key*2+1) + ")").append('<td>' + v + '</td>');
                   // 拼接电话信息-不用判空
                    if(isNotNull(standbyCellPhone)){
                        $("#firstInsideMatch_duplicateContactsInfos_table tr:last").append('<td>' + cellphoneNum +'<br>'+standbyCellPhone+ '</td>');
                    }else{
                        $("#firstInsideMatch_duplicateContactsInfos_table tr:last").append('<td>' + cellphoneNum + '</td>');
                    }

                }
            });
            // 重名联系人信息对比   标红
            $('#firstInsideMatch_duplicateContactsInfos_table tr').each(function (indexTr, trObj) {
                if (indexTr > 0) {//剔除表单头部
                    var firstValue = $(trObj).find("td:eq(2)").html();  //获取本次申请对比值
                    if(indexTr%2==0){
                        $(trObj).find("td:gt(2)").each(function(indexTd,tdObj){
                            var otherPhone = $(tdObj).html();
                            if (isNotNull(firstValue)) {
                                if (isNotNull(otherPhone)) {
                                    var thisPhoneArray = firstValue.split('<br>');//如果是联系人手机出现两个
                                    if (otherPhone.indexOf(thisPhoneArray[0]) < 0) {
                                        // 判断是否有两个手机或电话
                                        if(thisPhoneArray.length == 2){
                                            if (otherPhone.indexOf(thisPhoneArray[1]) < 0) {
                                                $(trObj).find("td:lt(2)").addClass("markRed");
                                                return false;
                                            }else{
                                                return false;
                                            }
                                        }else{
                                            $(trObj).find("td:lt(2)").addClass("markRed");
                                            return false;
                                        }
                                    }
                                } else {
                                    $(trObj).find("td:lt(2)").addClass("markRed");
                                    return false;
                                }
                            } else if (isNotNull(otherPhone)) {
                                $(trObj).find("td:lt(2)").addClass("markRed");
                                return false;
                            }
                        });
                    }else {
                        $(trObj).find('td:gt(2)').each(function (indexTd, tdObj) {    //循环历史申请对比值
                            var otherValue = $(tdObj).html();
                            if (isNotNull(firstValue) && firstValue != otherValue) {
                                $(trObj).find("td:lt(2)").addClass("markRed");
                                return false;
                            } else if (!isNotNull(firstValue) && isNotNull(otherValue)) {
                                $(trObj).find("td:lt(2)").addClass("markRed");
                                return false;
                            }
                        });
                    }
                }
            });
        }
    });
}

/**
 * 影像比对
 * 
 * @Author LuTing
 * @date 2017年3月2日
 * @mender fusj
 */
var insideMatchImageContrastHTMLWindow = null;
function insideMatchImageContrastDialog(picImageComUrl, firstApprovalCompare, sysCode, operator, jobNumber) {
	if ($("input[name='Fruit']:checked").length != 2) {
		$.info("提示", "请勾选两条需比对的申请记录");
		return false;
	}
	// add by zw at 2017-05-06 获取选中的两个比对申请单编号
	var comparisons = $("input[name='Fruit']:checked");
	var appNo1 = comparisons[0].value;
	var appNo2 = comparisons[1].value;
    $.ajax({
        type:"POST",
        url:ctx.rootPath()+'/firstInsideMatch/getAttachmentVoList/'+appNo1+'/'+appNo2,
        success:function(data){
        	if (data.type == "SUCCESS") {
                insideMatchImageContrastHTMLWindow = jDialog.open({url: ctx.rootPath() + '/applyApprove/imageMatchIframe/' + appNo1 + "/" + appNo2,width:1200, height:680});
			} else {
                $.info("提示", data.messages[0]);
			}
        }
    });
}

