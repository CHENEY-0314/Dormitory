package com.example.dormitory.Student.NotePageActivity;

import java.util.ArrayList;
import java.util.List;

public class Classify {
    List<Note> mlist;
    String str;
    List<Note> classifynote=new ArrayList();
    public Classify(List<Note> clist,String str){
        mlist = clist;
        this.str=str;
    }
    public List<Note> Select(){
        for(int i=0;i<mlist.size();i++){
            if(mlist.get(i).getType()==str)
                classifynote.add(mlist.get(i));
        }
        return classifynote;
    }

}
