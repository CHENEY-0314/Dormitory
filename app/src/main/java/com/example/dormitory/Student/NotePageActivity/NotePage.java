package com.example.dormitory.Student.NotePageActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitory.R;
import com.example.dormitory.Student.Adapters.ExpandFoldTextAdapter;
import com.example.dormitory.Student.Adapters.GirdDropDownAdapter;
import com.zxl.library.DropDownMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotePage extends Fragment {
    private RecyclerView mRecyclerView;
    List<Note> mList = new ArrayList<>();
    private String types[] = {"学校通知", "换宿申请", "申请结果", "维修受理"};
    DropDownMenu mDropDownMenu;
    private String headers[] = {"类型"};
    public List<HashMap<String,Object>> popupViews = new ArrayList<>();

    private GirdDropDownAdapter typeAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.notepage_nomessage_layout, container, false);
        //mDropDownMenu=(DropDownMenu)view.findViewById(R.id.dropDownMenu);
        //init dropdownview
        //mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, view);
       // initView(view);
        initData();
        ExpandFoldTextAdapter adapter = new ExpandFoldTextAdapter(mList, this.getActivity());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);

        return view;
    }
    /**
     * 初始化数据
     */
    private void initData() {
        String schoolnoteContent = "学校通知学校通知学校通知学校通知学校通知学校通知学校通知学校通知学校通知学校通知学校通知学校通知学校通知学校通知学校通知学校通知学校通知学校通知学校通知学校通知学校通知";
        String changdornoteContent = "换宿申请换宿申请换宿申请换宿申请换宿申请换宿申请换宿申请换宿申请换宿申请换宿申请换宿申请换宿申请换宿申请换宿申请换宿申请换宿申请换宿申请换宿申请换宿申请换宿申请换宿申请";
        String applyreslutoteContent = "申请结果申请结果申请结果申请结果";
        String repairnoteContent = "维修受理维修受理维修受理维修受理";
        for (int i = 0; i <= 20; i++) {
            Note note = new Note();
            switch (i%4){
                case 0:
                    note.setImage(R.drawable.school_note_image);
                    note.setType(types[0]);
                    note.setContent(i + schoolnoteContent);
                    note.setId(i);
                    break;
                case 1:
                    note.setImage(R.drawable.dorm_note_image);
                    note.setType(types[1]);
                    note.setContent(i + changdornoteContent);
                    note.setId(i);
                    break;
                case 2:
                    note.setImage(R.drawable.apply_note_image);
                    note.setType(types[2]);
                    note.setContent(i + applyreslutoteContent);
                    note.setId(i);
                    break;
                case 3:
                    note.setImage(R.drawable.rep_note_image);
                    note.setType(types[3]);
                    note.setContent(i + repairnoteContent);
                    note.setId(i);
                    break;
            }
            mList.add(note);
        }
    }
/*
    private void initView(View view) {
        //init type menu
        HashMap<String,Object> type=new HashMap<>();
        final ListView typeView = new ListView(getActivity());
        typeAdapter = new GirdDropDownAdapter(getActivity(), Arrays.asList(types));
        typeView.setDividerHeight(0);
        typeView.setAdapter(typeAdapter);
        //init popupViews
        type.put("类型",typeView);
        popupViews.add(type);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers),popupViews,view);
        //add item click event
        typeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeAdapter.setCheckItem(position);
                if(position==0)
                    mDropDownMenu.setTabText(0,headers[0]);
                else
                    mDropDownMenu.setTabText(0,types[position]);
                mDropDownMenu.closeMenu();
            }
        });
    }*/
}
