package com.example.dormitory.Student.MainPageActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.dormitory.R;
import com.example.dormitory.Student.NotePageActivity.TimeDifCalculater;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

import sakura.particle.Factory.ExplodeParticleFactory;
import sakura.particle.Main.ExplosionSite;

public class MainPage extends Fragment {

    private long exitTime=0;  //用于判断两次点击退出主页的事件间隔

    Boolean ifhaveIntention=false;  //用于标志是否有换宿意向
    Boolean light=false;  //用于标志是否点灯

    private ImageView changeIntention,back,mlight,qiqiu;
    private CardView toChange;//申请换宿舍
    private  CardView toRepair;//宿舍报修

    TimeDifCalculater timeDifCalculater;
    private SharedPreferences mUser;
    private SharedPreferences.Editor mUserEditor;

    private ExplosionSite mexplosionSite;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.mainpage_layout, container, false);
        //绑定控件
        toChange=view.findViewById(R.id.change);
        toRepair=view.findViewById(R.id.repair);
        changeIntention=view.findViewById(R.id.MPL_changeIntention);
        back=view.findViewById(R.id.mainback);
        mlight=view.findViewById(R.id.image13);
        qiqiu=view.findViewById(R.id.image12);

        mUser=getActivity().getSharedPreferences("userdata",MODE_PRIVATE);
        mUserEditor=mUser.edit();

        mexplosionSite  = new ExplosionSite(getActivity(), new ExplodeParticleFactory());
        mexplosionSite.addListener(qiqiu);

        //声明点击事件
        toChange.setOnClickListener(new ButtonListener());
        toRepair.setOnClickListener(new ButtonListener());
        changeIntention.setOnClickListener(new ButtonListener());
        back.setOnClickListener(new ButtonListener());
        mlight.setOnClickListener(new ButtonListener());

        if(mUser.getBoolean("switch_state",false)){
            ifhaveIntention=true;
            changeIntention.setImageDrawable(getResources().getDrawable((R.drawable.haveintentimg)));
        }

        return view;
    }
    //查看详情弹出的内容
    private void popDetail(){
        //设置弹窗
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        //弹窗图标
        builder.setIcon(R.drawable.select_item_info);
        //弹窗标题
        builder.setTitle("是否切换意向");
        //弹窗内容
        builder.setMessage("        开启“换宿意向”后，您将有可能收到来自他人的换宿舍申请；关闭则将不会收到来自他人的换宿舍申请；\n        如果您提交了换宿舍申请，将自动开启“换宿意向”；\n\nTips:\n为避免频繁操作，每天只能更改一次。");
        //弹窗点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mUser.getString("firstopentime","").equals("")){  //本地记录的上次切换时间不存在，则一定可以切换状态
                    setSwitch_state_enable();  //进行状态切换
                }else{   //本地有记录上一次切换转换状态时间，要判断是否经过了一天
                    timeDifCalculater=new TimeDifCalculater(mUser.getString("firstopentime","2020:06:18:00:00"));
                    int size=timeDifCalculater.getTimeDif().length();
                    String dw=timeDifCalculater.getTimeDif().substring(size-2,size-1);
                    System.out.println(dw+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    if(dw.equals("年")||dw.equals("月")||dw.equals("天")) {  //超过一天，进行状态切换
                        setSwitch_state_enable();
                    } else{   //未超过一天，不能切换
                        if(!ifhaveIntention){
                            Toast.makeText(getActivity(),"今天已无开启次数",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity(),"今天已无关闭次数",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击取消，则回退状态
            }
        });
        //显示弹窗
        builder.show();
    }
    private class ButtonListener implements View.OnClickListener{
        public void onClick(View v){
            switch (v.getId()){
                case R.id.change:{
                    toChange.setClickable(false);//跳转到申请换宿舍页面
                    Intent intent = new Intent(getActivity(), changeDormitory.class);
                    startActivity(intent);
                    toChange.setClickable(true);
                    break;
                }
                case R.id.repair:{
                    toRepair.setClickable(false);//跳转到宿舍报修页面
                    Intent intent = new Intent(getActivity(), repairDormitory.class);
                    startActivity(intent);
                    toRepair.setClickable(true);
                    break;
                }
                case R.id.MPL_changeIntention:{
                        //切换状态，出现再次确认弹窗
                        popDetail();
                        break;
                }
                case R.id.image13:{
                    //点灯
                    if(light){
                        //关灯
                        mlight.setImageDrawable(getResources().getDrawable((R.drawable.mainpage_nolight)));
                        light=false;
                    }else{
                        mlight.setImageDrawable(getResources().getDrawable((R.drawable.mainpage_light)));
                        light=true;
                    }
                    break;
                }
                case R.id.mainback:{  //退出程序
                    if ((System.currentTimeMillis() - exitTime) > 2000) {
                        Toast toast=Toast.makeText(getActivity(),null,Toast.LENGTH_SHORT);
                        toast.setText("再按一次退出程序");
                        toast.show();
                        exitTime = System.currentTimeMillis();
                    } else {
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);  //更改跳转动画
                    }
                    break;
                }
                default:break;
            }
        }
    }

    //状态切换函数
    public void setSwitch_state_enable() {
        final String s_id=mUser.getString("s_id","");
        final String password=mUser.getString("password","");
        //请求地址
        String url = "http://39.97.114.188/Dormitory/servlet/ChangeIntentionServlet?s_id="+s_id+"&password="+password;
        String tag = "changeIntention";
        //取得请求队列
        RequestQueue changeintention = Volley.newRequestQueue(getContext());
        //防止重复请求，所以先取消tag标识的请求队列
        changeintention.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest changeintentionrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //将从数据库获取到的消息保存在本地
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("result").equals("0")||jsonObject.getString("result").equals("false")){  //成功切换状态
                                mUserEditor.putString("firstopentime",getCurrentTime());  //更新本地记录
                                mUserEditor.apply();
                                if(!ifhaveIntention){
                                    //打开换宿意向
                                    changeIntention.setImageDrawable(getResources().getDrawable((R.drawable.haveintentimg)));
                                    ifhaveIntention=true;
                                    mUserEditor.putBoolean("switch_state",true);  //更新本地记录
                                    mUserEditor.apply();
                                    Toast.makeText(getActivity(),"已开启",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    //关闭换宿意向
                                    changeIntention.setImageDrawable(getResources().getDrawable((R.drawable.nointentionimage)));
                                    ifhaveIntention=false;
                                    mUserEditor.putBoolean("switch_state",false);  //更新
                                    mUserEditor.apply();
                                    Toast.makeText(getActivity(),"已关闭",Toast.LENGTH_SHORT).show();
                                }
                            }else if(jsonObject.getString("result").equals("1")){
                                Toast.makeText(getActivity(),"正在换宿中，无法切换状态！",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(),"操作失败，请稍后重试！",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(getActivity(),"请稍后重试！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getActivity(),"无网络连接,请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("s_id", s_id);
                params.put("password", password);
                return params;
            }
        };
        //设置Tag标签
        changeintentionrequest.setTag(tag);
        //将请求添加到队列中
        changeintention.add(changeintentionrequest);

    }
    private String getCurrentTime() {
        SimpleDateFormat dff = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String CurrentTime = dff.format(new Date());
        return CurrentTime;
    }

}
