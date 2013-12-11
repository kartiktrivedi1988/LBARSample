package com.ipol.lbarsample;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.animation.Interpolator;

import com.ipol.adaptors.ListFragmentAdaptor;
import com.ipol.adaptors.ScoreAdapter;
import com.ipol.lbarsample.items.HighScoreItem;
import com.ipol.lbarsample.views.FixedSpeedScroller;
import com.ipol.lbarsample.views.ListFragment;
import com.ipol.lbarsample.views.ListFragment.OnChaangePageListener;

public class HighScoreTableActivity extends BaseFragmentActivity implements OnChaangePageListener {
	private List<HighScoreItem> mItems;
	private ScoreAdapter mAdapter,mAdapter1,mAdapter2;
	private ListFragmentAdaptor mPageAdapter;
	private ArrayList<HighScoreItem> mItems1;
	private ArrayList<HighScoreItem> mItems2;
	private View mPager;
	private Interpolator sInterpolator;
	private static final int TOTAL_PAGE=3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_viewpager);
		
		mPageAdapter=new ListFragmentAdaptor(getSupportFragmentManager());
		ViewPager pager =(ViewPager)findViewById(R.id.pager);
		mPager=pager;
		setListAdaptor();
		mPageAdapter.setFragmentList(getFragments());
		pager.setAdapter(mPageAdapter);
//		setList();
		
	}
//	create fragements for each list item
	private List<ListFragment> getFragments(){
		  List<ListFragment> fList = new ArrayList<ListFragment>();
		  
		  ListFragment fragment= ListFragment.newInstance("Fragment 1");
		  fragment.setAdapter(mAdapter);
		  fragment.setChangeListener(this);
		   fList.add(fragment);
		  
		  ListFragment fragment1= ListFragment.newInstance("Fragment 2");
		  fragment1.setAdapter(mAdapter1);
		   fList.add(fragment1);
		   fragment1.setChangeListener(this);
		   
		   ListFragment fragment3= ListFragment.newInstance("Fragment 3");
			fragment3.setAdapter(mAdapter2);
			fList.add(fragment3);
			fragment3.setChangeListener(this);

		  return fList;
	}
//	settign adapter and creating dummy data
	
	private void setListAdaptor(){
//		ListView scoreList = (ListView)findViewById(R.id.listview);
		mItems=new ArrayList<HighScoreItem>();
		for(int i=1;i<25;i++){
			HighScoreItem item=new HighScoreItem();	
			mItems.add(item);
		}
		mAdapter=new ScoreAdapter(this);
		mAdapter.setScoreItems(mItems);
		
		mItems1=new ArrayList<HighScoreItem>();
		
		for(int i=1;i<10;i++){
			HighScoreItem item=new HighScoreItem();	
			mItems1.add(item);
		}
		
		mItems2=new ArrayList<HighScoreItem>();
		for(int i=1;i<19;i++){
			HighScoreItem item=new HighScoreItem();	
			mItems2.add(item);
		}
		
		mAdapter1=new ScoreAdapter(this);
		mAdapter1.setScoreItems(mItems1);
		
		mAdapter2=new ScoreAdapter(this);
		mAdapter2.setScoreItems(mItems2);
//		scoreList.setAdapter(mAdapter);
		
//		ListView scoreList1 = (ListView)findViewById(R.id.listview1);
//		scoreList1.setAdapter(mAdapter);
		
//		set animation and time duration for View pager transition
		try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true); 
//            FixedSpeedScroller scroller = new FixedSpeedScroller(mPager.getContext(), sInterpolator);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mPager.getContext());
//            scroller.setDuration(duration)
//            mScroller.setFixedDuration(5000);
            mScroller.set(mPager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.high_score_table, menu);
		return true;
	}
//	private void addRow(){
//		TableLayout table = (TableLayout)findViewById(R.id.tableLayout1);
//		for(int i=0;i<5;i++){
//			TableRow row=new TableRow(this);
//			 row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//		        row.setBackgroundColor(Color.BLUE);
//			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	        View tableRaw = inflater.inflate(R.layout.table_row, null);
////	        tableRaw.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//	        33343
////	        row.addView(tableRaw);
//	        DisplayMetrics displaymetrics = new DisplayMetrics();
//	    	getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
////	    	int height1 = displaymetrics.heightPixels;
//	    	int wwidth1 = displaymetrics.widthPixels;
//	       row.addView(tableRaw, wwidth1, LayoutParams.WRAP_CONTENT);
//	           
//	        table.addView(row, i);
//		}  
//	}

//	notification when the page will be created
	@Override
	public void onChangePage(boolean direction) {
		ViewPager pager =(ViewPager)findViewById(R.id.pager);
		int index=pager.getCurrentItem();
		if(direction){
			if(index==(TOTAL_PAGE-1)){
				pager.setCurrentItem(0);
			}
			else{
				pager.setCurrentItem(index+1);
			}
		}
		else{
			if(index==0){
				pager.setCurrentItem(TOTAL_PAGE-1);
				
			}
			else{
				pager.setCurrentItem(index-1);
			}
		}
	}

}
