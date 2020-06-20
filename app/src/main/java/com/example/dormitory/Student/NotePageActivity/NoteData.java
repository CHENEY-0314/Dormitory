package com.example.dormitory.Student.NotePageActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class NoteData {
    Context mContext;
    //请求地址
    //以下用于手机存用户信息
    private SharedPreferences mUser;
    private SharedPreferences.Editor mUserEditor;
    //取得请求队列
    Note note=new Note();
    String code,head,content,time;
    List<Note> mList=new ArrayList<>();
    public NoteData(Activity context){
        mContext=context;
        initNote("201830660178","123456");
    }
    private void initNote(final String s_id, final String password){
        mUser=mContext.getSharedPreferences("userdata",MODE_PRIVATE);
        mUserEditor=mUser.edit();
        //请求地址
        String url = "http://39.97.114.188/Dormitory/servlet/GetNoteServlet?s_id=201830660178&password=123456";
        String tag = "Notedata";
        //取得请求队列
        RequestQueue Notedata = Volley.newRequestQueue(mContext);
        //防止重复请求，所以先取消tag标识的请求队列
        Notedata.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest Notedatarequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject;
                            JSONArray jsonArray=new JSONArray(response);
                            //将从数据库获取到的消息保存在本地
                            for(int i=0;i<jsonArray.length();i++){
                                jsonObject = jsonArray.getJSONObject(i);
                            mUserEditor.putString("code",jsonObject.getString("code"));
                            mUserEditor.putString("head",jsonObject.getString("head"));
                            mUserEditor.putString("content",jsonObject.getString("content"));
                            mUserEditor.putString("time",jsonObject.getString("time"));
                            mUserEditor.apply();
                            note.setType(mUser.getString("code",null));
                            note.setTopic(mUser.getString("head",null));
                            note.setContent(mUser.getString("content",null));
                            note.setPushtime(mUser.getString("time",null));
                            mList.add(note);
                            }
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
                params.put("s_id", s_id);
                params.put("password", password);
                return params;
            }
        };
        //设置Tag标签
        Notedatarequest.setTag(tag);
        //将请求添加到队列中
        Notedata.add(Notedatarequest);
        System.out.println(mUser.getString("head","hhhhh"));
    }
    public List<Note> getList(){
        return mList;
    }

}
