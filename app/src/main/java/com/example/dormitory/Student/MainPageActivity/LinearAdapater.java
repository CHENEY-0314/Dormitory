package com.example.dormitory.Student.MainPageActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitory.R;

public class LinearAdapater extends RecyclerView.Adapter<LinearAdapater.LinearViewHolder> {
    @NonNull
    //这个Context我也不知道干嘛的，教程是这样的
    private Context mContext;
    //存放信息的二维字符串数组，用来显示在筛选结果列表
    private String[][] resultInformation;

    public LinearAdapater(Context context){
        this.mContext=context;
        //信息初始化
        resultInformation=new String[10][4];
        String pre_building=new String("C");
        String pre_dorNum=new String("");
        String pre_stuNum=new String("20183066000");
        String pre_bedNum=new String("");
        for(int i=0;i<10;i++){
            resultInformation[i][0]=pre_building+String.valueOf(i+1);
            resultInformation[i][1]=pre_dorNum+String.valueOf(143+i);
            resultInformation[i][2]=pre_stuNum+String.valueOf(i);
            resultInformation[i][3]=pre_bedNum+String.valueOf(i%4+1);
        }
    }
    @Override
    public LinearAdapater.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.select_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final LinearAdapater.LinearViewHolder holder, final int position) {
        //给item里面的TextView赋text值
        holder.tv_building.setText(resultInformation[position][0]);
        holder.tv_roomNum.setText(resultInformation[position][1]);
        holder.tv_stuNum.setText(resultInformation[position][2]);
        holder.tv_bedNum.setText(resultInformation[position][3]);
        //设置提交按钮点击事件
        holder.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"提交成功："+position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultInformation.length;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{
        //声明item中的组件
        private TextView tv_building,tv_roomNum,tv_stuNum,tv_bedNum;
        private Button btnSubmit;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            //绑定组件
            tv_building=itemView.findViewById(R.id.select_item_building);
            tv_roomNum=itemView.findViewById(R.id.select_item_roomNum);
            tv_stuNum=itemView.findViewById(R.id.select_item_stuNum);
            tv_bedNum=itemView.findViewById(R.id.select_item_bedNum);
            btnSubmit=itemView.findViewById(R.id.select_item_Submit);
        }
    }
}
