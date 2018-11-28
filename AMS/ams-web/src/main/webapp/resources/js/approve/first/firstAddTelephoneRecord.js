var vm;
function pageInit(option) {
    var defaultOption = {
		$id: 'firstAddTelPage',
		contactRelation: {
			'00000':'本人',
    		'00001':'父母',
    		'00002':'子女',
    		'00003':'兄弟',
    		'00004':'姐妹',
    		'00005':'兄妹',
    		'00006':'姐弟',
    		'00007':'朋友',
    		'00008':'同事',
    		'00009':'房东',
    		'00010':'亲属',
    		'00011':'同学',
    		'00012':'其他',
    		'00013':'配偶'
    	},
    	getContactRelation: function(code){
			return vm.contactRelation[code];
		}
    };
    var model = $.extend({}, defaultOption, option);
    vm = avalon.define(model);
    avalon.scan(document.body);
}

$(function() {
	$('#telRelationType').combobox({
		onChange : function(n, o) {
			if ('无人接听' == n &&  !isNotNull($('#askContent').textbox('getValue'))) {
				$('#askContent').textbox('setValue', '无人接听')
			}
		}
	});
});

var fisrtAddTeleSumbit = false;
function addTelephoneRecord(title) {
	if (!fisrtAddTeleSumbit) {
		var validata = $("#addPhoneRecordForm").form('validate');
		if (validata) {
			fisrtAddTeleSumbit = true;
			if ("新增电核记录" == title) {
				var params = $("#addPhoneRecordForm").serializeObject();
				post(ctx.rootPath() + '/firstTelephoneSummary/addTelephoneRecord', params, 'json', function(data) {
					if (data.success) {
						$.info("提示", '新增电核记录成功！', "info", 1000, function() {
							closeHTMLWindow();
						});
					} else {
						$.info("error", data.firstMessage);
					}
				});
			} else if ("修改电核记录" == title) {
				var createDate = $('#createdDate').val();
				var params = $("#addPhoneRecordForm").serializeObject();
				post(ctx.rootPath() + '/firstTelephoneSummary/updateTelephoneRecord', params, 'json', function(data) {
					if (data.success) {
						// 关闭当前对话框
						// $.info('提示', '修改电核记录成功！')
						closeHTMLWindow();
					} else {
						$.info("提示", data.message, "error");
					}
				})
			}
		}
	}
}