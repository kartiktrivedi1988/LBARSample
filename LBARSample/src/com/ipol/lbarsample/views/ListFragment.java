package com.ipol.lbarsample.views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.ipol.adaptors.ScoreAdapter;
import com.ipol.lbarsample.R;

public class ListFragment extends Fragment implements OnClickListener {
	 public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

	 public interface OnChaangePageListener{
			public void onChangePage(boolean direction);
		}
	 
	private OnChaangePageListener mChangeListener;
	private ScoreAdapter mAdapter;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        Log.e("Test", "hello");
	    }
	
	 
	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	 
	    }
	 
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.activity_high_score_table, container, false);
	        ListView list=(ListView) view.findViewById(R.id.listview);
	        list.setAdapter(mAdapter);
	        
	        Button nextButton= (Button)view.findViewById(R.id.nextBtn);
	        nextButton.setOnClickListener(this);
	        
	        Button previousButton = (Button)view.findViewById(R.id.previeousBtn);
	        previousButton.setOnClickListener(this);
	        
	        return view;
	    }

	    public static final ListFragment newInstance(String message)
	    {
	    	ListFragment f = new ListFragment();
	      Bundle bdl = new Bundle(1);
	      bdl.putString(EXTRA_MESSAGE, message);
	      f.setArguments(bdl);
	      return f;
	    }
		public ScoreAdapter getAdapter() {
			return mAdapter;
		}


		public void setAdapter(ScoreAdapter adapter) {
			mAdapter = adapter;
		}


		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.previeousBtn:
				mChangeListener.onChangePage(false);
				break;
			case R.id.nextBtn:
				mChangeListener.onChangePage(true);
				break;
			}
			
		}


		public OnChaangePageListener getChangeListener() {
			return mChangeListener;
		}


		public void setChangeListener(OnChaangePageListener changeListener) {
			mChangeListener = changeListener;
		}

}
