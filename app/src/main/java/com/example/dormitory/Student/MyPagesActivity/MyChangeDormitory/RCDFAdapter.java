package com.example.dormitory.Student.MyPagesActivity.MyChangeDormitory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.dormitory.R;

import java.util.ArrayList;
import java.util.List;

public class RCDFAdapter extends BaseAdapter {
    private Context context;
    private List<RecelveApply> ApplyList = new ArrayList<>(1);
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL= 0x0001;

    public RCDFAdapter(Context context, List<RecelveApply> ApplyList) {
        this.context = context;
        this.ApplyList = ApplyList;
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
        ViewHolder holder;
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
            convertView.setTag(holder);
        }

        holder.TextName.setText(apply.getapplyer());
        holder.TextNumber.setText(apply.getnumber());
        holder.TextTime.setText(apply.getapplyTime());

        holder.btn_Refuse.setOnClickListener(new View.OnClickListener() {  //点击拒绝
            @Override
            public void onClick(View v) {

            }
        });

        holder.btn_Agree.setOnClickListener(new View.OnClickListener() {   //点击同意
            @Override
            public void onClick(View v) {

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

    }
}
