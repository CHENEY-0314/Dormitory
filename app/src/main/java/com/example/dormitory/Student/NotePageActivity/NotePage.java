package com.example.dormitory.Student.NotePageActivity;

import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dormitory.R;
import com.example.dormitory.Student.Adapters.ExpandFoldTextAdapter;
import com.example.dormitory.Student.Adapters.GirdDropDownAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.api.ScrollBoundaryDecider;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

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
    private SharedPreferences mUser;
    private SharedPreferences.Editor mUserEditor;
    JSONObject jsonObject,jsonObject2;
    String data;
    private LinearLayout Nonote;
    boolean  refresh=false,load=false,nomoredata=true;
    int i=1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.notepage_nomessage_layout, container, false);
        dropdownmenu=(LinearLayout) view.findViewById(R.id.drop_down_menu);
        dropdownmenu.setClickable(true);
        Nonote=(LinearLayout)view.findViewById(R.id.NoNote);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        typeicon=(ImageView) view.findViewById(R.id.type_icon);
        initNote("201830660178","123456");
        initView();
        initRefreshLayout(view);
        return view;
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
        tabtext.setTextColor(getResources().getColor(R.color.white));
        menustate=false;
    }
    private void initRefreshLayout(View view){

        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() { //下拉刷新
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mList.clear();
                i=1;
                initNote("201830660178","123456");
                refreshlayout.finishRefresh(2000,refresh);//传入false表示刷新失败
            }
        });
        mRefreshLayout.setScrollBoundaryDecider(new ScrollBoundaryDecider() {
            @Override
            public boolean canRefresh(View content) {
                if (mRecyclerView == null) return false;
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition == 0) {
                    View child = layoutManager.findViewByPosition(firstCompletelyVisibleItemPosition);
                    int deltaY = mRecyclerView.getBottom() - mRecyclerView.getPaddingBottom() -
                            child.getBottom();
                    // firstCompletelyVisibleItemPosition为0时说明列表滚动到了顶部, 不再滚动
                    if (deltaY > 0) {
                        mRecyclerView.smoothScrollBy(0, -deltaY);
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean canLoadMore(View content) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                //屏幕中最后一个可见子项的position
                int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                //当前屏幕所看到的子项个数
                int visibleItemCount = layoutManager.getChildCount();
                //当前RecyclerView的所有子项个数
                int totalItemCount = layoutManager.getItemCount();
                //RecyclerView的滑动状态
                int state = mRecyclerView.getScrollState();
                if (visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && state == mRecyclerView.SCROLL_STATE_IDLE) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() { //上拉加载更多
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                try {
                    LoadMoreData(data,i);
                    Toast.makeText(getActivity(),"加载成功！",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    load=false;
                    Toast.makeText(getActivity(),"网络连！",Toast.LENGTH_SHORT).show();
                }
                refreshlayout.finishLoadMore(2000,load,nomoredata);//传入false表示加载失败
            }
        });
    }
    void initNote(final String s_id, final String password){
        mUser=getActivity().getSharedPreferences("userdata",MODE_PRIVATE);
        mUserEditor=mUser.edit();
        String url="http://39.97.114.188/Dormitory/servlet/GetNoteServlet?s_id="+s_id+"&password="+password;
        String tag= "getnote";
        //取得请求队列
        RequestQueue getnote = Volley.newRequestQueue(getActivity());
        //防止重复请求，所以先取消tag标识的请求队列
        getnote.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest getnoterequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.length()==0){
                                mRecyclerView.setVisibility(View.GONE);
                                Nonote.setVisibility(View.VISIBLE);
                            }else {
                                JSONObject jsonObject2;
                                data = response;
                                int n;
                                if (jsonObject.length() >= 10)
                                    n = 10;
                                else n = jsonObject.length();
                                for (int j = 1; j <= n; j++) {
                                    Note note = new Note();
                                    System.out.println(response);
                                    jsonObject2 = (JSONObject) new JSONObject(response).get(String.valueOf(j));
                                    note.setId(jsonObject2.getString("code"));
                                    note.setTopic(jsonObject2.getString("head"));
                                    note.setContent(jsonObject2.getString("content"));
                                    note.setPushtime(jsonObject2.getString("time"));
                                    switch (note.getId().substring(0, 1)) {
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
                                }
                                adapter = new ExpandFoldTextAdapter(mList, getActivity());
                                mRecyclerView.setAdapter(adapter);
                                refresh = true;
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            refresh=false;
                            Toast.makeText(getActivity(),"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getActivity(),"请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
        };
        //设置Tag标签
        getnoterequest.setTag(tag);
        //将请求添加到队列中
        getnote.add(getnoterequest);

    }
void LoadMoreData(String response,int i) throws JSONException {
    jsonObject = new JSONObject(response);
    if (jsonObject.length() / 10 - 1 >= i) {
        //System.out.println(jsonObject.length()+"+++++++++++++++++++++++");
        for (int j = 1; j <= 10; j++) {
            Note note = new Note();
            jsonObject2 = (JSONObject) new JSONObject(response).get(String.valueOf(i*10+j));
            note.setId(jsonObject2.getString("code"));
            note.setTopic(jsonObject2.getString("head"));
            note.setContent(jsonObject2.getString("content"));
            note.setPushtime(jsonObject2.getString("time"));
            switch (note.getId().substring(0, 1)) {
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
            adapter.addItem(note);
        }
        i++;
        nomoredata=false;

        if(jsonObject.length()%10==0)
            nomoredata=true;
    }
    if (jsonObject.length()/10-1<i){
    for (int j = 1; j < jsonObject.length() % 10; j++) {
        Note note = new Note();
        if(jsonObject.length()/10==0)
            jsonObject2 = (JSONObject) new JSONObject(response).get(String.valueOf(j));
        else jsonObject2 = (JSONObject) new JSONObject(response).get(String.valueOf(i*10+j));
        note.setId(jsonObject2.getString("code"));
        note.setTopic(jsonObject2.getString("head"));
        note.setContent(jsonObject2.getString("content"));
        note.setPushtime(jsonObject2.getString("time"));
        switch (note.getId().substring(0, 1)) {
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
        adapter.addItem(note);
    }
    nomoredata=true;
    }
    load=true;
    adapter.notifyDataSetChanged();
    mRecyclerView.setAdapter(adapter);
}
}