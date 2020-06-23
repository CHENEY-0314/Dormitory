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

    private String fixcode;

    private String sid;

    private String state;

    private String time;
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

    public void setTime(String time) {
        String year = time.substring(0, 4);
        String month = time.substring(5, 7);
        String day = time.substring(8, 10);
        String hour = time.substring(11, 13);
        String minute= time.substring(14,16);
        this.time = year+"-"+month+"-"+day+" "+hour+":"+minute;
    }

    public String getTime() {
        return time;
    }

    public void setFixcode(String fixcode) {
        this.fixcode = fixcode;
    }

    public String getFixcode() {
        return fixcode;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSid() {
        return sid;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
