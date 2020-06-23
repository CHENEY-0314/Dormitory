package com.example.dormitory.Student.NotePageActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeDifCalculater {
    String timedif;
    int pushyear,pushmonth,pushday,pushhour,pushminute;
    int currentyear,currentmonth,currentday,currenthour,currentminute;
    public TimeDifCalculater(String pushtime){
        pushyear = Integer.parseInt(pushtime.substring(0, 4));
        pushmonth = Integer.parseInt(pushtime.substring(5, 7));
        pushday = Integer.parseInt(pushtime.substring(8, 10));
        pushhour = Integer.parseInt(pushtime.substring(11, 13));
        pushminute=Integer.parseInt(pushtime.substring(14,16));
        getCurrentTime();
    }
    private void getCurrentTime(){
        SimpleDateFormat dff = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String CurrentTime = dff.format(new Date());
        currentyear=Integer.parseInt(CurrentTime.substring(0, 4));
        currentmonth=Integer.parseInt(CurrentTime.substring(5, 7));
        currentday= Integer.parseInt(CurrentTime.substring(8, 10));
        currenthour= Integer.parseInt(CurrentTime.substring(11, 13));
        currentminute=Integer.parseInt(CurrentTime.substring(14,16));
    }

    public String getTimeDif() {
        if(pushyear!=currentyear){
            timedif=String.valueOf(currentyear-pushyear)+"年前";
        }else if(pushmonth!=currentmonth){
            timedif=String.valueOf(currentmonth-pushmonth)+"月前";
        }else if(pushday!=currentday){
            timedif=String.valueOf(currentday-pushday)+"天前";
        }else if(pushhour!=currenthour){
            timedif=String.valueOf(currenthour-pushhour)+"小时前";
        }else if(pushminute!=currentminute){
            timedif=String.valueOf(currentminute-pushminute)+"分钟前";
        }
        return timedif;
    }
}
