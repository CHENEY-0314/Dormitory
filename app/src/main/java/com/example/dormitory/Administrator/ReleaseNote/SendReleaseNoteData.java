package com.example.dormitory.Administrator.ReleaseNote;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class SendReleaseNoteData {
    Activity mContext;
    String code,head,content,time;

    public SendReleaseNoteData(String head, String content,Activity mContext){
        code=String.valueOf(1);
        this.head=head;
        this.content=content;
        this.mContext=mContext;
        setTime();
    }
    public void setTime() {
        SimpleDateFormat dff = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        time = dff.format(new Date());
    }
    public void sendData(){
        //请求地址
        String url = "http://39.97.114.188:8080/Dormitory/servlet/ReleaseNoteServlet?code="+code+"&head="+head+"&content="+content+"&time="+time;
        //String url="http://39.97.114.188/Dormitory/servlet/ReleaseNoteServlet?code=0001&head=测试&content=text&time=06:19:09:15";
        String tag = "ReleaseData";
        //取得请求队列
        RequestQueue ReleaseData = Volley.newRequestQueue(mContext);
        //防止重复请求，所以先取消tag标识的请求队列
        ReleaseData.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest ReleaseDatarequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //将从数据库获取到的消息保存在本地
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            jsonObject.put("code",code);
                            jsonObject.put("head",head);
                            jsonObject.put("content",content);
                            jsonObject.put("time",time);
                            Toast toast=Toast.makeText(mContext,null,Toast.LENGTH_SHORT);
                            toast.setText("发布成功");
                            toast.show();
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(mContext,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(mContext,"请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("code", code);
                params.put("head", head);
                params.put("content",content);
                params.put("time",time);
                return params;
            }
        };
        //设置Tag标签
        ReleaseDatarequest.setTag(tag);
        //将请求添加到队列中
        ReleaseData.add(ReleaseDatarequest);
    }
}
