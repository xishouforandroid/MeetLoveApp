package com.lbins.meetlove.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.AnimateFirstDisplayListener;
import com.lbins.meetlove.adapter.GridClassViewAdapter;
import com.lbins.meetlove.base.BaseFragment;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.data.CityData;
import com.lbins.meetlove.module.City;
import com.lbins.meetlove.module.Province;
import com.lbins.meetlove.ui.SelectAreaActivity;
import com.lbins.meetlove.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProClassFragment extends BaseFragment {

	private ArrayList<City> list = new ArrayList<City>();
	private GridView gridView;
	private GridClassViewAdapter adapter;
	private Province province;
	private TextView toptype;

	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pro_type, null);
		gridView = (GridView) view.findViewById(R.id.listView);
		int index = getArguments().getInt("index");
		province = SelectAreaActivity.list.get(index);
		toptype = (TextView) view.findViewById(R.id.toptype);
		if(province != null){
			toptype.setText(province.getPname());
//			imageLoader.displayImage(goodsType.getLx_class_cover(), icon, MeirenmeibaAppApplication.txOptions, animateFirstListener);
		}
		GetTypeList();
		adapter = new GridClassViewAdapter(getActivity(), list);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if(list !=null){
					if(list.size() > i){
						City city = list.get(i);
						if(city != null){
							Intent intent = new Intent();
							intent.putExtra("cityObj", city);
							intent.putExtra("provinceObj", province);
							getActivity().setResult(10001, intent);
							getActivity().finish();
						}
					}
				}
			}
		});

		return view;
	}

	private void GetTypeList() {
			StringRequest request = new StringRequest(
					Request.Method.POST,
					InternetURL.appCitys,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String s) {
							if (StringUtil.isJson(s)) {
								try {
									JSONObject jo = new JSONObject(s);
									String code = jo.getString("code");
									if (Integer.parseInt(code) == 200) {
										list.clear();
										Gson gson = getGson();
										if(gson != null){
											CityData data = gson.fromJson(s, CityData.class);
											list.addAll(data.getData());
											adapter.notifyDataSetChanged();
										}
									}else {
										Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}


							}
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError volleyError) {
							Toast.makeText(getActivity(), getResources().getString(R.string.get_data_error), Toast.LENGTH_SHORT).show();
						}
					}
			) {
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> params = new HashMap<String, String>();
					params.put("provinceid", province.getProvinceid());
					return params;
				}

				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					Map<String, String> params = new HashMap<String, String>();
					params.put("Content-Type", "application/x-www-form-urlencoded");
					return params;
				}
			};
			getRequestQueue().add(request);
	}
}
