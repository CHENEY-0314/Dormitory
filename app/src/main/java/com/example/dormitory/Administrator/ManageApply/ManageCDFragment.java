package com.example.dormitory.Administrator.ManageApply;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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

public class ManageCDFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManageCDFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageCDFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageCDFragment newInstance(String param1, String param2) {
        ManageCDFragment fragment = new ManageCDFragment();
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

    //声明控件
    private ListView lvTrace;
    private List<ChangeDorApply> applyList = new ArrayList<>(10);
    private MCDFAdapter adapter;
    private LinearLayout Noapply;
    private SharedPreferences mAdm;//获取本地数据库

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_manage_r_d, container, false);

        lvTrace = (ListView) view.findViewById(R.id.MRDF_Listview);  //有申请时显示
        lvTrace.setVerticalScrollBarEnabled(false);
        Noapply=view.findViewById(R.id.MRDF_NoApply);   //无申请时显示
        loadApply(view);
        //判断当前是否有申请
        //-----------------------------若有则进行以下操作---------------------------------------------

        //创建四个人的信息，两两一对进行换宿舍，内容包括姓名，性别，楼栋，房间号，床位号，学号
//        String[] mess1_person1=new String[]{"张三","男","C10","234","4号","201812345678"};
//        String[] mess1_person2=new String[]{"李四","男","C12","567","1号","201911112222"};
//        String[] mess2_person1=new String[]{"王五","女","C5","666","2号","201933334444"};
//        String[] mess2_person2=new String[]{"晓晓","女","C5","520","3号","201920131415"};
//        //向list中添加两条换宿申请信息
//        applyList.add(new ChangeDorApply(mess1_person1,mess1_person2,true));
//        applyList.add(new ChangeDorApply(mess2_person1,mess2_person2,true));
//        adapter = new MCDFAdapter(view.getContext(), applyList);
//        lvTrace.setAdapter(adapter);


        //--------------------------------若无申请，进行以下操作--------------------------------------
//        lvTrace.setVisibility(View.GONE);
//        Noapply.setVisibility(View.VISIBLE);

        return view;
    }
    //初始化函数
    private void init(View view){ }

    //加载换宿申请函数，先进行是否存在的判断，有则加载，无则加载无申请界面
    private void loadApply(final View view){
        //获取账号密码
        mAdm=getActivity().getSharedPreferences("admdata", Context.MODE_PRIVATE);
        String a_id=mAdm.getString("a_id","");
        String password=mAdm.getString("password","");
        //请求地址
        //http://39.97.114.188/Dormitory/servlet/AdmGetExchangeAppServlet?a_id=000001&password=123456
        String url = "http://39.97.114.188/Dormitory/servlet/AdmGetExchangeAppServlet?a_id="+a_id+"&password="+password;
        String tag="loadApply";
        //取得请求队列
        RequestQueue loadApply= Volley.newRequestQueue(getActivity());
        //防止重复请求，先取消tag标识的全部请求
        loadApply.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest loadApplyRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=(JSONObject) new JSONObject(response);
                            System.out.println("输出jsonobject"+jsonObject);
                            int n=jsonObject.length();
                            int apply_num=0;
                            if(n!=0){
                                for(int i=1;i<=n;i++){
                                    JSONObject jsonObject2=(JSONObject) new JSONObject(response).get(String.valueOf(i));
                                    if(jsonObject2.getString("state").equals("2")){
                                        apply_num++;
                                        //给申请人和被申请人的信息赋值
                                        String[] person1=new String[6];
                                        String[] person2=new String[6];

                                        person1[0]=jsonObject2.getString("name");
                                        person1[1]=jsonObject2.getString("sex");
                                        person1[2]=jsonObject2.getString("building");
                                        person1[3]=jsonObject2.getString("room_num");
                                        person1[4]=jsonObject2.getString("bed_num");
                                        person1[5]=jsonObject2.getString("s_id");

                                        person2[0]=jsonObject2.getString("tname");
                                        person2[1]=jsonObject2.getString("tsex");
                                        person2[2]=jsonObject2.getString("t_building");
                                        person2[3]=jsonObject2.getString("t_room_num");
                                        person2[4]=jsonObject2.getString("t_bed_num");
                                        person2[5]=jsonObject2.getString("target_id");

                                        //向list中添加换宿申请信息
                                        applyList.add(new ChangeDorApply(person1,person2,true,jsonObject2.getString("change_code")));
                                    }
                                    else if(jsonObject2.getString("state").equals("4")){
                                        apply_num++;
                                        //给申请人和被申请人的信息赋值
                                        String[] person1=new String[6];
                                        String[] person2=new String[6];

                                        person1[0]=jsonObject2.getString("name");
                                        person1[1]=jsonObject2.getString("sex");
                                        person1[2]=jsonObject2.getString("building");
                                        person1[3]=jsonObject2.getString("room_num");
                                        person1[4]=jsonObject2.getString("bed_num");
                                        person1[5]=jsonObject2.getString("s_id");

                                        person2[0]=jsonObject2.getString("tname");
                                        person2[1]=jsonObject2.getString("tsex");
                                        person2[2]=jsonObject2.getString("t_building");
                                        person2[3]=jsonObject2.getString("t_room_num");
                                        person2[4]=jsonObject2.getString("t_bed_num");
                                        person2[5]=jsonObject2.getString("target_id");

                                        //向list中添加换宿申请信息
                                        applyList.add(new ChangeDorApply(person1,person2,false,jsonObject2.getString("change_code")));
                                    }
                                    else {
                                        continue;
                                    }
                                }
                            }
                            if(apply_num==0){
                                //长度为0，表示没有相应状态的申请，显示没有申请
                                //--------------------------------若无申请，进行以下操作--------------------------------------
                                lvTrace.setVisibility(View.GONE);
                                Noapply.setVisibility(View.VISIBLE);
                            }
                            else{
                                adapter = new MCDFAdapter(view.getContext(), applyList);
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
        loadApplyRequest.setTag(tag);
        //将请求添加到队列中
        loadApply.add(loadApplyRequest);
    }
}
