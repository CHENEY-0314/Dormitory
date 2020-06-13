package com.example.dormitory.Student.MyPagesActivity.MyChangeDormitory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.dormitory.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecelveCDFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecelveCDFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecelveCDFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecelveCDFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecelveCDFragment newInstance(String param1, String param2) {
        RecelveCDFragment fragment = new RecelveCDFragment();
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

    private ListView lvTrace;
    private List<RecelveApply> applyList = new ArrayList<>(10);
    private RCDFAdapter adapter;
    private LinearLayout Noapply;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recelve_c_d, container, false);

        lvTrace = (ListView) view.findViewById(R.id.RCDF_Listview);  //有申请时显示
        Noapply=view.findViewById(R.id.RCDF_NoApply);   //无申请时显示

        //判断当前是否有申请
        //-----------------------------若有则进行以下操作---------------------------------------------

        applyList.add(new RecelveApply("2020-06-11","张三","188172638443"));
        applyList.add(new RecelveApply("2020-06-15","里斯特","17273628372"));

        adapter = new RCDFAdapter(view.getContext(), applyList);
        lvTrace.setAdapter(adapter);


        //--------------------------------若无申请，进行以下操作--------------------------------------
        //lvTrace.setVisibility(View.GONE);
        //Noapply.setVisibility(View.VISIBLE);

        return view;
    }
}
