package com.yuminsoft.ams.system.vo.pic;/**
 * Created by YM10140 on 2017/4/26.
 */

/**
 * 质检反馈附件查看Vo
 *
 * @author phb
 * @create 2017-04-26 20:32
 **/
public class AttachmentVo {

    private Long id;// 标识

    private String imgName;//图片名称

    private String saveName;//保存名称

    private String subclassSort;//具体类型如：身份证件、工资证明、房产证明

    private String uptime;//上传日期

    private String appNo;//申请件编号

    private String sysName;//系统名称

    private String url;//  文件访问地址

    private String ifWaste;//作废（Y|是 N|否）

    private String ifPatchBolt;//补件（Y|是 N|否）

    private String createJobnum;//创建人工号
    private String creator;//创建人姓名
    private String createTime;//创建时间

    private String modifiedJobnum;//修改人工号
    private String modifier;//修改人姓名
    private String modifierTime;//修改时间


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }

    public String getSubclassSort() {
        return subclassSort;
    }

    public void setSubclassSort(String subclassSort) {
        this.subclassSort = subclassSort;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIfWaste() {
        return ifWaste;
    }

    public void setIfWaste(String ifWaste) {
        this.ifWaste = ifWaste;
    }

    public String getIfPatchBolt() {
        return ifPatchBolt;
    }

    public void setIfPatchBolt(String ifPatchBolt) {
        this.ifPatchBolt = ifPatchBolt;
    }

    public String getCreateJobnum() {
        return createJobnum;
    }

    public void setCreateJobnum(String createJobnum) {
        this.createJobnum = createJobnum;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifiedJobnum() {
        return modifiedJobnum;
    }

    public void setModifiedJobnum(String modifiedJobnum) {
        this.modifiedJobnum = modifiedJobnum;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModifierTime() {
        return modifierTime;
    }

    public void setModifierTime(String modifierTime) {
        this.modifierTime = modifierTime;
    }
}
