<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
  <head>
    
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 	<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css"> -->
	<link rel="stylesheet" href="${ctx}/resources/easyui/themes/default/easyui.css" type="text/css"></link>
    <link rel="stylesheet" href="${ctx}/resources/easyui/themes/icon.css" type="text/css"></link>
	<script src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="${ctx}/resources/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${ctx}/resources/easyui/jquery.cookie.js"></script>
	<script type="text/javascript" src="${ctx}/resources/easyui/locale/easyui-lang-zh_CN.js"></script>
	
	<script>	
	
		//编辑的行
		var editRow = null;
		
		$(function(){	

			//添加
			function addData(){
				if(editRow!=null){
	  				$dgg.datagrid('endEdit',editRow);		  					 					
	  			}	  			  			
	  			if(editRow==null){
	  				$dgg.datagrid('insertRow',{
			  			index: 0,	// 索引从0开始
			  			row: {
// 							id:0,
							operation:'add row',
							requestContent:'请求内容',
							responseContent:'响应内容',												
							requestDate:'requestDate',												
							responseDate:'responseDate',												
							ip:'ip'												
			  			}
			  		});	
			  		
	  				editRow=0;
	  				$dgg.datagrid('beginEdit',editRow);	
	  			}	  
			}		

			//DataGrid
			var $dgg = $('#dg').datagrid({    
			  	//data:{}
		  	    url:'${ctx}/loadDataGrid',  //要有url点击翻页才会发请求
		  	    striped:true,		//隔行变色
		  	    fitColumns:false,	//匹配列宽，false使用固定值
		  	    fit:false,			//匹配父容器，panel属性
		  	    nowrap:true,
		  	    pagination:true,
		  	  	pagePosition:'both',		  	    
		  	    pageSize:5,
		  	    pageList:[5,10,15,20,30],
		  	    pageNumber:1,	
		  	    sortName:'id',
		  	    sortOrder:'desc', 
		  	    //queryParams: {
					//date: new Date()				
				//},		  	    
		  	    //singleSelect:true, 
		  	    frozenColumns:[[
					{field:'id',title:'序号',width:100,sortable:true,styler:function(value){
						if(value<6){
							return  'color:red;';
						}else{
							return  'color:blue;';
						}
					  }},    
					  {field:'operation',title:'操作',width:100,
					  	formatter:function(value,row){
					  	  	return '<span title="'+value+'">'+value+'</span>';
					  	},
					      editor:{
					      	type:'validatebox',
					      	options:{required:true}
					      }
					  } 
			  	]],    
		  	    columns:[[ 		  	           
		  	        {field:'requestContent',title:'请求内容',width:100,
			  	        editor:{
			  	        	type:'validatebox',
			  	        	options:{required:true}
		  	            }
		  	        }, 
		  	        {field:'responseContent',title:'响应内容',width:200,
			  	        formatter:function(value,row){
								if (row.responseContent){
									return "<span title='"+row.responseContent+"'>"+row.responseContent+"</span>";
								} else {
									return value;
								}
						},editor:{
			  	        	type:'text',
			  	        	options:{required:true}
		  	            }			  	       
			  	    },
			  	    {field:'requestDate',title:'请求时间',width:400,editor:{type:'datebox',options:{}}},
			  	    {field:'responseDate',title:'响应时间',width:400,editor:{type:'datebox',options:{}}},
			  	    {field:'ip',title:'IP地址',width:400,editor:{type:'validatebox',options:{required:true}}}
		  	    ]],
			  	toolbar: [{
				  	text: '添加',
			  		iconCls: 'icon-add',
			  		handler: function(){			  										  			
			  			addData();			  			  								
			  		}
			  	},'-',{
			  		text: '编辑',
			  		iconCls: 'icon-edit',
			  		handler: function(){
			  			var rows = $dgg.datagrid('getSelections');
			  			if(rows.length==1){
			  				if(editRow!=null){
								$dgg.datagrid('endEdit',editRow);
							}
			  				if(editRow==null){			  		
								var index = $dgg.datagrid('getRowIndex',rows[0]);
								editRow = index;
								$dgg.datagrid('beginEdit',editRow);	
								$dgg.datagrid('unselectAll');
			  				}
							
			  			}else{
							$.messager.alert('提示','请选择要编辑的一行');
			  			}							
			  		}
			  	},'-',{
				  	text: '删除',
			  		iconCls: 'icon-remove',
			  		handler: function(){
			  			var rows = $dgg.datagrid('getSelections');
			  			if(rows.length>0){
			  				if(editRow!=null){
								$dgg.datagrid('endEdit',editRow);
							}
							var ids = [];
							$(rows).each(function(i){								
								//ids[i]=(rows[i].id);
								ids.push(rows[i].id);
							});
							console.info(ids);	

							$.messager.confirm('确定','您确定要删除选定的记录？',function(r){
								if(r){
									$.ajax({
										url:'${ctx}/deleteSystemLog',
										data:{ids:ids.join(',')},
										dataType:'json',
										success:function(r){
											if(r.flag){
												$.messager.show({
													title:'消息',
													msg:r.message
												});
												$dgg.datagrid('load');	
												$dgg.datagrid('unselectAll');											
											}else{
												$.messager.alert('消息',r.message);												
											}											
											editRow = null;
										}
									});
									
								}
							});						
							
			  			}else{
							$.messager.alert('提示','请选择要删除的行','warning');
			  			}
			  		}
			  	},'-',{
				  	text: '保存',
			  		iconCls: 'icon-save',
			  		handler: function(){
						if(editRow!=null){
							$dgg.datagrid('endEdit',editRow);
						}											
			  		}
			  	},'-',{
				  	text: '取消编辑',
			  		iconCls: 'icon-back',
			  		handler: function(){
			  			/*
						if(editRow!=null){
							$dgg.datagrid('endEdit',editRow);
							editRow = null;
						}*/
						$dgg.datagrid('rejectChanges');
						$dgg.datagrid('unselectAll');
						editRow=null;
			  		}
			  	}],
			  	//结束编辑时触发
			  	onAfterEdit:function(rowIndex,rowData,change){
					console.info(rowData);
										
					var insert = $dgg.datagrid('getChanges','inserted');
					var update = $dgg.datagrid('getChanges','updated');

					var url = '';
					if(insert.length>0){
						url='${ctx}/addSystemLog';
					}
					if(update.length>0){
						url='${ctx}/updateSystemLog';
					}
					console.info("url:"+url);
					console.info(rowData);
					$.ajax({
						url:url,
						data:{"param":JSON.stringify(rowData)}, //{id:rowData.id}
						dataType:'json',
						type:'post',
						success:function(r){
							$.messager.show({
								title:'消息',
								msg:r.message
							});
							$dgg.datagrid('acceptChanges');
							
							editRow = null;
						}
					});					
										
				},
			  	//双击编辑
			  	onDblClickRow:function(rowIndex,rowData){
			  		if(editRow!=null){
						$dgg.datagrid('endEdit',editRow);
					}
					if(editRow==null){
						$dgg.datagrid('beginEdit',rowIndex);
						var ed = $dgg.datagrid('getEditor', {index:rowIndex,field:'msg.message'});						
						$(ed.target).val(rowData.msg.message);										

				  		editRow = rowIndex;	
				  		$dgg.datagrid('unselectAll');
					}
			  		
				},
				//右键菜单				
				onRowContextMenu:function(e, rowIndex, rowData){
					e.preventDefault();
					$dgg.datagrid('unselectAll');
					//右键选中行
					$dgg.datagrid('selectRow',rowIndex);
					//弹出右键菜单
					$('#mm').menu('show', {    
						  left: e.pageX,    
						  top: e.pageY    
					});
				}				
		  		 	        
		  	}); 

			//菜单点击事件			
			$('#mm').menu({    
			    onClick:function(item){    
			        if(item.name=='add'){
			        	addData();
			        }else if(item.name=='edit'){			        	
			        	var rows = $('#dg').datagrid('getSelections');
			        	var index = $('#dg').datagrid('getRowIndex',rows[0]);
			        	
						editRow = index;
						//alert(index);						
						var ed = $('#dg').datagrid('getEditor', {index:editRow,field:'houseId'});						
						//alert(ed);// null??
						//$(ed.target).val(rows[0].msg.message);
						$('#dg').datagrid('beginEdit',editRow);	
						$('#dg').datagrid('unselectAll');
			        }else{
						alert('remove');
			        } 
			    }    
			});

			//将form表单元素序列化成对象
			serializeToObject=function(form){
				var o = {};
				$.each(form.serializeArray(),function(index){
					if(o[this['name']]){
						o[this['name']]=o[this['name']]+','+this['value'];
					}else{
						o[this['name']]=this['value'];
					}
				});
				console.info("对象="+o);
				return o;
			}

			//查询
			$('#qry').click(function(){				
				//alert($('#fm').serialize());
				//alert($('#fm').serializeArray());
				//console.info($('#fm').serializeArray());				
				//console.info(serializeToObject($('#fm')));
				$('#dg').datagrid('load',serializeToObject($('#fm')));				
			}); 
				
		});		
	  	
  	</script>
  </head>
  
  <body>  
    <form id='fm'>
	  	编号：<input type="text" id='id' name="id" />
		用户名：<input type="text" id='username' name="username" />
		<input id='qry' type="button" value="查询"  />
		<input id='btn' type="button" value="清空"  />
	</form>
	
    <table id="dg"></table>
    
     <!-- 右键菜单 -->
     <div id="mm" class="easyui-menu" style="width:120px;display:none;">   
	    <div data-options="name:'add',iconCls:'icon-add'">添加</div>  
	    <div data-options="name:'edit',iconCls:'icon-edit'">编辑</div> 
	    <div data-options="name:'remove',iconCls:'icon-remove'">删除</div>     
	 </div>  
     
    
  </body>
</html>
