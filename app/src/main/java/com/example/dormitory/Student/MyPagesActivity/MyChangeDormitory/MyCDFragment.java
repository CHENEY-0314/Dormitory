package com.example.dormitory.Student.MyPagesActivity.MyChangeDormitory;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dormitory.R;
import com.example.dormitory.Student.Adapters.TimeLineAdapter;
import com.example.dormitory.Student.MyPagesActivity.Trace;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyCDFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCDFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public MyCDFragment() { }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyCDFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCDFragment newInstance(String param1, String param2) {
        MyCDFragment fragment = new MyCDFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    //以上均使不需要理会,操作代码在下面写

    //声明控件
    private ListView lvTrace;
    private List<Trace> traceList = new ArrayList<>(10);
    private TimeLineAdapter adapter;
    private LinearLayout WithApply,NoApply;
    private TextView txt_ChangeApply,txt_DeleteApply,txt_Dormitory,txt_state;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my_c_d, container, false);

        txt_ChangeApply=view.findViewById(R.id.MyCDF_changeApply);  //点击修改申请
        txt_ChangeApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改换宿申请页面
                Intent intent = new Intent(getActivity(), ChangeChangeApplyActivity.class);
                MyCDFragment.this.startActivity(intent);
            }
        });
        txt_DeleteApply=view.findViewById(R.id.MyCDF_deleteApply);  //点击撤销申请
        txt_DeleteApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        txt_Dormitory=view.findViewById(R.id.MyCDF_Dormitory);  //目标宿舍如 "目标宿舍： C10-145"
        txt_state=view.findViewById(R.id.MyCDF_state);  // 未完成时显示 “处理中”  完成后显示 “已完成”


        WithApply=view.findViewById(R.id.MyCDF_HaveApply);  //有申请时显示
        NoApply=view.findViewById(R.id.MyCDF_NoApply);   //无申请时显示


        //判断当前是否有申请-------------若有则进行以下操作---------------------------------------------

        lvTrace = (ListView)view.findViewById(R.id.MyCDF_lvTrace);

        // 向List中添加数据，数据包括时间以及状态，如下所示
        traceList.add(new Trace("2020-06-10 14:13:00", "换宿申请完成!"));
        traceList.add(new Trace("2020-05-27 13:01:04", "管理员审核成功！请确认换宿。"));
        traceList.add(new Trace("2020-05-25 12:19:47", "等待管理员审核。"));
        traceList.add(new Trace("2020-05-20 11:12:44", "您的申请已提交，等待意向宿舍确认。"));

        //设置适配器
        adapter = new TimeLineAdapter(view.getContext(), traceList);
        lvTrace.setAdapter(adapter);



        //--------------------------------若无申请，进行以下操作--------------------------------------
        //WithApply.setVisibility(View.GONE);
        //NoApply.setVisibility(View.VISIBLE);


        return view;
    }

}
