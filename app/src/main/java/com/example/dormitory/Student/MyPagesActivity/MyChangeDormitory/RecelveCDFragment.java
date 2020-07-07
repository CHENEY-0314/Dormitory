package com.example.dormitory.Student.MyPagesActivity.MyChangeDormitory;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dormitory.R;
import com.example.dormitory.Student.Adapters.TimeLineAdapter;
import com.example.dormitory.Student.MyPagesActivity.Trace;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecelveCDFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecelveCDFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecelveCDFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecelveCDFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecelveCDFragment newInstance(String param1, String param2) {
        RecelveCDFragment fragment = new RecelveCDFragment();
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

    private ListView lvTrace;//有申请时显示
    private List<RecelveApply> applyList = new ArrayList<>(10);
    private RCDFAdapter adapter;
    private LinearLayout Noapply;//无申请时显示

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recelve_c_d, container, false);
        init(view);


        //判断当前是否有申请
        //-----------------------------若有则进行以下操作---------------------------------------------

//        applyList.add(new RecelveApply("2020-06-11","张三","188172638443"));
//        applyList.add(new RecelveApply("2020-06-15","里斯特","17273628372"));

//        adapter = new RCDFAdapter(view.getContext(), applyList);
//        lvTrace.setAdapter(adapter);


        //--------------------------------若无申请，进行以下操作--------------------------------------
        //lvTrace.setVisibility(View.GONE);
        //Noapply.setVisibility(View.VISIBLE);

        return view;
    }

    //初始化函数
    private void init(View view){
        lvTrace = (ListView) view.findViewById(R.id.RCDF_Listview);  //有申请时显示
        Noapply=view.findViewById(R.id.RCDF_NoApply);   //无申请时显示
        ifHaveReceive(view);
    }

    //判断当前是否有我收到的申请，有的话显示，没有的话显示“无申请”控件
    private void ifHaveReceive(final View view){
        //获取账号密码
        SharedPreferences mUser=getActivity().getSharedPreferences("userdata", Context.MODE_PRIVATE);
        String s_id=mUser.getString("s_id","");
        String password=mUser.getString("password","");
        //请求地址
        //http://39.97.114.188/Dormitory/servlet/ExchangeGetExchangeAppServlet?s_id=201830660174&password=123456
        String url = "http://39.97.114.188/Dormitory/servlet/ExchangeGetExchangeAppServlet?s_id="+s_id+"&password="+password;
        String tag="ifHaveReceive";
        //取得请求队列
        RequestQueue ifHaveReceive= Volley.newRequestQueue(getActivity());
        //防止重复请求，先取消tag标识的全部请求
        ifHaveReceive.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest ifHaveReceiveRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=(JSONObject) new JSONObject(response);
                            System.out.println("输出jsonobject"+jsonObject);
                            if(jsonObject.length()==0){
                                //长度为0，表示没有“我的申请”，显示没有申请
                                //--------------------------------若无申请，进行以下操作--------------------------------------
                                lvTrace.setVisibility(View.GONE);
                                Noapply.setVisibility(View.VISIBLE);
                            }
                            else if(jsonObject.length()==1){
                                int n=jsonObject.length();
                                String[] change_target_s=new String[3];//存放change_code target_id s_id，便于同意或拒绝时传参
                                for(int i=1;i<=n;i++){
                                    JSONObject jsonObject2=(JSONObject) new JSONObject(response).get(String.valueOf(i));
                                    String name=jsonObject2.getString("name");
                                    String contact=jsonObject2.getString("contact");
                                    String temp_time=jsonObject2.getString("time").substring(0,10);
                                    StringBuilder time=new StringBuilder(temp_time);
                                    time.replace(4,5,"-");
                                    time.replace(7,8,"-");
                                    applyList.add(new RecelveApply(time.toString(),name,contact));
                                    //为change_target_s赋值
                                    change_target_s[0]=jsonObject2.getString("change_code");
                                    change_target_s[1]=jsonObject2.getString("target_id");
                                    change_target_s[2]=jsonObject2.getString("s_id");
                                }

                                adapter = new RCDFAdapter(view.getContext(), applyList,true,change_target_s);
                                lvTrace.setAdapter(adapter);
                            }
                            else{
                                String[] change_target_s=new String[3];//存放change_code target_id s_id，便于同意或拒绝时传参
                                JSONObject jsonObject2=(JSONObject) new JSONObject(response).get("1");
                                String name=jsonObject2.getString("name");
                                String contact=jsonObject2.getString("contact");
                                String temp_time=jsonObject2.getString("time").substring(0,10);
                                StringBuilder time=new StringBuilder(temp_time);
                                time.replace(4,5,"-");
                                time.replace(7,8,"-");
                                applyList.add(new RecelveApply(time.toString(),name,contact));
                                //为change_target_s赋值
                                change_target_s[0]=jsonObject2.getString("change_code");
                                change_target_s[1]=jsonObject2.getString("target_id");
                                change_target_s[2]=jsonObject2.getString("s_id");
                                adapter = new RCDFAdapter(view.getContext(), applyList,false,change_target_s);
                                lvTrace.setAdapter(adapter);
                            }
                        }catch (JSONException e){
                            System.out.println(e);
                            Toast.makeText(getContext(),"JSONException:请稍后重试！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getContext(),"error:请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        });
        //设置Tag标签
        ifHaveReceiveRequest.setTag(tag);
        //将请求添加到队列中
        ifHaveReceive.add(ifHaveReceiveRequest);
    }
}
