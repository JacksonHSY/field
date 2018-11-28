var vm;
function pageInit(option) {
    var defaultOption = {
        $id: 'page',
        loanNo: '',
        oldApplyInfo: {},
        applyInfo: {},
        updateVersion: false,
        assetsList: [],
        save: save,
        assetsPanelShow: assetsPanelShow,
        assetsIsRequired: assetsIsRequired,
        isDirectApp: false,// 标记是否是直通车进件并且是TOPUP类型或RELOAN类型(用于判断配偶外籍人士身份证不必填)
        flagValidate:false,// 标记是否是直通车进件并且是TOPUP类型或RELOAN类型三个月结清结清精确到月(用于判断资产信息不必填)
        $computed:{
            showPrivate: function () {    // 是否显示私营业主信息
                return vm.applyInfo.basicInfoVO.workInfoVO.cusWorkType != '00002';
            },
            isPrivateOwner: function () {   // 客户工作类型是否为"私营业主"
                return vm.applyInfo.basicInfoVO.workInfoVO.cusWorkType == '00001';
            },
            birthAddress: function () {     // 户籍地址
                var provinceId = vm.applyInfo.basicInfoVO.personInfoVO.issuerStateId;
                var cityId = vm.applyInfo.basicInfoVO.personInfoVO.issuerCityId;
                var areaId = vm.applyInfo.basicInfoVO.personInfoVO.issuerZoneId;
                var other = vm.applyInfo.basicInfoVO.personInfoVO.idIssuerAddress;

                return other + provinceId + cityId + areaId;
            },
            familyAddress: function () {    // 家庭住址
                var provinceId = vm.applyInfo.basicInfoVO.personInfoVO.homeStateId;
                var cityId = vm.applyInfo.basicInfoVO.personInfoVO.homeCityId;
                var areaId = vm.applyInfo.basicInfoVO.personInfoVO.homeZoneId;
                var other = vm.applyInfo.basicInfoVO.personInfoVO.homeAddress;

                return other + provinceId + cityId + areaId;
            }
        }
    };
    var model = $.extend({}, defaultOption, option);
    vm = avalon.define(model);
    avalon.scan(document.body);
}

avalon.ready(function () {
    if (vm.updateVersion) {
        $.messager.alert({
            title: '提示',
            msg: '您当前办理的借款单有可能已经被改派,请重新办理!',
            icon: 'warning',
            onClose: function () {
                closeHTMLWindow();
            }
        });
    }

    // 获取省市级联信息
    post(ctx.rootPath() + "/bmsBasiceInfo/getProvinceCorrespond", null, null, function (data) {
        // 个人信息地址
        inintArea(data, "#firstCustomerInfo_birthplace_province_combobox", "#firstCustomerInfo_birthplace_city_combobox", "#firstCustomerInfo_birthplace_country_combobox", addressSynchronization);
        // 家庭地址
        inintArea(data, "#firstCustomerInfo_family_province_combobox", "#firstCustomerInfo_family_city_combobox", "#firstCustomerInfo_family_country_combobox",changeFamily);
        // 单位地址
        inintArea(data, "#firstCustomerInfo_company_province_combobox", "#firstCustomerInfo_company_city_combobox", "#firstCustomerInfo_company_country_combobox");
        // 房产信息
        inintArea(data, "#firstCustomerInfo_house_province_combobox", "#firstCustomerInfo_house_city_combobox", "#firstCustomerInfo_house_country_combobox");
    });

    // 初始化个人信息、联系人信息、资产信息的所有控件校验规则
    addRule();

    // 单位月收入
    $('#monthSalary').numberbox({
        precision: 0,
        buttonText: '元',
        onChange: function (newValue, oldValue) {
            // 计算月总收入
            var monthSalary = $('#monthSalary').numberbox('getValue');
            var otherIncome = $('#otherIncome').numberbox('getValue');
            var total = math.chain(monthSalary).add(otherIncome).done();
            $('#firstCustomerInfo_customerWork_totalMonthSalary').val(total.toFixed(0));
            $("#firstCustomerInfo_customerWork_totalMonthSalary_span").number(total.toFixed(0), 0);
        }
    });

    // 其他月收入
    $('#otherIncome').numberbox({
        precision: 0,
        buttonText: '元',
        onChange: function (newValue, oldValue) {
            // 计算月总收入
            var monthSalary = $('#monthSalary').numberbox('getValue');
            var otherIncome = $('#otherIncome').numberbox('getValue');
            var total = math.chain(monthSalary).add(otherIncome).done();
            $('#firstCustomerInfo_customerWork_totalMonthSalary').val(total.toFixed(0));
            $("#firstCustomerInfo_customerWork_totalMonthSalary_span").number(total.toFixed(0), 0);
        }
    });

    // 计算月总收入
    var monthSalary = $('#monthSalary').numberbox('getValue');
    var otherIncome = $('#otherIncome').numberbox('getValue');
    var totalIncome = math.chain(monthSalary).add(otherIncome).done();
    $('#firstCustomerInfo_customerWork_totalMonthSalary').val(totalIncome.toFixed(0));
    $("#firstCustomerInfo_customerWork_totalMonthSalary_span").number(totalIncome.toFixed(0), 0);

    // 资产信息中公积金缴存基数根据月缴存额和缴存比例自动赋值
    $('#first_customerInfo_assetsInfo_providentInfo_depositRate').numberbox({
        onChange: function (newValue, oldValue) {
            var depositBase = 0;
            if (isNotNull(newValue) && '0.00' != newValue) {
                var monthDepositAmt = $('#first_customerInfo_assetsInfo_providentInfo_monthDepositAmt').numberbox('getValue');
                if (isNotNull(monthDepositAmt)) {
                    depositBase = (monthDepositAmt / newValue) * 100;
                    $('#first_customerInfo_assetsInfo_providentInfo_depositBase').numberbox('setValue', depositBase.toFixed(0));
                }
            }
        }
    });

    $('#first_customerInfo_assetsInfo_providentInfo_monthDepositAmt').numberbox({
        onChange: function (newValue, oldValue) {
            var depositBase = 0;
            if (isNotNull(newValue)) {
                var depositRate = $('#first_customerInfo_assetsInfo_providentInfo_depositRate').numberbox('getValue');
                if (isNotNull(depositRate) && '0.00' != depositRate) {
                    depositBase = (newValue / depositRate) * 100;
                    $('#first_customerInfo_assetsInfo_providentInfo_depositBase').numberbox('setValue', depositBase.toFixed(0));
                }
            }
        }
    });

    // 资产信息房产信息时选择房产信息同住宅信息
    $("[name='estateSameRegistered']").on("change", function (e) {
        var yesOrNo = $(e.target).val();
        if (yesOrNo == 'Y') {
            // 选择是时，房产地址回填家庭地址内容，显示为文本不可编辑
            var homeProv = $("#firstCustomerInfo_family_province_combobox").combobox("getValue");// 住宅省
            var homeCity = $("#firstCustomerInfo_family_city_combobox").combobox("getValue");// 住宅市
            var homeCoun = $("#firstCustomerInfo_family_country_combobox").combobox("getValue");// 住宅区
            var homeAddr = $("#firstCustomerInfo_family_addr").val();// 家庭地址

            //验证家庭地址是否完整
            if (isEmpty(homeProv) || isEmpty(homeCity) || isEmpty(homeCoun) || isEmpty(homeAddr)) {
                $.info("提示", "家庭地址不完整，请先填写完整家庭地址!", "warning");
                $("[name='estateSameRegistered']").get(0).checked = false;
                return;
            }

            $("#firstCustomerInfo_house_province_combobox").combobox('setValue', homeProv);
            $("#firstCustomerInfo_house_city_combobox").combobox("setValue", homeCity);
            $("#firstCustomerInfo_house_country_combobox").combobox("setValue", homeCoun);
            $("#first_customerInfo_estateAddress").textbox('setValue', homeAddr);

            $('#firstCustomerInfo_house_province_combobox').combobox("readonly", true);
            $('#firstCustomerInfo_house_city_combobox').combobox("readonly", true);
            $('#firstCustomerInfo_house_country_combobox').combobox("readonly", true);
            $('#first_customerInfo_estateAddress').textbox("readonly", true);
        } else {
            // 选择否时，省、市、区/县为下拉框，详细地址为文本框，可编辑
            $("#firstCustomerInfo_house_province_combobox").combobox("setValue", "");
            $("#firstCustomerInfo_house_city_combobox").combobox("setValue", "");
            $("#firstCustomerInfo_house_country_combobox").combobox("setValue", "");
            $("#first_customerInfo_estateAddress").textbox('setValue', '');

            $('#firstCustomerInfo_house_province_combobox').combobox("readonly", false);
            $('#firstCustomerInfo_house_city_combobox').combobox("readonly", false);
            $('#firstCustomerInfo_house_country_combobox').combobox("readonly", false);
            $('#first_customerInfo_estateAddress').textbox("readonly", false);
        }
    });

    // 家庭地址同户籍地址
    $("[name='homeSameRegistered']").on("change", function (e) {
        var yesOrNo = $(e.target).val();
        if (yesOrNo == '0') {
            // 选择是时，房产地址回填家庭地址内容，显示为文本不可编辑,如果户籍地址填写不全弹窗提示
            var idIssuerProvince = $("#firstCustomerInfo_birthplace_province_combobox").combobox("getValue");// 户籍省
            var idIssuerCity = $("#firstCustomerInfo_birthplace_city_combobox").combobox("getValue");// 户籍市
            var idIssuerCountry = $("#firstCustomerInfo_birthplace_country_combobox").combobox("getValue");// 户籍区
            var idIssuerAddress = $("#firstCustomerInfo_birthplace_idIssuerAddress").val();// 户籍地址

            if (isEmpty(idIssuerProvince) || isEmpty(idIssuerCity) || isEmpty(idIssuerCountry) || isEmpty(idIssuerAddress)) {
                $.info("提示", "户籍地址不完整，请先填写完整户籍地址!", 'warning');
                $("[name='homeSameRegistered']").get(1).checked = true;
                return;
            }

            $("#firstCustomerInfo_family_province_combobox").combobox('setValue', idIssuerProvince);
            $("#firstCustomerInfo_family_city_combobox").combobox("setValue", idIssuerCity);
            $("#firstCustomerInfo_family_country_combobox").combobox("setValue", idIssuerCountry);
            $("#firstCustomerInfo_family_addr").textbox('setValue', idIssuerAddress);

            $('#firstCustomerInfo_family_province_combobox').combobox("readonly", true);
            $('#firstCustomerInfo_family_city_combobox').combobox("readonly", true);
            $('#firstCustomerInfo_family_country_combobox').combobox("readonly", true);
            $('#firstCustomerInfo_family_addr').textbox("readonly", true);
        } else {
            // 选择否时，省、市、区/县为下拉框，详细地址为文本框，可编辑
            $("#firstCustomerInfo_family_province_combobox").combobox("setValue", "");
            $("#firstCustomerInfo_family_city_combobox").combobox("setValue", "");
            $("#firstCustomerInfo_family_country_combobox").combobox("setValue", "");
            $("#firstCustomerInfo_family_addr").textbox('setValue', '');

            $('#firstCustomerInfo_family_province_combobox').combobox("readonly", false);
            $('#firstCustomerInfo_family_city_combobox').combobox("readonly", false);
            $('#firstCustomerInfo_family_country_combobox').combobox("readonly", false);
            $('#firstCustomerInfo_family_addr').textbox("readonly", false);
        }

        $("#firstCustomerInfo_birthplace_idIssuerAddress").textbox("validate");
        $("#firstCustomerInfo_family_addr").textbox("validate");
    });

    // 配偶为外籍的身份证号码校验
    $("[name='ifForeignPenple']").on("change", function (e) {
        changeCardType(e.target);
    });

    // 回显房产地址radio
    var isEqual = $("#isEqualHomeAddr").val();
    if ('Y' == isEqual) {
        $("input[name='estateSameRegistered']").eq(0).attr("checked", "checked");
        $("input[name='estateSameRegistered']").eq(1).removeAttr("checked");
        $('#firstCustomerInfo_house_province_combobox').combobox("readonly", true);
        $('#firstCustomerInfo_house_city_combobox').combobox("readonly", true);
        $('#firstCustomerInfo_house_country_combobox').combobox("readonly", true);
        $('#first_customerInfo_estateAddress').textbox("readonly", true);
    } else if ('N' == isEqual) {
        $("input[name='estateSameRegistered']").eq(0).removeAttr("checked");
        $("input[name='estateSameRegistered']").eq(1).attr("checked", "checked");
        $('#firstCustomerInfo_house_province_combobox').combobox("readonly", false);
        $('#firstCustomerInfo_house_city_combobox').combobox("readonly", false);
        $('#firstCustomerInfo_house_country_combobox').combobox("readonly", false);
        $('#first_customerInfo_estateAddress').textbox("readonly", false);
    }

    initYMDateBox("#first_customerInfo_date_CAR");                  // 购买时间
    initYMDateBox("#first_customerInfo_carInfo_carLoanGrantDate");  // 车贷发放年月
    initYMDateBox("#first_customerInfo_date_ESTATE");               // 购买时间
    initYMDateBox("#firstCustomerInfo_house_estateLoanGrantDate");  // 房贷发放年月
    initYMDateBox("#first_Customer_Info_graduationDate");           // 毕业时间
    initYMDateBox("#first_customerInfo_date_CARDLOAN");             // 发卡时间
    initYMDateBox("#first_customerInfo_date_MERCHANTLOAN");         // 开店时间
    initYMDateBox("#corpStandFrom");                                // 入职时间
    initYMDateBox("#first_customerPrivateOwner_setupDate");         // 成立时间

    // 大于当天的日期置灰
    $('#first_customerInfo_date_ESTATE').datebox().datebox('calendar').calendar({
        validator: function (date) {
            var now = new Date();
            var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            return day >= date;
        }
    });

    // 根据审核产品code显示资产信息
    /*
    var applyType = vm.applyInfo.applyInfoVO.applyType;
    if (isNotNull(applyType) && !('TOPUP' == applyType || 'RELOAN' == applyType)) {// 申请类型为TOPUP和RELOAN类型不对资产信息进行校验
        // getAssetsByProdCode(vm.applyInfo.applyInfoVO.productCd);
    } else {
        $("#first_customerInfo_assetsInfo_div").find("form").each(function (indF, valF) {
            $(valF).find(".easyui-combobox").each(function (indBox, valBox) {
                $(valBox).combobox({
                    required: false
                });
            });
            $(valF).form("disableValidation");
            var assetinfosId = '#first_customer_' + $(valF).attr("name") + '_div';
            if ($(assetinfosId).attr("name") == 'show') {
                // 对应资产信息收缩,已填的资产信息展开
                $('#first_customer_' + $(valF).attr("name") + '_div').panel({
                    collapsible: true,
                    collapsed: false
                });
            } else {
                // 对应资产信息收缩
                $('#first_customer_' + $(valF).attr("name") + '_div').panel({
                    collapsible: true,
                    collapsed: true
                });
            }
            $('#first_customerInfo_date_' + $(valF).attr('name')).datebox({
                required: false
            });// 去掉时间校验
        });
    }
    */

    // 回显是否有车贷
    if (!vm.flagValidate) {
        carLoanCheck($("#hasCarLoan").val());
    }
    $("[name='carLoan']").on("change", function (e) {
        // 标记是否是直通车进件并且是TOPUP类型或RELOAN类型三个月结清结清精确到月(用于判断资产信息不必填)
        if (!vm.flagValidate) {
            carLoanCheck($(e.target).val());
        }
    });

    dataInit();
});


