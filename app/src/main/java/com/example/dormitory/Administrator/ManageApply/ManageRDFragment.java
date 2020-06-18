package com.example.dormitory.Administrator.ManageApply;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.dormitory.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageRDFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageRDFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManageRDFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageRDFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageRDFragment newInstance(String param1, String param2) {
        ManageRDFragment fragment = new ManageRDFragment();
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

    //以上均使不需要理会

    //声明控件等
    private ListView lvTrace;
    private List<RepairDorApply> applyList = new ArrayList<>(10);
    private MRDFAdapter adapter;
    private LinearLayout Noapply;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_r_d, container, false);

        lvTrace = (ListView) view.findViewById(R.id.MRDF_Listview);  //有申请时显示
        Noapply=view.findViewById(R.id.MRDF_NoApply);   //无申请时显示

        //判断当前是否有申请
        //-----------------------------若有则进行以下操作---------------------------------------------

        //向list中添加两条数据，包括楼号、房间号、联系方式、报修类型、备注
        applyList.add(new RepairDorApply("C10","142","18088789989","电器 桌椅 中央空调 建筑 水设施 其他","风扇不转，天花板脱落，洗手台漏水"));
        applyList.add(new RepairDorApply("C12","520","13923336666","桌椅等家具",
                "这里会有好多好多好多的废话，我也不知道写啥，反正会有很多字就对了，如果字体太多会怎样呢，我也不知道，不如就让他显示两行然后省略吧！"));

        adapter = new MRDFAdapter(view.getContext(), applyList);
        lvTrace.setAdapter(adapter);


        //--------------------------------若无申请，进行以下操作--------------------------------------
//        lvTrace.setVisibility(View.GONE);
//        Noapply.setVisibility(View.VISIBLE);

        return view;
    }

}
