package com.example.dormitory.Student.NotePageActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
    private Activity mContext;
    //请求地址
    //以下用于手机存用户信息
    private SharedPreferences mUser;
    private SharedPreferences.Editor mUserEditor;
    //取得请求队列
    Note note=new Note();
    //String code,head,content,time;
    List<Note> mList=new ArrayList<>();
    public NoteData(Activity context, String s_id, String passward){
        mContext=context;
        initNote(s_id,passward);
    }
    private void initNote(final String s_id, final String password){
        mUser=mContext.getSharedPreferences("userdata",MODE_PRIVATE);
        mUserEditor=mUser.edit();
        String url="http://39.97.114.188/Dormitory/servlet/GetNoteServlet?s_id="+s_id+"&password="+password;
        String tag= "getnote";
        //取得请求队列
        RequestQueue login = Volley.newRequestQueue(mContext);
        //防止重复请求，所以先取消tag标识的请求队列
        login.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest loginrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response);
                            for(int i=1;i<=jsonObject.length();i++){
                                JSONObject jsonObject2 = (JSONObject) new JSONObject(response).get(String.valueOf(i));
                                mUserEditor.putString("code",jsonObject2.getString("code"));
                                mUserEditor.putString("head",jsonObject2.getString("head"));
                                mUserEditor.putString("content",jsonObject2.getString("content"));
                                mUserEditor.putString("time",jsonObject2.getString("time"));
                                mUserEditor.apply();
                                note.setId(jsonObject2.getString("code"));
                                note.setTopic(jsonObject2.getString("code"));
                                note.setContent(jsonObject2.getString("content"));
                                note.setPushtime(jsonObject2.getString("time"));
                                mList.add(note);
                            }
                            Toast.makeText(mContext,"输出",Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            System.out.print(e);
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(mContext,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(mContext,"请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
        };
        //设置Tag标签
        loginrequest.setTag(tag);
        //将请求添加到队列中
        login.add(loginrequest);
    }
    public List<Note> getList(){
        return mList;
    }
    public void p(){
        System.out.println(mList.get(0).getTopic());
    }

}