/**
 * 保存客户信息
 * @author dmz
 * @date 2017年3月23日
 */
var firstCustomerMarkSubmit = false;
function save(loanBaseId, loanNo, productCd, version) {
    //客户基本信息
    var baseCustomerInfo = $("#first_customerInfo_form").serializeObject();
    //客户房产信息
    var customerHomeInfo = $("#first_customerHome_form").serializeObject();

    if (isNotNull(baseCustomerInfo)){

        //家庭地址是否同房产地址必选校验
        if(isEmpty(baseCustomerInfo.homeSameRegistered)){
            $.info('提示信息', "请选择家庭地址是否同户籍地址!", "warning");
            return false;
        }

        //基本信息中家庭地址是否同户籍地址选择为否，两个地址不能相同
        if (baseCustomerInfo.homeSameRegistered == '1') {
            if (baseCustomerInfo.issuerStateId == baseCustomerInfo.homeStateId && baseCustomerInfo.issuerCityId == baseCustomerInfo.homeCityId &&
                baseCustomerInfo.issuerZoneId == baseCustomerInfo.homeZoneId && baseCustomerInfo.idIssuerAddress == baseCustomerInfo.homeAddress) {
                $.info('提示信息', "家庭地址是否同户籍地址选择为否，两个地址不能相同，请重新填写！", "warning");
                return false;
            }
        }

        //资产信息中房产信息中家庭地址是否同房产地址选择为否，两个地址不能相同
        if (isNotNull(customerHomeInfo) && customerHomeInfo.estateSameRegistered == 'N') {
            if (baseCustomerInfo.homeStateId == customerHomeInfo.estateStateId && baseCustomerInfo.homeCityId == customerHomeInfo.estateCityId &&
                baseCustomerInfo.homeZoneId == customerHomeInfo.estateZoneId && baseCustomerInfo.homeAddress == customerHomeInfo.estateAddress) {

                $.info('提示信息', "家庭地址是否同房产地址选择为否，两个地址不能相同，请重新填写！", "warning");
                return false;
            }
        }
    }



    if (!firstCustomerMarkSubmit) {
        // 婚姻状况和联系人的校验
        var contactInfoLen = $('#first_contactInfoVOList_len').val();
        var maritalStatus = $('#first_Customer_Info_maritalStatus').combobox('getValues');

        // 两个以上的父母和一个以上的配偶判断
        var parentNum = 0;
        var spouseNum = 0;
        $("#first_contactInfoVOList_div").find(".rlstionsp").each(function (key, v) {
            var relationValue = $(this).combobox('getValue');
            if ('00001' == relationValue) {// 父母
                parentNum++;
            } else if ('00013' == relationValue) {// 配偶
                spouseNum++;
            }
        });

        if (parentNum > 4) {
            focusTab('contact');
            $.info('提示信息', "不能有4条以上的父母信息！", "warning");
            return false;
        }
        if (spouseNum > 1) {
            focusTab('contact');
            $.info('提示信息', "不能有一条以上的配偶信息！", "warning");
            return false;
        }

        // 至少填写3个联系人单位名称
        var corporationNum = 0;
        avalon.each($('.contactEmpName'), function (index, element) {
            var corporationName = $(element).textbox('getValue');
            if (isNotNull(corporationName)) {
                corporationNum++;
            }
        });

        if (corporationNum < 3) {
            focusTab('contact');
            $.info('提示信息', "至少填写3个联系人单位名称！", "warning");
            return false;
        }

        // 若客户婚姻状况为“未婚”、“离异”、“丧偶”，若联系人关系中存在“父母”、“子女”，要求必填一个“父母”/“子女”关系联系人的单位名称
        if( maritalStatus == '00001' ||  maritalStatus ==  '00003' || maritalStatus == '00005'){
            var parentNum = 0;
            var companyNum = 0;
            $("#first_contactInfoVOList_div").find(".contactForm").each(function (index, element) {
                var $relationship = $('.rlstionsp', $(element));
                if ($relationship && ($relationship.combobox('getValue') == '00001' || $relationship.combobox('getValue') == '00002')) {// “父母”、“子女”
                    var companyName = $('.contactEmpName', $(element)).textbox('getValue');
                    if(isNotNull(companyName)){
                        companyNum++;
                    }
                    parentNum++;
                }
            });

            if(parentNum > 0 && companyNum == 0){
                $.info('提示信息', "至少填写1个父母或子女单位名称！", "warning");
                return false;
            }
        }

        // 微信号QQ号码邮箱必须填一个
        var qqNum = $('#firstCustomerInfo_customerInfo_qqNum').val();
        var weChatNum = $('#firstCustomerInfo_customerInfo_weChatNum').val();
        var eamil = $('#firstCustomerInfo_customerInfo_email').val();
        if (!isNotNull(qqNum) && !isNotNull(weChatNum) && !isNotNull(eamil)) {
            focusTab('base');
            $('#firstCustomerInfo_customerInfo_qqNum').textbox('textbox').focus();
            $.info('提示信息', 'QQ、微信 电子邮箱至少填写一项', 'warning');
            return;
        }

        // 单位性质职业校验
        var cusWorkType = $("#firstCustomerInfo_cusWorkType_combobox").combobox('getValue');// 客户工作类型
        var corpStructure = $("#firstCustomer_corpStructure_combobox").combobox('getValue');// 单位性质
        var occupation = $('#firstCustomer_occupation_combobox').combobox('getValue');// 职业
        if (!isNotNull(occupation) && !isNotNull(corpStructure)) {
            $.info('提示信息', '单位性质和职业字段不符合条件，请重新选择！', 'warning');
        } else if (!isNotNull(occupation)) {
            $.info('提示信息', '职业字段不符合条件，请重新选择！', 'warning');
        }

        // 验证基本信息
        if (!$("#first_customerInfo_form").form("validate")) {
            focusTab('base');
            $("#first_customerInfo_form").form("validate");
            return false;
        }

        // 验证工作信息
        if (!$("#first_customerWork_form").form("validate")) {
            focusTab('base');
            $("#first_customerWork_form").form("validate");
            return false;
        }

        // 验证私营业主信息
        if (!$("#first_customerPrivateOwner_form").form("validate")) {
            $("#firstCustomerInfo_tabs").tabs("select", 0);
            $("#first_customerPrivateOwner_form").form("validate");
            return false;
        }

        // 验证联系人
        var validateContact = true;
        var customerPhone = $("#first_customerInfo_cellphone").val();       //用户常用手机
        var customerPhoneSec = $("#first_customerInfo_cellphoneSec").val(); //用户备用手机
        //客户常用手机和备用手机相同
        if (customerPhone == customerPhoneSec) {
            focusTab('base');
            $.info('提示信息', "客户常用手机和备用手机号码相同", "warning");
            return false;
        }

        // 验证客户常用手机和备用手机，是否与联系人的常用手机和备用手机重复
        $.each($("#first_contactInfoVOList_div").find("form"), function (ind, val) {
            var contacts = $(val).serializeObject();
            //联系人常用手机和用户常用手机号码相同
            if (contacts.contactCellPhone == customerPhone){
                focusTab('contact');
                $.info('提示信息', "客户常用手机和"+contacts.contactName+"常用手机号码一致，不可重复录入！", "warning");
                validateContact = false;
                return false;
            }
            //联系人备用手机和用户常用手机号码相同
            if (contacts.contactCellPhone_1 == customerPhone){
                focusTab('contact');
                $.info('提示信息', "客户常用手机和"+contacts.contactName+"备用手机号码一致，不可重复录入！", "warning");
                validateContact = false;
                return false;
            }
            //联系人常用手机和用户备用手机号码相同
            if (contacts.contactCellPhone  == customerPhoneSec){
                focusTab('contact');
                $.info('提示信息', "客户备用手机和"+contacts.contactName+"常用手机号码一致，不可重复录入！", "warning");
                validateContact = false;
                return false;
            }
            //联系人备用手机和用户备用手机号码相同
            if (contacts.contactCellPhone_1 == customerPhoneSec){
                focusTab('contact');
                $.info('提示信息', "客户备用手机和"+contacts.contactName+"备用手机号码一致，不可重复录入！", "warning");
                validateContact = false;
                return false;
            }

            if (!$(val).form("validate")) {
                $("#firstCustomerInfo_tabs").tabs("select", 1);
                $(val).form("validate");
                validateContact = false;
                return false;
            }
        });
        if (!validateContact) {// 校验联系人
            return false;
        }

        // 验证保险信息
        if (!$("#first_customerPolicy_form").form("validate")) {
            $("#firstCustomerInfo_tabs").tabs("select", 2);
            $("#first_customerPolicy_form").form("validate");
            return false;
        }

        // 车辆信息
        if (!$("#first_customerCar_form").form("validate")) {
            $("#firstCustomerInfo_tabs").tabs("select", 2);
            $("#first_customerCar_form").form("validate");
            return false;
        }

        // 公积金信息
        if (!$("#first_customerProvident_form").form("validate")) {
            $("#firstCustomerInfo_tabs").tabs("select", 2);
            $("#first_customerProvident_form").form("validate");
            return false;
        }

        // 卡友贷信息
        if (!$("#first_customerCardLoan_form").form("validate")) {
            $("#firstCustomerInfo_tabs").tabs("select", 2);
            $("#first_customerCardLoan_form").form("validate");
            return false;
        }

        // 房产信息
        if (!$("#first_customerHome_form").form("validate")) {
            $("#firstCustomerInfo_tabs").tabs("select", 2);
            $("#first_customerHome_form").form("validate");
            return false;
        }

        // 网购达人贷A信息
        if (!$("#first_customeMasterLoanA_form").form("validate")) {
            $("#firstCustomerInfo_tabs").tabs("select", 2);
            $("#first_customeMasterLoanA_form").form("validate");
            return false;
        }

        // 网购达人贷B信息
        if (!$("#first_customeMasterLoanB_form").form("validate")) {
            $("#firstCustomerInfo_tabs").tabs("select", 2);
            $("#first_customeMasterLoanB_form").form("validate");
            return false;
        }

        // 淘宝商户信贷
        if (!$("#first_customerMerchant_form").form("validate")) {
            $("#firstCustomerInfo_tabs").tabs("select", 2);
            $("#first_customerMerchant_form").form("validate");
            return false;
        }

        var personInfoVO = $("#first_customerInfo_form").serializeObject();// 基本信息
        personInfoVO.gender = vm.applyInfo.basicInfoVO.personInfoVO.gender;
        personInfoVO.age = $("#first_customer_age").val();

        var workInfoVO = $("#first_customerWork_form").serializeObject();// 工作信息
        var privateOwnerInfoVO = $("#first_customerPrivateOwner_form").serializeObject();// 私营业主信息
        var contactInfoVOList = new Array();// 联系人信息
        var contactInfoFlag = null; // 标识是否知晓贷款
        var _sequenceNum = 2;	// 联系人序列号
        avalon.each($("#first_contactInfoVOList_div").find("form"), function (ind, val) {
            // 非复婚再婚已婚时保存加上申请关系为配偶
            var contactInfoVO = $(val).serializeObject();
            contactInfoVO.sequenceNum = _sequenceNum;
            if (isNotNull(contactInfoVO.contactRelation) && ('00013' == contactInfoVO.contactRelation)) {
                contactInfoVO.sequenceNum = 1;	// "配偶"默认序列号为1
            }
            if (!isNotNull($(val).find("input[name='ifKnowLoan']:checked").val())) { // 判断联系人是否知晓贷款
                contactInfoFlag = contactInfoVO.sequenceNum;// 标记是哪个联系人没有填写是否知晓贷款
                return;
            }
            if (isNotNull(contactInfoVO.contactName)) {
                contactInfoVOList.push(contactInfoVO);
                if (contactInfoVO.sequenceNum != 1)
                    _sequenceNum++;
            }
        });

        // 判断联系人是否知晓贷款字段是否填写
        if (isNotNull(contactInfoFlag)) {
            $("#firstCustomerInfo_tabs").tabs("select", 1);
            if (1 == contactInfoFlag) {
                $.info('提示', '请选择配偶信息是否知晓贷款!', 'warning');
            } else {
                $.info('提示', '请选择第' + (contactInfoFlag - 1) + '联系人是否知晓贷款!', 'warning');
            }
            return;
        }
        if (contactInfoVOList.length < 5) {
            $("#firstCustomerInfo_tabs").tabs("select", 1);// 定位
            $.info('提示信息', "联系人不能少于5个，请添加联系人！", "warning");
            return;
        }

        var policyInfoVO = $("#first_customerPolicy_form").serializeObject();// 保险信息
        var carInfoVO = $("#first_customerCar_form").serializeObject();// 车辆信息
        var providentInfoVO = $("#first_customerProvident_form").serializeObject();// 公积金信息
        var cardLoanInfoVO = $("#first_customerCardLoan_form").serializeObject();// 卡友贷信息
        var estateInfoVO = $("#first_customerHome_form").serializeObject();// 房产信息
        var salaryLoanInfoVO = $("#first_customerSalaryLoan_form").serializeObject();// 随薪贷信息
        var masterLoanInfoAVO = $("#first_customeMasterLoanA_form").serializeObject();// 网购达人贷B信息
        var masterLoanInfoBVO = $("#first_customeMasterLoanB_form").serializeObject();// 网购达人贷B信息
        var merchantLoanInfoVO = $("#first_customerMerchant_form").serializeObject();// 淘宝商户信贷
        if (isNotNull(carInfoVO.carBuyDate)) {
            carInfoVO.carBuyDate = carInfoVO.carBuyDate + "-01";
        }
        if (isNotNull(carInfoVO.carLoanIssueDate)) {
            carInfoVO.carLoanIssueDate = carInfoVO.carLoanIssueDate + "-01";
        }
        if (isNotNull(estateInfoVO.estateBuyDate)) {
            estateInfoVO.estateBuyDate = estateInfoVO.estateBuyDate + "-01";
        }
        if (isNotNull(estateInfoVO.estateLoanIssueDate)) {
            estateInfoVO.estateLoanIssueDate = estateInfoVO.estateLoanIssueDate + "-01";
        }
        if (isNotNull(privateOwnerInfoVO.setupDate)) {
            privateOwnerInfoVO.setupDate = privateOwnerInfoVO.setupDate + "-01";
        }
        if (isNotNull(personInfoVO.graduationDate)) {
            personInfoVO.graduationDate = personInfoVO.graduationDate + "-01";
        }
        if(isNotNull(cardLoanInfoVO.issuerDate)){
            cardLoanInfoVO.issuerDate = cardLoanInfoVO.issuerDate +"-01";
        }
        if(isNotNull(merchantLoanInfoVO.setupShopDate)){
            merchantLoanInfoVO.setupShopDate = merchantLoanInfoVO.setupShopDate+"-01";
        }
        if(isNotNull(workInfoVO.corpStandFrom)){
            workInfoVO.corpStandFrom = workInfoVO.corpStandFrom+"-01";
        }
        if("终身" == policyInfoVO.insuranceTerm){
            policyInfoVO.insuranceTerm = 999;
        }

        // 如果车辆信息必填，校验是否有车贷有没有选择
        if (carInfoNeedOrNot) {
            var carLoan = $("input[name='carLoan']:checked").val();
            if (isEmpty(carLoan)) {
                $("#firstCustomerInfo_tabs").tabs("select", 2);// 定位
                $.info("提示", "请选择是否有车贷！", "warning", 1000);
                return;
            }
        }

        // 如果房产信息必填，校验房产地址同家庭地址有没有选择
        if (estateInfoNeedOrNot) {
            var estate = $("input[name='estateSameRegistered']:checked").val();
            if (isEmpty(estate)) {
                $("#firstCustomerInfo_tabs").tabs("select", 2);// 定位
                $.info("提示", "请选择房产地址是否同家庭地址！", "warning", 1000);
                return;
            }
        }

        var basicInfoVO = new Object();
        basicInfoVO.personInfoVO = personInfoVO;
        basicInfoVO.workInfoVO = workInfoVO;
        basicInfoVO.privateOwnerInfoVO = privateOwnerInfoVO;
        var assetsInfoVO = new Object();
        assetsInfoVO.estateInfoVO = estateInfoVO;
        assetsInfoVO.carInfoVO = carInfoVO;
        assetsInfoVO.policyInfoVO = policyInfoVO;
        assetsInfoVO.providentInfoVO = providentInfoVO;
        assetsInfoVO.cardLoanInfoVO = cardLoanInfoVO;
        assetsInfoVO.salaryLoanInfoVO = salaryLoanInfoVO;
        assetsInfoVO.masterLoanInfoVO = $.extend(masterLoanInfoAVO, masterLoanInfoBVO);
        assetsInfoVO.merchantLoanInfoVO = merchantLoanInfoVO;

        var auditAmendEntryVO = new Object();
        auditAmendEntryVO.basicInfoVO = basicInfoVO;
        auditAmendEntryVO.assetsInfoVO = assetsInfoVO;
        auditAmendEntryVO.contactInfoVOList = contactInfoVOList;
        auditAmendEntryVO.productCd = productCd;
        auditAmendEntryVO.version = version;
        auditAmendEntryVO.loanBaseId = loanBaseId;
        firstCustomerMarkSubmit = true;

        $.ajax({
            url: ctx.rootPath() + "/firstApprove/updateCustomerInfo/" + loanNo,
            dataType: "JSON",
            method: 'post',
            contentType: 'application/json',
            data: JSON.stringify(auditAmendEntryVO),
            success: function (data) {
                firstCustomerMarkSubmit = false;
                if ("SUCCESS" == data.type) {
                    // 联系人数量重新保存
                    $('#first_contactInfoVOList_len').val(contactInfoVOList.length);
                    customeRuleEngine(loanNo); // 调用规则引擎
                } else if ("VERSIONERR" == data.type) {
                    $.messager.alert({
                        title: '提示',
                        msg: '您当前办理的借款单有可能已经被改派,请重新办理!',
                        icon: 'warning',
                        onClose: function () {
                            closeHTMLWindow();
                        }
                    });
                } else {
                    $.info("提示", data.firstMessage == null ? '保存失败，请检查数据！' : data.firstMessage, 'warning');
                }
            },
            error: function (data) {
                $.info("警告", data.responseText, "warning");
            }
        });
    }
}

