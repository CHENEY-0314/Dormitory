package com.example.dormitory.Student.MyPagesActivity.MyChangeDormitory;

import android.content.Context;
import android.content.Intent;
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
import com.example.dormitory.Student.MyPagesActivity.MyFixDormitory.MyFixDormitoryActivity;
import com.example.dormitory.Student.MyPagesActivity.Trace;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyCDFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCDFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public MyCDFragment() { }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyCDFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCDFragment newInstance(String param1, String param2) {
        MyCDFragment fragment = new MyCDFragment();
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


    //以上均使不需要理会,操作代码在下面写

    //声明控件
    private ListView lvTrace;
    private List<Trace> traceList = new ArrayList<>(10);
    private TimeLineAdapter adapter;
    private LinearLayout WithApply,NoApply,Loading;//有申请时显示、无申请时显示、加载时显示
    private TextView txt_ChangeApply,txt_DeleteApply,txt_Dormitory,txt_state;
    private LinearLayout alterApply,verifyApply;//中间白色的“修改申请”栏和“确认换宿”栏
    private Button btn_verify;//确认验收按钮
    private SharedPreferences mUser;//获取本地数据库
//    private String change_code,target_id;//为了避免麻烦，加载时间轴时存储这两个数据，就不放在本地数据库了
    private String[] dataForVerifyUrl;//为了减少换宿完成后对本地数据的删除，确认完成换宿按钮的接口url所需的参数放在这个字符串数组里面

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my_c_d, container, false);
        init(view);
        ifHaveMine(view);


        //判断当前是否有申请-------------若有则进行以下操作---------------------------------------------



        // 向List中添加数据，数据包括时间以及状态，如下所示
//        traceList.add(new Trace("2020-06-10 14:13:00", "换宿申请完成!"));
//        traceList.add(new Trace("2020-05-27 13:01:04", "管理员审核成功！请确认换宿。"));
//        traceList.add(new Trace("2020-05-25 12:19:47", "等待管理员审核。"));
//        traceList.add(new Trace("2020-05-20 11:12:44", "您的申请已提交，等待意向宿舍确认。"));

        //设置适配器
