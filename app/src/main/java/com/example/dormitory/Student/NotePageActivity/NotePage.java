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

import java.util.ArrayList;
import java.util.List;

public class NotePage extends Fragment {
    private RecyclerView mRecyclerView;
    List<Note> mList = new ArrayList<>();
    private String types[] = {"学校通知", "换宿申请", "申请结果", "维修受理"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.notepage_nomessage_layout, container, false);

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
}
