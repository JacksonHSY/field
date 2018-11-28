	//@ sourceURL=queryQualityOpoin.js

	var loanNo = $('#queryQualityOpion_quality_loanNo').val();
	var picImgUrl=$('#queryQualityOpion_quality_picImgUrl').val();
	var sysName=$('#queryQualityOpion_quality_sysName').val();
	var nodeKey=$('#queryQualityOpion_quality_nodeKey').val();
	var operator=$('#queryQualityOpion_quality_operator').val();
	var jobNumber=$('#queryQualityOpion_quality_jobNumber').val();
	var approvalMap=$.parseJSON($('#queryQualityOpion_quality_approvalMap').val());//获取审核人员和别称的对应关系
	var checkResList=$.parseJSON($('#queryQualityOpion_quality_checkResListJson').val());//获取案件历史质检结论
    var checkLeader = $('#addQualityOpinion_quality_checkLeader').val();
    var roleCode = $('#queryQualityOpion_quality_roleCode').val();
	$(function() {
		
		// 初始化差错代码下拉框
        initErrorCodeCombobox();

		$.ajax({ 
			async: false,
            type:"POST", 
            url:ctx.rootPath()+'/qualityControlDesk/getApproveLeader',
            success:function(data){
            	if('qualityCheck' == roleCode){
            		for(var i=0;i< data.length;i++){
        				data[i].name = '组长';
            		}
            	}
            	$('#queryQualityOpi_leader_approvalLeader').combobox({
            		prompt: '主管/领导',
            		editable: false,
            		hasDownArrow: true,
            		disabled:true,
            		textField:'name',
            		valueField:'usercode',
            		data: data,
            		filter: function(q, row){
            			var opts = $(this).combobox('options');
            			return row[opts.textField].indexOf(q) == 0;
            		},
            	});
            } 
        });

        //  设置默认值
        if(checkLeader!=null && checkLeader!=''){
            $('#queryQualityOpi_leader_approvalLeader').combobox('setValue', checkLeader);
        }

		//加载附件列表
		qualityAttachmentTable("#queryQualityOpion_quality_attach");

		//如果是质检详情 ，隐藏附件上传按钮
		$('#addQuality_quality_attachmentUpload').hide();

		addFirstAudit("#queryQualityOpion_quality_firstApprove");

		addFinalAudit("#queryQualityOpion_quality_finishApprove");
		
		addLeaderAudit();
		isNoError();
	});
	
		
	function selectErrorCode(str){
		var name ={name:str};
		$.ajax({ 
            type:"POST", 
            url:ctx.rootPath()+'/qualityControlDesk/quryErrorCode', 
            data:name, 
            success:function(data){
            	if(data.type == "SUCCESS"){
            		
            	}else{
            		
            	}
            } 
         }); 
	}

	function initErrorCodeCombobox(){
        //定义差错代码属性
        $('.errorCode').combobox({
            prompt:'差错代码',
            required:false,
            url:ctx.rootPath()+'/qualityControlDesk/quryErrorCode',
            editable:true,
            hasDownArrow:true,
            disabled: true,
            textField:'code',
            valueField:'id',
            filter: function(q, row){
                var opts = $(this).combobox('options');
                return row[opts.textField].indexOf(q) == 0;
            }
        });
	}

	// 添加初审
	function addFirstAudit(obj) {
		//根据质检件id查询全部质检结论和质检意见checkResList		
		//获取当前初审人员applist
		//遍历该applist
		//根据applist[i]跟checkResList进行匹配,获取实体对象进行回显
//		var strs = str.split(",");
		var strs = approvalMap['firstList'];
		for(var i = 0;i < strs.length; i++){
			if(null == strs[i] || strs[i] ==""){
				return;
			}
			var checkViewList = filtCheckView(strs[i],'checkPart');//获取对应质检结论
			var nameKey = 'XSCS'+strs[i];
			var name = approvalMap[nameKey];
			var html = $('<table class="table_ui W100">'
					+'<tr>'
					+'<td>'
					+'<input type="hidden" name="firstCheckError" value="apply-check/'+strs[i]+'"><label class="hand">&nbsp;'+name+'</label>'
					+'	<input type="radio" name="firstCheckResult'+strs[i]+'" value="E_000004">重大差错'
					+'	<input type="radio" name="firstCheckResult'+strs[i]+'" value="E_000003">一般差错'
					+'	<input type="radio" name="firstCheckResult'+strs[i]+'" value="E_000001">预警'
					+'	<input type="radio" name="firstCheckResult'+strs[i]+'" value="E_000002">建议'
					+'	<input type="radio" name="firstCheckResult'+strs[i]+'" value="E_000000">无差错'
					+'</td>'
					+'</tr>'
					+'<tr>'
					+'<td><input type="text" id="queryQualityOpi_firstApp_errorCode'+strs[i]+'" name="firstErrorCode" class="easyui-combobox input errorCode" >'
					+'</td>'
					+'</tr>'
					+'<tr>'
					+'<td><a href="javaScript:void(0);" onclick="diplayNextTr(this)">+&nbsp;</a>质检意见</td>'
					+'</tr>'
					+'<tr>'
					+'<td id="addQualityOpi_firstQuality_chickView'+strs[i]+'"></td>'
					+'</tr>'
					+'</table>');

			$(obj).parents("table").after(html);

            //定义差错代码属性
            $('#queryQualityOpi_firstApp_errorCode' + strs[i]).combobox({
                prompt:'差错代码',
                required:false,
                url:ctx.rootPath()+'/qualityControlDesk/quryErrorCode',
                editable:true,
                hasDownArrow:true,
                disabled: true,
                textField:'code',
                valueField:'id',
                filter: function(q, row){
                    var opts = $(this).combobox('options');
                    return row[opts.textField].indexOf(q) == 0;
                }
            });

			var qualityCheckRes = checkViewList[checkViewList.length-1];	// 获取最后一条质检结论
			if(qualityCheckRes){
				var checkResult = qualityCheckRes['checkResult'];	// 差错类型
				var errorCode = qualityCheckRes['errorCode'];		// 差错代码

				// 差错类型初始化
				$('input[name="firstCheckResult'+strs[i]+'"][value="'+checkResult+'"]').prop("checked", "checked");//根据value选择radio的默认选项

				// 差错代码下拉框设置初始值
				$('#queryQualityOpi_firstApp_errorCode'+strs[i]).combobox('setValue', errorCode).combobox('disable');

			}
            // 差错类型设置为只读
			$('input[name="firstCheckResult'+strs[i]+'"]').attr("disabled", "disabled");

			loadCheckView('#addQualityOpi_firstQuality_chickView'+strs[i],checkViewList);//显示历史的质检意见
		}
	}
	
	// 添加终审
	function addFinalAudit(obj){
		var strs = approvalMap['finishList'];
        for(var i = strs.length-1;i >= 0; i--){
		//for(var i = 0;i < strs.length; i++){
			if(null == strs[i] || strs[i] ==""){
				return;
			}
			var checkViewList = filtCheckView(strs[i],'finalPart');//获取对应质检结论
			var nameKey = 'XSZS'+strs[i];
			var name = approvalMap[nameKey];
			var html = $('<table class="table_ui W100">'
					+'<tr>'
					+'<td>'
					+'<input type="hidden" name="finishCheckError" value="applyinfo-finalaudit/'+strs[i]+'"><label class="hand">&nbsp;'+name+'</label>'
					+'	<input type="radio" name="finishCheckResult'+strs[i]+'" value="E_000004">重大差错'
					+'	<input type="radio" name="finishCheckResult'+strs[i]+'" value="E_000003">一般差错'
					+'	<input type="radio" name="finishCheckResult'+strs[i]+'" value="E_000001">预警'
					+'	<input type="radio" name="finishCheckResult'+strs[i]+'" value="E_000002">建议'
					+'	<input type="radio" name="finishCheckResult'+strs[i]+'" value="E_000000">无差错'
					+'</td>'
					+'</tr>'
					+'<tr>'
					+'<td><input type="text" id="queryQualityOpi_finishApp_errorCode'+strs[i]+'" name="finishErrorCode" class="easyui-combobox input errorCode">'
					+'</td>'
					+'</tr>'
					+'<tr>'
					+'<td><a href="javaScript:void(0);" onclick="diplayNextTr(this)">+&nbsp;</a>质检意见</td>'
					+'</tr>'
					+'<tr>'
					+'<td id="addQualityOpi_finishQuality_chickView'+strs[i]+'"></td>'
					+'</tr>'
					+'</table>');
			$(obj).parents("table").after(html);

			//定义差错代码属性
            $('#queryQualityOpi_finishApp_errorCode' + strs[i]).combobox({
                prompt:'差错代码',
                required:false,
                url:ctx.rootPath()+'/qualityControlDesk/quryErrorCode',
                editable:true,
                hasDownArrow:true,
                disabled: true,
                textField:'code',
                valueField:'id',
                filter: function(q, row){
                    var opts = $(this).combobox('options');
                    return row[opts.textField].indexOf(q) == 0;
                }
            });

            var qualityCheckRes = checkViewList[checkViewList.length-1];
			if(qualityCheckRes){
				var checkResult = qualityCheckRes['checkResult'];
				var errorCode = qualityCheckRes['errorCode'];

                // 差错类型
				$('input[name="finishCheckResult'+strs[i]+'"][value="'+checkResult+'"]').prop("checked", "checked");
				// 差错代码下拉框设置默认值，禁用
				$('#queryQualityOpi_finishApp_errorCode'+strs[i]).combobox("setValue",errorCode).combobox('disable');


			}

            // 差错类型设置为只读
			$('input[name="finishCheckResult'+strs[i]+'"]').attr("disabled", "disabled");

            //显示历史的质检意见
			loadCheckView('#addQualityOpi_finishQuality_chickView'+strs[i],checkViewList);
		}
	}
	
	//回显领导数据
	function addLeaderAudit(){
		var $historyView = $('.leaderPart');
		var $approvalLeader = $historyView.find('#queryQualityOpi_leader_approvalLeader');
		var $leaderCheckResult = $historyView.find('input[name="leaderCheckResult"]');
        var $leaderErrorCode = $historyView.find('#queryQualityOpi_leader_errorCode');

        $leaderCheckResult.attr("disabled", "disabled"); //设置选择按钮为只读

		var checkViewList = filtLeaderView();
        if(checkViewList.length > 0){
            var approvalLeader = checkViewList[checkViewList.length - 1]['approvePerson'];
			var checkResult = checkViewList[checkViewList.length - 1]['checkResult'];
			var errorCode = checkViewList[checkViewList.length - 1]['errorCode'];
			if(approvalLeader){
				$approvalLeader.combobox('setValues',approvalLeader);	// 信审人员
			}
            $historyView.find('input[name="leaderCheckResult"][value=' + checkResult + ']').attr('checked','checked');	// 差错类型
            if(errorCode){
            	$leaderErrorCode.combobox('setValues', errorCode);		// 差错代码
            }
		}
		loadCheckView('#queryQualityOpi_leaderQuality_chickView', checkViewList);//显示历史的质检意见
	}
	
	//隐藏显示input输入框
	function diplayNextTr(obj,chickView,errorCodeId) {
		
		if(obj){
			$('#'+chickView).show();
		}
		if(obj == 'E_000004' || obj == 'E_000003'){
			$('#'+errorCodeId).combobox({  
			       required:true,
			       disabled:false
			}); 
		}else{
			$('#'+errorCodeId).combobox({  
				required:false, 
				disabled:true
			});
		}
	}
	
	/**
	 * @Desc: 质检附件table加载
	 * @Author: lihm
	 * @Date: 2017/5/17
	 */
	function qualityAttachmentTable(id) {
	    $(id).datagrid({
	        url: ctx.rootPath() + '/qualityFeedBack/findFeedBackAttachmentList?loanNo=' + loanNo,
	        method: "POST",
	        idField: 'id',
	        fitColumns: false,
	        scrollbarSize: 0,
	        toolbar:'#queryQualityOpion_attach_toolBarBtn',
	        columns: [[{
	            field: 'imgName',
	            title: '附件',
	            width: 120,
	            formatter: function (value, row, index) {
	            	return formatString('<a href="javascript:void(0)"  onclick="viewAttachment(\'{0}\');" >' + row.imgName + '</a>', row.url);
	                // return '<a href="javascript:void(0)" target="_parent"><img src="'+row.url+'" width="200" height="80" border="0" /></a>';
	            }
	        }, {
	            field: 'createJobnum',
	            title: '操作人员',
	            width: 120,
	        }, {
	            field: 'uptime',
	            title: '时间',
	            width: 150,
	        }, {
	            field: 'option',
	            title: '操作',
	            width: 150,
	            formatter: function (value, row, index) {
	            	//if(flag != 'done'){
	            		//return formatString('<a href="javascript:void(0)" onclick="deleteAttachmentById(\'{0}\',\'{1}\',\'{2}\');" >删除</a>',row.id,row.creator,row.createJobnum);
	            	//}
	            }
	        }]]
	    });
	}
	
	/**
	 * @Desc: 质检附件上传
	 * @Author: lhm
	 * @Date: 2017/5/17 11:35
	 */
	function qualityAttachmentUpload() {
	    var ifm = '<iframe src="'+picImgUrl+'?nodeKey='+nodeKey+'&sysName='+sysName+'&operator=' + operator + '&jobNumber=' + jobNumber + '&appNo=' + loanNo +
        '" style="width:100%;height:99.5%;padding:0px;margin:0px;border:0px;display: table"></iframe>';
	    $("#addQuality_quality_attachUpWindow").removeClass("display_none").dialog({
	        title: "附件上传",
	        modal: true,
	        width: 800,
	        height: 680,
	        content: ifm
	    });
	}
	
	/**
	 * @param id
	 * @param creator
	 * @param createJobnum
	 * 根据ID删除附件
	 */
	function deleteAttachmentById(id,creator,createJobnum){
	    $.messager.confirm('确认', '确认删除该附件吗?', function(r){
	        if (r){
	            var api = ctx.rootPath() + '/qualityFeedBack/deleteAttachmentById';
	            var params={id:id,operator:creator,jobNumber:createJobnum};
	            var callback = function (data) {
	                if (data.type=='SUCCESS') {
	                    $.info("提示",data.firstMessage,"info", 2000);
	                }else{
	                    $.info("提示",data.firstMessage,"info", 2000);
	                }
	                $("#feedBackAttachmentTable").datagrid("reload");
	            }
	            var error = function (XMLHttpRequest, textStatus, errorThrown) {
	                console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
	            };
	            post(api, params, 'json', callback, error);
	        }
	    });
	}
	
	/**
	 * @param str
	 * @returns
	 */
	function formatString(str) {
	    for (var i = 0; i < arguments.length - 1; i++) {
	        str = str.replace("{" + i + "}", arguments[i + 1]);
	    }
	    return str;
	}
	
	/**
	 * 单击查看附件
	 * 
	 */
	function viewAttachment(url) {
	    var ifm = '<iframe width="100%" height="100%" src="' + url + '"><img src="' + url + '"  border="0" /></iframe>';
	    $('#attachmentDetailsDialog').dialog({
	        title: '反馈附件',
	        width:"60%",
	        height:"100%",
	        closed: false,
	        cache: false,
	        content: ifm,
	    });
	}
	
	function reloadFeedBackAttachment(){
	    $("#queryQualityOpion_quality_attach").datagrid("reload");
	}
	
	function leaderCheckViewListener(){
		$('input[name="leaderCheckResult"]').change(function() {  
			var leaderCheckResult = $(this).val();
			diplayNextTr(leaderCheckResult, 'addQualityOpi_leaderApp_chickView', 'addQualityOpi_leader_errorCode');
			
	    }); 
		
		//点击事件监听实现可以取消点击
		$('input[name="leaderCheckResult"]').click(function(){
			 if($(this).attr('checked')){
				 $(this).removeAttr('checked');
				 $('#addQualityOpi_leaderApp_chickView').hide();
				 $('#addQualityOpi_leader_errorCode').combobox({  
					 required:false 
				 });
				 $('#addQualityOpi_leaderApp_chickView').val(""); 
				 
			 }else{
				 $(this).attr('checked','checked');
			 }
		 });
	}
	
	/**
	 * 加载质检意见
	 * @author lihuimeng
	 * @date 2017年6月8日 下午6:01:53
	 */
	function loadCheckView(obj, checkViewList){
//		1、获取质检结论集合
//		2、根据审核人员和审核类型取出
//		3、遍历展出
		var strs = [];
		strs = checkViewList;
		if(strs.length >= 1){
			for(var i = 0;i < strs.length; i++){
				var checkView = "";
				if(strs[i]['checkView']){
					checkView = strs[i]['checkView'];
					var html = $('<textarea class="textarea" disabled="disabled">'+ checkView + '</textarea>');
					$(obj).append(html);
				}
			}
		}
	}
	/**
	 * 筛选质检结论
	 * @author lihuimeng
	 * @date 2017年6月8日 下午6:01:53
	 */
	function filtCheckView(person,checkPart){
		var strs = [];
		if(checkResList){
			for(var i=0;i<checkResList.length;i++){
				if(person == checkResList[i]['approvePerson'] && checkPart == checkResList[i]['checkPart']){
					strs.push(checkResList[i]);
				}
			}
		}
		return strs;
	}
	
	/**
	 * 筛选领导质检结论
	 * @author lihuimeng
	 * @date 2017年6月13日 上午11:09:09
	 */
	function filtLeaderView(){
		var strs = [];
		if(checkResList){
			for(var i=0;i<checkResList.length;i++){
				if(checkResList[i]['checkPart'] == 'checkLeaderPart'){
					strs.push(checkResList[i]);
				}
			}
		}
		return strs;
	}
	
	/**
	 * 判断是否为无差错模块
	 * @author lihuimeng
	 * @date 2017年6月13日 上午11:09:09
	 */
	function isNoError(){
		var bool = true;
		if(checkResList.length > 0){
			for(var i=0; i< checkResList.length; i++){
				if(checkResList[i]['checkResult'] != 'E_000000'){
					bool = false;
				}
				if(checkResList[i]['checkView']){
					bool = false;
				}
			}
		}else{
			bool = false;
		}
		if(bool == true){
			$("#queryQualityOpinoin_checkbox_noError").attr("checked","checked");
			$('#queryQualityOpinoin_quality_opoin input').each(function(){
				$(this).attr('checked',false);
			});
		}
	}
	
	