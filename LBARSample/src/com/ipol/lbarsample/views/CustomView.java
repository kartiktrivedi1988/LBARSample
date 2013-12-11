package com.ipol.lbarsample.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CustomView extends View {

	public static int rotation=45;
	@SuppressWarnings("unused")
	private static final String TAG = "com.ipol.lbarsample.views.CustomView";

	public CustomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		// TODO Auto-generated constructor stub
	}
	public CustomView(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Log.i(TAG, "::onDraw:" + "rotation:"+rotation);
		super.onDraw(canvas);
		canvas.save();
		canvas.rotate(rotation, getWidth() / 2, getHeight() / 2);
		super.onDraw(canvas);
		canvas.restore();
	}

}
