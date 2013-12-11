package com.ipol.lbarsample;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.core.Mat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.WindowManager;

public class OpenCVActivity extends Activity implements CvCameraViewListener2 {

	@SuppressWarnings("unused")
	private static final String TAG = "com.ipol.lbarsample.OpenCVActivity";
	private CameraBridgeViewBase mOpenCvCameraView;

	  private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
	        @Override
	        public void onManagerConnected(int status) {
	            switch (status) {
	                case LoaderCallbackInterface.SUCCESS:
	                {
	                    Log.i(TAG, "OpenCV loaded successfully");
	                    mOpenCvCameraView.enableView();
	                } break;
	                default:
	                {
	                    super.onManagerConnected(status);
	                } break;
	            }
	        }
	    };

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_open_cv);

		if (!OpenCVLoader.initDebug()) {
			// Handle initialization error
			Log.i(TAG, "::onCreate:" + "intialise error :"
					+ "Handle initialization error");
		}

		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);

		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

		mOpenCvCameraView.setCvCameraViewListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.open_cv, menu);
		return true;
	}

	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub

	}

	// @Override
	// public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
	// // TODO Auto-generated method stub
	// Log.i(TAG, "::onCameraFrame:" + "");
	// return inputFrame.rgba();
	// }

	// @Override
	// public Mat onCameraFrame(Mat inputFrame) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	@Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }


	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		// TODO Auto-generated method stub
		return inputFrame.rgba();
	}

}
