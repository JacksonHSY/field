//@ sourceURL=addQualityOption.js

	var flag = $('#addQualityOpinion_quality_flag').val();
	var loanNo = $('#addQualityOpinion_quality_loanNo').val();
    var picImgUrl=$('#addQualityOpinion_quality_picImgUrl').val();
    var sysName=$('#addQualityOpinion_quality_sysName').val();
    var nodeKey=$('#addQualityOpinion_quality_nodeKey').val();
    var operator=$('#addQualityOpinion_quality_operator').val();
    var jobNumber=$('#addQualityOpinion_quality_jobNumber').val();
    var approvalMap=$.parseJSON($('#addQualityOpinion_quality_approvalMap').val());//获取审核人员和别称的对应关系
    var checkResList=$.parseJSON($('#addQualityOpinion_quality_checkResListJson').val());//获取案件历史质检结论
	var checkLeader = $('#addQualityOpinion_quality_checkLeader').val();
	var roleCode = $('#addQualityOpinion_quality_roleCode').val();
	var checkUser = $('#addQualityOpinion_quality_checkUser').val();

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
            			if(data[i].usercode == checkLeader && data[i].name != '组长'){
            				data[i].name = '组长';
            				break;
            			}
            		}
            		$('#addQualityOpi_leader_approvePerson').combobox({
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
            	}else{
            		$('#addQualityOpi_leader_approvePerson').combobox({
            			prompt: '主管/领导',
            			editable: false,
            			hasDownArrow: true,
            			textField:'name',
            			valueField:'usercode',
            			data: data,
            			filter: function(q, row){
            				var opts = $(this).combobox('options');
            				return row[opts.textField].indexOf(q) == 0;
            			},
            		});
            	}
            } 
        });
        
        
        
        
        //  设置默认值
        if(checkLeader!=null && checkLeader!=''){
            $('#addQualityOpi_leader_approvePerson').combobox('setValue', checkLeader);
        }

		//加载附件列表
        $('#addQualityOpinion_quality_attach').datagrid({
            url: ctx.rootPath() + '/qualityFeedBack/findFeedBackAttachmentList?loanNo=' + loanNo,
            method: "POST",
            idField: 'id',
            fitColumns: true,
            scrollbarSize: 0,
            toolbar:'#addQualityOpinion_attach_toolBarBtn',
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
                width: 120
            }, {
                field: 'uptime',
                title: '时间',
                width: 120
            }, {
                field: 'option',
                title: '操作',
                width: 120,
                formatter: function (value, row, index) {
                    return formatString('<a href="javascript:void(0)" onclick="deleteAttachmentById(\'{0}\',\'{1}\',\'{2}\');" >删除</a>',row.id,row.creator,row.createJobnum);
                }
            }]]
        });

		buttonController();
		leaderCheckViewListener();

		addFirstAudit("#addQualityOpinion_quality_firstApprove"+loanNo);
		addFinalAudit("#addQualityOpinion_quality_finishApprove");
		addLeaderAudit();
		if(!$('#addQualityOpinion_quality_leaderFirstId').val()){
			$('#addQualityOpinion_quality_leaderTable').hide();
		}else{
			$('input[name="leaderCheckError"]').attr("checked","checked");
		}
		
		if(!$('#addQualityOpinion_quality_checkFirstdId').val()){
			$('#addQualityOpinion_quality_firstTable').hide();
		}else{
			$('input[name="firstCheckError"]').attr("checked","checked");
		}


        // 页面顶端无差错点击事件
        $("#addQualityOpinoin_checkbox_noError").change(function() {
            var $qualityOpinionForm = $('#qualityOpinion__add_Form');
            if(this.checked){
				// 清空差错类型、置灰
				$qualityOpinionForm.find(':radio').prop('checked', false).attr('disabled', 'disabled');
				// 清空差错代码、置灰
				$qualityOpinionForm.find('.easyui-combobox').not('#addQualityOpi_leader_approvePerson').combobox('disable').combobox('clear');
				// 清空质检意见、置灰
				$qualityOpinionForm.find('.checkView').attr('disabled', 'disabled').val('');
				// 信审领导下拉框置灰
				$qualityOpinionForm.find('#addQualityOpi_leader_approvePerson').combobox('disable');

			}else{
                // 差错类型取消置灰
                $qualityOpinionForm.find(':radio').removeAttr('disabled');
                // 差错代码下拉框取消置灰
                if('qualityCheck' == roleCode){
                	$qualityOpinionForm.find('.errorCode').combobox('enable');
                }else{
                	$qualityOpinionForm.find('.easyui-combobox').combobox('enable');
                }
                // 质检意见取消置灰
                $qualityOpinionForm.find('.checkView').removeAttr('disabled');
			}
        });

        // 判断该申请件全部质检结论是否都是"无差错"

       setTimeout(function () {
           var allNoneError = false;
           if(checkResList.length > 0){
               for(var i=0; i< checkResList.length; i++){
                   if(checkResList[i]['checkResult'] == 'E_000000' && (checkResList[i]['checkView'] == null || checkResList[i]['checkView'] == '')){
                       allNoneError = true;
                   }else{
                       allNoneError = false;
                       break;
                   }
               }
           }

           if(allNoneError) {
               $("#addQualityOpinoin_checkbox_noError").trigger('click');
           }
       }, 500);
	});


	function initErrorCodeCombobox(){
        //定义差错代码属性
        $('.errorCode').combobox({
            prompt:'差错代码',
            required:false,
            url:ctx.rootPath()+'/qualityControlDesk/quryErrorCode',
            editable:true,
            hasDownArrow:true,
            textField:'code',
            valueField:'id',
            filter: function(q, row){
                var opts = $(this).combobox('options');
                return row[opts.textField].indexOf(q) == 0;
            }
        });
	}
		
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
	
	// 添加初审
	function addFirstAudit(obj) {
		//根据质检件id查询全部质检结论和质检意见checkResList		
		//获取当前初审人员applist
		//遍历该applist
		//根据applist[i]跟checkResList进行匹配,获取实体对象进行回显
		var strs = approvalMap['firstList'];
		for(var i = strs.length-1;i >= 0; i--){
			if(null == strs[i] || strs[i] ==""){
				return;
			}
			var nameKey = 'XSCS'+strs[i];
			var name = approvalMap[nameKey];
			var html = $('<table class="table_ui W100">'
					+'<tr>'
					+'<td>'
					+'<input type="hidden" name="firstCheckError" value="apply-check/'+strs[i]+'"><label id="addQualityOpi_firstCheckError_label'+strs[i]+'" class="hand">&nbsp;'+name+'</label>'
					+'	<input type="radio" name="firstCheckResult'+strs[i]+'" value="E_000004">重大差错'
					+'	<input type="radio" name="firstCheckResult'+strs[i]+'" value="E_000003">一般差错'
					+'	<input type="radio" name="firstCheckResult'+strs[i]+'" value="E_000001">预警'
					+'	<input type="radio" name="firstCheckResult'+strs[i]+'" value="E_000002">建议'
					+'	<input type="radio" name="firstCheckResult'+strs[i]+'" value="E_000000">无差错'
					+'</td>'
					+'</tr>'
					+'<tr>'
					+'<td><input type="text" id="addQualityOpi_firstApp_errorCode'+strs[i]+'" name="firstErrorCode" class="easyui-combobox input errorCode" >'
					+'</td>'
					+'</tr>'
					+'<tr>'
					+'<td><a href="javaScript:void(0);" onclick="diplayNextTr(this)">+&nbsp;</a>质检意见</td>'
					+'</tr>'
					+'<tr>'
					+'<td id="addQualityOpi_firstQuality_chickView'+strs[i]+'"></td>'
					+'</tr>'
					+'<tr>'
					+'<td><textarea id="addQualityOpi_firstApp_chickView'+strs[i]+'" maxlength="1000" class="textarea checkView" name="checkView"></textarea></td>'
					+'</tr>'
					+'</table>');

			$(obj).parents("table").after(html);

			//定义差错代码属性
            $('#addQualityOpi_firstApp_errorCode' + strs[i]).combobox({
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
			
			var checkViewList = filtCheckView(strs[i],'checkPart');//获取对应质检结论
			var qualityCheckRes = null;
			if(checkViewList.length >0){
				qualityCheckRes =  checkViewList[checkViewList.length-1];//获取最后一条质检结论
				var isMe = false;
				if(jobNumber == qualityCheckRes['createdBy'] ){
					isMe = true;
				}
				loadCheckView('#addQualityOpi_firstQuality_chickView'+strs[i],checkViewList,isMe);
				if(isMe == true){//如果是本人的质检结论，则质检意见可编辑
					$('#addQualityOpi_firstApp_chickView'+strs[i]).text(qualityCheckRes['checkView']); 
				}

                // 差错类型设置默认值
				$('input[name="firstCheckResult'+strs[i]+'"][value="'+qualityCheckRes['checkResult']+'"]').prop("checked", "checked");
				diplayNextTr(qualityCheckRes['checkResult'],'addQualityOpi_firstApp_chickView'+strs[i], 'addQualityOpi_firstApp_errorCode'+strs[i],qualityCheckRes['errorCode']);

				// 差错代码下拉框设置默认值
				$('#addQualityOpi_firstApp_errorCode'+strs[i]).combobox("setValue", qualityCheckRes['errorCode']);
			}

			//监听多选变化
			$('input[name="firstCheckResult'+strs[i]+'"]').change(strs[i],function(event) { 
				var finishCheckResult = $(this).val();
				diplayNextTr(finishCheckResult,'addQualityOpi_firstApp_chickView'+event.data, 'addQualityOpi_firstApp_errorCode'+event.data,"");
			});

			//点击事件监听实现可以取消点击
/*			$('input[name="firstCheckResult'+strs[i]+'"]').click(strs[i],function(event){
				 if($(this).attr('checked')){
					 $(this).removeAttr('checked');
					 $('#addQualityOpi_firstApp_chickView'+event.data).hide();
					 $('#addQualityOpi_firstApp_errorCode'+event.data).combobox({  
						 required:false 
					 });
					 $('#addQualityOpi_firstApp_chickView'+event.data).val(""); 
					 
				 }else{
					 $(this).attr('checked','checked');
				 }
			 });*/
		}
	}
	
	// 添加终审
	function addFinalAudit(obj){
		var strs = approvalMap['finishList'];
		for(var i = strs.length-1;i >= 0; i--){
			if(null == strs[i] || strs[i] ==""){
				return;
			}
			var nameKey = 'XSZS'+strs[i];
			var name = approvalMap[nameKey];
			var html = $('<table class="table_ui W100">'
					+'<tr>'
					+'<td>'
					+'<input type="hidden" name="finishCheckError" value="applyinfo-finalaudit/'+strs[i]+'"><label id="addQualityOpi_finishCheckError_label'+strs[i]+'" class="hand">&nbsp;'+name+'</label>'
					+'	<input type="radio" name="finishCheckResult'+strs[i]+'" value="E_000004">重大差错'
					+'	<input type="radio" name="finishCheckResult'+strs[i]+'" value="E_000003">一般差错'
					+'	<input type="radio" name="finishCheckResult'+strs[i]+'" value="E_000001">预警'
					+'	<input type="radio" name="finishCheckResult'+strs[i]+'" value="E_000002">建议'
					+'	<input type="radio" name="finishCheckResult'+strs[i]+'" value="E_000000">无差错'
					+'</td>'
					+'</tr>'
					+'<tr>'
					+'<td><input type="text" id="addQualityOpi_finishApp_errorCode'+strs[i]+'" name="finishErrorCode" class="easyui-combobox input errorCode">'
					+'</td>'
					+'</tr>'
					+'<tr>'
					+'<td><a href="javaScript:void(0);" onclick="diplayNextTr(this)">+&nbsp;</a>质检意见</td>'
					+'</tr>'
					+'<tr>'
					+'<td id="addQualityOpi_finishQuality_chickView'+strs[i]+'"></td>'
					+'</tr>'
					+'<tr>'
					+'<td><textarea id="addQualityOpi_finishApp_chickView'+strs[i]+'" maxlength="1000" class="textarea checkView" name="checkView" ></textarea></td>'
					+'</tr>'
					+'</table>');

			$(obj).parents("table").after(html);

			//定义差错代码属性
            $('#addQualityOpi_finishApp_errorCode' + strs[i]).combobox({
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

			var checkViewList = filtCheckView(strs[i],'finalPart');//获取对应质检结论
			if(checkViewList.length >0){
				var qualityCheckRes =  checkViewList[checkViewList.length-1];//获取最后一条质检结论
				var isMe = false;
				if(qualityCheckRes){
					if(jobNumber == qualityCheckRes['createdBy'] ){
						isMe = true;
					}
				}
				loadCheckView('#addQualityOpi_finishQuality_chickView'+strs[i],checkViewList,isMe);
				if(isMe == true){//如果是本人的质检结论，则质检意见可编辑
					$('#addQualityOpi_finishApp_chickView'+strs[i]).text(qualityCheckRes['checkView']); 
				}

				// 差错类型设置默认值
				$('input[name="finishCheckResult'+strs[i]+'"][value="'+qualityCheckRes['checkResult']+'"]').prop("checked", "checked");//根据value选择radio的默认选项

				diplayNextTr(qualityCheckRes['checkResult'],'addQualityOpi_finishApp_chickView'+strs[i], 'addQualityOpi_finishApp_errorCode'+strs[i],qualityCheckRes['errorCode']);

				// 差错代码下拉框设置默认值
				$('#addQualityOpi_finishApp_errorCode'+strs[i]).combobox("setValue", qualityCheckRes['errorCode']);
			}
			
			//监听多选变化
			$('input[name="finishCheckResult'+strs[i]+'"]').change(strs[i],function(event) { 
				var finishCheckResult = $(this).val();
				diplayNextTr(finishCheckResult,'addQualityOpi_finishApp_chickView'+event.data, 'addQualityOpi_finishApp_errorCode'+event.data,"");
			});

			//点击事件监听实现可以取消点击
/*			$('input[name="finishCheckResult'+strs[i]+'"]').click(strs[i],function(event){
				 if($(this).attr('checked')){
					 $(this).removeAttr('checked');
					 $('#addQualityOpi_finishApp_chickView'+event.data).hide();
					 $('#addQualityOpi_finishApp_errorCode'+event.data).combobox({  
					       required:false 
					 });
					 $('#addQualityOpi_finishApp_chickView'+event.data).val(""); 
				 }else{
					 $(this).attr('checked','checked');
				 }
			 });*/
		}
	}
	
	//回显领导数据
	function addLeaderAudit(){
		var isMe = false;
		var checkViewList = filtLeaderView();
		if(checkViewList.length >0){
			var qualityCheckRes =  checkViewList[checkViewList.length - 1];//获取最后一条质检结论
			if(jobNumber == qualityCheckRes['createdBy'] ){
				isMe = true;
			}
			var approvePerson = qualityCheckRes['approvePerson'];
			var checkResult = qualityCheckRes['checkResult'];
			var errorCode = qualityCheckRes['errorCode'];
			$('#addQualityOpi_leader_approvePerson').combobox("setValue", approvePerson);
//			var a = $('#addQualityOpi_leader_approvePerson').combobox('options');
			$('input[name="leaderCheckResult"][value="'+checkResult+'"]').prop("checked", "checked");//显示差错级别
			if(isMe == true){//如果是本人的质检结论，则质检意见可编辑
				$('#addQualityOpi_leaderApp_checkView').text(qualityCheckRes['checkView']); 
			}
			$('#addQualityOpi_leader_errorCode').attr("value",errorCode);
			diplayNextTr(checkResult, 'addQualityOpi_leaderApp_checkView', 'addQualityOpi_leader_errorCode',errorCode);
			loadCheckView('#addQualityOpinion_leaderAppHistory_checkView',checkViewList,isMe);//显示历史的质检意见
		}
	}
	
	//隐藏显示input输入框
	function diplayNextTr(obj,chickView,errorCodeId,errorCode) {
		
		if(obj){
			$('#'+chickView).show();
		}
		if(obj == 'E_000004' || obj == 'E_000003'){
			$('#'+errorCodeId).combobox({  
			       required:true,
			       disabled:false,
			       value:errorCode
			}); 
		}else{
/*			$('#'+errorCodeId).combobox({  
				required:false, 
				disabled:true
			});*/
			$('#'+errorCodeId).combobox('disable').combobox('clear');
		}
	}
	
	//完成质检
	function completeQuality(flag){

        var $qualityOpinionForm = $('#qualityOpinion__add_Form');

		var saveDataAry=[]; 
		var bool = true;
		//初审质检意见对象生成
		$("input[name='firstCheckError']").each(function(){  
			var firstCheckError =$(this).val();
			var firstCheckErrors= firstCheckError.split("/");
			firstCheckError = firstCheckErrors[0];
			var i = firstCheckErrors[1];
			var firstCheckResult = $('input[name="firstCheckResult'+i+'"]:checked').val();
			var firstErrorCode = $('#addQualityOpi_firstApp_errorCode'+i).combobox('getValue');
			var firstCheckView = $('#addQualityOpi_firstApp_chickView'+i).val();
			var labelText = $('#addQualityOpi_firstCheckError_label'+i).text();
			
			if(!firstCheckResult && !$('#addQualityOpinoin_checkbox_noError').is(':checked')){
				$.messager.alert("提示",labelText+ "差错未选择！！","warn"); 
				bool = false;
				return false;
			}
			
			if(firstCheckResult == 'E_000004' || firstCheckResult == 'E_000003'){
				if(!firstErrorCode){
					$.messager.alert("提示", labelText+"差错代码不能为空！！","warn"); 
					bool = false;
					return false;
				}
			}
			
			if(firstCheckResult != 'E_000000' && !$('#addQualityOpinoin_checkbox_noError').is(':checked')){
				if(!firstCheckView){
					$.messager.alert("提示", labelText+"质检意见不能为空！！","warn"); 
					bool = false;
					return false;
				}
			}
			
			var firstCheckPerson =i;
			var dataFirst;
			if($('#addQualityOpinoin_checkbox_noError').is(':checked')){
				dataFirst={"checkResult": "E_000000","checkError": firstCheckError,"approvePerson":firstCheckPerson, "checkPart":"checkPart"};
			}else{
				dataFirst = {"checkResult": firstCheckResult, "checkError": firstCheckError, "errorCode": firstErrorCode, 
						"checkView": firstCheckView, "approvePerson":firstCheckPerson, "checkPart":"checkPart"};
			}
			saveDataAry.push(dataFirst);  
		}); 
		
		if(bool == false){
			return;
		}
		
		//终审质检意见对象生成
		$("input[name='finishCheckError']").each(function(){  
			var finishCheckError =$(this).val();
			var finishCheckErrors= finishCheckError.split("/");
			finishCheckError = finishCheckErrors[0];
			var i = finishCheckErrors[1];
			var finishCheckResult = $('input[name="finishCheckResult'+i+'"]:checked').val();
			var finishErrorCode = $('#addQualityOpi_finishApp_errorCode'+i).combobox('getValue');
			var finishCheckView = $('#addQualityOpi_finishApp_chickView'+i).val();
			var labelText = $('#addQualityOpi_finishCheckError_label'+i).text();
			
			if(!finishCheckResult && !$('#addQualityOpinoin_checkbox_noError').is(':checked')){
				$.messager.alert("提示", labelText+"差错未选择！！","warn"); 
				bool = false;
				return false;
			}
			
			if(finishCheckResult == 'E_000004' || finishCheckResult == 'E_000003'){
				if(!finishErrorCode){
					$.messager.alert("提示", labelText+"差错代码不能为空！！","warn"); 
					bool = false;
					return false;
				}
			}
			
			if(finishCheckResult != 'E_000000' && !$('#addQualityOpinoin_checkbox_noError').is(':checked')){
				if(!finishCheckView){
					$.messager.alert("提示",labelText+"质检意见不能为空！！","warn"); 
					bool = false;
					return false;
				}
			}
			
			var finishCheckPerson =i;
			var dataFinish;
			if($('#addQualityOpinoin_checkbox_noError').is(':checked')){
				dataFinish={"checkResult": "E_000000","checkError": finishCheckError,"approvePerson":finishCheckPerson, "checkPart":"finalPart"};
				
			}else{
				dataFinish = {"checkResult": finishCheckResult, "checkError": finishCheckError, "errorCode": finishErrorCode, 
						"checkView": finishCheckView, "approvePerson":finishCheckPerson, "checkPart":"finalPart"};
			}
			saveDataAry.push(dataFinish);  
		}); 
		
		if(bool == false){
			return;
		}
		//连带领导质检意见生成
		var leaderCheckError = $("input[name='leaderCheckError']").val();
		var leaderCheckResult = $("input[name='leaderCheckResult']:checked").val();

		var leaderErrorCode = $('#addQualityOpi_leader_errorCode').combobox('getValue');
		var leaderCheckView = $('#addQualityOpi_leaderApp_checkView').val();
		var leaderApp = $('#addQualityOpi_leader_approvePerson').combobox('getValue');
		var labelText = $('#addQualityOpi_leaderCheckError_label').text();
		var dataLeader;
		if($('#addQualityOpinoin_checkbox_noError').is(':checked')){
			leaderCheckResult = 'E_000000';
			dataLeader={"checkResult": leaderCheckResult,"checkError": leaderCheckError,"approvePerson":leaderApp, "checkPart":"checkLeaderPart"};
		}else{
			dataLeader = {"checkResult": leaderCheckResult, "checkError": leaderCheckError, "errorCode": leaderErrorCode, 
					"checkView": leaderCheckView, "approvePerson":leaderApp, "approvalLeader":leaderApp, "checkPart":"checkLeaderPart"};
		}
		
		if(!leaderApp){
			$.messager.alert("提示", labelText+"不能为空！！","warn"); 
			bool = false;
			return;
		}
		
		if(!leaderCheckResult && !$('#addQualityOpinoin_checkbox_noError').is(':checked')){
			$.messager.alert("提示", labelText+"差错未选择！！","warn"); 
			bool = false;
			return;
		}
		
		if(leaderCheckResult == 'E_000004' || leaderCheckResult == 'E_000003'){
			if(!leaderErrorCode){
				$.messager.alert("提示", labelText+"差错代码不能为空！！","warn"); 
				bool = false;
				return;
			}
		}
		if(leaderCheckResult != 'E_000000' && !$('#addQualityOpinoin_checkbox_noError').is(':checked')){
			if(!leaderCheckView){
				$.messager.alert("提示", labelText+"质检意见不能为空！！","warn"); 
				bool = false;
				return;
			}
		}
		saveDataAry.push(dataLeader);
		
		
		//验证提交是否能通过
		if(bool == false){
			return;
		}
		
		if('complete' == flag || "savedone" == flag){
			$.ajax({ 
				type:"POST", 
				url:ctx.rootPath()+'/qualityControlDesk/completeQuality/'+loanNo+'/'+flag+'?checkUser='+checkUser,
				dataType:"json",      
				contentType:"application/json",               
				data:JSON.stringify(saveDataAry), 
				success:function(data){
					if(data.type == "SUCCESS"){
						$.messager.alert("提示", data.messages[0],"info");
                        if(opener.opener.refreshDatagrid){
                            opener.opener.refreshDatagrid();
                        }
                        // 关闭质检意见窗口
                        window.setTimeout(function () {
                            // 关闭质检意见窗口
                            window.opener.close();
                        },1000);
					}else{
						$.messager.alert("提示", data.messages[0],"error"); 
					}
				} 
			}); 
		}
		
		if(flag == 'pause'){
			$.ajax({ 
				type:"POST",
				url:ctx.rootPath()+'/qualityControlDesk/pauseQuality/'+loanNo+'?checkUser='+checkUser,
				dataType:"json",      
				contentType:"application/json",               
				data:JSON.stringify(saveDataAry), 
				success:function(data){
					if(data.type == "SUCCESS"){
						$.messager.alert("提示", data.messages[0],"info");
                        // 关闭质检意见窗
                        if(opener.opener.refreshDatagrid){
                            opener.opener.refreshDatagrid();
                        }
                        window.setTimeout(function () {
                            window.opener.close();
                        },1000);
					}else{
						$.messager.alert("提示", data.messages[0],"error"); 
					}
				} 
			}); 
		}
		
		if(flag == 'reCheck'){
			$.ajax({ 
				type:"POST", 
				url:ctx.rootPath()+'/qualityControlDesk/reCheck/'+loanNo+'?checkUser='+checkUser,
				dataType:"json",      
				contentType:"application/json",               
				data:JSON.stringify(saveDataAry), 
				success:function(data){
					if(data.type == "SUCCESS"){
						if("recheck_wait" == data.messages[0]){
//							$.messager.alert("提示", "未找到合适的申请复核人，该件进入等待申请复核接收状态！！","info");
					        $.messager.confirm("提示", "未找到合适的申请复核人，该件进入等待申请复核接收状态！！", function (data) {
					            // 关闭质检意见窗口
					        	if(data){
					        		if(opener.opener.refreshDatagrid){
					        			opener.opener.refreshDatagrid();
					        		}
					        		window.opener.close();
					        	}
					        });
							
						}else{
							$.messager.alert("提示", data.messages[0],"info");
							// 关闭质检意见窗口
							if(opener.opener.refreshDatagrid){
								opener.opener.refreshDatagrid();
							}
							window.setTimeout(function () {
								// 关闭质检意见窗口
								window.opener.close();
							},1000);
						}

					}else{
						$.messager.alert("提示", data.messages[0],"error"); 
					}
				} 
			}); 
		}
	}

	/**
	 * @Desc: 质检附件上传
	 * @Author: lhm
	 * @Date: 2017/5/17 11:35
	 */
	function qualityAttachmentUpload() {
       var $attachUploadWindow = $("#addQuality_quality_attachUpWindow");

	    var ifm = '<iframe src="'+picImgUrl+'?nodeKey='+nodeKey+'&sysName='+sysName+'&operator=' + operator + '&jobNumber=' + jobNumber + '&appNo=' + loanNo +
        '" style="width:100%;height:99.5%;padding:0px;margin:0px;border:0px;display: table"></iframe>';

        $attachUploadWindow.show();
        $attachUploadWindow.dialog({
	        title: "附件上传",
	        modal: true,
	        width: 800,
	        height: 680,
	        content: ifm,
            onClose: function () {
				// 关闭上传附件窗口，刷新附件列表
                $("#addQualityOpinion_quality_attach").datagrid("reload");
            }
	    });
	}
	
	/**
	 * @param id
	 * @param creator
	 * @param createJobnum
	 * 根据ID删除附件
	 */
	function deleteAttachmentById(id, creator, createJobnum){
		
	    $.messager.confirm('确认', '确认删除该附件吗?', function(r){
	        if (r){
	            var api = ctx.rootPath() + '/qualityControlDesk/deleteAttachmentById';
	            var params={id:id,operator:creator,jobNumber:createJobnum,loanNo:loanNo};
	            var callback = function (data) {
	                if (data.type=='SUCCESS') {
	                    $.info("提示",data.firstMessage,"info", 2000);
	                }else{
	                    $.info("提示",data.firstMessage,"warning", 2000);
	                }
                    $("#addQualityOpinion_quality_attach").datagrid("reload");	// 刷新附件列表
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
	        content: ifm
	    });
	}

	function leaderCheckViewListener(){
		$('input[name="leaderCheckResult"]').change(function() {  
			var leaderCheckResult = $(this).val();
			diplayNextTr(leaderCheckResult, 'addQualityOpi_leaderApp_checkView', 'addQualityOpi_leader_errorCode',"");
	    }); 
		
		//点击事件监听实现可以取消点击
/*		$('input[name="leaderCheckResult"]').click(function(){
			 if($(this).attr('checked')){
				 $(this).removeAttr('checked');
				 $('#addQualityOpi_leaderApp_checkView').hide();
				 $('#addQualityOpi_leader_errorCode').combobox({  
					 required:false 
				 });
				 $('#addQualityOpi_leaderApp_checkView').val(""); 
				 
			 }else{
				 $(this).attr('checked','checked');
			 }
		 });*/
	}
	
	/**
	 * 加载质检意见
	 * @author lihuimeng
	 * @date 2017年6月8日 下午6:01:53
	 */
	function loadCheckView(obj,checkViewList,isMe){
//		1、获取质检结论集合
//		2、根据审核人员和审核类型取出
//		3、遍历展出
		var strs = checkViewList;
		var size = strs.length;
		if(isMe == true){
			size = size-1;
		}
		if(size >= 1){
			for(var i = 0;i < size; i++){
				var checkView = "";
				if(strs[i]['checkView']){
					checkView = strs[i]['checkView'];
					var html = $('<textarea class="textarea" disabled="disabled">' + checkView + '</textarea>');
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
	 * 按钮控制
	 * @author lihuimeng
	 * @date 2017年6月13日 上午11:09:09
	 */
	function buttonController(){
		if('todo' == flag){
			$('#addQualityOpinion_button_holdQuality').hide();
		}
		if('updateDone' == flag){//如果是编辑已完成的质检件，则隐藏“完成质检”、“暂存”、“申请复核”按钮
			$('#addQualityOpinion_button_completeQuality').hide();
			$('#addQualityOpinion_button_pauseQuality').hide();
			$('#addQualityOpinion_button_reCheckQuality').hide();
		}
		if('todoOthers' == flag){//如果是当前登录人员处理辖下的质检件，隐藏暂存和保存按钮
			$('#addQualityOpinion_button_pauseQuality').hide();
			$('#addQualityOpinion_button_holdQuality').hide();
		}
		
		if('qualityCheckManager' == roleCode){//如果当前登录人是质检经理角色，隐藏申请复核按钮
			$('#addQualityOpinion_button_reCheckQuality').hide();
		}
		
	}
	
	/**
	 * 控制下拉框disabled
	 * @author lihuimeng
	 * @date 2017年6月13日 上午11:09:09
	 */
	function comBoxController(id,bool){
		$(id).combobox({
			disabled:bool, 
			value:"",
		});
	}
	//如果当前操作人是普通质检员，则领导显示“组长”别称，并不可编辑
	function setTextField(roleCode){
		if('qualityCheck' == roleCode){
			$('#addQualityOpi_leader_approvePerson').combobox('disable');
			var a = $('#addQualityOpi_leader_approvePerson').combobox('options');
		}
	}
	
