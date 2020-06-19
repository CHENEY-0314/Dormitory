package com.example.dormitory.Administrator.ReleaseNote;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SendReleaseNoteData {
    String code,head,content,time;

    public SendReleaseNoteData(String head, String content){
        code=String.valueOf(1);
        this.head=head;
        this.content=content;
        setTime();
    }
    public void setTime() {
        SimpleDateFormat dff = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        time = dff.format(new Date());
    }
    public void sendData(){

    }
}
