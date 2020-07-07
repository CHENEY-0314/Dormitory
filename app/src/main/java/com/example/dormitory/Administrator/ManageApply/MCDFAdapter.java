package com.example.dormitory.Administrator.ManageApply;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dormitory.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MCDFAdapter extends BaseAdapter {
    private Context context;
    private List<ChangeDorApply> ApplyList=new ArrayList<>(10);
    public static final int TYPE_TOP=0x0000;
    public static final int TYPE_NORMAL=0x0001;

    public MCDFAdapter(Context context,List<ChangeDorApply> ApplyList){
        this.context=context;
        this.ApplyList=ApplyList;
    }
    @Override
    public int getCount() {
        return ApplyList.size();
    }

    @Override
    public ChangeDorApply getItem(int position) {
        return ApplyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final ChangeDorApply apply=getItem(position);
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else{
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.adapter_item_manage_change_d,parent,false);
            holder.tv_name_1=convertView.findViewById(R.id.item_manage_name_1);
            holder.tv_sex_1=convertView.findViewById(R.id.item_manage_sex_1);
            holder.tv_building_1=convertView.findViewById(R.id.item_manage_buildingNum_1);
            holder.tv_roonNum_1=convertView.findViewById(R.id.item_manage_roomNum_1);
            holder.tv_bedNum_1=convertView.findViewById(R.id.item_manage_bedNum_1);
            holder.tv_stuNum_1=convertView.findViewById(R.id.item_manage_stuNum_1);

            holder.tv_name_2=convertView.findViewById(R.id.item_manage_name_2);
            holder.tv_sex_2=convertView.findViewById(R.id.item_manage_sex_2);
            holder.tv_building_2=convertView.findViewById(R.id.item_manage_buildingNum_2);
            holder.tv_roonNum_2=convertView.findViewById(R.id.item_manage_roomNum_2);
            holder.tv_bedNum_2=convertView.findViewById(R.id.item_manage_bedNum_2);
            holder.tv_stuNum_2=convertView.findViewById(R.id.item_manage_stuNum_2);

            //绑定按钮处的两种状态
            holder.state_verify=convertView.findViewById(R.id.item_manage_verify);
            holder.state_yesOrNot=convertView.findViewById(R.id.item_manage_yesOrNot);
            //绑定整个item，点击同意时整个item隐藏
            holder.theWhole=convertView.findViewById(R.id.item_manage_wholeItem);
            //根据数据类型显示相应的按钮状态栏
            if(!apply.getFlag()){
                holder.state_yesOrNot.setVisibility(View.GONE);
                holder.state_verify.setVisibility(View.VISIBLE);
            }
            //绑定同意、拒绝和最终的确认按钮
            holder.btn_Refuse=convertView.findViewById(R.id.item_manage_btn_refuse);
            holder.btn_Agree=convertView.findViewById(R.id.item_manage_btn_agree);
            holder.btn_finally_verify=convertView.findViewById(R.id.item_manage_verify_btn);

            convertView.setTag(holder);
        }
        holder.tv_name_1.setText(apply.getName1());
        holder.tv_sex_1.setText(apply.getSex1());
        holder.tv_building_1.setText(apply.getBuildingNum1());
        holder.tv_roonNum_1.setText(apply.getRoomNum1());
        holder.tv_bedNum_1.setText(apply.getBedNum1());
        holder.tv_stuNum_1.setText(apply.getStuNum1());

        holder.tv_name_2.setText(apply.getName2());
        holder.tv_sex_2.setText(apply.getSex2());
        holder.tv_building_2.setText(apply.getBuildingNum2());
        holder.tv_roonNum_2.setText(apply.getRoomNum2());
        holder.tv_bedNum_2.setText(apply.getBedNum2());
        holder.tv_stuNum_2.setText(apply.getStuNum2());


        holder.btn_Refuse.setOnClickListener(new View.OnClickListener() {  //点击拒绝
            @Override
            public void onClick(View v) {
                submit(apply.getChange_code(),apply.getStuNum1(),apply.getStuNum2(),apply.getName1(),apply.getName2(),"0",holder);
            }
        });

        holder.btn_Agree.setOnClickListener(new View.OnClickListener() {   //点击同意
            @Override
            public void onClick(View v) {
                submit(apply.getChange_code(),apply.getStuNum1(),apply.getStuNum2(),apply.getName1(),apply.getName2(),"1",holder);
            }
        });

        holder.btn_finally_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finallyVerify(apply.getChange_code(),apply.getStuNum1(),apply.getName1(),apply.getStuNum2(),apply.getName2(),holder);
            }
        });
        return convertView;
    }

    @Override
    public int getItemViewType(int id) {
        if (id == 0) {
            return TYPE_TOP;
        }
        return TYPE_NORMAL;
    }

    static class ViewHolder {
        public TextView tv_name_1,tv_sex_1,tv_building_1,tv_roonNum_1,tv_bedNum_1,tv_stuNum_1;
        public TextView tv_name_2,tv_sex_2,tv_building_2,tv_roonNum_2,tv_bedNum_2,tv_stuNum_2;
        public Button btn_Refuse, btn_Agree,btn_finally_verify;
        public LinearLayout state_yesOrNot,state_verify;
        public CardView theWhole;
    }
    //点击同意或者拒绝按钮，将change_code、s_id、target_id、agree传参进去
    public void submit(String change_code, String s_id, String target_id,String name,String tname, String agree, final ViewHolder holder){
        //获取账号密码
        SharedPreferences mAdm;
        mAdm=context.getSharedPreferences("admdata",Context.MODE_PRIVATE);
        String a_id=mAdm.getString("a_id","");
        String password=mAdm.getString("password","");
        //获取时间
        SimpleDateFormat dff = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String time = dff.format(new Date());
        //请求地址
        //新的如下：http://39.97.114.188/Dormitory/servlet/AdmExchangeResponseServlet?a_id=000001
        // &password=123456&change_code=2330&s_id=201830660178&name=陈晓杰&target_id=201830660175
        // &tname=张三&time=2018:06:24:10:00&agree=1

        //http://39.97.114.188/Dormitory/servlet/AdmExchangeResponseServlet?a_id=000001
        // &password=123456&change_code=8000&s_id=201830660178&target_id=201830660174&time=2018:06:24:10:00&agree=0
        String url = "http://39.97.114.188/Dormitory/servlet/AdmExchangeResponseServlet?a_id="+a_id
                +"&password="+password+"&change_code="+change_code+"&s_id="+s_id+"&name="+name+"&target_id="+target_id
                +"&tname="+tname+"&time="+time+"&agree="+agree;
        String tag="submit";
        //取得请求队列
        RequestQueue submit= Volley.newRequestQueue(context);
        //防止重复请求，先取消tag标识的全部请求
        submit.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest submitRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=(JSONObject) new JSONObject(response).get("params");
                            System.out.println("输出submit中jsonobject"+jsonObject);
                            if(jsonObject.getString("result").equals("success")){
                                Toast.makeText(context,"提交成功",Toast.LENGTH_SHORT).show();
                                holder.theWhole.setVisibility(View.GONE);
                            }
                            else {
                                Toast.makeText(context,"提交失败，请稍后重试！",Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            System.out.println(e);
                            Toast.makeText(context,"JSONException:请稍后重试！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(context,"error:请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        });
        //设置Tag标签
        submitRequest.setTag(tag);
        //将请求添加到队列中
        submit.add(submitRequest);
    }
    //最终的管理员确认按钮，点击后该item直接隐藏
    public void finallyVerify(String change_code,String s_id,String name,String target_id,String tname,final ViewHolder holder){
        //获取时间
        SimpleDateFormat dff = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String time = dff.format(new Date());
        //请求地址
        //新的如下：
        // http://39.97.114.188/Dormitory/servlet/ExchangeConfirmServlet?change_code=8000&s_id=201830660178
        // &name=陈晓杰&target_id=201830660174&tname=张三&time=2029:06:24:10:00
        String url = "http://39.97.114.188/Dormitory/servlet/ExchangeConfirmServlet?change_code="+change_code
                +"&s_id="+s_id+"&name="+name+"&target_id="+target_id+"&tname="+tname+"&time="+time;
        String tag="finallyVerify";
        //取得请求队列
        RequestQueue finallyVerify= Volley.newRequestQueue(context);
        //防止重复请求，先取消tag标识的全部请求
        finallyVerify.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest finallyVerifyRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=(JSONObject) new JSONObject(response);
                            System.out.println("输出finallyVerify中jsonobject"+jsonObject);
                            if(jsonObject.getString("result").equals("success")){
                                Toast.makeText(context,"提交成功",Toast.LENGTH_SHORT).show();
                                holder.theWhole.setVisibility(View.GONE);
                            }
                            else {
                                Toast.makeText(context,"提交失败，请稍后重试！",Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            System.out.println(e);
                            Toast.makeText(context,"JSONException:请稍后重试！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(context,"error:请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        });
        //设置Tag标签
        finallyVerifyRequest.setTag(tag);
        //将请求添加到队列中
        finallyVerify.add(finallyVerifyRequest);
    }
}
