package com.example.dormitory.Student.NotePageActivity;

import android.app.Activity;

import com.example.dormitory.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetNote {
    String type;
    int datasize;
    String data;
    Activity mContext;
    private List<Note> mList=new ArrayList<>();
    private int lastread=0;
    private String types[] = {"学校通知", "换宿申请", "维修受理"};
    public GetNote(Activity mContext, String data, int datasize, String type){
        this.mContext=mContext;
        this.data=data;
        this.datasize=datasize;
        this.type=type;
    }

    public void getNoteList() throws JSONException {
        JSONObject jsonObject2;
        int i = 0;
        for (int j = lastread + 1; j <= datasize; j++) {
            Note note = new Note();
            System.out.println(data);
            jsonObject2 = (JSONObject) new JSONObject(data).get(String.valueOf(j));
            if (jsonObject2.getString("code").substring(0, 1).equals(type)) {
                note.setId(jsonObject2.getString("code"));
                note.setTopic(jsonObject2.getString("head"));
                note.setContent(jsonObject2.getString("content"));
                note.setPushtime(jsonObject2.getString("time"));
                switch (type) {
                    case "0":
                        note.setImage(R.drawable.school_note_image);
                        note.setType(types[0]);
                        break;
                    case "1":
                        note.setImage(R.drawable.dorm_note_image);
                        note.setType(types[1]);
                        break;
                    case "2":
                        note.setImage(R.drawable.rep_note_image);
                        note.setType(types[2]);
                        break;
                }
                mList.add(note);
                i++;
            }
            lastread = j;
            if (i == 10) break;
        }
    }
    public List<Note> getmList(){
        return mList;
    }

    public int getLastread() {
        return lastread;
    }

    public void setLastread() {
        this.lastread = 0;
    }

    public void reMoveLastGet(){
        mList.clear();
    }
}

