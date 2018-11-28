$('#calendarMaintain_Calendar').calendar({
	onSelect: function(date){
		calendarMaintain_ShowDialog(date);
	}
});

function calendarMaintain_ShowDialog(date) {
	$("#calendarMaintain_AddOrUpdate_dialog").removeClass("display_none").dialog({
		title : '日历设置',
		width : 800,
		modal : true,
		onBeforeOpen : function() {
			$("#calendarMaintain_date").textbox('setValue',date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate());
		},
		buttons : [{
			text : '确认',
			iconCls : 'fa fa-arrow-up',
			handler : function(){
				
			}
		},{
			text : '返回',
			iconCls : 'fa fa-reply',
			handler : function(){
				$("#calendarMaintain_AddOrUpdate_dialog").dialog("close");
			}
		}]
	});
}