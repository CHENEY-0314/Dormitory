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
    GetNote getnote1,getnote2,getnote3,getnote4;
    List<Note> mList = new ArrayList<>();
    List<Note> schoolnoteList=new ArrayList<>();
    List<Note> changedornoteList = new ArrayList<>();
    List<Note> repairapplynoteList=new ArrayList<>();
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRefreshLayout=(RefreshLayout)view.findViewById(R.id.refreshLayout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        typeicon=(ImageView) view.findViewById(R.id.type_icon);
        initNote("201830660178","123456",mList);
        initView();
        initRefreshLayout(mList,"3");
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
                    initRefreshLayout(mList,"3");
                    mRecyclerView.setAdapter(adapter);}else
                {girdDropDownAdapter.setCheckItem(position);
                    switch (position){
                        case 0:
                            tabtext.setText(types[0]);
                            adapter=new ExpandFoldTextAdapter(schoolnoteList, getActivity());
                            initRefreshLayout(schoolnoteList,"0");
                            break;
                        case 1:
                            tabtext.setText(types[1]);
                            adapter=new ExpandFoldTextAdapter(changedornoteList, getActivity());
                            initRefreshLayout(changedornoteList,"1");
                            break;
                        case 2:
                            tabtext.setText(types[2]);
                            adapter=new ExpandFoldTextAdapter(repairapplynoteList, getActivity());
                            initRefreshLayout(repairapplynoteList,"2");
                            break;
                    }
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
    private void initRefreshLayout(final List<Note> List, final String type){
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() { //下拉刷新
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getnote1.setLastread();
                getnote1.setLastread();
                getnote1.setLastread();
                schoolnoteList.clear();
                changedornoteList.clear();
                repairapplynoteList.clear();
                mList.clear();
                initNote("201830660178","123456",List);
                refreshlayout.finishRefresh(1000,refresh);//传入false表示刷新失败
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
                updateData("201830660178","123456");
                getnote1.updateData(data);
                getnote2.updateData(data);
                getnote3.updateData(data);
                getnote4.updateData(data);
                LoadMoreData(type);
                refreshlayout.finishLoadMore(1000,load,nomoredata);//传入false表示加载失败
            }
        });
    }
    void initNote(final String s_id, final String password, final List<Note> mList){
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
                        data = response;
                        try {
                                initGetNote(mList,response);
                                refresh = true;
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
    void LoadMoreData(String type) {
        try {
            switch (type) {
                case "0":
                    getnote1.reMoveLastGet();
                    getnote1.getNoteList();
                    schoolnoteList.addAll(getnote1.getmList());
                    if(getnote1.getLastread()==getnote1.datasize)
                        nomoredata=true;else nomoredata=false;
                    load = true;
                    adapter.notifyDataSetChanged();
                    break;
                case "1":
                    getnote2.reMoveLastGet();
                    getnote2.getNoteList();
                    changedornoteList.addAll(getnote2.getmList());
                    if(getnote2.getLastread()==getnote2.datasize)
                        nomoredata=true;else nomoredata=false;
                    load = true;
                    adapter.notifyDataSetChanged();
                    break;
                case "2":
                    getnote3.reMoveLastGet();
                    getnote3.getNoteList();
                    repairapplynoteList.addAll(getnote3.getmList());
                    if(getnote3.getLastread()==getnote3.datasize)
                        nomoredata=true;else nomoredata=false;
                    load = true;
                    adapter.notifyDataSetChanged();
                    break;
                case"3":
                    getnote4.reMoveLastGet();
                    getnote4.getAlltype();
                    mList.addAll(getnote4.getmList());
                    if(getnote4.getLastread()==getnote4.datasize)
                        nomoredata=true;else nomoredata=false;
                    load = true;
                    adapter.notifyDataSetChanged();
                    break;
            }
        } catch (JSONException e) {
            load = false;
            e.printStackTrace();
        }
    }
    void initGetNote(List<Note> List,String data) throws JSONException {
        JSONObject jsonObject=new JSONObject(data);
        getnote1=new GetNote(getActivity(),data,jsonObject.length(),"0");
        getnote2=new GetNote(getActivity(),data,jsonObject.length(),"1");
        getnote3=new GetNote(getActivity(),data,jsonObject.length(),"2");
        getnote4=new GetNote(getActivity(),data,jsonObject.length(),"3");
        getnote1.getNoteList();
        getnote2.getNoteList();
        getnote3.getNoteList();
        getnote4.getAlltype();
        schoolnoteList.addAll(getnote1.getmList());
        changedornoteList.addAll(getnote2.getmList());
        repairapplynoteList.addAll(getnote3.getmList());
        mList.addAll(getnote4.getmList());
        adapter=new ExpandFoldTextAdapter(List,getActivity());
        mRecyclerView.setAdapter(adapter);
    }
    String updateData(final String s_id, final String password){
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
                        data = response;
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
        return data;
    }
}