package com.yuminsoft.ams.system.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 申请人信息表
 * @author fuhongxing
 */
public class PersonInfo extends AbstractEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7337567904042694715L;

//	public static final String TABLE_NAME = "AMS_PERSON_INFO";

    private String loanNo;//申请件编号

    private String name;//姓名

    private Date birthday;//生日

    private String gender;//性别

    private Integer age;//年龄

    private String idNo;//身份证号码

    private Date idLastDate;//证件到期日

    private String idIssuerAddress;//户籍地址

    private String issuerState;//户籍所在省

    private String issuerCity;//户籍所在市

    private String issuerZone;//户籍所在区

    private String issuerPostcode;//户籍邮编

    private String nationality;//国籍

    private String residencyCountryCd;//永久居住地国家代码

    private String maritalStatus;//婚姻状况

    private Integer childrenNum;//子女数

    private String qualification;//教育状况

    private Date graduationDate;//毕业时间

    private String houseOwnership;//房屋持有类型

    private String houseType;//住宅类型

    private BigDecimal houseRent;//租金/元

    private String liquidAsset;//个人资产类型

    private String cellphone;//手机

    private String cellphoneSec;//手机2

    private String qqNum;//QQ号

    private String wechatNum;//微信号

    private String email;//电子邮箱

    private String familyMember;//家庭人口

    private BigDecimal familyAvgeVenue;//家庭人均年收入

    private BigDecimal familyMonthPay;//每月家庭支出

    private String homeAddrCtryCd;//家庭国家代码

    private String homeState;//家庭所在省

    private String homeCity;//家庭所在市

    private String homeZone;//家庭所在区县

    private String homeAddress;//家庭地址

    private String homePostcode;//家庭住宅邮编

    private String homePhone1;//宅电

    private String homePhone2;//宅电2

    private Date homeStandFrom;//现住址居住起始年月

    private String prOfCountry;//是否永久居住

    private String privateOwnersFlag;//是否私营业主

    private String corpName;//单位名称

    private String corpStructure;//公司性质

    private String corpAddrCtryCd;//公司国家代码

    private String corpProvince;//公司所在省

    private String corpCity;//公司所在市

    private String corpZone;//公司所在区/县

    private String corpAddress;//公司地址

    private String corpPostcode;//公司邮编

    private String corpPhone;//单电

    private String corpPhoneSec;//单电2

    private String corpFax;//公司传真

    private String corpDepapment;//任职部门

    private String corpPost;//职务

    private Date corpStandFrom;//入职时间,现单位工作起始年月

    private String businessNetWork;//工商网信息

    private String corpmemFlag;//是否公司员工

    private String corpmemNo;//本公司员工号

    private String corpType;//公司行业类别

    private Integer corpWorkyears;//本公司工作年限,本单位工作年限

    private String corpStability;//工作稳定性

    private String occupation;//职业

    private String titleOfTechnical;//职称

    private String empStatus;//是否在职

    private String corpPayWay;//发薪方式

    private String corpPayday;//发薪日

    private BigDecimal monthSalary;//单位月收入/元

    private BigDecimal otherIncome;//其他月收入

    private BigDecimal totalMonthSalary;//月总收入/元

    private Date setupDate;//成立时间

    private BigDecimal monthMaxRepay;//可接受的月最高还款

    private BigDecimal sharesScale;//占股比例/%

    private BigDecimal registerFunds;//注册资本/万元

    private String priEnterpriseType;//私营企业类型

    private String businessPlace;//经营场所

    private BigDecimal monthRent;//月租金/元

    private Integer employeeNum;//员工人数/人

    private BigDecimal enterpriseRate;//企业净利润率/%

    private String sharesName;//股东姓名(除客户外最大股东)

    private String sharesIdNo;//股东身份证

    private BigDecimal monthAmt;//每月净利率额/万元

    private BigDecimal reportId;//人行征信ID

    private String reportMessage;//绑定记录信息

    /**
     * <p>申请件编号</p>
     * <p>申请件编号</p>
     */
    public String getLoanNo() {
        return loanNo;
    }

    /**
     * <p>申请件编号</p>
     * <p>申请件编号</p>
     */
    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }

    /**
     * <p>姓名</p>
     */
    public String getName() {
        return name;
    }

    /**
     * <p>姓名</p>
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>生日</p>
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * <p>生日</p>
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * <p>性别</p>
     */
    public String getGender() {
        return gender;
    }

    /**
     * <p>性别</p>
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * <p>年龄</p>
     */
    public Integer getAge() {
        return age;
    }

    /**
     * <p>年龄</p>
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * <p>身份证号码</p>
     * <p>身份证号码</p>
     */
    public String getIdNo() {
        return idNo;
    }

    /**
     * <p>身份证号码</p>
     * <p>身份证号码</p>
     */
    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    /**
     * <p>证件到期日</p>
     */
    public Date getIdLastDate() {
        return idLastDate;
    }

    /**
     * <p>证件到期日</p>
     */
    public void setIdLastDate(Date idLastDate) {
        this.idLastDate = idLastDate;
    }

    /**
     * <p>户籍地址</p>
     */
    public String getIdIssuerAddress() {
        return idIssuerAddress;
    }

    /**
     * <p>户籍地址</p>
     */
    public void setIdIssuerAddress(String idIssuerAddress) {
        this.idIssuerAddress = idIssuerAddress;
    }

    /**
     * <p>户籍所在省</p>
     */
    public String getIssuerState() {
        return issuerState;
    }

    /**
     * <p>户籍所在省</p>
     */
    public void setIssuerState(String issuerState) {
        this.issuerState = issuerState;
    }

    /**
     * <p>户籍所在市</p>
     */
    public String getIssuerCity() {
        return issuerCity;
    }

    /**
     * <p>户籍所在市</p>
     */
    public void setIssuerCity(String issuerCity) {
        this.issuerCity = issuerCity;
    }

    /**
     * <p>户籍所在区</p>
     */
    public String getIssuerZone() {
        return issuerZone;
    }

    /**
     * <p>户籍所在区</p>
     */
    public void setIssuerZone(String issuerZone) {
        this.issuerZone = issuerZone;
    }

    /**
     * <p>户籍邮编</p>
     */
    public String getIssuerPostcode() {
        return issuerPostcode;
    }

    /**
     * <p>户籍邮编</p>
     */
    public void setIssuerPostcode(String issuerPostcode) {
        this.issuerPostcode = issuerPostcode;
    }

    /**
     * <p>国籍</p>
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * <p>国籍</p>
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     * <p>永久居住地国家代码</p>
     */
    public String getResidencyCountryCd() {
        return residencyCountryCd;
    }

    /**
     * <p>永久居住地国家代码</p>
     */
    public void setResidencyCountryCd(String residencyCountryCd) {
        this.residencyCountryCd = residencyCountryCd;
    }

    /**
     * <p>婚姻状况</p>
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * <p>婚姻状况</p>
     */
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * <p>子女数</p>
     */
    public Integer getChildrenNum() {
        return childrenNum;
    }

    /**
     * <p>子女数</p>
     */
    public void setChildrenNum(Integer childrenNum) {
        this.childrenNum = childrenNum;
    }

    /**
     * <p>教育状况</p>
     */
    public String getQualification() {
        return qualification;
    }

    /**
     * <p>教育状况</p>
     */
    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    /**
     * <p>毕业时间</p>
     */
    public Date getGraduationDate() {
        return graduationDate;
    }

    /**
     * <p>毕业时间</p>
     */
    public void setGraduationDate(Date graduationDate) {
        this.graduationDate = graduationDate;
    }

    /**
     * <p>房屋持有类型</p>
     */
    public String getHouseOwnership() {
        return houseOwnership;
    }

    /**
     * <p>房屋持有类型</p>
     */
    public void setHouseOwnership(String houseOwnership) {
        this.houseOwnership = houseOwnership;
    }

    /**
     * <p>住宅类型</p>
     */
    public String getHouseType() {
        return houseType;
    }

    /**
     * <p>住宅类型</p>
     */
    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    /**
     * <p>租金/元</p>
     */
    public BigDecimal getHouseRent() {
        return houseRent;
    }

    /**
     * <p>租金/元</p>
     */
    public void setHouseRent(BigDecimal houseRent) {
        this.houseRent = houseRent;
    }

    /**
     * <p>个人资产类型</p>
     */
    public String getLiquidAsset() {
        return liquidAsset;
    }

    /**
     * <p>个人资产类型</p>
     */
    public void setLiquidAsset(String liquidAsset) {
        this.liquidAsset = liquidAsset;
    }

    /**
     * <p>手机</p>
     */
    public String getCellphone() {
        return cellphone;
    }

    /**
     * <p>手机</p>
     */
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    /**
     * <p>手机2</p>
     */
    public String getCellphoneSec() {
        return cellphoneSec;
    }

    /**
     * <p>手机2</p>
     */
    public void setCellphoneSec(String cellphoneSec) {
        this.cellphoneSec = cellphoneSec;
    }

    /**
     * <p>QQ号</p>
     */
    public String getQqNum() {
        return qqNum;
    }

    /**
     * <p>QQ号</p>
     */
    public void setQqNum(String qqNum) {
        this.qqNum = qqNum;
    }

    /**
     * <p>微信号</p>
     */
    public String getWechatNum() {
        return wechatNum;
    }

    /**
     * <p>微信号</p>
     */
    public void setWechatNum(String wechatNum) {
        this.wechatNum = wechatNum;
    }

    /**
     * <p>电子邮箱</p>
     */
    public String getEmail() {
        return email;
    }

    /**
     * <p>电子邮箱</p>
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * <p>家庭人口</p>
     */
    public String getFamilyMember() {
        return familyMember;
    }

    /**
     * <p>家庭人口</p>
     */
    public void setFamilyMember(String familyMember) {
        this.familyMember = familyMember;
    }

    /**
     * <p>家庭人均年收入</p>
     */
    public BigDecimal getFamilyAvgeVenue() {
        return familyAvgeVenue;
    }

    /**
     * <p>家庭人均年收入</p>
     */
    public void setFamilyAvgeVenue(BigDecimal familyAvgeVenue) {
        this.familyAvgeVenue = familyAvgeVenue;
    }

    /**
     * <p>每月家庭支出</p>
     */
    public BigDecimal getFamilyMonthPay() {
        return familyMonthPay;
    }

    /**
     * <p>每月家庭支出</p>
     */
    public void setFamilyMonthPay(BigDecimal familyMonthPay) {
        this.familyMonthPay = familyMonthPay;
    }

    /**
     * <p>家庭国家代码</p>
     */
    public String getHomeAddrCtryCd() {
        return homeAddrCtryCd;
    }

    /**
     * <p>家庭国家代码</p>
     */
    public void setHomeAddrCtryCd(String homeAddrCtryCd) {
        this.homeAddrCtryCd = homeAddrCtryCd;
    }

    /**
     * <p>家庭所在省</p>
     * <p>家庭所在省</p>
     */
    public String getHomeState() {
        return homeState;
    }

    /**
     * <p>家庭所在省</p>
     * <p>家庭所在省</p>
     */
    public void setHomeState(String homeState) {
        this.homeState = homeState;
    }

    /**
     * <p>家庭所在市</p>
     * <p>家庭所在市</p>
     */
    public String getHomeCity() {
        return homeCity;
    }

    /**
     * <p>家庭所在市</p>
     * <p>家庭所在市</p>
     */
    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    /**
     * <p>家庭所在区县</p>
     * <p>家庭所在区县</p>
     */
    public String getHomeZone() {
        return homeZone;
    }

    /**
     * <p>家庭所在区县</p>
     * <p>家庭所在区县</p>
     */
    public void setHomeZone(String homeZone) {
        this.homeZone = homeZone;
    }

    /**
     * <p>家庭地址</p>
     * <p>家庭地址</p>
     */
    public String getHomeAddress() {
        return homeAddress;
    }

    /**
     * <p>家庭地址</p>
     * <p>家庭地址</p>
     */
    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    /**
     * <p>家庭住宅邮编</p>
     * <p>家庭住宅邮编</p>
     */
    public String getHomePostcode() {
        return homePostcode;
    }

    /**
     * <p>家庭住宅邮编</p>
     * <p>家庭住宅邮编</p>
     */
    public void setHomePostcode(String homePostcode) {
        this.homePostcode = homePostcode;
    }

    /**
     * <p>宅电</p>
     * <p>宅电</p>
     */
    public String getHomePhone1() {
        return homePhone1;
    }

    /**
     * <p>宅电</p>
     * <p>宅电</p>
     */
    public void setHomePhone1(String homePhone1) {
        this.homePhone1 = homePhone1;
    }

    /**
     * <p>宅电2</p>
     * <p>宅电2</p>
     */
    public String getHomePhone2() {
        return homePhone2;
    }

    /**
     * <p>宅电2</p>
     * <p>宅电2</p>
     */
    public void setHomePhone2(String homePhone2) {
        this.homePhone2 = homePhone2;
    }

    /**
     * <p>现住址居住起始年月</p>
     */
    public Date getHomeStandFrom() {
        return homeStandFrom;
    }

    /**
     * <p>现住址居住起始年月</p>
     */
    public void setHomeStandFrom(Date homeStandFrom) {
        this.homeStandFrom = homeStandFrom;
    }

    /**
     * <p>是否永久居住</p>
     */
    public String getPrOfCountry() {
        return prOfCountry;
    }

    /**
     * <p>是否永久居住</p>
     */
    public void setPrOfCountry(String prOfCountry) {
        this.prOfCountry = prOfCountry;
    }

    /**
     * <p>是否私营业主</p>
     */
    public String getPrivateOwnersFlag() {
        return privateOwnersFlag;
    }

    /**
     * <p>是否私营业主</p>
     */
    public void setPrivateOwnersFlag(String privateOwnersFlag) {
        this.privateOwnersFlag = privateOwnersFlag;
    }

    /**
     * <p>单位名称</p>
     */
    public String getCorpName() {
        return corpName;
    }

    /**
     * <p>单位名称</p>
     */
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    /**
     * <p>公司性质</p>
     */
    public String getCorpStructure() {
        return corpStructure;
    }

    /**
     * <p>公司性质</p>
     */
    public void setCorpStructure(String corpStructure) {
        this.corpStructure = corpStructure;
    }

    /**
     * <p>公司国家代码</p>
     */
    public String getCorpAddrCtryCd() {
        return corpAddrCtryCd;
    }

    /**
     * <p>公司国家代码</p>
     */
    public void setCorpAddrCtryCd(String corpAddrCtryCd) {
        this.corpAddrCtryCd = corpAddrCtryCd;
    }

    /**
     * <p>公司所在省</p>
     */
    public String getCorpProvince() {
        return corpProvince;
    }

    /**
     * <p>公司所在省</p>
     */
    public void setCorpProvince(String corpProvince) {
        this.corpProvince = corpProvince;
    }

    /**
     * <p>公司所在市</p>
     */
    public String getCorpCity() {
        return corpCity;
    }

    /**
     * <p>公司所在市</p>
     */
    public void setCorpCity(String corpCity) {
        this.corpCity = corpCity;
    }

    /**
     * <p>公司所在区/县</p>
     */
    public String getCorpZone() {
        return corpZone;
    }

    /**
     * <p>公司所在区/县</p>
     */
    public void setCorpZone(String corpZone) {
        this.corpZone = corpZone;
    }

    /**
     * <p>公司地址</p>
     * <p>公司地址</p>
     */
    public String getCorpAddress() {
        return corpAddress;
    }

    /**
     * <p>公司地址</p>
     * <p>公司地址</p>
     */
    public void setCorpAddress(String corpAddress) {
        this.corpAddress = corpAddress;
    }

    /**
     * <p>公司邮编</p>
     * <p>公司邮编</p>
     */
    public String getCorpPostcode() {
        return corpPostcode;
    }

    /**
     * <p>公司邮编</p>
     * <p>公司邮编</p>
     */
    public void setCorpPostcode(String corpPostcode) {
        this.corpPostcode = corpPostcode;
    }

    /**
     * <p>单电</p>
     * <p>单电</p>
     */
    public String getCorpPhone() {
        return corpPhone;
    }

    /**
     * <p>单电</p>
     * <p>单电</p>
     */
    public void setCorpPhone(String corpPhone) {
        this.corpPhone = corpPhone;
    }

    /**
     * <p>单电2</p>
     * <p>单电2</p>
     */
    public String getCorpPhoneSec() {
        return corpPhoneSec;
    }

    /**
     * <p>单电2</p>
     * <p>单电2</p>
     */
    public void setCorpPhoneSec(String corpPhoneSec) {
        this.corpPhoneSec = corpPhoneSec;
    }

    /**
     * <p>公司传真</p>
     */
    public String getCorpFax() {
        return corpFax;
    }

    /**
     * <p>公司传真</p>
     */
    public void setCorpFax(String corpFax) {
        this.corpFax = corpFax;
    }

    /**
     * <p>任职部门</p>
     * <p>任职部门</p>
     */
    public String getCorpDepapment() {
        return corpDepapment;
    }

    /**
     * <p>任职部门</p>
     * <p>任职部门</p>
     */
    public void setCorpDepapment(String corpDepapment) {
        this.corpDepapment = corpDepapment;
    }

    /**
     * <p>职务</p>
     */
    public String getCorpPost() {
        return corpPost;
    }

    /**
     * <p>职务</p>
     */
    public void setCorpPost(String corpPost) {
        this.corpPost = corpPost;
    }

    /**
     * <p>入职时间</p>
     * <p>现单位工作起始年月</p>
     */
    public Date getCorpStandFrom() {
        return corpStandFrom;
    }

    /**
     * <p>入职时间</p>
     * <p>现单位工作起始年月</p>
     */
    public void setCorpStandFrom(Date corpStandFrom) {
        this.corpStandFrom = corpStandFrom;
    }

    /**
     * <p>工商网信息</p>
     */
    public String getBusinessNetWork() {
        return businessNetWork;
    }

    /**
     * <p>工商网信息</p>
     */
    public void setBusinessNetWork(String businessNetWork) {
        this.businessNetWork = businessNetWork;
    }

    /**
     * <p>是否公司员工</p>
     */
    public String getCorpmemFlag() {
        return corpmemFlag;
    }

    /**
     * <p>是否公司员工</p>
     */
    public void setCorpmemFlag(String corpmemFlag) {
        this.corpmemFlag = corpmemFlag;
    }

    /**
     * <p>本公司员工号</p>
     */
    public String getCorpmemNo() {
        return corpmemNo;
    }

    /**
     * <p>本公司员工号</p>
     */
    public void setCorpmemNo(String corpmemNo) {
        this.corpmemNo = corpmemNo;
    }

    /**
     * <p>公司行业类别</p>
     */
    public String getCorpType() {
        return corpType;
    }

    /**
     * <p>公司行业类别</p>
     */
    public void setCorpType(String corpType) {
        this.corpType = corpType;
    }

    /**
     * <p>本公司工作年限</p>
     * <p>本单位工作年限</p>
     */
    public Integer getCorpWorkyears() {
        return corpWorkyears;
    }

    /**
     * <p>本公司工作年限</p>
     * <p>本单位工作年限</p>
     */
    public void setCorpWorkyears(Integer corpWorkyears) {
        this.corpWorkyears = corpWorkyears;
    }

    /**
     * <p>工作稳定性</p>
     */
    public String getCorpStability() {
        return corpStability;
    }

    /**
     * <p>工作稳定性</p>
     */
    public void setCorpStability(String corpStability) {
        this.corpStability = corpStability;
    }

    /**
     * <p>职业</p>
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * <p>职业</p>
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    /**
     * <p>职称</p>
     */
    public String getTitleOfTechnical() {
        return titleOfTechnical;
    }

    /**
     * <p>职称</p>
     */
    public void setTitleOfTechnical(String titleOfTechnical) {
        this.titleOfTechnical = titleOfTechnical;
    }

    /**
     * <p>是否在职</p>
     */
    public String getEmpStatus() {
        return empStatus;
    }

    /**
     * <p>是否在职</p>
     */
    public void setEmpStatus(String empStatus) {
        this.empStatus = empStatus;
    }

    /**
     * <p>发薪方式</p>
     */
    public String getCorpPayWay() {
        return corpPayWay;
    }

    /**
     * <p>发薪方式</p>
     */
    public void setCorpPayWay(String corpPayWay) {
        this.corpPayWay = corpPayWay;
    }

    /**
     * <p>发薪日</p>
     */
    public String getCorpPayday() {
        return corpPayday;
    }

    /**
     * <p>发薪日</p>
     */
    public void setCorpPayday(String corpPayday) {
        this.corpPayday = corpPayday;
    }

    /**
     * <p>单位月收入/元</p>
     */
    public BigDecimal getMonthSalary() {
        return monthSalary;
    }

    /**
     * <p>单位月收入/元</p>
     */
    public void setMonthSalary(BigDecimal monthSalary) {
        this.monthSalary = monthSalary;
    }

    /**
     * <p>其他月收入</p>
     */
    public BigDecimal getOtherIncome() {
        return otherIncome;
    }

    /**
     * <p>其他月收入</p>
     */
    public void setOtherIncome(BigDecimal otherIncome) {
        this.otherIncome = otherIncome;
    }

    /**
     * <p>月总收入/元</p>
     */
    public BigDecimal getTotalMonthSalary() {
        return totalMonthSalary;
    }

    /**
     * <p>月总收入/元</p>
     */
    public void setTotalMonthSalary(BigDecimal totalMonthSalary) {
        this.totalMonthSalary = totalMonthSalary;
    }

    /**
     * <p>成立时间</p>
     */
    public Date getSetupDate() {
        return setupDate;
    }

    /**
     * <p>成立时间</p>
     */
    public void setSetupDate(Date setupDate) {
        this.setupDate = setupDate;
    }

    /**
     * <p>可接受的月最高还款</p>
     */
    public BigDecimal getMonthMaxRepay() {
        return monthMaxRepay;
    }

    /**
     * <p>可接受的月最高还款</p>
     */
    public void setMonthMaxRepay(BigDecimal monthMaxRepay) {
        this.monthMaxRepay = monthMaxRepay;
    }

    /**
     * <p>占股比例/%</p>
     */
    public BigDecimal getSharesScale() {
        return sharesScale;
    }

    /**
     * <p>占股比例/%</p>
     */
    public void setSharesScale(BigDecimal sharesScale) {
        this.sharesScale = sharesScale;
    }

    /**
     * <p>注册资本/万元</p>
     */
    public BigDecimal getRegisterFunds() {
        return registerFunds;
    }

    /**
     * <p>注册资本/万元</p>
     */
    public void setRegisterFunds(BigDecimal registerFunds) {
        this.registerFunds = registerFunds;
    }

    /**
     * <p>私营企业类型</p>
     */
    public String getPriEnterpriseType() {
        return priEnterpriseType;
    }

    /**
     * <p>私营企业类型</p>
     */
    public void setPriEnterpriseType(String priEnterpriseType) {
        this.priEnterpriseType = priEnterpriseType;
    }

    /**
     * <p>经营场所</p>
     */
    public String getBusinessPlace() {
        return businessPlace;
    }

    /**
     * <p>经营场所</p>
     */
    public void setBusinessPlace(String businessPlace) {
        this.businessPlace = businessPlace;
    }

    /**
     * <p>月租金/元</p>
     */
    public BigDecimal getMonthRent() {
        return monthRent;
    }

    /**
     * <p>月租金/元</p>
     */
    public void setMonthRent(BigDecimal monthRent) {
        this.monthRent = monthRent;
    }

    /**
     * <p>员工人数/人</p>
     */
    public Integer getEmployeeNum() {
        return employeeNum;
    }

    /**
     * <p>员工人数/人</p>
     */
    public void setEmployeeNum(Integer employeeNum) {
        this.employeeNum = employeeNum;
    }

    /**
     * <p>企业净利润率/%</p>
     */
    public BigDecimal getEnterpriseRate() {
        return enterpriseRate;
    }

    /**
     * <p>企业净利润率/%</p>
     */
    public void setEnterpriseRate(BigDecimal enterpriseRate) {
        this.enterpriseRate = enterpriseRate;
    }

    /**
     * <p>股东姓名(除客户外最大股东)</p>
     */
    public String getSharesName() {
        return sharesName;
    }

    /**
     * <p>股东姓名(除客户外最大股东)</p>
     */
    public void setSharesName(String sharesName) {
        this.sharesName = sharesName;
    }

    /**
     * <p>股东身份证</p>
     */
    public String getSharesIdNo() {
        return sharesIdNo;
    }

    /**
     * <p>股东身份证</p>
     */
    public void setSharesIdNo(String sharesIdNo) {
        this.sharesIdNo = sharesIdNo;
    }

    /**
     * <p>每月净利率额/万元</p>
     */
    public BigDecimal getMonthAmt() {
        return monthAmt;
    }

    /**
     * <p>每月净利率额/万元</p>
     */
    public void setMonthAmt(BigDecimal monthAmt) {
        this.monthAmt = monthAmt;
    }

    /**
     * <p>人行征信ID</p>
     */
    public BigDecimal getReportId() {
        return reportId;
    }

    /**
     * <p>人行征信ID</p>
     */
    public void setReportId(BigDecimal reportId) {
        this.reportId = reportId;
    }

    /**
     * <p>绑定记录信息</p>
     */
    public String getReportMessage() {
        return reportMessage;
    }

    /**
     * <p>绑定记录信息</p>
     */
    public void setReportMessage(String reportMessage) {
        this.reportMessage = reportMessage;
    }
}