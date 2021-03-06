package com.jaaziel.work4kits;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class 	ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context mContex;
	private List<String> mListDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> mListDataChild;
	private LayoutInflater mInflater;
	private List<Integer> intList;


	public ExpandableListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<String>> listChildData) {
		mContex = context;
		mListDataHeader = listDataHeader;
		mListDataChild = listChildData;
		mInflater = LayoutInflater.from(context);
		intList = Arrays.asList(0,0,0,0,0,0,0);
	}

	@Override
	public int getGroupCount() {
		return mListDataHeader.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mListDataHeader.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mListDataChild.get(this.mListDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.expandablelist_group, null);
		}
		TextView headerLabel = (TextView) convertView
				.findViewById(R.id.tv_headder);
		headerLabel.setTypeface(null, Typeface.BOLD);
		headerLabel.setText(headerTitle);

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.view, null);
		}
		CustomGridView gridView = (CustomGridView) convertView
				.findViewById(R.id.GridView_toolbar);

		gridView.setNumColumns(2);// gridView.setGravity(Gravity.CENTER);//
		gridView.setHorizontalSpacing(10);// SimpleAdapter adapter =


        GridAdapter adapter;

        if (IOSingleton.Instance().getDays().contains(getGroup(groupPosition))) {
            adapter = new GridAdapter(mContex, IOSingleton.Instance().getJobs((String) getGroup(groupPosition)));
        } else {
            adapter = new GridAdapter(mContex, new String[]{"   "});
        }

        gridView.setAdapter(adapter);// Adapter

        int totalHeight = 0;
        RelativeLayout relativeLayout = (RelativeLayout) adapter.getView(
					0, null, gridView);

        for (int i = 0; i < relativeLayout.getChildCount(); i++ ){
            TextView textView = (TextView) relativeLayout.getChildAt(i);
            textView.measure(0, 0);
            totalHeight += textView.getMeasuredHeight();
        }
		gridView.SetHeight(totalHeight * (gridView.getCount()/2) + 20);
        return convertView;
	}

	private void setChild(int groupPosition, int count) {
		intList.set(groupPosition, count);
	}

	public int getCount(int groupPosition) {
		return intList.get(groupPosition);
	}


	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}