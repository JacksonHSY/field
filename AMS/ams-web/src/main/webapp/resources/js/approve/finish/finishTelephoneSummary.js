var vm;

function pageInit(option) {
    var defaultOption = {
        $id: 'page',
        qualityPersonMap: {},
        businessType: '',
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
    	getcontactRelation: function(code){
			return vm.contactRelation[code];
		}
    };
    var model = $.extend({}, defaultOption, option);
    vm = avalon.define(model);
    avalon.scan(document.body);
}

avalon.ready(function () {
    // 初始化电核记录datagrid
    initfinishTelephoneSummary_phoneRecord_table($("#loanNo_hidden").val());
});

/**
 * 初始化电核记录datagrid
 *
 * @Author Shipf
 * @date
 */
function initfinishTelephoneSummary_phoneRecord_table(loanId) {
    $("#finishTelephoneSummary_phoneRecord_table").datagrid({
        url: ctx.rootPath() + '/firstApprove/telephoneHistory',
        striped: true,
        singleSelect: true,
        idField: 'id',
        pagination: true,
        pageSize : 20,
        fit : true,
        queryParams: {
            loanNo: loanId
        },
        onLoadSuccess: function () {
            $("#finishTelephoneSummary_phoneRecord_table").datagrid('resize');
            $(".easyui-tooltip").tooltip({
                onShow: function () {
                    $(this).tooltip('tip').css({
                        borderColor: '#000'
                    });
                }
            });
        },
        fitColumns: true,
        scrollbarSize: 0,
        columns: [[{
            field: 'telDate',
            title: '核查时间',
//            width: 120,
            formatter: formatDateYMDHS,
        }, {
            field: 'name',
            title: '姓名',
//            width: 90,
        }, {
            field: 'nameTitle',
            title: '关系',
//            width: 90,
        }, {
            field: 'telPhoneType',
            title: '电话类型',
            hidden: true,
//            width: 90,
        }, {
            field: 'telPhone',
            title: '电话号码',
//            width: 125,
            formatter: function (value, data, index) {
                if (value != null) {
                    if (value != null) {
                        return "<label title=\"" + value + "\" class=\"easyui-tooltip\">" + value + "</label>";
                    }
                }
            }
        }, {
            field: 'telRelationType',
            title: '与接听人关系',
//            width: 90,
        }, {
            field: 'askContent',
            title: '电核备注',
            formatter: function (value, row, index) {
                var showText = "";
                var sourceText = value.split("");
                $.each(sourceText, function (index, val) {
                    showText = showText + val;
                    if (Number(index + 1) % 30 == 0) {
                        showText = showText + "<br>";
                    }
                })
                showText = '<span title="' + value + '"  class="easyui-tooltip">' + showText + '</span>'
                return showText;
            }
        }, {
            field: 'createdBy',
            title: '创建人',
//            width: 90,
            hidden: vm.businessType == 'quality' ? true : false
        }, {
            field: 'createdByCode',
            title: '创建人',
//            width: 90,
            hidden: vm.businessType == 'quality' ? false : true,
            formatter : function(value, row, index) {
                var nameKeyFirst = 'XSCS'+value;
                var nameKeyFinish = 'XSZS'+value;
                var name = '';
                if(isNotNull(vm.qualityPersonMap[nameKeyFirst])){
                    name = vm.qualityPersonMap[nameKeyFirst];
                }

                if(isNotNull(vm.qualityPersonMap[nameKeyFinish])){
                    name = vm.qualityPersonMap[nameKeyFinish];
                }
                return name;
            }
        }]]
    });
}

// 百度地图
function baiduMap(province, city, zone, address) {
    if (isNotNull(province) && isNotNull(city) && isNotNull(zone) && isNotNull(address)) {
        var location = province + " " + city + " " + zone + " " + address;
        window.open("http://api.map.baidu.com/geocoder?address=" + location + "&output=html&src=yourCompanyName")
    }
}
/**
 * 家庭地址到工作地址路线
 * @param homeProvince-家所在省
 * @param homeCity-家所在市
 * @param homeZone-家所在区
 * @param homeAddress-详细地址
 * @param province-公司所在省
 * @param city-公司所在市
 * @param zone-公司所在区
 * @param address-公司详细地址
 */
