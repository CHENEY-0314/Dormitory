package com.example.dormitory.Student.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dormitory.R;
import com.example.dormitory.Student.MyPagesActivity.Trace;

import java.util.ArrayList;
import java.util.List;

//换宿舍申请的时间线所使用的适配器
public class TimeLineAdapter extends BaseAdapter {
    private Context context;
    private List<Trace> traceList = new ArrayList<>(1);
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL= 0x0001;

    public TimeLineAdapter(Context context, List<Trace> traceList) {
        this.context = context;
        this.traceList = traceList;
    }

    @Override
    public int getCount() {
        return traceList.size();
    }

    @Override
    public Trace getItem(int position) {
        return traceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Trace trace = getItem(position);
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adpter_item_myfixd_timeline, parent, false);
            holder.tvAcceptTime = (TextView) convertView.findViewById(R.id.MyCDF_time);
            holder.tvAcceptStation = (TextView) convertView.findViewById(R.id.MyCDF_status);
            holder.tvTopLine = (ImageView) convertView.findViewById(R.id.MyCDF_topline);
            holder.tvDot = (ImageView) convertView.findViewById(R.id.MyCDF_iv_status);
            convertView.setTag(holder);
        }

        //设置时间线样式
        if (getItemViewType(position) == TYPE_TOP) {
            // 第一行头的竖线不显示
            holder.tvTopLine.setVisibility(View.INVISIBLE);
            // 字体颜色加深
            holder.tvAcceptTime.setTextColor(0xff000000);
            holder.tvAcceptStation.setTextColor(0xff000000);
            holder.tvDot.setImageResource(R.drawable.shape_circle_logistics_green);
        } else if (getItemViewType(position) == TYPE_NORMAL) {
            holder.tvTopLine.setVisibility(View.VISIBLE);
            holder.tvAcceptTime.setTextColor(0xff999999);
            holder.tvAcceptStation.setTextColor(0xff999999);
            holder.tvDot.setImageResource(R.drawable.shape_circle_logistics_gray);
        }

        //设置文字
        holder.tvAcceptTime.setText(trace.getAcceptTime());
        holder.tvAcceptStation.setText(trace.getAcceptStation());
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
        public TextView tvAcceptTime, tvAcceptStation;
        public ImageView tvTopLine, tvDot;
    }
}
