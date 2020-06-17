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

public class ManageCDFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManageCDFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageCDFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageCDFragment newInstance(String param1, String param2) {
        ManageCDFragment fragment = new ManageCDFragment();
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

    //声明控件
    private ListView lvTrace;
    private List<ChangeDorApply> applyList = new ArrayList<>(10);
    private MCDFAdapter adapter;
    private LinearLayout Noapply;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_manage_r_d, container, false);

        lvTrace = (ListView) view.findViewById(R.id.MRDF_Listview);  //有申请时显示
        Noapply=view.findViewById(R.id.MRDF_NoApply);   //无申请时显示

        //判断当前是否有申请
        //-----------------------------若有则进行以下操作---------------------------------------------

        //创建四个人的信息，两两一对进行换宿舍，内容包括姓名，性别，楼栋，房间号，床位号，学号
        String[] mess1_person1=new String[]{"张三","男","C10","234","4号","201812345678"};
        String[] mess1_person2=new String[]{"李四","男","C12","567","1号","201911112222"};
        String[] mess2_person1=new String[]{"王五","女","C5","666","2号","201933334444"};
        String[] mess2_person2=new String[]{"晓晓","女","C5","520","3号","201920131415"};
        //向list中添加两条换宿申请信息
        applyList.add(new ChangeDorApply(mess1_person1,mess1_person2));
        applyList.add(new ChangeDorApply(mess2_person1,mess2_person2));
        adapter = new MCDFAdapter(view.getContext(), applyList);
        lvTrace.setAdapter(adapter);


        //--------------------------------若无申请，进行以下操作--------------------------------------
//        lvTrace.setVisibility(View.GONE);
//        Noapply.setVisibility(View.VISIBLE);

        return view;
    }
}