/**
 * 根据产品code，获取资产配置信息
 * @code 产品编码
 */
var carInfoNeedOrNot = false;// 车辆信息是否必填
var estateInfoNeedOrNot = false;// 房产信息是否必填
function getAssetsByProdCode(code) {
    post(ctx.rootPath() + "/bmsBasiceInfo/getListProductAssetsInfoByCode/" + code, null, "json", function (data) {
        if (data.success) {
            var currentAssetsInfo = "";
            $("#first_customerInfo_assetsInfo_div").find("form").each(function (index, element) {
                var isDisable = false;
                var assetsList = data.dataList;
                $.each(assetsList, function (ind, val) {
                    if ($(element).attr("name") == val.code) {
                        if ("CAR" == val.code) {
                            carInfoNeedOrNot = true;
                        }
                        if ("ESTATE" == val.code) {
                            estateInfoNeedOrNot = true;
                        }
                        // 对应资产信息收缩,必填的资产信息展开
                        currentAssetsInfo = '#first_customer_' + $(element).attr("name") + '_div';
                        //$(currentAssetsInfo).panel({collapsible: true, collapsed: false});
                        isDisable = true;
                        return false;
                    }
                });


                var assetinfosId = '#first_customer_' + $(element).attr("name") + '_div';
                /*
                if (assetinfosId != currentAssetsInfo) {
                    if ($(assetinfosId).attr("name") == 'show') {
                        // 对应资产信息收缩,必填的资产信息展开
                        $('#first_customer_' + $(element).attr("name") + '_div').panel({
                            collapsible: true,
                            collapsed: false
                        });
                    } else {
                        // 对应资产信息收缩,必填的资产信息展开
                        $('#first_customer_' + $(element).attr("name") + '_div').panel({
                            collapsible: true,
                            collapsed: true
                        });
                    }
                }
                */


                if (isDisable) {
                    // 表单不必校验时combobox设置为必填
                    $(element).find(".easyui-combobox").each(function (indBox, valBox) {
                        $(valBox).combobox({
                            required: true
                        });
                    });

                    $(element).form("enableValidation");

                    if ($(element).attr('name') == 'ESTATE') {
                        // 取消房产地址校验时，手动添加
                        //var estateLoan = $('#first_customerInfo_estateInfo_estateLoan').val();// 房产信息
                        //houseLoanSelect(estateLoan, "");
                        //$("#first_customerInfo_estateInfo_estateLoan").combobox({
                        //    onChange: function (n, o) {
                        //        houseLoanSelect(n);
                        //    }
                        //});

                    } else if ($(element).attr('name') == 'CAR') {
                        // 取消车辆信息必填校验时，手动添加车牌号非必填校验、车贷校验、一手车校验
                        var carLoan = $("input[name='carLoan']:checked").val();
                        carLoanCheck(carLoan, "");
                        $("[name='carLoan']").on("change", function (e) {
                            carLoanCheck($(e.target).val());
                        });
                    }
                } else {
                    // 表单不必校验时combobox设置为非必填
                    $(element).find(".easyui-combobox").each(function (indBox, valBox) {
                        $(valBox).combobox({
                            required: false
                        });
                    });
                    $(element).form("disableValidation");
                    $("#first_customerInfo_assetsInfo_div").find("th.requiredFontWeight").addClass("requiredNotFontWeight").removeClass("requiredFontWeight");
                    if ($(element).attr('name') == 'ESTATE') {// 取消房产地址校验时，手动添加
                        $('#first_customerInfo_estateAddress').textbox({
                            required: false
                        });
                    } else if ($(element).attr('name') == 'CAR') {// 取消车辆信息必填校验时，手动添加车牌号非必填校验、车贷校验、一手车校验
                        $('#first_customerInfo_car_plateNum').textbox({
                            required: false
                        });
                    }
                    var assetinfosId = '#first_customer_' + $(element).attr("name") + '_div';// 非必填项的时间校验添加
                    if (currentAssetsInfo != assetinfosId) {
                        $('#first_customerInfo_date_' + $(element).attr('name')).datebox({
                            required: false
                        });
                    }
                }
            });
        }
    });
}

/**
 * 数据初始化函数
 */
function dataInit() {
    // edit by zw at 2017-04-26 时间框格式化
    $('.easyui-datebox').datebox({
        formatter: function (date) {
            if (isNotNull(date)) {
                return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
            }
        },
        parser: function (date) {
            if (isNotNull(date)) {
                return new Date(Date.parse(date.replace(/-/g, "/")));
            }
        }
    });
    // 是否显示配偶信息
    var marriageStatues = $('#first_Customer_Info_maritalStatus').val();
    marriageCheck(marriageStatues, null);

    // 租房类型时租金校验
    houseTypeCheck($("#first_customerInfo_houseType").val());

    // 企业类型选择时的校验
    $("#first_customerInfo_priEnterpriseType").combobox({
        onChange: function (n, o) {
            $('#firstCustomerInfo_priEnterpriseType_input').val(n);
        }
    });
    // 配偶为必填时，其他联系人关系去掉配偶关系
    relationJudge();
}

/**
 * 房贷为"还款中"时，月供、房贷发放年月必填，单据户名为本人为非必填
 * @param newValue
 * @param oldValue
 */
function houseLoanSelect(newValue, oldValue) {
    var loanGrantDateOption = $('#firstCustomerInfo_house_estateLoanGrantDate').datebox('options');
    var monthPaymentAmtOption = $('#firstCustomerInfo_house_monthPaymentAmt').numberbox('options');
    var houseIfMeOption = $('#firstCustomerInfo_house_ifMe').combobox('options');

    if ("ING" == newValue) {    // 还款中
        loanGrantDateOption = $.extend({}, loanGrantDateOption, {
            required: true,
        });
        monthPaymentAmtOption = $.extend({}, monthPaymentAmtOption, {
            required: true,
        });
        houseIfMeOption = $.extend({}, houseIfMeOption, {
            required: false,
            validType: [],
        });
    } else if ("ALL" == newValue || "END" == newValue) {    // 全款中、已结清
        loanGrantDateOption = $.extend({}, loanGrantDateOption, {
            required: false,
        });
        monthPaymentAmtOption = $.extend({}, monthPaymentAmtOption, {
            required: false,
        });
        houseIfMeOption = $.extend({}, houseIfMeOption, {
            required: true,
            //validType: ["comboboxUnSelectWithParam['单据户名是否为本人']"]
        });
    } else {
        loanGrantDateOption = $.extend({}, loanGrantDateOption, {
            required: false,
            value: ''
        });
        monthPaymentAmtOption = $.extend({}, monthPaymentAmtOption, {
            required: false,
            value: ''
        });
        houseIfMeOption = $.extend({}, houseIfMeOption, {
            required: false,
            value: '',
            validType: []
        });
    }

    $('#firstCustomerInfo_house_estateLoanGrantDate').datebox(loanGrantDateOption);
    $('#firstCustomerInfo_house_monthPaymentAmt').numberbox(monthPaymentAmtOption);
    $('#firstCustomerInfo_house_ifMe').combobox(houseIfMeOption);
}

/**
 * 切换婚姻状况校验联系人信息
 * @param newValue 最新选择的值
 * @param oldValue 上一次值
 */
function marriageCheck(newValue, oldValue) {
    var ifShowSpouseForm = ('00002' == newValue || '00006' == newValue || '00007' == newValue);// 已婚00002再婚00006复婚00007时加配偶信息校验
    var spouseCardNumber = $('#first_contactInfoVOList_div_spouseIdCard');// 配偶身份证号
    // 判断是否需要有配偶信息
    if (ifShowSpouseForm) { // 如果需要配偶信息
        if (spouseCardNumber.length != 1) {// 如果没有就创建配偶联系人
            customerAddContacts("00013");// 创建配偶联系人
        }
    } else { // 如果不需要配偶信息
        if (spouseCardNumber.length == 1) { // 如果有就把配偶变成普通联系人
            var $spouseForm = spouseCardNumber.parents("form")
            var spouseFormObj = $spouseForm.serializeObject();//获取配偶信息
            $spouseForm.remove();// 删除配偶联系
            // 判断配偶信息是否为空(姓名、手机、电话、单位名称、备用手机、备用电话是否为空)
            var ifSpouseFormObjEmpty = (isNotNull(spouseFormObj.contactName) || isNotNull(spouseFormObj.contactCellPhone) || isNotNull(spouseFormObj.contactCorpPhone) || isNotNull(spouseFormObj.contactEmpName) || isNotNull(spouseFormObj.contactCellPhone_1) || isNotNull(spouseFormObj.contactCorpPhone_1));
            if (ifSpouseFormObjEmpty) {
                customerAddContacts();// 创建普通联系人
                if (isNotNull(spouseFormObj.contactCellPhone_1)) {// 备用手机
                    addCellPhone($("#first_contactInfoVOList_div>form:last").find("tr:eq(1)").find("a:first").get(0), 'cellPhone');// 添加备用手机
                }
                if (isNotNull(spouseFormObj.contactCorpPhone_1)) {// 备用电话
                    addCellPhone($("#first_contactInfoVOList_div>form:last").find("tr:eq(1)").find("a:last").get(0), 'corpPhone');//添加备用电话
                }
                spouseFormObj.contactRelation = null;// 清空与配偶关系
                $("#first_contactInfoVOList_div").find("form:last").form("load", spouseFormObj);// 赋值form
            }
        }
    }
    // 婚姻状况为已婚或者离异时子女数必填
    if (isNotNull(newValue) && ('00002' == newValue || '00003' == newValue || '00006' == newValue || '00007' == newValue)) {
        $('#first_Customer_Info_childrenNum').numberspinner({
            required: true
        });
    } else {
        $('#first_Customer_Info_childrenNum').numberspinner({
            required: false
        });
    }
}

