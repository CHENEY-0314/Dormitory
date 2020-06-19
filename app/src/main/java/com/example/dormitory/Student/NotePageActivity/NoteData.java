package com.example.dormitory.Student.NotePageActivity;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NoteData {
    Context mContext;
    //请求地址
    String url = "http://39.97.114.188:8080/Dormitory/servlet/";
    String tag = "Notedata";
    //取得请求队列
    String code,head,content,time;
    public NoteData(Activity context){
        mContext=context;
        initNote();
    }
    private void initNote(){
        RequestQueue Notedata = Volley.newRequestQueue(mContext);
        //防止重复请求，所以先取消tag标识的请求队列
        Notedata.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest Notedatarequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("结果");
                            code=jsonObject.getString("code");
                            head=jsonObject.getString("head");
                            content=jsonObject.getString("content");
                            time=jsonObject.getString("time");
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
        Notedatarequest.setTag(tag);
        //将请求添加到队列中
        Notedata.add(Notedatarequest);
    }
    public String getcode(){
        return code;
    }
    public String gethead(){
        return head;
    }
    public String getcontent(){
        return content;
    }
    public String gettime(){
        return time;
    }
    public void deleteNote(Note note){

    }

}
