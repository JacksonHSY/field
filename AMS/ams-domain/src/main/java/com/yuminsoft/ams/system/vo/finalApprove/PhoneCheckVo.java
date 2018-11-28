package com.yuminsoft.ams.system.vo.finalApprove;

/**
 * Created by ZJY on 2017/3/24.
 */
public class PhoneCheckVo {
    private  String[] contactName;
    private  String[] contactRelation;
    private  String[] ifKnowLoan;
    private  String[] contactEmpName;
    private  Integer[] sequenceNum;

    public Integer[] getSequenceNum() {
        return sequenceNum;
    }

    public void setSequenceNum(Integer[] sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    public String[] getContactName() {
        return contactName;
    }

    public void setContactName(String[] contactName) {
        this.contactName = contactName;
    }

    public String[] getContactRelation() {
        return contactRelation;
    }

    public void setContactRelation(String[] contactRelation) {
        this.contactRelation = contactRelation;
    }

    public String[] getIfKnowLoan() {
        return ifKnowLoan;
    }

    public void setIfKnowLoan(String[] ifKnowLoan) {
        this.ifKnowLoan = ifKnowLoan;
    }

    public String[] getContactEmpName() {
        return contactEmpName;
    }

    public void setContactEmpName(String[] contactEmpName) {
        this.contactEmpName = contactEmpName;
    }
}