/**
 * 根据是否有车贷，月供、车贷发放年月是否必填
 * @param newValue Y-月供、车贷方法年月必填；N-月供、车贷方法年月不必填
 * @param oldValue
 */
function carLoanCheck(newValue, oldValue) {
    var _options1 = $('#first_Customer_Info_monthPaymentAmt').numberbox('options');
    var _options2 = $('#first_customerInfo_carInfo_carLoanGrantDate').datebox('options');
    if ("Y" == newValue) {
        _options1= $.extend({}, _options1, {required: true, readonly: false, disabled:false});
        _options2= $.extend({}, _options2, {required: true, readonly: false, disabled:false});
    } else if ("N" == newValue) {
        _options1 = $.extend({}, _options1, {required: false, readonly: true, disabled:true, value: null});
        _options2 = $.extend({}, _options2, {required: false, readonly: true, disabled:true, value: null});
    }

    $('#first_Customer_Info_monthPaymentAmt').numberbox(_options1);
    $('#first_customerInfo_carInfo_carLoanGrantDate').datebox(_options2);
}

/**
 * 计算账单月均值
 * @param html
 */
function calcBillAverageAmount(type) {
   if(type == 'CARDLOAN'){   // 卡友贷
       var billAmt1 = $('#cardLoanBillAmt1').numberbox('getValue');
       var billAmt2 = $('#cardLoanBillAmt2').numberbox('getValue');
       var billAmt3 = $('#cardLoanBillAmt3').numberbox('getValue');
       var billAmt4 = $('#cardLoanBillAmt4').numberbox('getValue');
       var billAverage = math.chain(billAmt1).add(billAmt2).add(billAmt3).add(billAmt4).divide(4).done();
       $("#first_customerInfo_cardLoan_table").find("tr:eq(2) span").html($.number(billAverage.toFixed(0), 0));
       $("#first_customerInfo_cardLoan_payMonthAmt").val(billAverage.toFixed(0));
   }else if(type == 'MERCHANTLOAN'){    // 淘宝商户贷
       var billAmt1 = $('#merchantBillAmt1').numberbox('getValue');
       var billAmt2 = $('#merchantBillAmt2').numberbox('getValue');
       var billAmt3 = $('#merchantBillAmt3').numberbox('getValue');
       var billAmt4 = $('#merchantBillAmt4').numberbox('getValue');
       var billAmt5 = $('#merchantBillAmt5').numberbox('getValue');
       var billAmt6 = $('#merchantBillAmt6').numberbox('getValue');
       var billAverage = math.chain(billAmt1).add(billAmt2).add(billAmt3).add(billAmt4).add(billAmt5).add(billAmt6).divide(6).done();
       $("#first_customerInfo_merchant_table").find("tr:eq(3) td").html($.number(billAverage.toFixed(0), 0) + "元");
       $("#first_customerInfo_merchantLoan_payMonthAmt").val(billAverage.toFixed(2));
   }
}

/**
 * 住房类型时为租房时，租金文本框必填
 * @param newValue 住房类型
 */
function houseTypeCheck(newValue) {
    var options = $('#first_customerInfo_houseRent').numberbox('options');
    if ('00004' == newValue) {
        options = $.extend({}, options, {required: true, disabled: false});
    } else {
        options = $.extend({}, options, {required: false, disabled: true, value: null});
    }
    $('#first_customerInfo_houseRent').numberbox(options);
}

function deleteCellPhone(obj, type) {
    var phoneHtml = '<a href="javaScript:void(0);" onclick="addCellPhone(this,\'' + type + '\')"><i class="fa fa-plus" aria-hidden="true"></i></a>';
    $(obj).parent().html(phoneHtml);
}


/**
 * 婚姻状况为已婚复婚再婚时其他联系人的申请关系去掉配偶
 */
function relationJudge() {// TODO 常规联系人去掉配偶
    var maritalStatus = $('#first_Customer_Info_maritalStatus').combobox('getValues');
    $('.maritalRelation').each(function (ind, v) {
        var comboboxArray = $(v).combobox('getData');
        $.each(comboboxArray, function (key, obj) {
            if ('00013' == obj.code) {
                comboboxArray.pop();
                return false;
            }
        });
        $(v).combobox('loadData', comboboxArray);
    });
}

/**
 * 当家庭地址或房产地址同户籍地址时户籍地址改变其他地址也要改变
 *
 * @author dmz
 * @date 2017年7月31日
 */
function addressSynchronization() {
    var homeAddress = $("input:checked[name='homeSameRegistered']").val();
    if ('0' == homeAddress) {
        // 选择是时，房产地址回填家庭地址内容，显示为文本不可编辑
        var idIssuerProvince = $("#firstCustomerInfo_birthplace_province_combobox").combobox("getValue");// 户籍省
        var idIssuerCity = $("#firstCustomerInfo_birthplace_city_combobox").combobox("getValue");// 户籍市
        var idIssuerCountry = $("#firstCustomerInfo_birthplace_country_combobox").combobox("getValue");// 户籍区
        var idIssuerAddress = $("#firstCustomerInfo_birthplace_idIssuerAddress").val();// 户籍地址

        $("#firstCustomerInfo_family_province_combobox").combobox('setValue', idIssuerProvince);
        $("#firstCustomerInfo_family_city_combobox").combobox("setValue", idIssuerCity);
        $("#firstCustomerInfo_family_country_combobox").combobox("setValue", idIssuerCountry);
        $("#firstCustomerInfo_family_addr").textbox('setValue', idIssuerAddress);
    }
    var houseAddress = $("input:checked[name='estateSameRegistered']").val();
    if ("Y" == houseAddress) {
        // 选择是时，房产地址回填家庭地址内容，显示为文本不可编辑
        var homeProv = $("#firstCustomerInfo_family_province_combobox").combobox("getValue");// 住宅省
        var homeCity = $("#firstCustomerInfo_family_city_combobox").combobox("getValue");// 住宅市
        var homeCoun = $("#firstCustomerInfo_family_country_combobox").combobox("getValue");// 住宅区
        var homeAddr = $("#firstCustomerInfo_family_addr").val();// 家庭地址

        $("#firstCustomerInfo_house_province_combobox").combobox('setValue', homeProv);
        $("#firstCustomerInfo_house_city_combobox").combobox("setValue", homeCity);
        $("#firstCustomerInfo_house_country_combobox").combobox("setValue", homeCoun);
        $("#first_customerInfo_estateAddress").textbox('setValue', homeAddr);
    }
}
/**
 * 当住宅地址修改时
 */
function changeFamily() {
    var estateSameRegistered = $("input:checked[name='estateSameRegistered']","#first_customerHome_form").val();
    if ("Y" == estateSameRegistered) {
        var homeProv = $("#firstCustomerInfo_family_province_combobox").combobox("getValue");// 住宅省
        var homeCity = $("#firstCustomerInfo_family_city_combobox").combobox("getValue");// 住宅市
        var homeCoun = $("#firstCustomerInfo_family_country_combobox").combobox("getValue");// 住宅区
        var homeAddr = $("#firstCustomerInfo_family_addr").val();// 家庭地址

        $("#firstCustomerInfo_house_province_combobox").combobox('setValue', homeProv);
        $("#firstCustomerInfo_house_city_combobox").combobox("setValue", homeCity);
        $("#firstCustomerInfo_house_country_combobox").combobox("setValue", homeCoun);
        $("#first_customerInfo_estateAddress").textbox('setValue', homeAddr);
    }
}

/**
 * 客户信息调用规则引擎
 *
 * @author dmz
 * @date 2017年8月7日
 * @param loanNo
 */
function customeRuleEngine(loanNo) {
    post(ctx.rootPath() + "/ruleEngine/executeRuleEngine/" + loanNo + "/XSCS07", null, "JSON", function (data) {
        if (data.success) {
            if (isNotNull(data.data)) {
                window.opener.firstSettingRuleEngineData(data.data);// 规则引擎返回值更新页面显示(欺诈可疑,综合信用评级)
            }
            var msgList = data.messages;
            if ("PASS" == data.firstMessage || "HINT" == data.firstMessage) {
                window.opener.limitSubmitSetting("true");
                $.info("提示", "保存成功", "info", 1000);
            } else if ("REJECT" == data.firstMessage) {
                $.info("提示", "规则引擎拒绝", "warning", 1000, function () {
                    window.opener.firstRuleEngineReject();
                });
            } else if ("LIMIT" == data.firstMessage) {
                $.info("提示", "保存成功", "info", 1000);
                window.opener.limitSubmitSetting("false");
            }
            // 提示信息设置
            $("#ruleEngineHint_div").find("ul").html('');
            for (var i = 2; i < msgList.length; i++) {
                $("#ruleEngineHint_div").find("ul").append("<li>" + msgList[i] + "<br><span>" + msgList[1] + "</span></li>")
            }
            ruleEngineHintShow(msgList.length - 2);// 是否显示提示
            $("#ruleEngineHint_number_div").html(msgList.length - 2);
        } else {
            $.info("警告", data.firstMessage, "warning");
        }
    });
}

/**
 * 获取枚举值下拉框
 * @param type
 * @returns {Array}
 */
function getEnum(type) {
    var enumList = [];

    // 获取各种枚举值
    post(ctx.rootPath() + "/bmsBasiceInfo/getEnumCode", {emnuType: type,app:'0'}, null, function (result) {
        if(isNotNull(result)){
            enumList = result;
        }
    });
    return enumList;
}

/******************************************Begin 联系人相关js**************************************************************************/
/**
 * 配偶切换证件类型时校验证件号码合法性
 * @param target
 */
function changeCardType(target) {
    var YOrN = $(target).val();
    var applicantID = $("#applicant_id").val();// 获取申请人省份证信息用于判断配偶身份证性别
    if ("Y" == YOrN) {
    	if(vm.isDirectApp){// 标记是否是直通车进件并且是TOPUP类型或RELOAN类型(用于判断配偶外籍人士身份证不必填)
    		$('#first_contactInfoVOList_div_spouseIdCard').textbox({
    			required: false,
                validType: ['consortInfo[1,100]']
            });
    	}else{
    		$('#first_contactInfoVOList_div_spouseIdCard').textbox({
    			required: false,
    			validType: ['consortInfo[1,100]']
    		});
    	}
    } else {
    	if(vm.isDirectApp){// 标记是否是直通车进件并且是TOPUP类型或RELOAN类型(用于判断配偶外籍人士身份证不必填)
    		$('#first_contactInfoVOList_div_spouseIdCard').textbox({
    			required: false,
                validType: ['consortIDCard', 'checkSexForContacts["' + applicantID + '"]']
            });
    	}else{
    		$('#first_contactInfoVOList_div_spouseIdCard').textbox({
    			missingMessage: '客户配偶非外籍人士，请填写身份证！',
    			required: true,
    			validType: ['consortIDCard', 'checkSexForContacts["' + applicantID + '"]']
    		});
    	}
    }
}

/**
 * 添加备用手机或备用电话
 * @param obj
 * @param type
 */
function addCellPhone(obj, type) {
    var phoneHtml = '';
    if ('cellPhone' == type) {// 增加备用手机
        phoneHtml = '<input class="easyui-textbox input cellPhoneCheck secondPhone" data-options="validType:[\'length[11,11]\',\'mobile\']" value="" name="contactCellPhone_1">&nbsp;&nbsp;&nbsp;&nbsp;<a href="javaScript:void(0);" onclick="deleteCellPhone(this,\'cellPhone\')"><i class="fa fa-minus" aria-hidden="true"></i></a>';
        var cellPhoneTd = $(obj).parent();
        $(cellPhoneTd).html(phoneHtml);
        $(cellPhoneTd).find("input[name='contactCellPhone_1']").textbox({
            validType: ["mobile", "samePhoneWithName['secondPhone']"],
            width: 180
        });
    } else if ('corpPhone' == type) {
        phoneHtml = '<input class="easyui-textbox input" data-options="validType:\'telNum\'" value="" name="contactCorpPhone_1">&nbsp;&nbsp;&nbsp;&nbsp;<a href="javaScript:void(0);" onclick="deleteCellPhone(this,\'corpPhone\')"><i class="fa fa-minus" aria-hidden="true"></i></a>';
        var cellPhoneTd = $(obj).parent();
        $(cellPhoneTd).html(phoneHtml);
        $(cellPhoneTd).find("input[name='contactCorpPhone_1']").textbox({
            validType: 'telNum',
            width: 180
        });
    }
}

/**
 * 添加联系人
 * 注意：只有配偶信息才需要填写身份证号(配偶身份证号有且只有一个或者没有)
 * @param contactType-联系人类型(区分配偶或常规联系人)
 */