//        adapter = new TimeLineAdapter(view.getContext(), traceList);
//        lvTrace.setAdapter(adapter);

        return view;
    }
    //初始化函数，简单的对控件进行绑定和监听点击事件等
    private void init(final View view){
        txt_ChangeApply=view.findViewById(R.id.MyCDF_changeApply);  //点击修改申请
        txt_ChangeApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改换宿申请页面
                Intent intent = new Intent(getActivity(), ChangeChangeApplyActivity.class);
                MyCDFragment.this.startActivity(intent);
            }
        });

        txt_DeleteApply=view.findViewById(R.id.MyCDF_deleteApply);  //点击撤销申请
        txt_DeleteApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { }
        });

        txt_Dormitory=view.findViewById(R.id.MyCDF_Dormitory);  //目标宿舍如 "目标宿舍： C10-145"
        txt_state=view.findViewById(R.id.MyCDF_state);  // 未完成时显示 “处理中”  完成后显示 “已完成”

        alterApply=view.findViewById(R.id.MyCDF_alterApply);//中间白色“修改申请”状态栏
        verifyApply=view.findViewById(R.id.MyCDF_verifyApply);//中间白色“确认完成”状态栏

        btn_verify=view.findViewById(R.id.MyCDF_btn_verify);//确认换宿按钮以及点击事件
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyChange(view);
            }
        });

        WithApply=view.findViewById(R.id.MyCDF_HaveApply);  //有申请时显示
        NoApply=view.findViewById(R.id.MyCDF_NoApply);   //无申请时显示
        Loading=view.findViewById(R.id.gif_loading);//加载时显示

        lvTrace = (ListView)view.findViewById(R.id.MyCDF_lvTrace);//时间轴
    }

    //判断是否存在“我的申请”，有则加载时间轴内容，无则显示“无申请”
    private void ifHaveMine(final View view){
        //获取账号密码
        mUser=getActivity().getSharedPreferences("userdata", Context.MODE_PRIVATE);
        String s_id=mUser.getString("s_id","");
        String password=mUser.getString("password","");
        //请求地址
        //http://39.97.114.188/Dormitory/servlet/ExchangeGetMyAppServlet?s_id=201830660178&password=123456
        String url = "http://39.97.114.188/Dormitory/servlet/ExchangeGetMyAppServlet?s_id="+s_id+"&password="+password;
        String tag="ifHaveMine";
        //取得请求队列
        RequestQueue ifHaveMine= Volley.newRequestQueue(getActivity());
        //防止重复请求，先取消tag标识的全部请求
        ifHaveMine.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest ifHaveMineRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=(JSONObject) new JSONObject(response);
                            System.out.println("输出jsonobject"+jsonObject);
                            if(jsonObject.length()==0){
                                //长度为0，表示没有“我的申请”，显示没有申请
                                //--------------------------------若无申请，进行以下操作--------------------------------------
                                Loading.setVisibility(View.GONE);
                                WithApply.setVisibility(View.GONE);
                                NoApply.setVisibility(View.VISIBLE);
                            }
                            else{
                                int n=jsonObject.length();
                                if(n>=4){
                                    JSONObject jsonObject2=(JSONObject) new JSONObject(response).get("4");
                                    String time=jsonObject2.getString("time");
                                    StringBuilder time2=new StringBuilder(time);
                                    time2.replace(4,5,"-");
                                    time2.replace(7,8,"-");
                                    time2.replace(10,11," ");
                                    traceList.add(new Trace(time2.toString(), "换宿申请完成!"));

                                }
                                if(n>=3){
                                    JSONObject jsonObject2=(JSONObject) new JSONObject(response).get("3");
                                    String time=jsonObject2.getString("time");
                                    StringBuilder time2=new StringBuilder(time);
                                    time2.replace(4,5,"-");
                                    time2.replace(7,8,"-");
                                    time2.replace(10,11," ");
                                    traceList.add(new Trace(time2.toString(), "管理员审核成功！ 换宿完成后请确认完成。"));
                                }
                                if(n>=2){
                                    JSONObject jsonObject2=(JSONObject) new JSONObject(response).get("2");
                                    String time=jsonObject2.getString("time");
                                    StringBuilder time2=new StringBuilder(time);
                                    time2.replace(4,5,"-");
                                    time2.replace(7,8,"-");
                                    time2.replace(10,11," ");
                                    traceList.add(new Trace(time2.toString(), "等待管理员审核。"));
                                }
                                if(n>=1){
                                    JSONObject jsonObject2=(JSONObject) new JSONObject(response).get("1");
                                    String time=jsonObject2.getString("time");
                                    StringBuilder time2=new StringBuilder(time);
                                    time2.replace(4,5,"-");
                                    time2.replace(7,8,"-");
                                    time2.replace(10,11," ");
                                    traceList.add(new Trace(time2.toString(), "已提交，等待对方同意，对方联系方式为："
                                            +jsonObject2.getString("tcontact")));
                                    //顺便设置“目标宿舍”TextView
                                    TextView txt_targetDor=view.findViewById(R.id.MyCDF_Dormitory);
                                    txt_targetDor.setText("目标宿舍： "+jsonObject2.getString("tbuilding")+"-"+jsonObject2.getString("troom_num"));
                                    //给change_code、s_id、target_id赋值
//                                    change_code=jsonObject2.getString("change_code");
//                                    target_id=jsonObject2.getString("target_id");

                                    //给dataForVerifyUrl赋值
                                    System.out.println("准备给dataForVerify赋值");
                                    dataForVerifyUrl=new String[10];
                                    dataForVerifyUrl[0]=jsonObject2.getString("change_code");
                                    dataForVerifyUrl[1]=jsonObject2.getString("name");
                                    dataForVerifyUrl[2]=jsonObject2.getString("building");
                                    dataForVerifyUrl[3]=jsonObject2.getString("room_num");
                                    dataForVerifyUrl[4]=jsonObject2.getString("bed_num");
                                    dataForVerifyUrl[5]=jsonObject2.getString("target_id");
                                    dataForVerifyUrl[6]=jsonObject2.getString("tname");
                                    dataForVerifyUrl[7]=jsonObject2.getString("tbuilding");
                                    dataForVerifyUrl[8]=jsonObject2.getString("troom_num");
                                    dataForVerifyUrl[9]=jsonObject2.getString("tbed_num");
                                }
                                //设置适配器
                                adapter = new TimeLineAdapter(view.getContext(), traceList);
                                lvTrace.setAdapter(adapter);

                                //设置上方和中间状态栏
                                switch (n){
                                    case 1:{
                                        txt_state.setText("处理中");//处于状态1时修改上方状态，显示为“处理中”
                                        Loading.setVisibility(View.GONE);
                                        WithApply.setVisibility(View.VISIBLE);
                                        verifyApply.setVisibility(View.GONE);
                                        alterApply.setVisibility(View.VISIBLE);
                                        break;
                                    }
                                    case 2:{
                                        txt_state.setText("处理中");//处于状态2时修改上方状态，显示为“处理中”
                                        Loading.setVisibility(View.GONE);
                                        WithApply.setVisibility(View.VISIBLE);
                                        verifyApply.setVisibility(View.VISIBLE);
                                        alterApply.setVisibility(View.GONE);
//                                        Button btn_verify=view.findViewById(R.id.MyCDF_btn_verify);
                                        btn_verify.setEnabled(false);
                                        btn_verify.setText("确认换宿");
                                        break;
                                    }
                                    case 3:{
                                        txt_state.setText("待确认");//处于状态3时修改上方状态，显示为“待确认”
                                        Loading.setVisibility(View.GONE);
                                        WithApply.setVisibility(View.VISIBLE);
                                        verifyApply.setVisibility(View.VISIBLE);
                                        alterApply.setVisibility(View.GONE);
//                                        Button btn_verify=view.findViewById(R.id.MyCDF_btn_verify);
                                        btn_verify.setEnabled(true);
                                        btn_verify.setText("确认完成");
                                        break;
                                    }
                                    case 4:
                                    case 5:{
                                        txt_state.setText("已完成");//维修完成时修改上方状态，显示为“已完成”
                                        Loading.setVisibility(View.GONE);
                                        WithApply.setVisibility(View.VISIBLE);
                                        verifyApply.setVisibility(View.VISIBLE);
                                        alterApply.setVisibility(View.GONE);
//                                        Button btn_verify=view.findViewById(R.id.MyCDF_btn_verify);
                                        btn_verify.setEnabled(false);
                                        btn_verify.setText("已完成");
                                        break;
                                    }
                                    default:{
                                        break;
                                    }
                                }
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
                Toast.makeText(getContext(),"error:无网络连接！",Toast.LENGTH_SHORT).show();
            }
        });
        //设置Tag标签
        ifHaveMineRequest.setTag(tag);
        //将请求添加到队列中
        ifHaveMine.add(ifHaveMineRequest);
    }

    //点击确认换宿按钮
    private void verifyChange(final View view){
        //获取账号
        mUser=getActivity().getSharedPreferences("userdata", Context.MODE_PRIVATE);
        String s_id=mUser.getString("s_id","");
        String password=mUser.getString("password","");
        //获取时间
        SimpleDateFormat dff = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String time = dff.format(new Date());
        //请求地址
        //新地址如下：
        //http://39.97.114.188/Dormitory/servlet/ExchangeFinishServlet?change_code=8000&s_id=201830660178&password=123456
        // &name=陈晓杰&building=C10&room_num=145&bed_num=1&target_id=201830660175&tname=张三&tbuilding=C11&troom_num=121
        // &tbed_num=3&time=2000:06:12:12:33
        String url = "http://39.97.114.188/Dormitory/servlet/ExchangeFinishServlet?change_code="+dataForVerifyUrl[0]
                +"&s_id="+s_id+"&password="+password+"&name="+dataForVerifyUrl[1]+"&building="+dataForVerifyUrl[2]
                +"&room_num="+dataForVerifyUrl[3]+"&bed_num="+dataForVerifyUrl[4]+"&target_id="+dataForVerifyUrl[5]
                +"&tname="+dataForVerifyUrl[6]+"&tbuilding="+dataForVerifyUrl[7]+"&troom_num="+dataForVerifyUrl[8]
                +"&tbed_num="+dataForVerifyUrl[9]+"&time="+time;
        String tag="verifyChange";
        //取得请求队列
        RequestQueue verifyChange= Volley.newRequestQueue(getActivity());
        //防止重复请求，先取消tag标识的全部请求
        verifyChange.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest verifyChangeRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=(JSONObject) new JSONObject(response);
                            System.out.println("输出jsonobject"+jsonObject);
                            if(jsonObject.getString("result").equals("success")){
                                Toast.makeText(getContext(),"操作成功！",Toast.LENGTH_SHORT).show();
                                traceList.clear();
                                ifHaveMine(view);
                            }
                            else{
                                Toast.makeText(getContext(),"操作失败，请重新加载页面！",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(),"error:无网络连接！",Toast.LENGTH_SHORT).show();
            }
        });
        //设置Tag标签
        verifyChangeRequest.setTag(tag);
        //将请求添加到队列中
        verifyChange.add(verifyChangeRequest);
    }
}
