package com.example.dormitory.Administrator.ManageApply;

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

public class MCDFAdapter extends BaseAdapter {
    private Context context;
    private List<ChangeDorApply> ApplyList=new ArrayList<>(10);
    public static final int TYPE_TOP=0x0000;
    public static final int TYPE_NORMAL=0x0001;

    public MCDFAdapter(Context context,List<ChangeDorApply> ApplyList){
        this.context=context;
        this.ApplyList=ApplyList;
    }
    @Override
    public int getCount() {
        return ApplyList.size();
    }

    @Override
    public ChangeDorApply getItem(int position) {
        return ApplyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final ChangeDorApply apply=getItem(position);
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else{
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.adapter_item_manage_change_d,parent,false);
            holder.tv_name_1=convertView.findViewById(R.id.item_manage_name_1);
            holder.tv_sex_1=convertView.findViewById(R.id.item_manage_sex_1);
            holder.tv_building_1=convertView.findViewById(R.id.item_manage_buildingNum_1);
            holder.tv_roonNum_1=convertView.findViewById(R.id.item_manage_roomNum_1);
            holder.tv_bedNum_1=convertView.findViewById(R.id.item_manage_bedNum_1);
            holder.tv_stuNum_1=convertView.findViewById(R.id.item_manage_stuNum_1);

            holder.tv_name_2=convertView.findViewById(R.id.item_manage_name_2);
            holder.tv_sex_2=convertView.findViewById(R.id.item_manage_sex_2);
            holder.tv_building_2=convertView.findViewById(R.id.item_manage_buildingNum_2);
            holder.tv_roonNum_2=convertView.findViewById(R.id.item_manage_roomNum_2);
            holder.tv_bedNum_2=convertView.findViewById(R.id.item_manage_bedNum_2);
            holder.tv_stuNum_2=convertView.findViewById(R.id.item_manage_stuNum_2);

            holder.btn_Refuse=convertView.findViewById(R.id.item_manage_btn_refuse);
            holder.btn_Agree=convertView.findViewById(R.id.item_manage_btn_agree);

            convertView.setTag(holder);
        }
        holder.tv_name_1.setText(apply.getName1());
        holder.tv_sex_1.setText(apply.getSex1());
        holder.tv_building_1.setText(apply.getBuildingNum1());
        holder.tv_roonNum_1.setText(apply.getRoomNum1());
        holder.tv_bedNum_1.setText(apply.getBedNum1());
        holder.tv_stuNum_1.setText(apply.getStuNum1());

        holder.tv_name_2.setText(apply.getName2());
        holder.tv_sex_2.setText(apply.getSex2());
        holder.tv_building_2.setText(apply.getBuildingNum2());
        holder.tv_roonNum_2.setText(apply.getRoomNum2());
        holder.tv_bedNum_2.setText(apply.getBedNum2());
        holder.tv_stuNum_2.setText(apply.getStuNum2());


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
        public TextView tv_name_1,tv_sex_1,tv_building_1,tv_roonNum_1,tv_bedNum_1,tv_stuNum_1;
        public TextView tv_name_2,tv_sex_2,tv_building_2,tv_roonNum_2,tv_bedNum_2,tv_stuNum_2;
        public Button btn_Refuse, btn_Agree;

    }
}