function customerAddContacts(contactType) {
    if ($("#first_contactInfoVOList_div").find("form").length>=20) {
        $.info('提示', '联系已达上限!', 'warning');
        return false;
    }
    var contactsHTML = $("<form class='contactForm'></for><div><table class='table_ui W100'><tr>" +
        "<th width='10%' class='requiredFontWeight'>姓名:</th>	<td width='12%'><input class='input peopleName' name='contactName'></td><td width='18%'></td>" +
        "<th width='8%' class='requiredFontWeight'>与申请人关系:</th><td width='12%'><input class='maritalRelation input rlstionsp' name='contactRelation' /></td>" +
        "<th width='18%' class='requiredFontWeight'>是否知晓贷款:</th><td width='12%'>是<input type='radio' name='ifKnowLoan' value='Y' /> 否<input type='radio' name='ifKnowLoan' value='N' /></td>" +
        "</tr><tr>" +
        "<th width='10%' class='requiredFontWeight'>外籍人士:</th><td width='12%'>是<input type='radio' name='ifForeignPenple' value='Y' /> 否<input type='radio' name='ifForeignPenple' value='N' checked='checked' /></td><td></td>" +
        "<th class='requiredFontWeight'>身份证号码:</th>	<td colspan='3'><input id='first_contactInfoVOList_div_spouseIdCard' class='input' name='contactIdNo'></td>" +
        "</tr><tr>" +
        "<th class='requiredFontWeight'>手机:</th>	<td><input class='input cellPhoneCheck firstPhone' name='contactCellPhone'></td><td><a href='javaScript:void(0);' onclick=addCellPhone(this,'cellPhone')><i class='fa fa-plus' aria-hidden='true'></i></a></td>" +
        /*"<td><input class='input cellPhoneCheck' name='contactCellPhone_1'><a style='margin-left: 15px' href='javaScript:void(0);' onclick=deleteCellPhone(this,'cellPhone')><i class='fa fa-minus' aria-hidden='true'></i></a></td>" +*/
        "<th>单电:</th><td><input class='input' name='contactCorpPhone'></td><td><a href='javaScript:void(0);' onclick=addCellPhone(this,'corpPhone')><i class='fa fa-plus' aria-hidden='true'></i></a></td>" +
        /* "<td><input class='input' name='contactCorpPhone_1'><a style='margin-left: 15px' href='javaScript:void(0);' onclick='deleteCellPhone(this,'corpPhone')'><i class='fa fa-minus' aria-hidden='true'></i></a></td>" +*/
        "</tr><tr><th>单位名称:</th><td colspan='5'><input class='input contactEmpName' name='contactEmpName'></td></tr></table></div></form>");

    if (isNotNull(contactType) && "00013" == contactType) {// 标识添加配偶信息
        contactsHTML.find("tr:first").find("td:eq(2)").html("<input type='hidden'  value='00013' name='contactRelation' />配偶");// 联系人直接修改成配偶
        $("#first_contactInfoVOList_div").prepend(contactsHTML);
        contactsHTML.find("div").panel({
            title: "配偶信息",
            height: 186,
            width: 1382
        })

        /*
        contactsHTML.find("input[name='contactName']").textbox({
        missingMessage: '请填写姓名！',
        validType: "peopleNameWithParam['配偶姓名']"
        });
        */

        if(vm.isDirectApp){// 标记是否是直通车进件并且是TOPUP类型或RELOAN类型(用于判断配偶外籍人士身份证不必填)
        	contactsHTML.find("input[name='contactIdNo']").textbox({// 加载配偶身份证信息
        		required: false,
        		validType: ['consortIDCard', 'checkSexForContacts["' + vm.applyInfo.applyInfoVO.idNo + '"]'],
        		width: 435,
        	});
        }else{
        	contactsHTML.find("input[name='contactIdNo']").textbox({// 加载配偶身份证信息
        		required: true,
        		missingMessage: '客户配偶非外籍人士，请填写身份证！',
        		validType: ['consortIDCard', 'checkSexForContacts["' + vm.applyInfo.applyInfoVO.idNo + '"]'],
        		width: 435,
        	});
        }

        contactsHTML.find("input[name='ifForeignPenple']").on("change", function (event) {// 加载配偶证件类型事件
            changeCardType(event.target);
        });

        contactsHTML.find("input[name='contactName']").textbox({// 加载联系人姓名
            required: true,
            width: 210,
            validType: ["peopleNameWithParam['配偶姓名']","contactName"],
            missingMessage: "请填写姓名!"
        });

        contactsHTML.find("input[name='contactEmpName']").textbox({// 加载单位名称
            required: true,
            width: 415,
            validType: ["notAllNumberAndLength[200, '单位名称']"],
            missingMessage: "请填写单位名称!",
        });

    } else {
        contactsHTML.find("tr:eq(1)").remove();// 删除身份证信息
        $("#first_contactInfoVOList_div").append(contactsHTML);
        contactsHTML.find("div").panel({
            title: "第<span class='first_contactsIndex'>" + ($(".first_contactsIndex").length + 1) + "</span>联系人信息  &nbsp;<a class='easyui-linkbutton_ok04 l-btn l-btn-small' href='javaScript:void(0);' onclick='deleteContact(this)'>删 除</a>",
            height: 154,
            width: 1382
        })
        contactsHTML.find("input[name='contactRelation']").combobox({// 加载与申请人关系
            required: true,
            editable: false,
            valueField: 'code',
            textField: 'nameCN',
            width: 210,
            missingMessage: "请选择与申请人关系！",
            prompt: '请选择',
            // validType: ["comboboxUnSelectWithParam['与申请人关系']"],
            data: getEnum('Relationship')
        });
        relationJudge();// 去掉配偶
        // 添加联系人加载控件
        contactsHTML.find("input[name='contactName']").textbox({// 加载联系人姓名
            required: true,
            width: 210,
            missingMessage: "请填写联系人姓名!",
            validType: ["peopleNameWithParam['联系人姓名']","contactName"]
        });
        contactsHTML.find("input[name='contactEmpName']").textbox({// 加载单位名称
            width: 415,
            validType: ["notAllNumberAndLength[200, '单位名称']"],
            missingMessage: "请填写单位名称!",
        });
    }

    contactsHTML.find("input[name='contactCellPhone']").textbox({// 加载联系电话
        required: true,
        width: 210,
        missingMessage: '请填写常用手机!',
        validType: ["mobile", "samePhoneWithName['firstPhone']"]
    });

    contactsHTML.find("input[name='contactCorpPhone']").textbox({// 加载单位电话
        width: 210,
        validType: 'telNum'
    });

}

/**
 * 删除联系人(页面删除非数据库删除)
 * @param obj-this
 */
function deleteContact(obj) {
    var maritalStatus = $("#first_Customer_Info_maritalStatus").combobox("getValue");// 获取婚姻状况
    var contactsCount = 5; // 联系人最小值
    if ('00002' == maritalStatus || '00006' == maritalStatus || '00007' == maritalStatus) {//判断婚姻状态(已婚、再婚、复婚)) {
        contactsCount = 4; // 如果是已婚、再婚、复婚 (4+1)
    }
    if (contactsCount < $(".first_contactsIndex").length) {
        $(obj).parents("form").remove();// 删除联系人
        $.each($(".first_contactsIndex"), function (ind, val) {// 重新编排联系人顺序
            $(val).text(ind + 1);
        });
    } else {
        $.info('提示', '联系人不能少于五个!', 'warning');
    }
}

/**
 * 客户信息初始化
 * @author lihm
 */
