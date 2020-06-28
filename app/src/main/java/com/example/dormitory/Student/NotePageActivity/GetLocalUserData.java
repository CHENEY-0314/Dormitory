package com.example.dormitory.Student.NotePageActivity;

import android.app.Activity;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class GetLocalUserData {
    private String id,password;
    Activity mContext;
    private SharedPreferences mUser;
    boolean isstudent;
    public GetLocalUserData(Activity mContext, boolean isstudent){
        this.mContext=mContext;
        this.isstudent=isstudent;
        if(isstudent)
        mUser=mContext.getSharedPreferences("userdata",MODE_PRIVATE);
        else mUser=mContext.getSharedPreferences("admdata",MODE_PRIVATE);
    }

    public String getId() {
        if(isstudent)
            id=mUser.getString("s_id",null);
        else id=mUser.getString("a_id",null);
        return id;
    }

    public String getPassword() {
        password=mUser.getString("password",null);
        return password;
    }
}
