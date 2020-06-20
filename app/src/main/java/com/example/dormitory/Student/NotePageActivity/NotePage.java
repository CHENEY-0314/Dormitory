package com.example.dormitory.Student.NotePageActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitory.R;
import com.example.dormitory.Student.Adapters.ExpandFoldTextAdapter;
import com.example.dormitory.Student.Adapters.GirdDropDownAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotePage extends Fragment {
    private RecyclerView mRecyclerView;
    List<Note> mList = new ArrayList<>();
    List<Note> classifyList=new ArrayList<>();
    private String types[] = {"学校通知", "换宿申请", "维修受理"};
    LinearLayout dropdownmenu;
    ImageView typeicon;
    boolean menustate=false;
    ListView typeView;
    PopupWindow popupWindow;
    TextView tabtext;
    Classify classify;
    ExpandFoldTextAdapter adapter;
    RefreshLayout mRefreshLayout;
    NoteData noteData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.notepage_nomessage_layout, container, false);
        dropdownmenu=(LinearLayout) view.findViewById(R.id.drop_down_menu);
        dropdownmenu.setClickable(true);
        typeicon=(ImageView) view.findViewById(R.id.type_icon);
        initData();
        initView();
        initRefreshLayout(view);
        adapter=new ExpandFoldTextAdapter(mList, getActivity());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);
        return view;
    }
    /**
     * 初始化数据
     */
    private void initData() {
        noteData=new NoteData(getActivity());
        mList=noteData.getList();
        for (int i = 0; i <= mList.size(); i++) {
            Note note = new Note();
            switch (mList.get(i).getType().substring(0,1)){
                case "0":
                    note.setImage(R.drawable.school_note_image);
                    note.setType(types[0]);
                    note.setContent(mList.get(i).getContent());
                    note.setId(i);
                    note.setPushtime(mList.get(i).getPushtime());
                    note.setTopic(mList.get(i).getTopic());
                    break;
                case "1":
                    note.setImage(R.drawable.dorm_note_image);
                    note.setType(types[1]);
                    note.setContent(mList.get(i).getContent());
                    note.setId(i);
                    note.setPushtime(mList.get(i).getPushtime());
                    note.setTopic(mList.get(i).getTopic());
                    break;
                case "2":
                    note.setImage(R.drawable.rep_note_image);
                    note.setType(types[3]);
                    note.setContent(mList.get(i).getContent());
                    note.setId(i);
                    note.setPushtime(mList.get(i).getPushtime());
                    note.setTopic(mList.get(i).getTopic());
                    break;
            }
            mList.add(note);
        }
    }
    private void initView() {
        tabtext=dropdownmenu.findViewById(R.id.type_text);
        typeicon=dropdownmenu.findViewById(R.id.type_icon);
        typeView = new ListView(this.getActivity());
        final GirdDropDownAdapter girdDropDownAdapter = new GirdDropDownAdapter(Arrays.asList(types), getActivity());
        typeView.setDividerHeight(0);
        typeView.setAdapter(girdDropDownAdapter);
        popupWindow = new PopupWindow(typeView,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        typeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(girdDropDownAdapter.getCheckItemPosition()==position)
                { girdDropDownAdapter.setCheckItem(4);
                tabtext.setText("通知类型");
                adapter=new ExpandFoldTextAdapter(mList, getActivity());
                    mRecyclerView.setAdapter(adapter);}else
                {girdDropDownAdapter.setCheckItem(position);
                tabtext.setText(types[position]); classify=new Classify(mList,types[position]);
                classifyList.clear();
                classifyList=classify.Select();
                adapter=new ExpandFoldTextAdapter(classifyList, getActivity());
                mRecyclerView.setAdapter(adapter);}
                closeMenu(popupWindow);
            }
        });
        dropdownmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.setTouchable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(51, 255, 255, 255)));
                popupWindow.setAnimationStyle(R.style.AnimTop);
                popupWindow.setClippingEnabled(true);
                popupWindow.showAtLocation(dropdownmenu, Gravity.TOP,0,dropdownmenu.getHeight());
                popupWindow.update();
                popupWindow.setOutsideTouchable(true);
                if (!menustate) {
                    typeicon.setImageResource(R.drawable.drop_down_selected_icon);
                    tabtext.setTextColor(getResources().getColor(R.color.colorAccent));
                    menustate=true;
                    popupWindow.showAsDropDown(dropdownmenu,0,0);
                } else {
                    closeMenu(popupWindow);
                }
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                closeMenu(popupWindow);
            }
        });
    }
    public void closeMenu(PopupWindow popupWindow){
        popupWindow.dismiss();
        typeicon.setImageResource(R.drawable.drop_down_unselected_icon);
        tabtext.setTextColor(getResources().getColor(R.color.drop_down_unselected));
        menustate=false;
    }
    private void initRefreshLayout(View view){
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() { //下拉刷新
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() { //上拉加载更多
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
    }


}