function addRule() {
    /**资产信息start*/
    /*保单信息初始化start*/
    //保险金额
    $("#first_customerInfo_assetsInfo_policyInfo_insuranceAmt").numberbox({
        required: vm.assetsIsRequired('POLICY') ? true : false,
        groupSeparator: ',',
        precision: 0,
        buttonText: '元',
        missingMessage: "请填写保险金额!",
        tipPosition: 'top',
        validType: ["range[1, 99999999, '保险金额']"]
    });

    //保险年限
    $("#first_customerInfo_assetsInfo_policyInfo_insuranceTerm").textbox({
        required: vm.assetsIsRequired('POLICY') ? true : false,
        buttonText: '年',
        missingMessage: "请填写保险年限!",
        tipPosition: 'top',
        validType: ["insurancePeriod[1, 99999999, '保险年限']"]
    });

    //已缴年限
    $("#first_customerInfo_assetsInfo_policyInfo_paidTerm").numberbox({
        required: vm.assetsIsRequired('POLICY') ? true : false,
        precision: 0,
        buttonText: '年',
        missingMessage: "请填写已缴年限!",
        tipPosition: 'top',
        validType: ["nonnegativePnum['已缴年限']", "range[1, 99999999, '已缴年限']"]
    });

    //年缴金额
    $("#first_customerInfo_assetsInfo_policyInfo_yearPaymentAmt").numberbox({
        required: vm.assetsIsRequired('POLICY') ? true : false,
        groupSeparator: ',',
        precision: 0,
        buttonText: '元',
        missingMessage: "请填写年缴金额!",
        tipPosition: 'top',
        validType: ["nonnegativePnum['年缴金额']", "range[0, 99999999, '年缴金额']"]
    });
    /*保单信息初始化end*/

    /*车辆信息初始化start*/
    // 购买价
    $("#first_customerInfo_carInfo_carBuyPrice").numberbox({
        required: vm.assetsIsRequired('CAR') ? true : false,
        groupSeparator: ',',
        precision: 0,
        buttonText: '元',
        missingMessage: "请填写购买价!",
        validType: ["nonnegativePnum['购买价']", "range[0, 99999999, '购买价']"]
    });

    // 购买时间
    $("#first_customerInfo_date_CAR").datebox({
        required: vm.assetsIsRequired('CAR') ? true : false,
        prompt: '购买时间',
        editable: false,
        missingMessage: "请选择购买时间!",
        validType: ["compareTimeLimitWithParam['1990年1月', '购买时间不可']", "compareNowDate['购买时间']"]
    }).datebox('calendar').calendar({
        validator: function (date) {
            var now = new Date();
            var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            return day >= date;
        }
    });

    //车牌号
    $("#first_customerInfo_car_plateNum").textbox({
        required: vm.assetsIsRequired('CAR') ? true : false,
        missingMessage: "请填写车牌号!",
        validType: ["carPlateNum['车牌号']"]
    });

    // 车贷发放年月
    $("#first_customerInfo_carInfo_carLoanGrantDate").datebox({
        value: isNotNull(vm.applyInfo.assetsInfoVO.carInfoVO.carLoanIssueDate) ? moment(vm.applyInfo.assetsInfoVO.carInfoVO.carLoanIssueDate).format('YYYY-MM-DD') : '',
        required: false,
        prompt: '车贷发放年月',
        editable: false,
        missingMessage: "请选择车贷发放年月!",
        validType: ["compareTimeLimitWithParam['1980年1月', '车贷发放年月']", "compareNowDate['车贷发放年月']"],
    });

    // 月供
    $("#first_Customer_Info_monthPaymentAmt").numberbox({
        groupSeparator: ',',
        required: false,
        precision: 0,
        buttonText: '元',
        missingMessage: "请填写月供!",
        validType: ["nonnegativePnum['月供']", "range[0, 99999999, '月供']"],
    });

    /*车辆信息初始化end*/

    /*公积金初始化start*/
    // 缴存比例
    $("#first_customerInfo_assetsInfo_providentInfo_depositRate").numberbox({
        required: vm.assetsIsRequired('PROVIDENT'),
        precision: 2,
        min: 0,
        max: 100,
        buttonText: '%',
        missingMessage: "请填写缴存比例!",
        tipPosition: 'top',
        validType: ["decimalsVerified[0,100,'缴存比例']"],
    });

    // 月缴存额
    $("#first_customerInfo_assetsInfo_providentInfo_monthDepositAmt").numberbox({
        required: vm.assetsIsRequired('PROVIDENT'),
        groupSeparator: ',',
        buttonText: '元',
        missingMessage: "请填写月缴存额!",
        tipPosition: 'top',
        validType: ["nonnegativePnum['月缴存额']", "range[0, 99999999, '月缴存额']"],
    });

    // 缴存基数
    $("#first_customerInfo_assetsInfo_providentInfo_depositBase").numberbox({
        required: vm.assetsIsRequired('PROVIDENT'),
        groupSeparator: ',',
        precision: 0,
        buttonText: '元',
        missingMessage: "请填写缴存基数!",
        tipPosition: 'top',
        validType: ["nonnegativePnum['缴存基数']", "range[0, 99999999, '缴存基数']"],
    });

    // 公积金材料
    $("#first_customerInfo_assetsInfo_providentInfo_providentInfo").combobox({
        required: vm.assetsIsRequired('PROVIDENT'),
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('ProvidentInfo'),
        tipPosition: 'top',
        prompt: '请选择',
        missingMessage: '请选择公积金材料',
        //validType: ["comboboxUnSelectWithParam['公积金材料']"]
    });

    // 缴纳单位同申请单位
    $("#first_customerInfo_assetsInfo_providentInfo_paymentUnit").combobox({
        required: vm.assetsIsRequired('PROVIDENT'),
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        tipPosition: 'top',
        missingMessage: '请选择缴纳单位同申请单位',
        prompt: '请选择',
        data: getEnum('Indicator')
        // validType: ["comboboxUnSelectWithParam['缴纳单位同申请单位']"]
    });

    // 申请单位已缴月数
    $("#first_customerInfo_assetsInfo_providentInfo_paymentMonthNum").numberbox({
        required: vm.assetsIsRequired('PROVIDENT'),
        precision: 0,
        missingMessage: "请填写申请单位已缴月数!",
        tipPosition: 'top',
        buttonText: '月',
        validType: ["nonnegativePnum['申请单位已缴月数']", "range[1, 99999999, '申请单位已缴月数']"]
    });
    /*公积金初始化end*/


    /*卡友贷信息初始化start*/
    // 发卡时间
    $("#first_customerInfo_date_CARDLOAN").datebox({
        value: isNotNull(vm.applyInfo.assetsInfoVO.cardLoanInfoVO.issuerDate) ? moment(vm.applyInfo.assetsInfoVO.cardLoanInfoVO.issuerDate).format('YYYY-MM-DD') : '',
        required: vm.assetsIsRequired('CARDLOAN') ? true : false,
        editable: false,
        prompt: '发卡时间',
        missingMessage: "请选择发卡时间!",
        tipPosition: 'top',
        validType: ["compareTimeLimitWithParam['1990年1月', '发卡时间']", "compareNowDate['发卡时间']"],
    }).datebox('calendar').calendar({
        validator: function (date) {
            var now = new Date();
            var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            return day >= date;
        }
    });

    // 额度
    $("#first_customerInfo_assetsInfo_cardLoanInfo_creditLimit").numberbox({
        groupSeparator: ',',
        required: vm.assetsIsRequired('CARDLOAN') ? true : false,
        precision: 0,
        buttonText: '元',
        missingMessage: "请填写信用卡额度!",
        tipPosition: 'top',
        validType: ["nonnegativePnum['信用卡额度']", "range[0, 99999999, '额度']"],
    });


    // 近四个月账单金额
    $("#first_customerInfo_cardLoan_table").find("tr.cardLoanRecordTr").each(function (ind, val) {
        $(val).find(".billAmt").numberbox({
            required: vm.assetsIsRequired('CARDLOAN') ? true : false,
            precision: 0,
            groupSeparator: ',',
            buttonText: '元',
            missingMessage: "请填写近4个月的账单金额！",
            tipPosition: 'top',
            validType: ["nonnegativePnum['账单金额']", "range[0, 99999999, '账单金额']"],
            onChange: function () {
                calcBillAverageAmount('CARDLOAN');
            }
        });
    });
    /*卡友贷信息初始化end*/

    /*房产信息初始化start*/
    //房产类型
    $("#first_customerInfo_assetsInfo_estateInfo_estateType").combobox({
        required: vm.assetsIsRequired('ESTATE') ? true : false,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('FangType'),
        missingMessage: '请选择房产类型',
        prompt: '请选择',
        tipPosition: 'top',
        //validType: ["comboboxUnSelectWithParam['房产类型']"]
    });

    //购买时间
    $("#first_customerInfo_date_ESTATE").datebox({
        value: isNotNull(vm.applyInfo.assetsInfoVO.estateInfoVO.estateBuyDate) ? moment(vm.applyInfo.assetsInfoVO.estateInfoVO.estateBuyDate).format('YYYY-MM-DD') : '',
        required: vm.assetsIsRequired('ESTATE') ? true : false,
        editable: false,
        prompt: '购买时间',
        missingMessage: "请选择购买时间!",
        tipPosition: 'top',
        validType: ["compareTimeLimitWithParam['1980年1月', '购买时间不可']", "compareNowDate['购买时间']"],
    }).datebox('calendar').calendar({
        validator: function (date) {
            var now = new Date();
            var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            return day >= date;
        }
    });

    //市值参考价
    $("#first_customerInfo_assetsInfo_estateInfo_referenceAmt").numberbox({
        groupSeparator: ',',
        required: vm.assetsIsRequired('ESTATE') ? true : false,
        precision: 0,
        buttonText: '元',
        missingMessage: "请填写市值参考价!",
        tipPosition: 'top',
        validType: ["nonnegativePnum['市值参考价']", "range[0, 99999999, '市值参考价']"],
    });

    //房产地址区信息
    extendOption('firstCustomerInfo_house_country_combobox', {
        required: vm.assetsIsRequired('ESTATE') ? true : false,
        tipPosition: 'top',
        missingMessage: "房产地址输入不完整，下拉框均需选择，输入框不能为空!"
    });

    //房产地址市信息
    extendOption('firstCustomerInfo_house_city_combobox', {
        required: vm.assetsIsRequired('ESTATE') ? true : false,
        tipPosition: 'top',
        missingMessage: "房产地址输入不完整，下拉框均需选择，输入框不能为空!"
    });

    //房产地址省信息
    extendOption('firstCustomerInfo_house_province_combobox', {
        required: vm.assetsIsRequired('ESTATE') ? true : false,
        tipPosition: 'top',
        missingMessage: "房产地址输入不完整，下拉框均需选择，输入框不能为空!"
    });

    //房产地址具体信息
    $("#first_customerInfo_estateAddress").textbox({
        required: vm.assetsIsRequired('ESTATE') ? true : false,
        tipPosition: 'top',
        validType: ["chineseAndNoSpace['房产地址输入框中']", "maxLength[200]"],
        missingMessage: "房产地址输入不完整，下拉框均需选择，输入框不能为空！"
    });

    //房贷发放年月
    $("#firstCustomerInfo_house_estateLoanGrantDate").datebox({
        value: isNotNull(vm.applyInfo.assetsInfoVO.estateInfoVO.estateLoanIssueDate) ? moment(vm.applyInfo.assetsInfoVO.estateInfoVO.estateLoanIssueDate).format('YYYY-MM-DD') : '',
        required: false,
        editable: false,
        missingMessage: "请选择房贷发放年月!",
        tipPosition: 'top',
        validType: ["compareTimeLimitWithParam['1980年1月', '房贷发放年月']", "compareNowDate['房贷发放年月']"]
    }).datebox('calendar').calendar({
        validator: function (date) {
            var now = new Date();
            var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            return day >= date;
        }
    });

    //月供
    $("#firstCustomerInfo_house_monthPaymentAmt").numberbox({
        groupSeparator: ',',
        required: false,
        precision: 0,
        buttonText: '元',
        missingMessage: "请填写月供!",
        tipPosition: 'top',
        validType: ["nonnegativePnum['月供']", "range[0, 99999999, '月供']"]
    });

    //产权比例
    $("#firstCustomerInfo_house_Ownership_numberbox").numberbox({
        required: vm.assetsIsRequired('ESTATE') ? true : false,
        min: 0,
        max: 100,
        precision: 2,
        buttonText: '%',
        missingMessage: "请填写产权比例!",
        tipPosition: 'top',
        validType: ["decimalsVerified[0,100,'产权比例']"]
    });

    //单据户名为本人
    $("#firstCustomerInfo_house_ifMe").combobox({
        required: false,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('Indicator'),
        tipPosition: 'top',
        prompt: '请选择',
        missingMessage: '请选择单据户名是否为本人',
        //validType: ["comboboxUnSelectWithParam['单据户名是否为本人']"]
    });

    //房贷情况
    $("#first_customerInfo_estateInfo_estateLoan").combobox({
        required: vm.assetsIsRequired('ESTATE') ? true : false,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('EstateType'),
        prompt: '请选择',
        missingMessage: '请选择房贷情况',
        //validType: ["comboboxUnSelectWithParam['房贷情况']"],
        tipPosition: 'top',
        onChange: function (newValue, oldValue) {git
            // 标记是否是直通车进件并且是TOPUP类型或RELOAN类型三个月结清结清精确到月(用于判断资产信息不必填)
            if (!vm.flagValidate) {
                houseLoanSelect(newValue);
            }
        },
        onLoadSuccess: function () {
            var value = $(this).combobox("getValue");
            if (isNotNull(value) && !vm.flagValidate) {
                // 标记是否是直通车进件并且是TOPUP类型或RELOAN类型三个月结清结清精确到月(用于判断资产信息不必填)
               houseLoanSelect(value);
            }
        }
    });
    /*房产信息初始化end*/

    /*网购达人贷B信息始化start*/
    //京东用户等级
    $("#first_customerInfo_assetsInfo_masterLoanInfoB_jiDongUserLevel").combobox({
        required: vm.assetsIsRequired('MASTERLOAN_B') ? true : false,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('JiDongUserLevel'),
        tipPosition: 'top',
        prompt: '请选择',
        missingMessage: '请选择京东用户等级',
        //validType: ["comboboxUnSelectWithParam['京东用户等级']"]
    });

    //小白信用分
    $("#first_customerInfo_assetsInfo_masterLoanInfoB_whiteCreditValue").numberbox({
        required: vm.assetsIsRequired('MASTERLOAN_B') ? true : false,
        precision: 1,
        min: 0,
        max: 150,
        missingMessage: "请填写小白信用分!",
        validType: ["decimalsVerifiedOne[0,150,'小白信用分']"],
    });

    //近一年实际消费金额
    $("#first_customerInfo_assetsInfo_masterLoanInfoB_pastYearShoppingAmount").numberbox({
        groupSeparator: ',',
        required: vm.assetsIsRequired('MASTERLOAN_B') ? true : false,
        precision: 0,
        buttonText: '元',
        missingMessage: "请填写近一年实际消费金额!",
        validType: ["nonnegativePnum['近一年实际消费金额']", "range[0, 99999999, '近一年实际消费金额']"],
    });
    /*网购达人贷B信息始化end*/


    /*网购达人贷A信息始化start*/
    //买家信用等级
    $("#first_customerInfo_assetsInfo_masterLoanInfoA_buyerCreditLevel").combobox({
        required: vm.assetsIsRequired('MASTERLOAN_A') ? true : false,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('SellerCreditLevel'),
        prompt: '请选择',
        missingMessage: '请选择买家信用等级',
        //validType: ["comboboxUnSelectWithParam['买家信用等级']"]
    });

    // 买家信用类型
    $("#first_customerInfo_assetsInfo_masterLoanInfoA_buyerCreditType").combobox({
        required: vm.assetsIsRequired('MASTERLOAN_A') ? true : false,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('SellerCreditType'),
        prompt: '请选择',
        missingMessage: '请选择买家信用类型',
        //validType: ["comboboxUnSelectWithParam['买家信用类型']"]
    });

    // 芝麻信用分
    $("#first_customerInfo_assetsInfo_masterLoanInfoA_sesameCreditValue").numberbox({
        required: vm.assetsIsRequired('MASTERLOAN_A') ? true : false,
        precision: 0,
        missingMessage: "请填写芝麻信用分!",
        validType: ["range[350,950, '芝麻信用分']"]
    });

    // 近12个月支出额
    $("#first_customerInfo_assetsInfo_masterLoanInfoA_lastYearPayAmt").numberbox({
        groupSeparator: ',',
        required: vm.assetsIsRequired('MASTERLOAN_A') ? true : false,
        precision: 0,
        buttonText: '元',
        missingMessage: "请填写近12个月支出额!",
        validType: ["nonnegativePnum['近12个月支出额']", "range[0, 99999999, '近12个月支出额']"],
    });

    // 淘气值
    $('#first_customerInfo_assetsInfo_masterLoanInfoA_naughtyValue').numberbox({
        groupSeparator: ',',
        required: vm.assetsIsRequired('MASTERLOAN_A') ? true : false,
        precision: 0,
        missingMessage: "请填写淘气值!",
        validType: ["range[0, 9999, '淘气值']"]
    });

    /*网购达人贷A信息初始化end*/

    /*淘宝商户贷初始化start*/
    //开店时间
    $("#first_customerInfo_date_MERCHANTLOAN").datebox({
        value: isNotNull(vm.applyInfo.assetsInfoVO.merchantLoanInfoVO.setupShopDate) ? moment(vm.applyInfo.assetsInfoVO.merchantLoanInfoVO.setupShopDate).format('YYYY-MM-DD') : '',
        required: vm.assetsIsRequired('MERCHANTLOAN') ? true : false,
        editable: false,
        prompt: '开店时间',
        tipPosition: 'top',
        missingMessage: "请选择开店时间!",
        validType: ["compareTimeLimitWithParam['1990年1月', '开店时间']", "compareNowDate['开店时间']"],
    }).datebox('calendar').calendar({
        validator: function (date) {
            var now = new Date();
            var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            return day >= date;
        }
    });

    //卖家信用等级
    $("#first_customerInfo_date_SELLERCREDITLEVEL").combobox({
        required: vm.assetsIsRequired('MERCHANTLOAN') ? true : false,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('SellerCreditLevel'),
        prompt: '请选择',
        tipPosition: 'top',
        missingMessage: '请选择卖家信用等级',
        //validType: ["comboboxUnSelectWithParam['卖家信用等级']"]
    });

    //卖家信用类型
    $("#first_customerInfo_date_SELLERCREDITTYPE").combobox({
        required: vm.assetsIsRequired('MERCHANTLOAN') ? true : false,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('TaobaoSellerCreditType'),
        prompt: '请选择',
        tipPosition: 'top',
        missingMessage: '请选择卖家信用类型',
        //validType: ["comboboxUnSelectWithParam['卖家信用类型']"]
    });

    //近半年好评数
    $("#first_customerInfo_date_REGARDEDNUM").numberbox({
        required: vm.assetsIsRequired('MERCHANTLOAN') ? true : false,
        precision: 0,
        tipPosition: 'top',
        missingMessage: "请填写近半年好评数!",
        validType: ["nonnegativePnum['近半年好评数']", "range[0, 99999999, '近半年好评数']"],
    });

    // 近六个月账单金额
    $("#first_customerInfo_merchant_table").find("tr.cardLoanRecordTr").each(function (ind, val) {
        $(val).find(".input").numberbox({
            required: vm.assetsIsRequired('MERCHANTLOAN') ? true : false,
            validType: ["nonnegativePnum['月账单金额']", "range[0, 99999999, '月账单金额']"],
            precision: 0,
            buttonText: '元',
            tipPosition: 'top',
            missingMessage: "请填写近6个月账单金额!",
            onChange: function () {
                calcBillAverageAmount('MERCHANTLOAN');
            }
        });
    });

    /*淘宝商户贷始化end*/
    /**资产信息end*/

    /**基本信息start*/
    /*个人信息初始化start*/

    // 婚姻状况
    $("#first_Customer_Info_maritalStatus").combobox({
        required: true,
        editable: false,
        valueField: 'code',
        prompt: '请选择',
        missingMessage: '请选择婚姻状况',
        textField: 'nameCN',
        data: getEnum('MaritalStatus'),
        onChange: function (newValue, oldValue) {
            marriageCheck(newValue, oldValue);
        }
    });

    //子女数
    $("#first_Customer_Info_childrenNum").numberspinner({
        min: 0,
        max: 9,
        validType: ["range[0,9,'子女数']"],
        missingMessage: "请填写子女数！",
    });

    //最高学历
    $("#first_Customer_Info_qualification").combobox({
        required: true,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        prompt: '请选择',
        missingMessage: '请选择最高学历',
        //validType: ["comboboxUnSelectWithParam['最高学历']"],
        value: vm.applyInfo.basicInfoVO.personInfoVO.qualification,
        data: getEnum('EducationType')
    });

    //毕业时间
    $("#first_Customer_Info_graduationDate").datebox({
        value: isNotNull(vm.applyInfo.basicInfoVO.personInfoVO.graduationDate) ? moment(vm.applyInfo.basicInfoVO.personInfoVO.graduationDate).format('YYYY-MM-DD') : '',
        required: vm.applyInfo.applyInfoVO.productCd == '00017' ? true : false,
        prompt: '毕业时间',
        editable: false,
        validType: ["compareTimeLimitWithParam['1970年1月', '毕业时间']", "compareNowDate['毕业时间']"],
        missingMessage: "请填写毕业时间！",

    }).datebox('calendar').calendar({
        validator: function (date) {
            var now = new Date();
            var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            return day >= date;
        }
    });

    //户籍地址（省、市、区）
    var birthPlaceAreaHandler = $("#firstCustomerInfo_birthplace_country_combobox").combobox('options').onChange;
    $("#firstCustomerInfo_birthplace_country_combobox").combobox({
        required: true,
        prompt: '-----区-----',
        missingMessage: "户籍地址输入不完整，下拉框均需选择，输入框不能为空！",
        onChange: function (newValue, oldValue) {
            reloadAddress('B');
            //addressSynchronization();
            birthPlaceAreaHandler.call(this, newValue, oldValue);
        }
    });

    var birthPlaceCityHandler = $("#firstCustomerInfo_birthplace_city_combobox").combobox('options').onChange;
    $("#firstCustomerInfo_birthplace_city_combobox").combobox( {
        required: true,
        prompt: '-----市-----',
        missingMessage: "户籍地址输入不完整，下拉框均需选择，输入框不能为空！",
        onChange: function (newValue, oldValue) {
            reloadAddress('B');
            //addressSynchronization();
            birthPlaceCityHandler.call(this , newValue, oldValue);
        }
    });

    var birthPlaceProvinceHandler = $("#firstCustomerInfo_birthplace_province_combobox").combobox('options').onChange;
    $("#firstCustomerInfo_birthplace_province_combobox").combobox({
        required: true,
        prompt: '-----省-----',
        missingMessage: "户籍地址输入不完整，下拉框均需选择，输入框不能为空！",
        onChange: function (newValue, oldValue) {
            reloadAddress('B');
            //addressSynchronization();
            birthPlaceProvinceHandler.call(this, newValue, oldValue);
        }
    });

    //户籍地址具体信息
    $("#firstCustomerInfo_birthplace_idIssuerAddress").textbox({
        prompt: 'xxx镇/乡 xxx村 xxx路/街道',
        required: true,
        validType: ["chineseAndNoSpace['户籍地址输入框中']", "maxLength[200]", "duplicateAddress['#birthplaceAddress', '#familyAddress', 'input:checked[name=homeSameRegistered]','家庭地址', '户籍地址']"],
        multiline: true,
        width: 388,
        tipPosition: 'top',
        missingMessage: "户籍地址输入不完整，下拉框均需选择，输入框不能为空！",
        onChange: function (newValue, oldValue) {
            reloadAddress('B');
            addressSynchronization();
        }
    });

    //家庭地址（区）
    var familyCountryHandler = $("#firstCustomerInfo_family_country_combobox").combobox('options').onChange;
    $("#firstCustomerInfo_family_country_combobox").combobox({
        readonly: vm.applyInfo.basicInfoVO.personInfoVO.homeSameRegistered == '0' ? true : false,
        required: true,
        prompt: '-----区-----',
        tipPosition: 'top',
        missingMessage: "家庭地址输入不完整，下拉框均需选择，输入框不能为空！",
        onChange: function (newValue, oldValue) {
            reloadAddress('C');
            familyCountryHandler.call(this, newValue, oldValue);
        }
    });

    //家庭地址（市）
    var familyCityHandler = $("#firstCustomerInfo_family_city_combobox").combobox('options').onChange;
    $("#firstCustomerInfo_family_city_combobox").combobox({
        readonly: vm.applyInfo.basicInfoVO.personInfoVO.homeSameRegistered == '0' ? true : false,
        required: true,
        prompt: '-----市-----',
        tipPosition: 'top',
        missingMessage: "家庭地址输入不完整，下拉框均需选择，输入框不能为空！",
        onChange: function (newValue, oldValue) {
            reloadAddress('C');
            familyCityHandler.call(this, newValue, oldValue);
        }
    });

    //家庭地址（省）
    var familyProvinceHandler = $("#firstCustomerInfo_family_province_combobox").combobox('options').onChange;
    $("#firstCustomerInfo_family_province_combobox").combobox({
        readonly: vm.applyInfo.basicInfoVO.personInfoVO.homeSameRegistered == '0' ? true : false,
        required: true,
        prompt: '-----省-----',
        tipPosition: 'top',
        missingMessage: "家庭地址输入不完整，下拉框均需选择，输入框不能为空！",
        onChange: function (newValue, oldValue) {
            reloadAddress('C');
            familyProvinceHandler.call(this, newValue, oldValue);
        }
    });

    //家庭地址具体信息
    $("#firstCustomerInfo_family_addr").textbox({
        readonly: vm.applyInfo.basicInfoVO.personInfoVO.homeSameRegistered == '0' ? true : false,
        prompt: 'xxx镇/乡 xxx村 xxx路/街道',
        required: true,
        multiline: true,
        width: 388,
        tipPosition: 'top',
        missingMessage: "家庭地址输入不完整，下拉框均需选择，输入框不能为空！",
        validType: ["chineseAndNoSpace['家庭地址输入框中']", "maxLength[200]", "duplicateAddress['#birthplaceAddress', '#familyAddress', 'input:checked[name=homeSameRegistered]', '家庭地址', '户籍地址']"],
        onChange: function (newValue, oldValue) {
            reloadAddress('C');
            changeFamily();
        }
    });

    reloadAddress('B');
    reloadAddress('C');

    //住宅邮编
    $("#firstCustomerInfo_basicInfo_personInfo_homePostcode").textbox({
        validType: ["postcode"],
        missingMessage: "请填写住宅邮编"
    });

    //住宅类型
    $("#first_customerInfo_houseType").combobox({
        required: true,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('HouseType'),
        prompt: '请选择',
        missingMessage: '请选择住宅类型',
        //validType: ["comboboxUnSelectWithParam['住宅类型']"],
        onChange: houseTypeCheck
    });

    //租金
    $("#first_customerInfo_houseRent").numberbox({
        groupSeparator: ',',
        precision: 0,
        buttonText: '元',
        validType: ["nonnegativePnum['']", "range[0, 99999999, '租金']"],
        missingMessage: "请输入租金！"
    });

    // 检查住宅类型是否为租房，如果为租房，则租金文本框不可编辑
    houseTypeCheck(vm.applyInfo.basicInfoVO.personInfoVO.houseType);

    //每月家庭支出
    $("#first_customerInfo_familyMonthPay").numberbox({
        groupSeparator: ',',
        required: true,
        precision: 0,
        buttonText: '元',
        validType: ["nonnegativePnum['']", "range[0, 99999999, '每月家庭支出']"],
        missingMessage: "请输入每月家庭支出/元！",
    });

    //可接受的月最高还款
    $("#first_customerInfo_monthMaxRepay").numberbox({
        groupSeparator: ',',
        required: true,
        precision: 0,
        buttonText: '元',
        validType: ["nonnegativePnum['']", "range[0, 99999999, '可接受的月最高还款']"],
        missingMessage: "请填写可接受的月最高还款!",
    });

    //户籍邮编
    $("#first_customerInfo_issuerPostcode").textbox({
        validType: ["postcode"],
        missingMessage: "请填写户籍邮编!",
    });

    //常用手机
    $("#first_customerInfo_cellphone").textbox({
        required: true,
        precision: 0,
        validType: ['mobile', 'samePhone', 'same["#first_customerInfo_cellphoneSec", "常用手机", "备用手机"]'],
        missingMessage: "请填写常用手机!",
    });

    //备用手机
    $("#first_customerInfo_cellphoneSec").textbox({
        precision: 0,
        validType: ['mobile', 'samePhone', 'same["#first_customerInfo_cellphone", "常用手机", "备用手机"]']
    });

    //宅电
    $("#first_customerInfo_homePhone1").textbox({
        // required: true,
        validType: ['telNum'],
        missingMessage:"请填写宅电!"
    });

    //QQ号
    $("#firstCustomerInfo_customerInfo_qqNum").numberbox({
        precision: 0,
        validType: ['QQ', 'maxLength[15]'],
    });

    //微信号
    $("#firstCustomerInfo_customerInfo_weChatNum").textbox({
        precision: 0,
        validType: ['weChat', 'maxLength[30]']
    });

    //电子邮箱
    $("#firstCustomerInfo_customerInfo_email").textbox({
        validType: ['email', 'maxLength[80]']
    });

    /*个人信息初始化end*/

    /*工作信息初始化start*/
    //单位名称
    $("#firstCustomerInfo_workInfo_corpName_input").textbox({
        required: true,
        multiline: true,
        width: 460,
        missingMessage: "请填写单位名称!",
        validType: ["notAllNumberAndLength[200, '单位名称']"]
    });

    //客户工作类型
    $("#firstCustomerInfo_cusWorkType_combobox").combobox({
        required: true,
        prompt: '请选择',
        missingMessage: '请选择客户工作类型',
        //validType: ["comboboxUnSelectWithParam['客户工作类型']"],
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('JobType'),
        onChange: function (newValue, oldValue) {
            vm.applyInfo.basicInfoVO.workInfoVO.cusWorkType = newValue;

            if (newValue == "00001") {           // 私营业主

                $('#first_customerPrivateOwner_div').panel({title: "私营业主信息"});     // 初始化title
                $("#first_customerPrivateOwner_div").show();                            // 显示私营业主信息
                $("#firstCustomer_corpStructure_combobox").combobox("reload", ctx.rootPath() + "/bmsBasiceInfo/findCodeByUnit?code=" + newValue); // 单位性质重新加载

            } else if (newValue == "00002") {    // 受薪人士

                $("#first_customerPrivateOwner_form").form("clear");                    // 清空私营业主表单
                $('#first_customerPrivateOwner_div').panel({title: ""});                // 清空title
                $("#first_customerPrivateOwner_div").hide();                            // 隐藏私营业主信息
                $("#firstCustomer_corpStructure_combobox").combobox("reload", ctx.rootPath() + "/bmsBasiceInfo/findCodeByUnit?code=" + newValue); // 单位性质重新加载


            } else if (newValue == "00003") {    // 自雇人士

                $('#first_customerPrivateOwner_div').panel({title: "私营业主信息"});     // 改变title
                $("#first_customerPrivateOwner_div").show();                            // 显示私营业主信息
                $("#firstCustomer_corpStructure_combobox").combobox("reload", ctx.rootPath() + "/bmsBasiceInfo/findCodeByUnit?code=" + newValue);  // 单位性质重新加载

            } else if (!isNotNull(newValue)) {// 客户工作类型选择为空时清空单位性质和职业

                $('#firstCustomer_corpStructure_combobox').combobox('clear', '');
                $('#firstCustomer_occupation_combobox').combobox('clear', '');
                $('#firstCustomer_corpStructure_combobox').combobox('loadData', '');
                $('#firstCustomer_occupation_combobox').combobox('loadData', '');
            }

            // 私营业主信息必填项验证
            var optionsSetUpDate = $("#first_customerPrivateOwner_setupDate").datebox('options');
            var optionsSharesScale = $("#sharesScale").numberbox('options');
            var optionsRegisterFunds = $("#registerFunds").numberbox('options');
            var optionsPriEnterPriseType= $("#first_customerInfo_priEnterpriseType").combobox('options');
            var optionsMonthAmt = $("#monthAmt").numberbox('options');

            $("#first_customerPrivateOwner_setupDate").datebox($.extend({}, optionsSetUpDate, {required: vm.isPrivateOwner}));
            $("#sharesScale").numberbox($.extend({}, optionsSharesScale, {required: vm.isPrivateOwner}));
            $("#registerFunds").numberbox($.extend({}, optionsRegisterFunds, {required: vm.isPrivateOwner}));
            $("#first_customerInfo_priEnterpriseType").combobox($.extend({}, optionsPriEnterPriseType, {required: vm.isPrivateOwner}));
            $("#monthAmt").numberbox($.extend({}, optionsMonthAmt, {required: vm.isPrivateOwner}));
        }
    });

    //单位地址（区）
    $("#firstCustomerInfo_company_country_combobox").combobox({
        prompt: '-----区-----',
        tipPosition: 'top',
        missingMessage: "单位地址输入不完整，下拉框均需选择，输入框不能为空！"
    });

    //单位地址（市）
    $("#firstCustomerInfo_company_city_combobox").combobox({
        prompt: '-----市-----',
        tipPosition: 'top',
        missingMessage: "单位地址输入不完整，下拉框均需选择，输入框不能为空！"
    });

    //单位地址（省）
    $("#firstCustomerInfo_company_province_combobox").combobox({
        required: true,
        prompt: '-----省-----',
        tipPosition: 'top',
        missingMessage: "单位地址输入不完整，下拉框均需选择，输入框不能为空！"
    });


    //单位地址具体信息
    $("#corpAddress").textbox({
        required: true,
        multiline: true,
        width: 392,
        prompt: 'xxx镇/乡 xxx村 xxx路/街道',
        tipPosition: 'top',
        validType: ["chineseAndNoSpace['单位地址输入框中']", "maxLength[200]"],
        missingMessage: "单位地址输入不完整，下拉框均需选择，输入框不能为空！"
    });

    //工商网信息
    $("#firstCustomer_businessNetWork_combobox").combobox({
        required: true,
        //validType: ["comboboxUnSelectWithParam['工商网信息']"],
        prompt: '请选择',
        missingMessage: '请选择工商网信息',
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('BusinessNetWork')
    });

    // 单位性质
    $("#firstCustomer_corpStructure_combobox").combobox({
        required: true,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        //validType: ["comboboxUnSelectWithParam['单位性质']"],
        prompt: '请选择',
        missingMessage: '请选择单位性质',
        url: ctx.rootPath() + "/bmsBasiceInfo/findCodeByUnit?code=" + $("#firstCustomerInfo_cusWorkType_combobox").combobox('getValue'),
        onChange: function (newValue, oldValue) {
            // 单位性质下拉框该表，职务下拉框联动改变
            if (isNotNull(newValue)) {
                //var _options = $("#firstCustomer_corpPost_combobox").combobox('options');
                if ('00001' == newValue || '00002' == newValue || "00003" == newValue) {
                    //$.extend({}, _options, {data: getEnum('EmpPositionAttrType'), url: null});    // 获取政府机构/事业单位/国企的职业列表
                    $("#firstCustomer_corpPost_combobox").combobox("reload", ctx.rootPath() + "/bmsBasiceInfo/getEnumCode?emnuType=EmpPositionAttrType");
                } else {
                    //$.extend({}, _options, {data: getEnum('noGovInstitution'), url: null});       // 外资/民营/私营/其它/其它/个体的职业列表
                    $("#firstCustomer_corpPost_combobox").combobox("reload", ctx.rootPath() + "/bmsBasiceInfo/getEnumCode?emnuType=noGovInstitution");
                }
                //$("#firstCustomer_corpPost_combobox").combobox(_options);

                // 如果客户工作类型是"受薪人士"，且单位性质是"个体",则职业下拉框只读
                var cusWorkType = $("#firstCustomerInfo_cusWorkType_combobox").combobox('getValue');
                if (cusWorkType == '00002' && newValue == '00009') {
                    $('#firstCustomer_occupation_combobox').combobox('readonly', true);
                } else {
                    $('#firstCustomer_occupation_combobox').combobox('readonly', false);
                }
                $('#firstCustomer_occupation_combobox').combobox("reload", ctx.rootPath() + "/bmsBasiceInfo/findCodeByProfession?code=" + newValue + "&parentCode=" + cusWorkType);

            } else {
                // 单位性质下拉框选空时，职业下拉框也要清空
                $('#firstCustomer_occupation_combobox').combobox('clear', '');
                $('#firstCustomer_occupation_combobox').combobox('loadData', '');
            }
        },
        onShowPanel: function () {
            var cusWorkType = $("#firstCustomerInfo_cusWorkType_combobox").combobox('getValue');
            if (!isNotNull(cusWorkType)) {
                $.info('提示', '请先填写客户工作类型', 'warning');
            }
        },
        onLoadSuccess: function () {
            var corpStructure = $(this).combobox("getValue");
            if (isNotNull(corpStructure)) {
                if ('00001' == corpStructure || '00002' == corpStructure) { // 获取政府机构/事业单位的职业列表
                    $("#firstCustomer_corpPost_combobox").combobox("reload", ctx.rootPath() + "/bmsBasiceInfo/getEnumCode?emnuType=EmpPositionAttrType");
                } else {
                    $("#firstCustomer_corpPost_combobox").combobox("reload", ctx.rootPath() + "/bmsBasiceInfo/getEnumCode?emnuType=noGovInstitution");
                }
                var defVal = $("#firstCustomer_corpStructure_combobox").combobox("options").finder.getRow($("#firstCustomer_corpStructure_combobox").get(0), corpStructure);
                if (!isNotNull(defVal)) {
                    $("#firstCustomer_corpStructure_combobox").combobox("setValue", "");
                }
                // 单位性质改变职业也随着改变
                if ($("#firstCustomerInfo_cusWorkType_combobox").combobox('getValue') == '00002' && corpStructure == '00009') {
                    $('#firstCustomer_occupation_combobox').combobox('readonly', true);
                } else {
                    $('#firstCustomer_occupation_combobox').combobox('readonly', false);
                }
                $('#firstCustomer_occupation_combobox').combobox("reload", ctx.rootPath() + "/bmsBasiceInfo/findCodeByProfession?code=" + corpStructure + "&parentCode=" + $("#firstCustomerInfo_cusWorkType_combobox").combobox('getValue'));

            } else {
                // $("#firstCustomer_corpPost_combobox").combobox('clear');
                $('#firstCustomer_occupation_combobox').combobox('clear', '');
                $('#firstCustomer_occupation_combobox').combobox('loadData', '');
            }
        }
    });

    // 职业
    $("#firstCustomer_occupation_combobox").combobox({
        required: true,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        //validType: ["comboboxUnSelectWithParam['职业']"],
        tipPosition: 'top',
        prompt: '请选择',
        missingMessage: '请选择职业',
        onShowPanel: function () {
            var corpStructure = $("#firstCustomer_corpStructure_combobox").combobox('getValue');
            var cusWorkType = $("#firstCustomerInfo_cusWorkType_combobox").combobox('getValue');
            if (!isNotNull(cusWorkType)) {
                $.info('提示', '请先填写客户工作类型', 'warning');
            } else if (!isNotNull(corpStructure)) {
                $.info('提示', '请先填写单位性质', 'warning');
            }
        },
        onLoadSuccess: function () {
            var defValue = $(this).combobox("getValue");
            if (isNotNull(defValue)) {
                var defVal = $("#firstCustomer_occupation_combobox").combobox("options").finder.getRow($("#firstCustomer_occupation_combobox").get(0), defValue);
                if (!isNotNull(defVal)) {
                    $("#firstCustomer_occupation_combobox").combobox("setValue", "");
                }
            }
            var parentCode = $("#firstCustomerInfo_cusWorkType_combobox").combobox('getValue');
            var code = $("#firstCustomer_corpStructure_combobox").combobox('getValue');
            if ('00009' == code && parentCode == '00002') {// 客户工作类型为受薪人士且单位性质是个体时职业为工薪不可修改
                $("#firstCustomer_occupation_combobox").combobox('setValue', '00001');
            }
        }
    });

    //任职部门
    $("#corpDepapment").textbox({
        required: true,
        missingMessage:"请填写任职部门！",
        validType: ["maxLength[200]"]
    });

    // 职务
    $("#firstCustomer_corpPost_combobox").combobox({
        required: true,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        //validType: ["comboboxUnSelectWithParam['职务']"],
        prompt: '请选择',
        missingMessage: '请选择职务',
        onLoadSuccess: function () {
            var defValue = $(this).combobox("getValue");
            if (isNotNull(defValue)) {
                var defVal = $("#firstCustomer_corpPost_combobox").combobox("options").finder.getRow($("#firstCustomer_corpPost_combobox").get(0), defValue);
                if (!isNotNull(defVal)) {
                    $("#firstCustomer_corpPost_combobox").combobox("setValue", "");
                }
            }
        }
    });

    // 单位行业类别
    $("#corpType").combobox({
        required: true,
        //validType: ["comboboxUnSelectWithParam['单位行业类别']"],
        prompt: '请选择',
        missingMessage: '请选择单位行业类别',
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('EmpType')
    });

    // 入职时间
    $("#corpStandFrom").datebox({
        value: isNotNull(vm.applyInfo.basicInfoVO.workInfoVO.corpStandFrom) ? moment(vm.applyInfo.basicInfoVO.workInfoVO.corpStandFrom).format('YYYY-MM-DD') : '',
        required: true,
        editable: false,
        prompt: '入职时间',
        missingMessage: "请选择入职时间!",
        validType: ["compareTimeLimitWithParam['1970年1月', '入职时间']", "compareNowDate['入职时间']"],
    }).datebox('calendar').calendar({
        validator: function (date) {
            var now = new Date();
            var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            return day >= date;
        }
    });

    // 发薪方式
    $("#corpPayWay").combobox({
        required: true,
        //validType: ["comboboxUnSelectWithParam['发薪方式']"],
        prompt: '请选择',
        missingMessage: '请选择发薪方式',
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('CorpPayWay')
    });

    //单电
    $("#corpPhone").textbox({
        required: true,
        missingMessage: "请填写单位电话!",
        validType: ['telNum']
    });

    //单电2
    $("#corpPhoneSec").textbox({
        missingMessage: "请填写单位电话!",
        validType: ['telNum']
    });

    //单位邮编
    $("#corpPostcode").textbox({
        validType: ["postcode"]
    });

    //单位月收入
    $("#monthSalary").numberbox({
        groupSeparator: ',',
        required: true,
        precision: 0,
        buttonText: '元',
        missingMessage: "请填写单位月收入!",
        validType: ["nonnegativePnum['单位月收入']","range[0, 99999999, '单位月收入']"]
    });

    //其他月收入
    $("#otherIncome").numberbox({
        groupSeparator: ',',
        precision: 0,
        buttonText: '元',
        value: vm.applyInfo.basicInfoVO.workInfoVO.otherIncome,
        validType: ["nonnegativePnum['其他月收入']", "range[0, 99999999, '其他月收入']"]
    });


    /*工作信息初始化end*/

    /*私营业主信息初始化start*/

    //成立时间
    $("#first_customerPrivateOwner_setupDate").datebox({
        value: isNotNull(vm.applyInfo.basicInfoVO.privateOwnerInfoVO.setupDate) ? moment(vm.applyInfo.basicInfoVO.privateOwnerInfoVO.setupDate).format('YYYY-MM-DD') : '',
        required: vm.isPrivateOwner,
        editable: false,
        width:210,
        prompt: '私营业主成立时间',
        missingMessage: "请选择成立时间!",
        tipPosition: 'top',
        validType: ["compareTimeLimitWithParam['1960年1月', '私营业主成立时间']", "compareNowDate['私营业主成立时间']"]
    }).datebox('calendar').calendar({
        validator: function (date) {
            var now = new Date();
            var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            return day >= date;
        }
    });

    //占股比例
    $("#sharesScale").numberbox({
        required: vm.isPrivateOwner,
        precision: 2,
        buttonText: '%',
        min: 0,
        max: 100,
        width:210,
        missingMessage: "请填写占股比例!",
        tipPosition: 'top',
        validType: ["decimalsVerified[0,100,'占股比例']"],
    });

    //注册资本
    $("#registerFunds").numberbox({
        required: vm.isPrivateOwner,
        groupSeparator: ',',
        precision: 0,
        width:210,
        buttonText: '元',
        missingMessage: "请填写注册资本!",
        tipPosition: 'top',
        validType: ["range[0, 999999999, '注册资本','9个9']"]
    });

    //私营企业类型
    $("#first_customerInfo_priEnterpriseType").combobox({
        required: vm.isPrivateOwner,
        width:210,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        prompt: '请选择',
        missingMessage: '请选择私营企业类型',
        tipPosition: 'top',
        data: getEnum('PriEnterpriseType')
    });

    //每月净利润额
    $("#monthAmt").numberbox({
        required: vm.isPrivateOwner,
        width:210,
        groupSeparator: ',',
        precision: 0,
        buttonText: '元',
        missingMessage: "请填写每月净利润额!",
        tipPosition: 'top',
        validType: ["nonnegativePnum['每月净利润额']", "range[0, 99999999, '每月净利润额']"]
    });

    /*私营业主信息初始化end*/

    /**基本信息end*/

    /**************** 联系人信息初始化start ****************/
    // 所有的姓名
    $(".peopleName").each(function (ind, val) {
        $(val).textbox({
            required: true,
            validType: isRelation(ind) ? ["peopleNameWithParam['配偶姓名']","contactName"] : ["peopleNameWithParam['联系人姓名']","contactName"],
            missingMessage: isRelation(ind) ? "请填写姓名!" : "请填写联系人姓名!"
        });
    });

    // 与申请人关系
    $(".rlstionsp").each(function (ind, val) {
        $(val).combobox({
            required: true,
            editable: false,
            valueField: 'code',
            textField: 'nameCN',
            data: getEnum('Relationship'),
            prompt: '请选择',
            missingMessage: '请选择与申请人关系'
            //validType: ["comboboxUnSelectWithParam['与申请人关系']"]
        });
    });

    // 常用手机号码
    $(".firstPhone").each(function (ind, val) {
        $(val).textbox({
            validType: ["mobile", "samePhoneWithName['firstPhone']"],
            required: true,
            missingMessage: "请填写常用手机号码!"
        });
    });

    //备用手机号码
    $(".secondPhone").each(function (ind, val) {
        $(val).textbox({
            validType: ["mobile", "samePhoneWithName['secondPhone']"]
        });
    });

    //单电
    $(".contactCorpPhone").each(function (ind, val) {
        $(val).textbox({
            validType: ['telNum']
        });
    });

    //单位名称
    $(".contactEmpName").each(function (ind, val) {
        $(val).textbox({
            required: isRelation(ind) ? true : false,
            width: 415,
            validType: ["notAllNumberAndLength[200, '单位名称']"],
            missingMessage: "请填写单位名称!",
        });
    });

    // 是否外籍人士，是-身份证不必填，否-身份证必填
    $('input[type=radio][name="ifForeignPenple"]').on('change', function () {
        var options = $('#first_contactInfoVOList_div_spouseIdCard').textbox('options');
        // 标记是否是直通车进件并且是TOPUP类型或RELOAN类型(用于判断配偶外籍人士身份证不必填)
        if(vm.isDirectApp){
        	options = $.extend({}, options, {required: false});
        }else{
        	options = this.value == 'Y' ? $.extend({}, options, {required: false}) : $.extend({}, options, {required: true});
        }
        $('#first_contactInfoVOList_div_spouseIdCard').textbox(options);
    });

    /**************** 联系人信息初始化end ****************/
}

