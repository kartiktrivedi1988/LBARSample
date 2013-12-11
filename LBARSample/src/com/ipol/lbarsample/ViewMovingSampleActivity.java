package com.ipol.lbarsample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.ipol.lbarsample.views.CustomView;

public class ViewMovingSampleActivity extends Activity implements OnClickListener {

	View view;
	float mStartX=0,mStartY=0;
	private Animation rotateAnim;
	@SuppressWarnings("unused")
	private static final String TAG = "com.ipol.lbarsample.ViewMovingSampleActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_moving_sample);
		 view = (View)findViewById(R.id.textView1);
		 Button pluButton = (Button)findViewById(R.id.button1);
		 pluButton.setOnClickListener(this);
		 
		 Button minusButton = (Button)findViewById(R.id.button2);
		 minusButton.setOnClickListener(this);
		 
		 Button plusYbutton = (Button)findViewById(R.id.button3);
		 plusYbutton.setOnClickListener(this);
		 
		  Button minusYbutton = (Button)findViewById(R.id.button4);
		  minusYbutton.setOnClickListener(this);
		  
		  TextView statusText = (TextView)findViewById(R.id.textView2);
		  statusText.setText("X:"+mStartX + " Y:"+mStartY);
		 
//		  rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotation);
////		 LayoutAnimationController animController = new LayoutAnimationController(rotateAnim, 0);
//		 view.setAnimation(rotateAnim);
//		 view.setRO
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_moving_sample, menu);
		return true;
	}

	private void startAnimation(View view,float start, float end){
		Log.i(TAG, "::startAnimation:" + "start:"+start + "end:"+end);
		Animation an = new RotateAnimation(start, end, view.getWidth()/2, view.getHeight()/2);
		

	    // Set the animation's parameters
	    an.setDuration(1000);               // duration in ms
	    an.setRepeatCount(0);                // -1 = infinite repeated
	    an.setFillAfter(true);   
	    an.setRepeatMode(Animation.REVERSE); // reverses each repeat
	    view.setAnimation(an);
	    view.startAnimation(an);
	}
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		Log.i(TAG, "::onClick:" + "");
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.button1:
//				CustomView.rotation = CustomView.rotation+ 18;
//				startAnimation(view, mStart, mEnd);
//				mStart=mEnd;
//				mEnd=mStart+30;
			mStartX+=10;
			if(mStartX>360){
				mStartX=0;
			}
			view.setRotationX(mStartX);
			
				
//				view.invalidate();
			break;
		case R.id.button2 :
//			CustomView.rotation-=18;
			mStartX-=10;
			if(mStartX<0){
				mStartX=360;
			}
			view.setRotationX(mStartX);
			break;
		case R.id.button3:
			mStartY+=10;
			if(mStartY>360){
				mStartY=0;
			}
			view.setRotationY(mStartY);
			
			break;
		case R.id.button4:
			mStartY-=10;
			if(mStartY<0){
				mStartY=360;
			}
			view.setRotationY(mStartY);
			
			break;
		}
		 TextView statusText = (TextView)findViewById(R.id.textView2);
		  statusText.setText("X:"+mStartX + " Y:"+mStartY);
//		view.setAnimation(rotateAnim);
//		view.setRotation(CustomView.rotation);
	}

}
