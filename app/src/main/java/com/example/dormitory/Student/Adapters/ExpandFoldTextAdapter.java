package com.example.dormitory.Student.Adapters;

import android.app.Activity;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dormitory.R;
import com.example.dormitory.Student.NotePageActivity.Note;
import com.example.dormitory.Student.NotePageActivity.TimeDifCalculater;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ExpandFoldTextAdapter extends RecyclerView.Adapter<ExpandFoldTextAdapter.MyViewHolder> {
    private Activity mContent;

    public static final int TYPE_NOTEMPTY = 0;
    public static final int TYPE_EMPTY = 1;

    private final int MAX_LINE_COUNT = 3;//最大显示行数

    private final int STATE_UNKNOW = -1;//未知状态

    private final int STATE_NOT_OVERFLOW = 1;//文本行数小于最大可显示行数

    private final int STATE_COLLAPSED = 2;//折叠状态

    private final int STATE_EXPANDED = 3;//展开状态

    private SparseArray<Integer> mTextStateList;//保存文本状态集合

    TimeDifCalculater timeDifCalculater;

    private  boolean readstate=false;
    List<Note> mList;
    public ExpandFoldTextAdapter(List<Note> list, Activity context) {
        mContent = context;
        this.mList = list;
        mTextStateList = new SparseArray<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //if(viewType==TYPE_NOTEMPTY)
        return new MyViewHolder(mContent.getLayoutInflater().inflate(R.layout.item_note, parent, false));
        //else if(viewType==TYPE_EMPTY)
           // return new EmptyViewHolder(mContent.getLayoutInflater().inflate(R.layout.empty_note_page, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        int state = mTextStateList.get(Integer.parseInt(mList.get(position).getId().substring(0,1)), STATE_UNKNOW);
        //第一次初始化，未知状态
        if (state == STATE_UNKNOW) {
            holder.content.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //这个回掉会调用多次，获取完行数后记得注销监听
                    holder.content.getViewTreeObserver().removeOnPreDrawListener(this);
                    //holder.content.getViewTreeObserver().addOnPreDrawListener(null);
                    //如果内容显示的行数大于最大显示行数
                    if (holder.content.getLineCount() > MAX_LINE_COUNT) {
                        holder.content.setMaxLines(MAX_LINE_COUNT);//设置最大显示行数
                        holder.expandOrFold.setVisibility(View.VISIBLE);//显示全文
                        holder.expandOrFold.setText("点击展开全文");
                        mTextStateList.put(Integer.parseInt(mList.get(position).getId()), STATE_COLLAPSED);//保存状态
                    } else {
                        holder.expandOrFold.setVisibility(View.GONE);
                        mTextStateList.put(Integer.parseInt(mList.get(position).getId()), STATE_NOT_OVERFLOW);
                    }
                    return true;
                }
            });

            holder.content.setMaxLines(Integer.MAX_VALUE);//设置文本的最大行数，为整数的最大数值
            if(mList.get(position).getTopic()!=null)
                holder.content.setText(mList.get(position).getTopic()+"\n"+mList.get(position).getContent());
            else
                holder.content.setText(mList.get(position).getContent());
        } else {
            //如果之前已经初始化过了，则使用保存的状态。
            switch (state) {
                case STATE_NOT_OVERFLOW:
                    holder.expandOrFold.setVisibility(View.GONE);
                    break;
                case STATE_COLLAPSED:
                    holder.content.setMaxLines(MAX_LINE_COUNT);
                    holder.expandOrFold.setVisibility(View.VISIBLE);
                    holder.expandOrFold.setText("点击展开全文");
                    break;
                case STATE_EXPANDED:
                    holder.content.setMaxLines(Integer.MAX_VALUE);
                    holder.expandOrFold.setVisibility(View.VISIBLE);
                    holder.expandOrFold.setText("收起");
                    break;
            }
            holder.content.setText(mList.get(position).getContent());
        }

        //全文和收起的点击事件
        holder.expandOrFold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = mTextStateList.get(Integer.parseInt(mList.get(position).getId()), STATE_UNKNOW);
                if (state == STATE_COLLAPSED) {
                    holder.content.setMaxLines(Integer.MAX_VALUE);
                    holder.expandOrFold.setText("收起");
                    mTextStateList.put(Integer.parseInt(mList.get(position).getId()), STATE_EXPANDED);
                } else if (state == STATE_EXPANDED) {
                    holder.content.setMaxLines(MAX_LINE_COUNT);
                    holder.expandOrFold.setText("点击展开全文");
                    mTextStateList.put(Integer.parseInt(mList.get(position).getId()), STATE_COLLAPSED);
                }
            }
        });
        if(mList.get(position).getId().substring(0,1).equals("0")){
            holder.delete.setEnabled(false);
            holder.delete.setVisibility(View.INVISIBLE);
        }else{
            holder.delete.setEnabled(true);
            holder.delete.setVisibility(View.VISIBLE);
        }
        //删除点击事件
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteNote("201830660178","123456",mList.get(position).getId());
                mList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.image.setImageResource(mList.get(position).getImage());
        holder.typename.setText(mList.get(position).getType());
        holder.read.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               if(!readstate)
                                               {holder.read.setImageResource(R.drawable.read_note_icon);readstate=!readstate;}
                                               else
                                               {holder.read.setImageResource(R.drawable.unread_note_icon);readstate=!readstate;}
                                           }
                                       });
        timeDifCalculater=new TimeDifCalculater(mList.get(position).getPushtime());
        holder.time.setText(timeDifCalculater.getTimeDif());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    final class EmptyViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public ImageView image;
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    final class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView typename;
        public TextView content;
        public TextView delete;
        public TextView expandOrFold;
        public TextView time;
        public ImageView image;
        public ImageButton read;
        public RefreshLayout mRefreshlayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            typename = (TextView) itemView.findViewById(R.id.tv_typename);
            content = (TextView) itemView.findViewById(R.id.tv_content);
            delete = (TextView) itemView.findViewById(R.id.tv_delete);
            expandOrFold = (TextView) itemView.findViewById(R.id.tv_expand_or_fold);
            time=(TextView)itemView.findViewById(R.id.time_text);
            image=(ImageView)itemView.findViewById(R.id.note_type_image);
            read=(ImageButton) itemView.findViewById(R.id.read_note_btn);
        }
    }
    public void addItem(Note note){
        mList.add(note);
        notifyDataSetChanged();
    }
    void DeleteNote(final String s_id, final String password,String code){

            String url="http://39.97.114.188/Dormitory/servlet/DeleteNoteServlet?s_id="+s_id+"&password="+password+"&code="+code;
            String tag= "getnote";
            //取得请求队列
            RequestQueue getnote = Volley.newRequestQueue(mContent);
            //防止重复请求，所以先取消tag标识的请求队列
            getnote.cancelAll(tag);
            //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
            final StringRequest getnoterequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                String Result=jsonObject.getString("result").toString();
                                if(Result=="success")
                                    Toast.makeText(mContent,"删除成功！",Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                                Toast.makeText(mContent,"删除失败！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                    Toast.makeText(mContent,"请稍后重试！",Toast.LENGTH_SHORT).show();
                }
            }) {
            };
            //设置Tag标签
            getnoterequest.setTag(tag);
            //将请求添加到队列中
            getnote.add(getnoterequest);


    }
}

