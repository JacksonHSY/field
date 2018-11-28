var vm;
function pageInit(option) {
    var defaultOption = {
        $id: 'moneyCustomerInfo',
        loanNo: '',
        applyInfo: {},// 申请信息
        updateVersion: false,// 版本号是否更改
        assetsList: [],// 需要显示的资产信息
        save: save,
        assetsPanelShow: assetsPanelShow,
        assetsIsRequired: assetsIsRequired,
        isDirectApp: {},//判断是否是直通车营业部进件或直通车营业部录单且客户类型是RELAON或TOPUP的申请
        $computed: {
            showPrivate: function () {    // 是否显示私营业主信息
                return vm.applyInfo.applicantInfo.workInfo.cusWorkType != '00002';
            },
            isPrivateOwner: function () {   // 客户工作类型是否为"私营业主"
                return vm.applyInfo.applicantInfo.workInfo.cusWorkType == '00001';
            },
            birthAddress: function () {     // 户籍地址
                var provinceId = vm.applyInfo.applicantInfo.personalInfo.issuerStateId;
                var cityId = vm.applyInfo.applicantInfo.personalInfo.issuerCityId;
                var areaId = vm.applyInfo.applicantInfo.personalInfo.issuerZoneId;
                var other = vm.applyInfo.applicantInfo.personalInfo.idIssuerAddress;
                return other + provinceId + cityId + areaId;
            },
            familyAddress: function () {    // 住宅住址
                var provinceId = vm.applyInfo.applicantInfo.personalInfo.homeStateId;
                var cityId = vm.applyInfo.applicantInfo.personalInfo.homeCityId;
                var areaId = vm.applyInfo.applicantInfo.personalInfo.homeZoneId;
                var other = vm.applyInfo.applicantInfo.personalInfo.homeAddress;
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
        // 个人信息户籍地址
        inintArea(data, "#moneyCustomerInfo_birthplace_province_combobox", "#moneyCustomerInfo_birthplace_city_combobox", "#moneyCustomerInfo_birthplace_country_combobox", addressSynchronization);
        // 住宅地址
        inintArea(data, "#moneyCustomerInfo_family_province_combobox", "#moneyCustomerInfo_family_city_combobox", "#moneyCustomerInfo_family_country_combobox",changeFamily);
        // 单位地址
        inintArea(data, "#moneyCustomerInfo_company_province_combobox", "#moneyCustomerInfo_company_city_combobox", "#moneyCustomerInfo_company_country_combobox");
    });
    // 初始化个人信息、联系人信息
    initCustomerInfo();
    // 住宅地址同户籍地址
    $("[name='homeSameRegistered']","#money_customerInfo_form").on("change", function (e) {
        var yesOrNo = $(e.target).val();
        if (yesOrNo == '0') {
            // 选择是时，房产地址回填住宅地址内容，显示为文本不可编辑,如果户籍地址填写不全弹窗提示
            var idIssuerProvince = $("#moneyCustomerInfo_birthplace_province_combobox").combobox("getValue");// 户籍省
            var idIssuerCity = $("#moneyCustomerInfo_birthplace_city_combobox").combobox("getValue");// 户籍市
            var idIssuerCountry = $("#moneyCustomerInfo_birthplace_country_combobox").combobox("getValue");// 户籍区
            var idIssuerAddress = $("#moneyCustomerInfo_birthplace_idIssuerAddress").val();// 户籍地址
            if (isEmpty(idIssuerProvince) || isEmpty(idIssuerCity) || isEmpty(idIssuerCountry) || isEmpty(idIssuerAddress)) {
                $.info("提示", "户籍地址不完整，请先填写完整户籍地址!", 'warning');
                $("[name='homeSameRegistered']").get(1).checked = true;
                return;
            }
            $("#moneyCustomerInfo_family_province_combobox").combobox('setValue', idIssuerProvince);
            $("#moneyCustomerInfo_family_city_combobox").combobox("setValue", idIssuerCity);
            $("#moneyCustomerInfo_family_country_combobox").combobox("setValue", idIssuerCountry);
            $("#moneyCustomerInfo_family_addr").textbox('setValue', idIssuerAddress);

            $('#moneyCustomerInfo_family_province_combobox').combobox("readonly", true);
            $('#moneyCustomerInfo_family_city_combobox').combobox("readonly", true);
            $('#moneyCustomerInfo_family_country_combobox').combobox("readonly", true);
            $('#moneyCustomerInfo_family_addr').textbox("readonly", true);
        } else {
            // 选择否时，省、市、区/县为下拉框，详细地址为文本框，可编辑
            $("#moneyCustomerInfo_family_province_combobox").combobox("setValue", "");
            $("#moneyCustomerInfo_family_city_combobox").combobox("setValue", "");
            $("#moneyCustomerInfo_family_country_combobox").combobox("setValue", "");
            $("#moneyCustomerInfo_family_addr").textbox('setValue', '');

            $('#moneyCustomerInfo_family_province_combobox').combobox("readonly", false);
            $('#moneyCustomerInfo_family_city_combobox').combobox("readonly", false);
            $('#moneyCustomerInfo_family_country_combobox').combobox("readonly", false);
            $('#moneyCustomerInfo_family_addr').textbox("readonly", false);
        }

        $("#moneyCustomerInfo_birthplace_idIssuerAddress").textbox("validate");
        $("#moneyCustomerInfo_family_addr").textbox("validate");
    });

    // 配偶为外籍的身份证号码校验
    $("[name='ifForeignPenple']","#money_contactInfoVOList_div").on("change", function (e) {
        changeCardType(e.target);
    });

    // 回显房产地址radio
    var isEqual = $("#money_isEqualHomeAddr").val();
    if ('Y' == isEqual) {
        $("input[name='estateSameRegistered']").eq(0).attr("checked", "checked");
        $("input[name='estateSameRegistered']").eq(1).removeAttr("checked");
        $('#moneyCustomerInfo_house_province_combobox').combobox("readonly", true);
        $('#moneyCustomerInfo_house_city_combobox').combobox("readonly", true);
        $('#moneyCustomerInfo_house_country_combobox').combobox("readonly", true);
        $('#money_customerInfo_estateAddress').textbox("readonly", true);
    } else if ('N' == isEqual) {
        $("input[name='estateSameRegistered']").eq(0).removeAttr("checked");
        $("input[name='estateSameRegistered']").eq(1).attr("checked", "checked");
        $('#moneyCustomerInfo_house_province_combobox').combobox("readonly", false);
        $('#moneyCustomerInfo_house_city_combobox').combobox("readonly", false);
        $('#moneyCustomerInfo_house_country_combobox').combobox("readonly", false);
        $('#money_customerInfo_estateAddress').textbox("readonly", false);
    }
    // 是否显示配偶信息
    marriageCheck(vm.applyInfo.applicantInfo.personalInfo.maritalStatus, null);
});


