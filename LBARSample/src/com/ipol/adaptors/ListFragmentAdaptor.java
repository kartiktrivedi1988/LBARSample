package com.ipol.adaptors;

import java.util.List;



import android.support.v4.app.FragmentPagerAdapter;

import com.ipol.lbarsample.views.ListFragment;

public class ListFragmentAdaptor extends FragmentPagerAdapter {
private List< ListFragment> mFragmentList;

	public ListFragmentAdaptor(android.support.v4.app.FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public android.support.v4.app.Fragment getItem(int position) {
		return mFragmentList.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mFragmentList!=null)
			return mFragmentList.size();
		
		return 0;
	}

	public List<ListFragment> getFragmentList() {
		return mFragmentList;
	}

	public void setFragmentList(List<ListFragment> fragmentList) {
		mFragmentList = fragmentList;
	}

}
