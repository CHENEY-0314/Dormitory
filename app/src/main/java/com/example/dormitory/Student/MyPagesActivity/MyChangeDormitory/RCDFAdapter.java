package com.example.dormitory.Student.MyPagesActivity.MyChangeDormitory;

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

public class RCDFAdapter extends BaseAdapter {
    private Context context;
    private List<RecelveApply> ApplyList = new ArrayList<>(1);
    private boolean flag;
    private String[] params;
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL= 0x0001;

    public RCDFAdapter(Context context, List<RecelveApply> ApplyList,boolean flag,String[] params) {
        this.context = context;
        this.ApplyList = ApplyList;
        this.flag=flag;
        this.params=params;
    }

    @Override
    public int getCount() {
        return ApplyList.size();
    }

    @Override
    public RecelveApply getItem(int position) {
        return ApplyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final RecelveApply apply = getItem(position);
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_item_recelve_d, parent, false);
            holder.TextName=convertView.findViewById(R.id.AIRD_Name);
            holder.TextNumber=convertView.findViewById(R.id.AIRD_Number);
            holder.TextTime=convertView.findViewById(R.id.AIRD_Time);
            holder.btn_Agree=convertView.findViewById(R.id.AIRD_Agree);
            holder.btn_Refuse=convertView.findViewById(R.id.AIRD_Refuse);
            holder.alreadyAgree=convertView.findViewById(R.id.AIRD_allreadyAgree);//显示“已同意，正在处理”按钮
            holder.notYetAgree=convertView.findViewById(R.id.AIRD_notYetAgree);//显示拒绝和同意两个按钮
            holder.theWholeItem=convertView.findViewById(R.id.AIRD_wholeItem);
            convertView.setTag(holder);
        }

        holder.TextName.setText(apply.getapplyer());
        holder.TextNumber.setText(apply.getnumber());
        holder.TextTime.setText(apply.getapplyTime());
        if(!flag){
            holder.notYetAgree.setVisibility(View.GONE);
            holder.alreadyAgree.setVisibility(View.VISIBLE);
        }
        holder.btn_Refuse.setOnClickListener(new View.OnClickListener() {  //点击拒绝
            @Override
            public void onClick(View v) {
                //提交函数，参数为0表示拒绝
                submitAgreeOrNot(0);
                //隐藏整个item
                holder.theWholeItem.setVisibility(View.GONE);
            }
        });

        holder.btn_Agree.setOnClickListener(new View.OnClickListener() {   //点击同意
            @Override
            public void onClick(View v) {
                //提交函数，参数为1表示同意
                submitAgreeOrNot(1);
                //同意后将两个button隐藏并显示新的button
                holder.notYetAgree.setVisibility(View.GONE);
                holder.alreadyAgree.setVisibility(View.VISIBLE);
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
        public TextView TextTime, TextName,TextNumber;
        public Button btn_Refuse, btn_Agree;
        public LinearLayout notYetAgree,alreadyAgree;
        public CardView theWholeItem;//整个cardview控件
    }
    private void submitAgreeOrNot(final int agree){
        //获取当前时间
        SimpleDateFormat dff = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String time = dff.format(new Date());
        //获取本人名字
        SharedPreferences mUser=context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
        String tname=mUser.getString("s_name","");
        //请求地址
        //http://39.97.114.188/Dormitory/servlet/ExchangeTargetResponseServlet?change_code=7011
        // &target_id=201830660174&tname=张三&s_id=201830660178&time=2018:06:24:10:00&agree=0
        String url = "http://39.97.114.188/Dormitory/servlet/ExchangeTargetResponseServlet?change_code="+params[0]
                +"&target_id="+params[1]+"&tname="+tname+"&s_id="+params[2]+"&time="+time+"&agree="+agree;
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
                            System.out.println("输出jsonobject"+jsonObject);
                            if(jsonObject.getString("result").equals("success")){
                                Toast.makeText(context,"提交成功",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(context,"提交失败，请重新刷新",Toast.LENGTH_SHORT).show();
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
}
