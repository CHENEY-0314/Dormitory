package com.example.dormitory.Administrator.ManageApply;

//这是一个用于描述所接收到的报修的类，包括报修楼栋、房间号、联系方式、报修类别、备注五个特征，以及操作这五个特征的函数
public class RepairDorApply {
    //报修楼栋号
    private String buildingNum;
    //报修房间号
    private String roomNum;
    //报修人联系方式
    private String phoneNum;
    //报修类型
    private String repairTypes;
    //报修备注
    private String moreMessage;
    public RepairDorApply(){}
    public RepairDorApply(String buildingNum,String roomNum, String phoneNum, String repairTypes, String moreMessage){
        this.buildingNum=buildingNum;
        this.roomNum=roomNum;
        this.phoneNum=phoneNum;
        this.repairTypes=repairTypes;
        this.moreMessage=moreMessage;
    }

    //对数据进行赋值与操作的函数
    public String getBuildingNum(){return buildingNum;}

    public void setBuildingNum(String buildingNum){this.buildingNum=buildingNum;}

    public String getRoomNum(){return roomNum;}

    public void setRoomNum(String roomNum){this.roomNum=roomNum;}

    public String getPhoneNum(){return phoneNum;}

    public void setPhoneNum(String phoneNum){this.phoneNum=phoneNum;}

    public String getRepairTypes(){return repairTypes;}

    public void setRepairTypes(String repairTypes){this.repairTypes=repairTypes;}

    public String getMoreMessage(){return moreMessage;}

    public void setMoreMessage(String moreMessage){this.moreMessage=moreMessage;}
}
