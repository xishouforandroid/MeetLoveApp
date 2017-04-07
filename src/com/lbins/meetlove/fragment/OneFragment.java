package com.lbins.meetlove.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.TuijianGroupdapter;
import com.lbins.meetlove.adapter.TuijianPeopledapter;
import com.lbins.meetlove.base.BaseFragment;
import com.lbins.meetlove.ui.ProfileEmpActivity;
import com.lbins.meetlove.ui.TuijianGroupActivity;
import com.lbins.meetlove.ui.TuijianPeopleActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2016/7/1.
 * 推荐
 */
public class OneFragment extends BaseFragment implements View.OnClickListener  {

    private View view;
    private Resources res;

    private GridView gridView1;
    private GridView gridView2;
    private TuijianPeopledapter adapter1;
    private TuijianGroupdapter adapter2;
    private List<String> list1 = new ArrayList<String>();
    private List<String> list2 = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.one_fragment, null);
        res = getActivity().getResources();
        list1.add("");
        list1.add("");
        list2.add("");
        list2.add("");
        initView();
        return view;
    }

    void initView(){
        gridView1 = (GridView) view.findViewById(R.id.gridView1);
        gridView2 = (GridView) view.findViewById(R.id.gridView2);

        adapter1 = new TuijianPeopledapter(list1, getActivity());
        adapter2 = new TuijianGroupdapter(list2, getActivity());
        gridView1.setAdapter(adapter1);
        gridView2.setAdapter(adapter2);
        gridView1.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView2.setSelector(new ColorDrawable(Color.TRANSPARENT));

        view.findViewById(R.id.btn_one).setOnClickListener(this);
        view.findViewById(R.id.btn_two).setOnClickListener(this);

        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =  new Intent(getActivity(), ProfileEmpActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_one:
            {
                //更多推荐人
                Intent intent = new Intent(getActivity(), TuijianPeopleActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_two:
            {
                //更多推荐群
                Intent intent = new Intent(getActivity(), TuijianGroupActivity.class);
                startActivity(intent);
            }
                break;
        }
    }

}
