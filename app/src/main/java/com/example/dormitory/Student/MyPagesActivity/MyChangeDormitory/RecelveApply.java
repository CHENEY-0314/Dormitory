package com.example.dormitory.Student.MyPagesActivity.MyChangeDormitory;

//这是一个用于描述所接收到的申请的类，包括申请人姓名、联系方式、申请时间三个特征，以及操作这三个特征的函数
public class RecelveApply {
    /** 申请时间 */
    private String applyTime;
    /** 申请人姓名 */
    private String applyer;
    /** 联系方式 */
    private String number;

    public RecelveApply() {
    }

    public RecelveApply(String applyTime, String applyer,String number) {
        this.applyTime = applyTime;
        this.applyer = applyer;
        this.number = number;
    }

    public String getapplyTime() {
        return applyTime;
    }

    public void setapplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getapplyer() {
        return applyer;
    }

    public void setapplyer(String applyer) {
        this.applyer = applyer;
    }

    public String getnumber() {
        return number;
    }

    public void setnumber(String number) {
        this.number = number;
    }
}