/**
 * 定位客户信息tab页面
 * @param tabName(base, contact, assets)
 */
function focusTab(tabName) {
    switch (tabName) {
        case 'base':
            $('#firstCustomerInfo_tabs').tabs('select', 0);
            break;
        case 'contact':
            $('#firstCustomerInfo_tabs').tabs('select', 1);
            break;
        case 'assets':
            $('#firstCustomerInfo_tabs').tabs('select', 2);
            break;
    }
}

function isRelation(ind) {
    if ('00013' == vm.applyInfo.contactInfoVOList[ind].contactRelation) {
        return true;
    }
    return false;
}

/**
 * 判断资产面板是否需要折叠
 * @param assets  资产信息
 * @param code    资产类型
 * @returns {boolean}
 */
function assetsPanelShow(assets, code) {
    // 资产信息不为空的时候，默认展开该资产的panel
    if(assets.ifEmpty == 1){
        return 'collapsible: true, collapsed: false';
    }

    // 校验客户申请的产品对应的资产配置是否包含该code
    var target = _.find(vm.assetsList, function (item) {
        return item.code == code;
    });

    // 包含则默认展开该资产panel
    if(target != null){
        return 'collapsible: true, collapsed: false';
    }

    return 'collapsible: true, collapsed: true';
}

/**
 * 判断资产是否为必填项
 * @param code 资产code
 */