function showMapFromHomeToCompany(homeProvince, homeCity, homeZone, homeAddress,province, city, zone, address) {
    var notNull =isNotNull(homeProvince) && isNotNull(homeCity) && isNotNull(homeZone) && isNotNull(homeAddress) &&  isNotNull(province) && isNotNull(city) && isNotNull(zone) && isNotNull(address);
    if (notNull) {
        var home = homeProvince + " " + homeCity + " " + homeZone + " " + homeAddress;
        var name = homeProvince + homeCity + homeZone + homeAddress;
        var company = province + " " + city + " " + zone + " " + address;
        window.open("http://api.map.baidu.com/direction?origin="+home+"|name:" + name + "&destination="+company+"&mode=driving&region="+homeProvince+"&output=html&src=yourCompanyName");
    }
}

/**
 * 百度搜索
 * @param mobile
 */
function seachForBaidu(mobile, obj){
	/*//第三方电话修改后，取值还是获取的默认值
	 // 终审不能修改
	if(isNotNull(obj) && $(obj).parents("tr").hasClass("mobileHistoryInfo")){
		mobile = $(obj).parents("tr").children().eq(3).find("input").val();// 第三方手机号码
	}*/
	
	//固定电话处理
	if(mobile.indexOf("-") >= 0){
		console.info(mobile);
        var arrays = new Array();
        arrays = mobile.split("-");
        window.open('http://www.baidu.com/s?wd=' + arrays[0] + "-" +arrays[1], '_blank', 'height=700,width=911,scrollbars=yes,status =yes,toolbar=yes,location=yes,titlebar=yes');
		window.open('http://www.baidu.com/s?wd=' + arrays[0] + " "+arrays[1], '_blank', 'height=700,width=911,scrollbars=yes,status =yes,toolbar=yes,location=yes,titlebar=yes');
		window.open('http://www.baidu.com/s?wd=' + arrays[0] + arrays[1], '_blank', 'height=700,width=911,scrollbars=yes,status =yes,toolbar=yes,location=yes,titlebar=yes');
	}else {
        window.open('http://www.baidu.com/s?wd=' + mobile, '_blank', 'height=700,width=911,scrollbars=yes,status =yes,toolbar=yes,location=yes,titlebar=yes');
	}
}

/**
 * 百度批量搜索电话号码
 * @param mobile
 */
function seachBaidu(){
	var rows = $("#contact_phone_table").find("tr").length; //行数
	var cells = $("#contact_phone_table").find("tr").find("td").length; //列数
	console.info("row="+rows);
	console.info("cell="+cells);
	var phone;
	$("#contact_phone_table tr").each(function(){
		//$(this).children().eq(3).text()!='关系'
		//排除第一行
		if($(this).children().eq(0).text()!='关系'){
			// 判断是否存在合并
			if ($(this).hasClass("merge")) {
				phone = $(this).children().eq(3).text();// 手机号码
			} else if ($(this).hasClass("mobileHistoryInfo")){
				phone = $(this).children().eq(3).find("input").val();// 第三方手机号码
			}else {
				phone = $(this).children().eq(1).text();// 手机号码
			}
			
			//固定电话处理
			if(phone.indexOf("-") >= 0){
	            var arrays = new Array();
	            arrays = phone.split("-");
	            window.open('http://www.baidu.com/s?wd='+arrays[0] + "-" +arrays[1]);
		        window.open('http://www.baidu.com/s?wd='+arrays[0] + " " + arrays[1]);
		        window.open('http://www.baidu.com/s?wd='+arrays[0] + arrays[1]);
			}else{
				window.open('http://www.baidu.com/s?wd='+phone);
			}
		}
	})
}
