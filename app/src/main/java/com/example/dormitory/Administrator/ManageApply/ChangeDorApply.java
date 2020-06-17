package com.example.dormitory.Administrator.ManageApply;
//这是一个用于描述收到的换宿申请的类，申请人与被申请人的信息分别用一个长度为6的字符串数组存储，顺序依次是姓名、性别、楼栋、房间号、床位号、学号
public class ChangeDorApply {
    //申请人信息，长度为6的字符串数组，顺序依次是姓名、性别、楼栋、房间号、床位号、学号
    private String[] person_1;
    //被申请人信息，长度为6的字符串数组，顺序依次是姓名、性别、楼栋、房间号、床位号、学号
    private String[] person_2;
    public ChangeDorApply(){}
    public ChangeDorApply(String[] person_1,String[] person_2){
        this.person_1=person_1;
        this.person_2=person_2;
    }
    //以下为对申请人信息进行操作的函数
    public String getName1(){return person_1[0];}

    public void setName1(String name1){this.person_1[0]=name1;}

    public String getSex1(){return person_1[1];}

    public void setSex1(String sex1){this.person_1[1]=sex1;}

    public String getBuildingNum1(){return person_1[2];}

    public void setBuildingNum1(String buildingNum1){this.person_1[2]=buildingNum1;}

    public String getRoomNum1(){return person_1[3];}

    public void setRoomNum1(String roomNum1){this.person_1[3]=roomNum1;}

    public String getBedNum1(){return person_1[4];}

    public void setBedNum1(String bedNum1){this.person_1[4]=bedNum1;}

    public String getStuNum1(){return person_1[5];}

    public void setStuNum1(String stuNum1){this.person_1[5]=stuNum1;}

    //以下为对被申请人信息进行操作的函数
    public String getName2(){return person_2[0];}

    public void setName2(String name2){this.person_2[0]=name2;}

    public String getSex2(){return person_2[1];}

    public void setSex2(String sex2){this.person_2[1]=sex2;}

    public String getBuildingNum2(){return person_2[2];}

    public void setBuildingNum2(String buildingNum2){this.person_2[2]=buildingNum2;}

    public String getRoomNum2(){return person_2[3];}

    public void setRoomNum2(String roomNum2){this.person_2[3]=roomNum2;}

    public String getBedNum2(){return person_2[4];}

    public void setBedNum2(String bedNum2){this.person_2[4]=bedNum2;}

    public String getStuNum2(){return person_2[5];}

    public void setStuNum2(String stuNum2){this.person_2[5]=stuNum2;}
}