function assetsIsRequired(code){
    var assets = _.find(vm.assetsList, function (assets) {
        return assets.code == code;
    });

    if(isNotNull(assets) && !vm.flagValidate){
        return true;
    }

    return false;
}

/**
 * 扩展easyui空间选项
 * @param id
 * @param options
 */
function extendOption(id, options){
    var defaultOptions = $('#' + id).combobox('options');
    options = $.extend({}, defaultOptions, options);
    $('#' + id).combobox(options);
}

/**
 * 重新加载地址信息
 * @param type B-户籍地址，F-家庭住址
 */
function reloadAddress(type){
    if(type == 'B'){
        var province = $('#firstCustomerInfo_birthplace_province_combobox').combobox('getValue');
        var city = $('#firstCustomerInfo_birthplace_city_combobox').combobox('getValue');
        var area = $('#firstCustomerInfo_birthplace_country_combobox').combobox('getValue');
        var other = $('#firstCustomerInfo_birthplace_idIssuerAddress').textbox('getValue');
        $('#birthplaceAddress').val(other + province + city + area);
    }else{
        var province = $('#firstCustomerInfo_family_province_combobox').combobox('getValue');
        var city = $('#firstCustomerInfo_family_city_combobox').combobox('getValue');
        var area = $('#firstCustomerInfo_family_country_combobox').combobox('getValue');
        var other = $('#firstCustomerInfo_family_addr').textbox('getValue');
        $('#familyAddress').val(other + province + city + area);
    }
    $("#firstCustomerInfo_birthplace_idIssuerAddress").textbox("validate");
    $("#firstCustomerInfo_family_addr").textbox("validate");
}