package com.ipol.adaptors;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ipol.lbarsample.R;
import com.ipol.lbarsample.items.HighScoreItem;

public class ScoreAdapter extends BaseAdapter {

	private List<HighScoreItem> mScoreItems;
	private Context context;
	@Override
	public int getCount() {
		if(mScoreItems!=null){
			return mScoreItems.size();
		}
		return 0;
	}
	public ScoreAdapter(Context context){
		this.context=context;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			LayoutInflater inflater =(LayoutInflater) context
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        convertView= inflater.inflate(R.layout.table_row, null);
//	        convertView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	      TextView textView1=  (TextView) convertView.findViewById(R.id.textView1);
	      textView1.setText("1");
	      
	      TextView textView2=  (TextView) convertView.findViewById(R.id.textView2);
	      textView2.setText("Felix");
	      
	      TextView textView3=  (TextView) convertView.findViewById(R.id.textView3);
	      textView3.setText("60");
	      
	      TextView textView4=  (TextView) convertView.findViewById(R.id.textView4);
	      textView4.setText("10");
	      
	      TextView textView5=  (TextView) convertView.findViewById(R.id.textView5);
	      textView5.setText("15.476");

	      
	      
	      
	      
		}
		return convertView;
	}

	public List<HighScoreItem> getScoreItems() {
		return mScoreItems;
	}

	public void setScoreItems(List<HighScoreItem> scoreItems) {
		mScoreItems = scoreItems;
	}

	

}
