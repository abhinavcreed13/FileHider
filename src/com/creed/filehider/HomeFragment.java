package com.creed.filehider;


import java.util.ArrayList;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class HomeFragment extends Fragment {
	public GridView gridview;
	public GridViewAdapter gridviewAdapter;
	SQLcontroller controller;
	public static ArrayList<Item> data = new ArrayList<Item>();
	public HomeFragment(){}
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        controller = new SQLcontroller(activity);
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        gridview = (GridView)rootView.findViewById(R.id.gridview_home);
        init_data();
	        gridviewAdapter = new GridViewAdapter(getActivity(), R.layout.row_grid,data);
	        gridview.setAdapter(gridviewAdapter);
	        gridview.setOnItemClickListener(null);
        return rootView;
    }

	private void init_data() {
		data.clear();
		String val1=controller.getcount("Pictures");
		String val2=controller.getcount("Videos");
		String val3=controller.getcount("Music");
		String val4=controller.getcount("Documents");
		String val5=controller.getcount("Others");
		data.add(new Item("Pictures: "+val1,getResources().getDrawable(R.drawable.photofile)));
		data.add(new Item("Videos: "+val2,getResources().getDrawable(R.drawable.videofile)));
		data.add(new Item("Music: "+val3,getResources().getDrawable(R.drawable.audiofile)));
		data.add(new Item("Documents: "+val4,getResources().getDrawable(R.drawable.word7)));
		data.add(new Item("Others: "+val5,getResources().getDrawable(R.drawable.unknown)));
		
		
	}
}
