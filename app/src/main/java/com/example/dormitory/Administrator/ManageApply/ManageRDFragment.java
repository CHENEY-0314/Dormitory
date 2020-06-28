package com.example.dormitory.Administrator.ManageApply;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dormitory.R;
import com.example.dormitory.RefreshListView;
import com.example.dormitory.Student.Adapters.ExpandFoldTextAdapter;
import com.example.dormitory.Student.NotePageActivity.Note;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageRDFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageRDFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    boolean finishrefresh=false;
    public ManageRDFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageRDFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageRDFragment newInstance(String param1, String param2) {
        ManageRDFragment fragment = new ManageRDFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //以上均使不需要理会

    //声明控件等
    private RefreshListView lvTrace;
    private List<RepairDorApply> applyList = new ArrayList<>();
    private MRDFAdapter adapter;
    private LinearLayout Noapply;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_manage_r_d, container, false);

        lvTrace = (RefreshListView) view.findViewById(R.id.MRDF_Listview);  //有申请时显示
        lvTrace.setVerticalScrollBarEnabled(false);

        adapter = new MRDFAdapter(getActivity(), applyList);
        lvTrace.setAdapter(adapter);
        lvTrace.setonRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                applyList.clear();
                initRepairApply("00001","123456");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(finishrefresh) {
                            lvTrace.onRefreshComplete();
                            finishrefresh=false;}
                    }
                },500);

            }
        });
        Noapply=view.findViewById(R.id.MRDF_NoApply);
        lvTrace.setEmptyView(Noapply);
        //判断当前是否有申请
        //-----------------------------若有则进行以下操作---------------------------------------------
        initRepairApply("00001","123456");
        return view;
    }
    void initRepairApply(final String a_id, final String password){
        String url="http://39.97.114.188/Dormitory/servlet/AdmGetFixApplyServlet?a_id=000001&password=123456";
        String tag= "getrepaiapply";
        //取得请求队列
        RequestQueue getrepaiapply = Volley.newRequestQueue(getActivity());
        //防止重复请求，所以先取消tag标识的请求队列
        getrepaiapply.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest getrepaiapplyrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONObject jsonObject2;
                            for(int j=1;j<=jsonObject.length();j++) {
                                RepairDorApply rapply = new RepairDorApply();
                                jsonObject2 = (JSONObject) new JSONObject(response).get(String.valueOf(j));
                                rapply.setBuildingNum(jsonObject2.getString("building"));
                                rapply.setTime(jsonObject2.getString("time"));
                                rapply.setPhoneNum(jsonObject2.getString("contact"));
                                rapply.setRepairTypes(jsonObject2.getString("maintenance"));
                                System.out.println(response);
                                rapply.setRoomNum(jsonObject2.getString("room_num"));
                                rapply.setMoreMessage(jsonObject2.getString("remark"));
                                rapply.setFixcode(jsonObject2.getString("fix_code"));
                                rapply.setSid(jsonObject2.getString("s_id"));
                                rapply.setState(jsonObject2.getString("state"));
                                applyList.add(rapply);
                                adapter.notifyDataSetChanged();
                                finishrefresh=true;
                            }
                        } catch (JSONException e) {
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
        getrepaiapplyrequest.setTag(tag);
        //将请求添加到队列中
        getrepaiapply.add(getrepaiapplyrequest);
    }
}
