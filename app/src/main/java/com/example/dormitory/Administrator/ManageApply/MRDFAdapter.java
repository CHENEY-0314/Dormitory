package com.example.dormitory.Administrator.ManageApply;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dormitory.R;
import com.example.dormitory.Student.MyPagesActivity.SetupActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MRDFAdapter extends BaseAdapter {
    private Activity context;
    private List<RepairDorApply> ApplyList = new ArrayList<>(1);
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL= 0x0001;
    String refusecontent;
    ViewHolder holder;
    private ListView lvTrace;
    private LinearLayout Noapply;
    public MRDFAdapter(Activity context,List<RepairDorApply> ApplyList){
        this.context=context;
        this.ApplyList= ApplyList;
        lvTrace = (ListView) context.findViewById(R.id.MRDF_Listview);  //有申请时显示
        Noapply=context.findViewById(R.id.MRDF_NoApply);   //无申请时显示
    }
    @Override
    public int getCount() {
        return ApplyList.size();
    }

    @Override
    public RepairDorApply getItem(int position) {
        return ApplyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final RepairDorApply apply = getItem(position);
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_item_manage_repair_d, parent, false);
            holder.textBuilding = convertView.findViewById(R.id.item_manage_buildingNum);
            holder.textRoomNum = convertView.findViewById(R.id.item_manage_roomNum);
            holder.textPhoneNum = convertView.findViewById(R.id.item_manage_phoneNum);
            holder.textRepairTypes = convertView.findViewById(R.id.item_manage_repairTypes);
            holder.textMoreMessage = convertView.findViewById(R.id.item_manage_moreMessage);
            holder.btn_Refuse = convertView.findViewById(R.id.item_manage_btn_refuse);
            holder.btn_Agree = convertView.findViewById(R.id.item_manage_btn_agree);
            holder.applytime = convertView.findViewById(R.id.time_apply_repair);
            convertView.setTag(holder);
        }

        holder.textBuilding.setText(apply.getBuildingNum());
        holder.textRoomNum.setText(apply.getRoomNum());
        holder.textPhoneNum.setText(apply.getPhoneNum());
        holder.textRepairTypes.setText(apply.getRepairTypes());
        holder.textMoreMessage.setText(apply.getMoreMessage());
        holder.applytime.setText(apply.getTime());
        if (ApplyList.get(position).getState().equals("1")) {
            holder.btn_Refuse.setOnClickListener(new View.OnClickListener() {  //点击拒绝
                @Override
                public void onClick(View v) {
                    popDialog1(position);
                }
            });

            holder.btn_Agree.setOnClickListener(new View.OnClickListener() {   //点击同意
                @Override
                public void onClick(View v) {
                    popDialog2(position);
                }
            });
        }
        else{
            holder.btn_Agree.setText("完成维修");
            holder.btn_Agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishApply(position);
                    ApplyList.remove(position);
                    notifyDataSetChanged();
                }
            });
            holder.btn_Refuse.setVisibility(View.INVISIBLE);
        }
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
        public TextView textBuilding,textRoomNum,textPhoneNum,textRepairTypes,textMoreMessage,applytime;
        public Button btn_Refuse, btn_Agree;

    }
    public void popDialog1(final int position){
        final Dialog dlg = new Dialog(context);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dlg.show();
        Window window = dlg.getWindow();
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
        Point size =new Point();
        display.getSize(size);
        int width= size.x;
        lp.width = (int) (width-200); // 设置宽度
        dlg.getWindow().setAttributes(lp);
        window.setContentView(R.layout.dialog_send_reply);
        // 为确认按钮添加事件,执行退出应用操作
        Button btnok=(Button)window.findViewById(R.id.btn_ok);
        Button btncancel=(Button)window.findViewById(R.id.btn_cancel);
        final EditText refusetext=(EditText) window.findViewById(R.id.reply_edt);
        btnok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refusecontent=refusetext.getText().toString();
                sendManageResult(position);
                dlg.dismiss();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }
    public void popDialog2(final int position){
        final Dialog dlg = new Dialog(context);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dlg.show();
        Window window = dlg.getWindow();
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
        Point size =new Point();
        display.getSize(size);
        int width= size.x;
        lp.width = (int) (width-200); // 设置宽度
        dlg.getWindow().setAttributes(lp);
        window.setContentView(R.layout.dialog_box_adm);
        // 为确认按钮添加事件,执行退出应用操作
        Button btnok=(Button)window.findViewById(R.id.btn_ok);
        Button btncancel=(Button)window.findViewById(R.id.btn_cancel);
        btnok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ApplyList.get(position).setState("2");
                changeState(position);
                dlg.dismiss();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }
    void sendManageResult(final int position){
        String url="http://39.97.114.188/Dormitory/servlet/RefuseFixApplyServlet?fix_code="+ApplyList.get(position).getFixcode()+"&s_id="+ApplyList.get(position).getSid()+"&time="+getCurrentTime()+"&content="+refusecontent;
        String tag= "sendrefusetext";
        //取得请求队列
        RequestQueue sendrefusetext = Volley.newRequestQueue(context);
        //防止重复请求，所以先取消tag标识的请求队列
        sendrefusetext.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest sendrefusetextrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=(JSONObject) new JSONObject(response);
                            String Result=jsonObject.getString("result").toString();
                            if(Result.equals("success"))
                            {ApplyList.remove(position);
                                notifyDataSetChanged();
                            if(ApplyList.isEmpty())
                            {
                                lvTrace.setVisibility(View.GONE);
                                Noapply.setVisibility(View.VISIBLE);
                            }
                            }
                            Toast.makeText(context,Result,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(context,"发送失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(context,"请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
        };
        //设置Tag标签
        sendrefusetextrequest.setTag(tag);
        //将请求添加到队列中
        sendrefusetext.add(sendrefusetextrequest);
    }
    void changeState(final int position){
        String url="http://39.97.114.188/Dormitory/servlet/HandleFixApplyServlet?fix_code="+ApplyList.get(position).getFixcode()+"&s_id="+ApplyList.get(position).getSid()+"&time="+getCurrentTime();
        String tag= "changstatetext";
        //取得请求队列
        RequestQueue changstate = Volley.newRequestQueue(context);
        //防止重复请求，所以先取消tag标识的请求队列
        changstate.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest changstaterequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=(JSONObject) new JSONObject(response);
                            String Result=jsonObject.getString("result").toString();
                            System.out.println(Result);
                            if(Result.equals("success"))
                            {holder.btn_Agree.setText("维修完成");
                                holder.btn_Refuse.setVisibility(View.INVISIBLE);
                            holder.btn_Agree.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finishApply(position);
                                    ApplyList.remove(position);
                                    notifyDataSetChanged();
                                    if(ApplyList.isEmpty())
                                    {
                                        lvTrace.setVisibility(View.GONE);
                                        Noapply.setVisibility(View.VISIBLE);
                                    }
                                }
                            });}
                            else Toast.makeText(context,Result,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(context,"受理失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(context,"请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
        };
        //设置Tag标签
        changstaterequest.setTag(tag);
        //将请求添加到队列中
        changstate.add(changstaterequest);
    }
    String getCurrentTime(){
        SimpleDateFormat dff = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String CurrentTime = dff.format(new Date());
        return CurrentTime;
    }
    void finishApply(int position){
        String url="http://39.97.114.188/Dormitory/servlet/ComfirmFixOverServlet?fix_code="+ApplyList.get(position).getFixcode()+"&s_id="+ApplyList.get(position).getSid()+"&time="+getCurrentTime();
        String tag= "finishapply";
        //取得请求队列
        RequestQueue finishapply = Volley.newRequestQueue(context);
        //防止重复请求，所以先取消tag标识的请求队列
        finishapply.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest finishapplyrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=(JSONObject) new JSONObject(response);
                            String Result=jsonObject.getString("result");
                                Toast.makeText(context,Result,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(context,"发送失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(context,"请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
        };
        //设置Tag标签
        finishapplyrequest.setTag(tag);
        //将请求添加到队列中
        finishapply.add(finishapplyrequest);
    }

}
