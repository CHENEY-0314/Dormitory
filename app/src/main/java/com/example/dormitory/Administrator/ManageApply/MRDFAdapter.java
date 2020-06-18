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

public class MRDFAdapter extends BaseAdapter {
    private Context context;
    private List<RepairDorApply> ApplyList = new ArrayList<>(1);
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL= 0x0001;

    public MRDFAdapter(Context context,List<RepairDorApply> ApplyList){
        this.context=context;
        this.ApplyList= ApplyList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final RepairDorApply apply=getItem(position);
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else{
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.adapter_item_manage_repair_d,parent,false);
            holder.textBuilding=convertView.findViewById(R.id.item_manage_buildingNum);
            holder.textRoomNum=convertView.findViewById(R.id.item_manage_roomNum);
            holder.textPhoneNum=convertView.findViewById(R.id.item_manage_phoneNum);
            holder.textRepairTypes=convertView.findViewById(R.id.item_manage_repairTypes);
            holder.textMoreMessage=convertView.findViewById(R.id.item_manage_moreMessage);
            holder.btn_Refuse=convertView.findViewById(R.id.item_manage_btn_refuse);
            holder.btn_Agree=convertView.findViewById(R.id.item_manage_btn_agree);
            convertView.setTag(holder);
        }

        holder.textBuilding.setText(apply.getBuildingNum());
        holder.textRoomNum.setText(apply.getRoomNum());
        holder.textPhoneNum.setText(apply.getPhoneNum());
        holder.textRepairTypes.setText(apply.getRepairTypes());
        holder.textMoreMessage.setText(apply.getMoreMessage());

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
        public TextView textBuilding,textRoomNum,textPhoneNum,textRepairTypes,textMoreMessage;
        public Button btn_Refuse, btn_Agree;

    }
}