/**
 * 保存客户信息
 * @author dmz
 * @date 2017年3月23日
 */
var firstCustomerMarkSubmit = false;
function save(loanBaseId, loanNo,productCd, version) {
    //客户基本信息
    var baseCustomerInfo = $("#money_customerInfo_form").serializeObject();
    //客户房产信息
    var customerHomeInfo = $("#money_customerHome_form").serializeObject();
    if (isNotNull(baseCustomerInfo)) {
        //住宅地址是否同房产地址必选校验
        if (isEmpty(baseCustomerInfo.homeSameRegistered)) {
            $.info('提示信息', "请选择住宅地址是否同户籍地址!", "warning");
            return false;
        }
        //基本信息中住宅地址是否同户籍地址选择为否，两个地址不能相同
        if (baseCustomerInfo.homeSameRegistered == '1') {
            if (baseCustomerInfo.issuerStateId == baseCustomerInfo.homeStateId && baseCustomerInfo.issuerCityId == baseCustomerInfo.homeCityId &&
                baseCustomerInfo.issuerZoneId == baseCustomerInfo.homeZoneId && baseCustomerInfo.idIssuerAddress == baseCustomerInfo.homeAddress) {
                $.info('提示信息', "住宅地址是否同户籍地址选择为否，两个地址不能相同，请重新填写！", "warning");
                return false;
            }
        }
        //资产信息中房产信息中住宅地址是否同房产地址选择为否，两个地址不能相同
        if (isNotNull(customerHomeInfo) && customerHomeInfo.estateSameRegistered == 'N') {
            if (baseCustomerInfo.homeStateId == customerHomeInfo.estateStateId && baseCustomerInfo.homeCityId == customerHomeInfo.estateCityId &&
                baseCustomerInfo.homeZoneId == customerHomeInfo.estateZoneId && baseCustomerInfo.homeAddress == customerHomeInfo.estateAddress) {

                $.info('提示信息', "住宅地址是否同房产地址选择为否，两个地址不能相同，请重新填写！", "warning");
                return false;
            }
        }
    }
    if (!firstCustomerMarkSubmit) {
        // 婚姻状况和联系人的校验
        var contactInfoLen = $('#money_contactInfoVOList_len').val();
        var maritalStatus = $('#money_Customer_Info_maritalStatus').combobox('getValues');
        //获取联系人关系父母信息 和 单位信息 个数
        var parentNum = 0;// 关系人父母不能超过四个
        var corporationNum = 0;// 联系人 单位信息不能少于3个
        $("#money_contactInfoVOList_div").find("form").each(function (ind, formObj) {
            var relationObj = $(formObj).find(".rlstionsp");
            if (relationObj.length > 0) {
                var relationValue =  $(relationObj).combobox('getValue');// 联系人关系
                if ('00001' == relationValue) {// 父母
                    parentNum++;
                }
            }
            var corporationName = $(formObj).find(".contactEmpName").textbox('getValue');// 单位信息
            if (isNotNull(corporationName)) {
                corporationNum++;
            }
        });
        // 联系人关系父母不能超过四个
        if (parentNum > 4) {
            focusTab('contact');
            $.info('提示信息', "不能有4条以上的父母信息！", "warning");
            return false;
        }
        // 至少填写3个联系人单位名称
        if (corporationNum < 3) {
            focusTab('contact');
            $.info('提示信息', "至少填写3个联系人单位名称！", "warning");
            return false;
        }

        // 若客户婚姻状况为“未婚”、“离异”、“丧偶”，若联系人关系中存在“父母”、“子女”，要求必填一个“父母”/“子女”关系联系人的单位名称
        if (maritalStatus == '00001' || maritalStatus == '00003' || maritalStatus == '00005') {
            var parentNum = 0;
            var companyNum = 0;
            $("#money_contactInfoVOList_div").find(".contactForm").each(function (index, element) {
                var $relationship = $('.rlstionsp', $(element));
                if ($relationship && ($relationship.combobox('getValue') == '00001' || $relationship.combobox('getValue') == '00002')) {// “父母”、“子女”
                    var companyName = $('.contactEmpName', $(element)).textbox('getValue');
                    if (isNotNull(companyName)) {
                        companyNum++;
                    }
                    parentNum++;
                }
            });

            if (parentNum > 0 && companyNum == 0) {
                $.info('提示信息', "至少填写1个父母或子女单位名称！", "warning");
                return false;
            }
        }

        // 验证基本信息
        if (!$("#money_customerInfo_form").form("validate")) {
            focusTab('base');
            $("#money_customerInfo_form").form("validate");
            return false;
        }

        // 验证工作信息
        if (!$("#money_customerWork_form").form("validate")) {
            focusTab('base');
            $("#money_customerWork_form").form("validate");
            return false;
        }

        // 验证私营业主信息
        if (!$("#money_customerPrivateOwner_form").form("validate")) {
            focusTab('base');
            $("#money_customerPrivateOwner_form").form("validate");
            return false;
        }

        // 验证客户信息联系人手机
        var validateContact = true;
        var customerPhone = $("#money_customerInfo_cellphone").val();       //用户常用手机
        var customerPhoneSec = $("#money_customerInfo_cellphoneSec").val(); //用户备用手机
        //客户常用手机和备用手机相同
        if (customerPhone == customerPhoneSec) {
            focusTab('base');
            $.info('提示信息', "客户常用手机和备用手机号码相同", "warning");
            return false;
        }

        // 验证客户常用手机和备用手机，是否与联系人的常用手机和备用手机重复
        $.each($("#money_contactInfoVOList_div").find("form"), function (ind, val) {
            var contacts = $(val).serializeObject();
            //联系人常用手机和用户常用手机号码相同
            if (contacts.contactCellPhone == customerPhone) {
                focusTab('contact');
                $.info('提示信息', "客户常用手机和" + contacts.contactName + "常用手机号码一致，不可重复录入！", "warning");
                validateContact = false;
                return false;
            }
            //联系人备用手机和用户常用手机号码相同
            if (contacts.contactCellPhone_1 == customerPhone) {
                focusTab('contact');
                $.info('提示信息', "客户常用手机和" + contacts.contactName + "备用手机号码一致，不可重复录入！", "warning");
                validateContact = false;
                return false;
            }
            //联系人常用手机和用户备用手机号码相同
            if (contacts.contactCellPhone == customerPhoneSec) {
                focusTab('contact');
                $.info('提示信息', "客户备用手机和" + contacts.contactName + "常用手机号码一致，不可重复录入！", "warning");
                validateContact = false;
                return false;
            }
            //联系人备用手机和用户备用手机号码相同
            if (contacts.contactCellPhone_1 == customerPhoneSec) {
                focusTab('contact');
                $.info('提示信息', "客户备用手机和" + contacts.contactName + "备用手机号码一致，不可重复录入！", "warning");
                validateContact = false;
                return false;
            }

            if (!$(val).form("validate")) {
                focusTab('contact');
                $(val).form("validate");
                validateContact = false;
                return false;
            }
        });
        if (!validateContact) {// 校验联系人
            return false;
        }

        var personInfoVO = $("#money_customerInfo_form").serializeObject();// 基本信息
        personInfoVO.gender = vm.applyInfo.applicantInfo.personalInfo.gender;
        personInfoVO.age = vm.applyInfo.applicantInfo.personalInfo.age;
        personInfoVO.idNo = vm.applyInfo.applicantInfo.personalInfo.idNo;

        var workInfoVO = $("#money_customerWork_form").serializeObject();// 工作信息
        var privateOwnerInfoVO = $("#money_customerPrivateOwner_form").serializeObject();// 私营业主信息
        var contactInfoVOList = new Array();// 联系人信息
        var contactInfoFlag = null; // 标识是否知晓贷款
        var _sequenceNum = 2;	// 联系人序列号
        avalon.each($("#money_contactInfoVOList_div").find("form"), function (ind, val) {
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
            focusTab('contact');
            if (1 == contactInfoFlag) {
                $.info('提示', '请选择配偶信息是否知晓贷款!', 'warning');
            } else {
                $.info('提示', '请选择第' + (contactInfoFlag - 1) + '联系人是否知晓贷款!', 'warning');
            }
            return;
        }
        if (contactInfoVOList.length < 5) {
            focusTab('contact');
            $.info('提示信息', "联系人不能少于5个，请添加联系人！", "warning");
            return;
        }






        if (isNotNull(privateOwnerInfoVO.setupDate)) {//私营业成立时间
            privateOwnerInfoVO.setupDate = privateOwnerInfoVO.setupDate + "-01";
        }

        if (isNotNull(workInfoVO.corpStandFrom)) {//入职时间
            workInfoVO.corpStandFrom = workInfoVO.corpStandFrom + "-01";
        }
        // 客户基本信息
        var applicantInfo = new Object();
        applicantInfo.personalInfo = personInfoVO;
        applicantInfo.workInfo = workInfoVO;
        applicantInfo.privateOwnerInfo = privateOwnerInfoVO;
        // 客户申请信息
        var baseInfo = new Object();
        baseInfo.loanNo= loanNo;
        baseInfo.loanBaseId=loanBaseId;
        baseInfo.version = version;
        baseInfo.productCd = productCd;
        // 客户信息组装
        var auditAmendEntryVO = new Object();
        auditAmendEntryVO.applicantInfo = applicantInfo;
        auditAmendEntryVO.contactInfo = contactInfoVOList;
        auditAmendEntryVO.baseInfo = baseInfo;
        moneyCustomerMarkSubmit = true;
        $.ajax({
            url: ctx.rootPath() + "/firstApprove/updateMoneyCustomerInfo/" + loanNo,
            dataType: "JSON",
            method: 'post',
            contentType: 'application/json',
            data: JSON.stringify(auditAmendEntryVO),
            success: function (data) {
                moneyCustomerMarkSubmit = false;
                if ("SUCCESS" == data.type) {
                    // 联系人数量重新保存
                    $('#money_contactInfoVOList_len').val(contactInfoVOList.length);
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
 * 房贷为"还款中"时，月供、房贷发放年月必填，单据户名为本人为非必填
 * @param newValue
 * @param oldValue
 */
function moneyHouseLoanSelect(newValue, oldValue) {
    var loanGrantDateOption = $('#moneyCustomerInfo_house_estateLoanGrantDate').datebox('options');
    var monthPaymentAmtOption = $('#moneyCustomerInfo_house_monthPaymentAmt').numberbox('options');

    if ("ING" == newValue) {    // 还款中
        loanGrantDateOption = $.extend({}, loanGrantDateOption, {
            required: true,
        });
        monthPaymentAmtOption = $.extend({}, monthPaymentAmtOption, {
            required: true,
        });
    } else if ("ALL" == newValue || "END" == newValue) {    // 全款中、已结清
        loanGrantDateOption = $.extend({}, loanGrantDateOption, {
            required: false,
        });
        monthPaymentAmtOption = $.extend({}, monthPaymentAmtOption, {
            required: false,
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
    }
    $('#moneyCustomerInfo_house_estateLoanGrantDate').datebox(loanGrantDateOption);
    $('#moneyCustomerInfo_house_monthPaymentAmt').numberbox(monthPaymentAmtOption);
}

/**
 * 切换婚姻状况校验联系人信息
 * @param newValue 最新选择的值
 * @param oldValue 上一次值
 */
function marriageCheck(newValue, oldValue) {
    var ifShowSpouseForm = ('00002' == newValue || '00006' == newValue || '00007' == newValue);// 已婚00002再婚00006复婚00007时加配偶信息校验
    var spouseCardNumber = $('#money_contactInfoVOList_div_spouseIdCard');// 配偶身份证号
    // 判断是否需要有配偶信息
    if (ifShowSpouseForm) { // 如果需要配偶信息
        if (spouseCardNumber.length != 1) {// 如果没有就创建配偶联系人
            moneyCustomerAddContacts("00013");// 创建配偶联系人
        }
    } else { // 如果不需要配偶信息
        if (spouseCardNumber.length == 1) { // 如果有就把配偶变成普通联系人
            var $spouseForm = spouseCardNumber.parents("form")
            var spouseFormObj = $spouseForm.serializeObject();//获取配偶信息
            $spouseForm.remove();// 删除配偶联系
            // 判断配偶信息是否为空(姓名、手机、电话、单位名称、备用手机、备用电话是否为空)
            var ifSpouseFormObjEmpty = (isNotNull(spouseFormObj.contactName) || isNotNull(spouseFormObj.contactCellPhone) || isNotNull(spouseFormObj.contactCorpPhone) || isNotNull(spouseFormObj.contactEmpName) || isNotNull(spouseFormObj.contactCellPhone_1) || isNotNull(spouseFormObj.contactCorpPhone_1));
            if (ifSpouseFormObjEmpty) {
                moneyCustomerAddContacts();// 创建普通联系人
                if (isNotNull(spouseFormObj.contactCellPhone_1)) {// 备用手机
                    addCellPhone($("#money_contactInfoVOList_div>form:last").find("tr:eq(1)").find("a:first").get(0), 'cellPhone');// 添加备用手机
                }
                if (isNotNull(spouseFormObj.contactCorpPhone_1)) {// 备用电话
                    addCellPhone($("#money_contactInfoVOList_div>form:last").find("tr:eq(1)").find("a:last").get(0), 'corpPhone');//添加备用电话
                }
                spouseFormObj.contactRelation = null;// 清空与配偶关系
                $("#money_contactInfoVOList_div").find("form:last").form("load", spouseFormObj);// 赋值form
            }
        }
    }
    // 婚姻状况为已婚或者离异时子女数必填
    if (isNotNull(newValue) && ('00002' == newValue || '00003' == newValue || '00006' == newValue || '00007' == newValue)) {
        $('#money_Customer_Info_childrenNum').numberspinner({
            required: true
        });
    } else {
        $('#money_Customer_Info_childrenNum').numberspinner({
            required: false
        });
    }
}

function deleteCellPhone(obj, type) {
    var phoneHtml = '<a href="javaScript:void(0);" onclick="addCellPhone(this,\'' + type + '\')"><i class="fa fa-plus" aria-hidden="true"></i></a>';
    $(obj).parent().html(phoneHtml);
}


/**
 * 婚姻状况为已婚复婚再婚时其他联系人的申请关系去掉配偶
 */
function relationJudge() {
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
 * 当住宅地址或房产地址同户籍地址时户籍地址改变其他地址也要改变
 *
 * @author dmz
 * @date 2017年7月31日
 */
function addressSynchronization() {
    var homeAddress = $("input:checked[name='homeSameRegistered']").val();
    if ('0' == homeAddress) {
        // 选择是时，房产地址回填住宅地址内容，显示为文本不可编辑
        var idIssuerProvince = $("#moneyCustomerInfo_birthplace_province_combobox").combobox("getValue");// 户籍省
        var idIssuerCity = $("#moneyCustomerInfo_birthplace_city_combobox").combobox("getValue");// 户籍市
        var idIssuerCountry = $("#moneyCustomerInfo_birthplace_country_combobox").combobox("getValue");// 户籍区
        var idIssuerAddress = $("#moneyCustomerInfo_birthplace_idIssuerAddress").val();// 户籍地址

        $("#moneyCustomerInfo_family_province_combobox").combobox('setValue', idIssuerProvince);
        $("#moneyCustomerInfo_family_city_combobox").combobox("setValue", idIssuerCity);
        $("#moneyCustomerInfo_family_country_combobox").combobox("setValue", idIssuerCountry);
        $("#moneyCustomerInfo_family_addr").textbox('setValue', idIssuerAddress);
    }
    var houseAddress = $("input:checked[name='estateSameRegistered']").val();
    if ("Y" == houseAddress) {
        // 选择是时，房产地址回填住宅地址内容，显示为文本不可编辑
        var homeProv = $("#moneyCustomerInfo_family_province_combobox").combobox("getValue");// 住宅省
        var homeCity = $("#moneyCustomerInfo_family_city_combobox").combobox("getValue");// 住宅市
        var homeCoun = $("#moneyCustomerInfo_family_country_combobox").combobox("getValue");// 住宅区
        var homeAddr = $("#moneyCustomerInfo_family_addr").val();// 住宅地址

        $("#moneyCustomerInfo_house_province_combobox").combobox('setValue', homeProv);
        $("#moneyCustomerInfo_house_city_combobox").combobox("setValue", homeCity);
        $("#moneyCustomerInfo_house_country_combobox").combobox("setValue", homeCoun);
        $("#money_customerInfo_estateAddress").textbox('setValue', homeAddr);
    }
}
/**
 * 当住宅地址改变是
 */
function changeFamily() {
    if ("Y" == vm.applyInfo.assetsInfo.estateInfo.unabridged && "Y"==vm.applyInfo.assetsInfo.estateInfo.estateSameRegistered) {
        // 选择是时，房产地址回填住宅地址内容，显示为文本不可编辑
        var homeProv = $("#moneyCustomerInfo_family_province_combobox").combobox("getText");// 住宅省
        var homeCity = $("#moneyCustomerInfo_family_city_combobox").combobox("getText");// 住宅市
        var homeCoun = $("#moneyCustomerInfo_family_country_combobox").combobox("getText");// 住宅区
        var homeAddr = $("#moneyCustomerInfo_family_addr").val();// 住宅地址
        $("#money_customer_ESTATE_div").find(".isEqueryFamilyAddress").html(homeProv + "&nbsp;" +homeCity+"&nbsp;"+homeCoun+"&nbsp;"+homeAddr);
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
    post(ctx.rootPath() + "/bmsBasiceInfo/getEnumCode", {emnuType: type, app: 1}, null, function (result) {
        if (isNotNull(result)) {
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
    var applicantID = $("#money_applicant_id").val();// 获取申请人省份证信息用于判断配偶身份证性别
    if ("Y" == YOrN) {
        if (vm.isDirectApp) {
            $('#money_contactInfoVOList_div_spouseIdCard').textbox({
                required: false,
                validType: ['consortInfo[1,100]']
            });
        } else {
            $('#money_contactInfoVOList_div_spouseIdCard').textbox({
                required: false,
                validType: ['consortInfo[1,100]']
            });
        }
    } else {
        if (vm.isDirectApp) {//直通车营业部进件或直通车营业部录单且客户类型是RELAON或TOPUP的申请
            $('#money_contactInfoVOList_div_spouseIdCard').textbox({
                required: false,
                validType: ['consortIDCard', 'checkSexForContacts["' + applicantID + '"]']
            });
        } else {
            $('#money_contactInfoVOList_div_spouseIdCard').textbox({
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
function moneyCustomerAddContacts(contactType) {
    if ($("#money_contactInfoVOList_div").find("form").length>=20) {
        $.info('提示', '联系已达上限!', 'warning');
        return false;
    }
    var contactsHTML = $("<form class='contactForm'></for><div><table class='table_ui W100'><tr>" +
        "<th width='10%' class='requiredFontWeight'>姓名:</th>	<td width='12%'><input class='input peopleName' name='contactName'></td><td width='18%'></td>" +
        "<th width='8%' class='requiredFontWeight'>与申请人关系:</th><td width='12%'><input class='maritalRelation input rlstionsp' name='contactRelation' /></td>" +
        "<th width='18%' class='requiredFontWeight'>是否知晓贷款:</th><td width='12%'>是<input type='radio' name='ifKnowLoan' value='Y' /> 否<input type='radio' name='ifKnowLoan' value='N' /></td>" +
        "</tr><tr>" +
        "<th width='10%' class='requiredFontWeight'>外籍人士:</th><td width='12%'>是<input type='radio' name='ifForeignPenple' value='Y' /> 否<input type='radio' name='ifForeignPenple' value='N' checked='checked' /></td><td></td>" +
        "<th class='requiredFontWeight'>身份证号码:</th>	<td colspan='3'><input id='money_contactInfoVOList_div_spouseIdCard' class='input' name='contactIdNo'></td>" +
        "</tr><tr>" +
        "<th class='requiredFontWeight'>手机:</th>	<td><input class='input cellPhoneCheck firstPhone' name='contactCellPhone'></td><td><a href='javaScript:void(0);' onclick=addCellPhone(this,'cellPhone')><i class='fa fa-plus' aria-hidden='true'></i></a></td>" +
        /*"<td><input class='input cellPhoneCheck' name='contactCellPhone_1'><a style='margin-left: 15px' href='javaScript:void(0);' onclick=deleteCellPhone(this,'cellPhone')><i class='fa fa-minus' aria-hidden='true'></i></a></td>" +*/
        "<th>单位电话:</th><td><input class='input' name='contactCorpPhone'></td><td><a href='javaScript:void(0);' onclick=addCellPhone(this,'corpPhone')><i class='fa fa-plus' aria-hidden='true'></i></a></td>" +
        /* "<td><input class='input' name='contactCorpPhone_1'><a style='margin-left: 15px' href='javaScript:void(0);' onclick='deleteCellPhone(this,'corpPhone')'><i class='fa fa-minus' aria-hidden='true'></i></a></td>" +*/
        "</tr><tr><th>单位名称:</th><td colspan='5'><input class='input contactEmpName' name='contactEmpName'></td></tr></table></div></form>");

    if (isNotNull(contactType) && "00013" == contactType) {// 标识添加配偶信息
        contactsHTML.find("tr:first").find("td:eq(2)").html("<input type='hidden'  value='00013' name='contactRelation' />配偶");// 联系人直接修改成配偶
        $("#money_contactInfoVOList_div").prepend(contactsHTML);
        contactsHTML.find("div").panel({
            title: "配偶信息",
            height: 186,
            width: 1382
        })
        if (vm.isDirectApp) {//如果是直通车营业部进件或直通车营业部录单且客户类型是RELAON或TOPUP的申请
            contactsHTML.find("input[name='contactIdNo']").textbox({// 加载配偶身份证信息
                required: false,
                validType: ['consortIDCard', 'checkSexForContacts["' + vm.applyInfo.baseInfo.idNo + '"]'],
                width: 435,
            });
        } else {
            contactsHTML.find("input[name='contactIdNo']").textbox({// 加载配偶身份证信息
                required: true,
                missingMessage: '客户配偶非外籍人士，请填写身份证！',
                validType: ['consortIDCard', 'checkSexForContacts["' + vm.applyInfo.baseInfo.idNo + '"]'],
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
        $("#money_contactInfoVOList_div").append(contactsHTML);
        contactsHTML.find("div").panel({
            title: "第<span class='money_contactsIndex'>" + ($(".money_contactsIndex").length + 1) + "</span>联系人信息  &nbsp;<a class='easyui-linkbutton_ok04 l-btn l-btn-small' href='javaScript:void(0);' onclick='deleteContact(this)'>删 除</a>",
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
            data: getEnum('Relationship'),
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
    var maritalStatus = $("#money_Customer_Info_maritalStatus").combobox("getValue");// 获取婚姻状况
    var contactsCount = 5; // 联系人最小值
    if ('00002' == maritalStatus || '00006' == maritalStatus || '00007' == maritalStatus) {//判断婚姻状态(已婚、再婚、复婚)) {
        contactsCount = 4; // 如果是已婚、再婚、复婚 (4+1)
    }
    if (contactsCount < $(".money_contactsIndex").length) {
        $(obj).parents("form").remove();// 删除联系人
        $.each($(".money_contactsIndex"), function (ind, val) {// 重新编排联系人顺序
            $(val).text(ind + 1);
        });
    } else {
        $.info('提示', '联系人不能少于五个!', 'warning');
    }
}

/**
 * 定位客户信息tab页面
 * @param tabName(base, contact, assets)
 */
function focusTab(tabName) {
    switch (tabName) {
        case 'base':
            $('#moneyCustomerInfo_tabs').tabs('select', 0);
            break;
        case 'contact':
            $('#moneyCustomerInfo_tabs').tabs('select', 1);
            break;
        case 'assets':
            $('#moneyCustomerInfo_tabs').tabs('select', 2);
            break;
    }
}
/**
 * 根据关系判断显示联系人或配偶
 * @param ind
 * @returns {boolean}
 */
function isRelation(ind) {
    return '00013' == vm.applyInfo.contactInfo[ind].contactRelation;
}

/**
 * 判断资产面板是否需要折叠
 * @param assets  资产信息
 * @param code    资产类型
 * @returns {boolean}
 */
function assetsPanelShow(assets, code) {
    return 'collapsible: true, collapsed: false';
   /* // 资产信息不为空的时候，默认展开该资产的panel
    if (assets.ifEmpty == 1) {
        return 'collapsible: true, collapsed: false';
    }
    // 校验客户申请的产品对应的资产配置是否包含该code
    var target = _.find(vm.assetsList, function (item) {
        return item.code == code;
    });
    // 包含则默认展开该资产panel
    if (target != null) {
        return 'collapsible: true, collapsed: false';
    }
    return 'collapsible: true, collapsed: true';*/
}

/**
 * 判断资产是否为必填项
 * @param code 资产code
 */
function assetsIsRequired(code) {
    var assets = _.find(vm.assetsList, function (assets) {
        return assets.code == code;
    });
    if (isNotNull(assets)) {
        return true;
    }
    return false;
}

/**
 * 扩展easyui空间选项
 * @param id
 * @param options
 */
function extendOption(id, options) {
    var defaultOptions = $('#' + id).combobox('options');
    options = $.extend({}, defaultOptions, options);
    $('#' + id).combobox(options);
}

/**
 * 重新加载地址信息
 * @param type B-户籍地址，F-住宅住址
 */
function reloadAddress(type) {
    if (type == 'B') {
        var province = $('#moneyCustomerInfo_birthplace_province_combobox').combobox('getValue');
        var city = $('#moneyCustomerInfo_birthplace_city_combobox').combobox('getValue');
        var area = $('#moneyCustomerInfo_birthplace_country_combobox').combobox('getValue');
        var other = $('#moneyCustomerInfo_birthplace_idIssuerAddress').textbox('getValue');
        $('#birthplaceAddress').val(other + province + city + area);
    } else {
        var province = $('#moneyCustomerInfo_family_province_combobox').combobox('getValue');
        var city = $('#moneyCustomerInfo_family_city_combobox').combobox('getValue');
        var area = $('#moneyCustomerInfo_family_country_combobox').combobox('getValue');
        var other = $('#moneyCustomerInfo_family_addr').textbox('getValue');
        $('#familyAddress').val(other + province + city + area);
    }
    $("#moneyCustomerInfo_birthplace_idIssuerAddress").textbox("validate");
    $("#moneyCustomerInfo_family_addr").textbox("validate");
}


/**
 * 初始化客户信息控件
 */
function initCustomerInfo() {
    // ------------------------------------------------ begin 客户个人信息初始化-------------------------------------------------------------------------
    // 婚姻状况
    $("#money_Customer_Info_maritalStatus").combobox({
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

    //最高学历
    $("#money_Customer_Info_qualification").combobox({
        required: true,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        prompt: '请选择',
        missingMessage: '请选择最高学历',
        value: vm.applyInfo.applicantInfo.personalInfo.qualification,
        data: getEnum('EducationType')
    });

    //可接受的月最高还款
    $("#money_customerInfo_monthMaxRepay").numberbox({
        groupSeparator: ',',
        required: true,
        precision: 0,
        buttonText: '元',
        validType: ["nonnegativePnum['']", "range[0, 99999999, '可接受的月最高还款']"],
        missingMessage: "请填写可接受的月最高还款!",
    });

    //户籍地址（省、市、区）
    var birthPlaceAreaHandler = $("#moneyCustomerInfo_birthplace_country_combobox").combobox('options').onChange;
    $("#moneyCustomerInfo_birthplace_country_combobox").combobox({
        required: true,
        prompt: '-----区-----',
        missingMessage: "户籍地址输入不完整，下拉框均需选择，输入框不能为空！",
        onChange: function (newValue, oldValue) {
            reloadAddress('B');
            addressSynchronization();
            birthPlaceAreaHandler.call(this, newValue, oldValue);
        }
    });

    var birthPlaceCityHandler = $("#moneyCustomerInfo_birthplace_city_combobox").combobox('options').onChange;
    $("#moneyCustomerInfo_birthplace_city_combobox").combobox({
        required: true,
        prompt: '-----市-----',
        missingMessage: "户籍地址输入不完整，下拉框均需选择，输入框不能为空！",
        onChange: function (newValue, oldValue) {
            reloadAddress('B');
            addressSynchronization();
            birthPlaceCityHandler.call(this, newValue, oldValue);
        }
    });

    var birthPlaceProvinceHandler = $("#moneyCustomerInfo_birthplace_province_combobox").combobox('options').onChange;
    $("#moneyCustomerInfo_birthplace_province_combobox").combobox({
        required: true,
        prompt: '-----省-----',
        missingMessage: "户籍地址输入不完整，下拉框均需选择，输入框不能为空！",
        onChange: function (newValue, oldValue) {
            reloadAddress('B');
            addressSynchronization();
            birthPlaceProvinceHandler.call(this, newValue, oldValue);
        }
    });

    //户籍地址具体信息
    $("#moneyCustomerInfo_birthplace_idIssuerAddress").textbox({
        prompt: 'xxx镇/乡 xxx村 xxx路/街道',
        required: true,
        validType: ["chineseAndNoSpace['户籍地址输入框中']", "maxLength[200]", "duplicateAddress['#birthplaceAddress', '#familyAddress', 'input:checked[name=homeSameRegistered]','住宅地址', '户籍地址']"],
        multiline: true,
        width: 388,
        missingMessage: "户籍地址输入不完整，下拉框均需选择，输入框不能为空！",
        onChange: function (newValue, oldValue) {
            reloadAddress('B');
            addressSynchronization();
        }
    });

    //住宅地址（区）
    var familyCountryHandler = $("#moneyCustomerInfo_family_country_combobox").combobox('options').onChange;
    $("#moneyCustomerInfo_family_country_combobox").combobox({
        readonly: vm.applyInfo.applicantInfo.personalInfo.homeSameRegistered == '0' ? true : false,
        required: true,
        prompt: '-----区-----',
        missingMessage: "住宅地址输入不完整，下拉框均需选择，输入框不能为空！",
        onChange: function (newValue, oldValue) {
            reloadAddress('C');
            familyCountryHandler.call(this, newValue, oldValue);
        }
    });

    //住宅地址（市）
    var familyCityHandler = $("#moneyCustomerInfo_family_city_combobox").combobox('options').onChange;
    $("#moneyCustomerInfo_family_city_combobox").combobox({
        readonly: vm.applyInfo.applicantInfo.personalInfo.homeSameRegistered == '0' ? true : false,
        required: true,
        prompt: '-----市-----',
        missingMessage: "住宅地址输入不完整，下拉框均需选择，输入框不能为空！",
        onChange: function (newValue, oldValue) {
            reloadAddress('C');
            familyCityHandler.call(this, newValue, oldValue);
        }
    });

    //住宅地址（省）
    var familyProvinceHandler = $("#moneyCustomerInfo_family_province_combobox").combobox('options').onChange;
    $("#moneyCustomerInfo_family_province_combobox").combobox({
        readonly: vm.applyInfo.applicantInfo.personalInfo.homeSameRegistered == '0' ? true : false,
        required: true,
        prompt: '-----省-----',
        missingMessage: "住宅地址输入不完整，下拉框均需选择，输入框不能为空！",
        onChange: function (newValue, oldValue) {
            reloadAddress('C');
            familyProvinceHandler.call(this, newValue, oldValue);
        }
    });

    //住宅地址具体信息
    $("#moneyCustomerInfo_family_addr").textbox({
        readonly: vm.applyInfo.applicantInfo.personalInfo.homeSameRegistered == '0' ? true : false,
        prompt: 'xxx镇/乡 xxx村 xxx路/街道',
        required: true,
        multiline: true,
        width: 388,
        missingMessage: "住宅地址输入不完整，下拉框均需选择，输入框不能为空！",
        validType: ["chineseAndNoSpace['住宅地址输入框中']", "maxLength[200]", "duplicateAddress['#birthplaceAddress', '#familyAddress', 'input:checked[name=homeSameRegistered]', '住宅地址', '户籍地址']"],
        onChange: function (newValue, oldValue) {
            reloadAddress('C');
            changeFamily();
        }
    });

    reloadAddress('B');
    reloadAddress('C');

    //常用手机
    $("#money_customerInfo_cellphone").textbox({
        required: true,
        precision: 0,
        validType: ['mobile', 'samePhone', 'same["#money_customerInfo_cellphoneSec", "常用手机", "备用手机"]'],
        missingMessage: "请填写常用手机!",
    });

    //备用手机
    $("#money_customerInfo_cellphoneSec").textbox({
        precision: 0,
        validType: ['mobile', 'samePhone', 'same["#money_customerInfo_cellphone", "常用手机", "备用手机"]']
    });

    //家庭电话
    $("#money_customerInfo_homePhone1").textbox({
        // required: true,
        validType: ['telNum'],
        missingMessage: "请填写家庭电话!"
    });

    //QQ号
    $("#moneyCustomerInfo_customerInfo_qqNum").numberbox({
        precision: 0,
        validType: ['QQ', 'maxLength[15]'],
    });

    //微信号
    $("#moneyCustomerInfo_customerInfo_weChatNum").textbox({
        precision: 0,
        validType: ['weChat', 'maxLength[30]']
    });

    //电子邮箱
    $("#moneyCustomerInfo_customerInfo_email").textbox({
        required:true,
        validType: ['email', 'maxLength[80]']
    });

    // ------------------------------------------------ end 客户个人信息初始化-------------------------------------------------------------------------

    // ------------------------------------------------ begin 客户工作信息初始化------------------------------------------------------------------------
    //单位名称
    $("#moneyCustomerInfo_workInfo_corpName_input").textbox({
        required: true,
        multiline: true,
        width: 460,
        missingMessage: "请填写单位名称!",
        validType: ["notAllNumberAndLength[200, '单位名称']"]
    });

    //单位地址（区）
    $("#moneyCustomerInfo_company_country_combobox").combobox({
        prompt: '-----区-----',
        missingMessage: "单位地址输入不完整，下拉框均需选择，输入框不能为空！"
    });

    //单位地址（市）
    $("#moneyCustomerInfo_company_city_combobox").combobox({
        prompt: '-----市-----',
        missingMessage: "单位地址输入不完整，下拉框均需选择，输入框不能为空！"
    });

    //单位地址（省）
    $("#moneyCustomerInfo_company_province_combobox").combobox({
        required: true,
        prompt: '-----省-----',
        missingMessage: "单位地址输入不完整，下拉框均需选择，输入框不能为空！"
    });

    //单位地址具体信息
    $("#corpAddress").textbox({
        required: true,
        multiline: true,
        width: 392,
        prompt: 'xxx镇/乡 xxx村 xxx路/街道',
        validType: ["chineseAndNoSpace['单位地址输入框中']", "maxLength[200]"],
        missingMessage: "单位地址输入不完整，下拉框均需选择，输入框不能为空！"
    });

    // 单位性质
    $("#moneyCustomer_corpStructure_combobox").combobox({
        required: true,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        //validType: ["comboboxUnSelectWithParam['单位性质']"],
        prompt: '请选择',
        missingMessage: '请选择单位性质',
        data: getEnum('CorpStructure'),
    });



    //客户工作类型
    $("#moneyCustomerInfo_cusWorkType_combobox").combobox({
        required: true,
        prompt: '请选择',
       readonly:true,
        missingMessage: '请选择客户工作类型',
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('JobType'),
        onChange: function (newValue, oldValue) {
         vm.applyInfo.applicantInfo.workInfo.cusWorkType = newValue;
            if (newValue == "00001") {           // 私营业主
                $('#money_customerPrivateOwner_div').panel({title: "私营业主信息"});     // 初始化title
            } else if (newValue == "00002") {    // 受薪人士
                $("#money_customerPrivateOwner_form").form("clear");                    // 清空私营业主表单
                $('#money_customerPrivateOwner_div').panel({title: ""});                // 清空title
            } else if (newValue == "00003") {    // 自雇人士
                $('#money_customerPrivateOwner_div').panel({title: "私营业主信息"});     // 改变title
                $("#moneyCustomer_corpStructure_combobox").combobox("reload", ctx.rootPath() + "/bmsBasiceInfo/findCodeByUnit?code=" + newValue);  // 单位性质重新加载
            }
            // 私营业主信息必填项验证
            if (newValue == "00002") {    // 受薪人士
                $("#money_customerPrivateOwner_setupDate").datebox({required: vm.isPrivateOwner,value:""});
                $("#money_customerInfo_priEnterpriseType").combobox({required: vm.isPrivateOwner,value:""});
            }else {
                $("#money_customerPrivateOwner_setupDate").datebox({required: vm.isPrivateOwner});
                $("#money_customerInfo_priEnterpriseType").combobox({required: vm.isPrivateOwner});
            }
            $("#money_sharesScale").numberbox({required: vm.isPrivateOwner});
            $("#money_registerFunds").numberbox({required: vm.isPrivateOwner});

            $("#money_monthAmt").numberbox({required: vm.isPrivateOwner});
        }
    });

    // 职业类型
    $("#moneyCustomer_occupation_combobox").combobox({
        required: true,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        prompt: '请选择职业类型',
        missingMessage: '请选择职业类型',
        data: getEnum('professionType'),
        onChange:function(newValue,oldValue) {
            if (isNotNull(newValue)) {
                if ("00001" == newValue || "00002" == newValue || "00005" == newValue)  {// 工薪、白领、公务员
                    $("#moneyCustomerInfo_cusWorkType_combobox").combobox("setValue","00002");// 受薪人士
                } else if("A" ==  newValue || "B" == newValue){// 个体业主、企业主
                    $("#moneyCustomerInfo_cusWorkType_combobox").combobox("setValue","00001");// 私营业主
                } else {//自雇人士/自由职业
                    $("#moneyCustomerInfo_cusWorkType_combobox").combobox("setValue","00003"); // 自雇人士
                }
            } else {
                $("#moneyCustomerInfo_cusWorkType_combobox").combobox("setValue","");
            }
        }
    });
    //任职部门
    $("#money_corpDepapment").textbox({
        required: true,
        missingMessage: "请填写任职部门！",
        validType: ["maxLength[200]"]
    });

    // 职务
    $("#moneyCustomer_corpPost_combobox").combobox({
        required: true,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        prompt: '请选择',
        missingMessage: '请选择职务',
        data: getEnum('NoGovInstitution'),
    });

    // 单位行业类别
    $("#money_corpType").combobox({
        required: true,
        prompt: '请选择',
        missingMessage: '请选择单位行业类别',
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('EmpType')
    });

    // 入职时间
    $("#money_corpStandFrom").datebox({
        value: isNotNull(vm.applyInfo.applicantInfo.workInfo.corpStandFrom) ? moment(vm.applyInfo.applicantInfo.workInfo.corpStandFrom).format('YYYY-MM') : '',
        required: true,
        editable: false,
        prompt: '入职时间',
        missingMessage: "请选择入职时间!",
        validType: ["compareTimeLimitWithParam['1980年1月', '入职时间']", "compareNowDate['入职时间']"],
        formatter : myformatter,
        parser : myparser
    }).datebox('calendar').calendar({
        validator: function (date) {
            var now = new Date();
            var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            return day >= date;
        }
    });

    // 发薪方式
    $("#money_corpPayWay").combobox({
        required: true,
        prompt: '请选择',
        missingMessage: '请选择发薪方式',
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        data: getEnum('CorpPayWay'),
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

    //  税前月总收入/元
    $("#money_monthCountPay").numberbox({
        groupSeparator: ',',
        required: true,
        precision: 0,
        buttonText: '元',
        missingMessage: "请填写税前月总收入!",
        validType: ["nonnegativePnum['税前月总收入']", "range[0, 99999999, '税前月总收入']"]
    });
    // ------------------------------------------------ end 客户工作信息初始化-------------------------------------------------------------------------

    // ------------------------------------------------ begin 客户私营业主信息初始化-------------------------------------------------------------------
    //成立时间
    $("#money_customerPrivateOwner_setupDate").datebox({
        value: isNotNull(vm.applyInfo.applicantInfo.privateOwnerInfo.setupDate) ? moment(vm.applyInfo.applicantInfo.privateOwnerInfo.setupDate).format('YYYY-MM') : '',
        required: vm.isPrivateOwner,
        editable: false,
        width: 210,
        prompt: '私营业主成立时间',
        missingMessage: "请选择成立时间!",
        validType: ["compareTimeLimitWithParam['1960年1月', '私营业主成立时间']", "compareNowDate['私营业主成立时间']"],
        formatter : myformatter,
        parser : myparser
    }).datebox('calendar').calendar({
        validator: function (date) {
            var now = new Date();
            var day = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            return day >= date;
        }
    });

    //占股比例
    $("#money_sharesScale").numberbox({
        required: vm.isPrivateOwner,
        precision: 2,
        buttonText: '%',
        min: 0,
        max: 100,
        width: 210,
        missingMessage: "请填写占股比例!",
        validType: ["decimalsVerified[0,100,'占股比例']"],
    });

    //注册资本
    $("#money_registerFunds").numberbox({
        required: vm.isPrivateOwner,
        groupSeparator: ',',
        precision: 0,
        width: 210,
        buttonText: '元',
        missingMessage: "请填写注册资本!",
        validType: ["range[0, 999999999, '注册资本','9个9']"]
    });

    //私营企业类型
    $("#money_customerInfo_priEnterpriseType").combobox({
        required: vm.isPrivateOwner,
        width: 210,
        editable: false,
        valueField: 'code',
        textField: 'nameCN',
        prompt: '请选择',
        missingMessage: '请选择私营企业类型',
        data: getEnum('PriEnterpriseType')
    });

    //每月净利润额
    $("#money_monthAmt").numberbox({
        required: vm.isPrivateOwner,
        width: 210,
        groupSeparator: ',',
        precision: 0,
        buttonText: '元',
        missingMessage: "请填写每月净利润额!",
        validType: ["nonnegativePnum['每月净利润额']", "range[0, 99999999, '每月净利润额']"]
    });
    // ------------------------------------------------ end 客户私营业主信息初始化-------------------------------------------------------------------

    // ------------------------------------------------ begin 客户联系人信息初始化-------------------------------------------------------------------
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
    relationJudge();
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
        var options = $('#money_contactInfoVOList_div_spouseIdCard').textbox('options');
        //如果是直通车营业部进件或直通车营业部录单且客户类型是RELAON或TOPUP的申请，身份证不必填
        if (vm.isDirectApp) {
            options = $.extend({}, options, {required: false});
        } else {
            options = this.value == 'Y' ? $.extend({}, options, {required: false}) : $.extend({}, options, {required: true});
        }
        $('#money_contactInfoVOList_div_spouseIdCard').textbox(options);
    });
    // ------------------------------------------------ end 客户联系人信息初始化---------------------------------------------------------------------
}
