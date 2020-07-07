package com.example.dormitory.Student.MainPageActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Date;
import java.util.TimeZone;

public class LinearAdapater extends RecyclerView.Adapter<LinearAdapater.LinearViewHolder> {
    @NonNull
    //这个Context我也不知道干嘛的，教程是这样的
    private Context mContext;
    //存放信息的二维字符串数组，用来显示在筛选结果列表
    private String[][] resultInformation;
    //存放信息的字符串数组，每一条字符串里面用分号隔开楼栋、房间号等等
    private String[] selectedResult;
    //回调接口，用于点击提交申请后关闭当前页面
    private itemOnClickListener BTListener;
    public void setBTListener(itemOnClickListener btListener){
        this.BTListener=btListener;
    }

    public LinearAdapater(Context context,String[] selectedResult){
        this.mContext=context;
        this.selectedResult=selectedResult;
        //信息初始化
//        resultInformation=new String[10][4];
//        String pre_building=new String("C");
//        String pre_dorNum=new String("");
//        String pre_stuNum=new String("20183066000");
//        String pre_bedNum=new String("");
//        for(int i=0;i<10;i++){
//            resultInformation[i][0]=pre_building+String.valueOf(i+1);
//            resultInformation[i][1]=pre_dorNum+String.valueOf(143+i);
//            resultInformation[i][2]=pre_stuNum+String.valueOf(i);
//            resultInformation[i][3]=pre_bedNum+String.valueOf(i%4+1);
//        }
    }
    @Override
    public LinearAdapater.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.select_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final LinearAdapater.LinearViewHolder holder, final int position) {
        String[] result=selectedResult[position].split(";");
        //给item里面的TextView赋text值
        holder.tv_building.setText(result[0]);
        holder.tv_roomNum.setText(result[1]);
//        holder.tv_stuNum.setText("到时删掉");
        holder.tv_bedNum.setText(result[2]);
        //给item里面的TextView赋text值
//        holder.tv_building.setText(resultInformation[position][0]);
//        holder.tv_roomNum.setText(resultInformation[position][1]);
//        holder.tv_stuNum.setText(resultInformation[position][2]);
//        holder.tv_bedNum.setText(resultInformation[position][3]);
        //设置提交按钮点击事件
        holder.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,"提交成功："+position,Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                builder.setTitle("是否申请").setNegativeButton("取消",null);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        submitApply(position);
                        BTListener.on(0,0);
                    }
                });
                builder.setCancelable(false).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedResult.length;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{
        //声明item中的组件
        private TextView tv_building,tv_roomNum,tv_stuNum,tv_bedNum;
        private Button btnSubmit;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            //绑定组件
            tv_building=itemView.findViewById(R.id.select_item_building);
            tv_roomNum=itemView.findViewById(R.id.select_item_roomNum);
//            tv_stuNum=itemView.findViewById(R.id.select_item_stuNum);
            tv_bedNum=itemView.findViewById(R.id.select_item_bedNum);
            btnSubmit=itemView.findViewById(R.id.select_item_Submit);
        }
    }

    //点击提交申请按钮，通过传入的参数判断具体是哪一个item
    private void submitApply(int position){
        //将本条item的数据按分号拆开
        String[] singleResult=selectedResult[position].split(";");
        //获取本地数据库，以获得账号密码等
        final SharedPreferences mUser=mContext.getSharedPreferences("userdata",Context.MODE_PRIVATE);
        final SharedPreferences.Editor mUserEditor=mUser.edit();
        //以下数据均为接口所需
        String s_id=mUser.getString("s_id","");
        String password=mUser.getString("password","");
        String name=mUser.getString("s_name","");
        String sex=mUser.getString("sex","");


        String building=mUser.getString("building","");
        String room_num=mUser.getString("room_num","");
        String bed_num=mUser.getString("bed_num","");
        String contact=mUser.getString("phone_num","");
        String target_id=singleResult[4];
        String tname=singleResult[5];
        String tsex=singleResult[6];
        String tbuilding=singleResult[0];
        String troom_num=singleResult[1];
        String tbed_num=singleResult[2];
        String tcontact=singleResult[3];
        SimpleDateFormat dff = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String time = dff.format(new Date());
        //请求地址
        //http://39.97.114.188/Dormitory/servlet/ExchangeSubmitServlet?s_id=201830660178
        // &password=123456&name=陈晓杰&sex=男&building=C10&room_num=145&bed_num=1&contact=13502246751
        // &target_id=201830660175&tname=张三&tsex=女&tbuilding=C11&troom_num=121&tbed_num=3
        // &tcontact=13502243761&time=2000:06:12:12:33
        String url = "http://39.97.114.188/Dormitory/servlet/ExchangeSubmitServlet?s_id="+s_id
                +"&password="+password+"&name="+name+"&sex="+sex+"&building="+building+"&room_num="+room_num+"&bed_num="+bed_num
                +"&contact="+contact+"&target_id="+target_id+"&tname="+tname+"&tsex="+tsex+"&tbuilding="+tbuilding+"&troom_num="+troom_num+"&tbed_num="+tbed_num+"&tcontact="+tcontact+"&time="+time;
        String tag = "submitApply";
        //取得请求队列
        RequestQueue submitApply = Volley.newRequestQueue(this.mContext);
        //防止重复请求，所以先取消tag标识的请求队列
        submitApply.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest submitApplyrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            System.out.println("输出JSONObject："+jsonObject);
                            //当结果不等于failed，即结果为success的时候
                            if(!jsonObject.getString("result").equals("failed")){
                                String[] temp=jsonObject.getString("result").split(";");
                                mUserEditor.putString("change_code",temp[1]);
                                mUserEditor.commit();
                                Toast.makeText(mContext,"申请成功",Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(mContext,"返回值：failed",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            System.out.println(e);
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(mContext,"错误：JSONException",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(mContext,"error",Toast.LENGTH_SHORT).show();
            }
        }) {
        };
        //设置Tag标签
        submitApplyrequest.setTag(tag);
        //将请求添加到队列中
        submitApply.add(submitApplyrequest);
    }
}
