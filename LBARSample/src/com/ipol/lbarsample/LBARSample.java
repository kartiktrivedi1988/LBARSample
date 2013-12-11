package com.ipol.lbarsample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ipol.lbarsample.items.POILocation;
import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.SensorsComponentAndroid;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.IRadar;
import com.metaio.sdk.jni.ImageStruct;
import com.metaio.sdk.jni.LLACoordinate;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.Vector2d;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;


@SuppressLint("NewApi")
public class LBARSample extends ARViewActivity implements SensorsComponentAndroid.Callback ,  CvCameraViewListener2 , android.view.View.OnClickListener {

	
	@SuppressWarnings("unused")
	private static final String TAG = "com.ipol.lbarsample.LBARSample";
	
	private IRadar mRadar;
	private IGeometry mGeomatryHotel;
	private IGeometry mGeomatryRect;
//	private IGeometry mGeomatryBistroCafe;
	private IGeometry mGeomatryVFRHeilbronn;
	private POILocation mLocationItems[];
	private Vector<RotatedRect> pointsList=new Vector<RotatedRect>();
	private float factorToMinus=0;
	private float factorToMinusY;
	
	IpolSDKCallBack mCallbackHandler;
	private Handler handler;
	private Mat mCameraImage;
	
	private BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
				Log.i(TAG, "Open CV loaded successfully");
				break;

			default:
				super.onManagerConnected(status);
				break;
			}
		};
	};
//	Loading open cv
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
	private CameraBridgeViewBase mOpenCvCameraView;

	private ArrayList<MatOfPoint> contours;

	private boolean mLogging;

	private boolean isTriangle=true;

	private long mSeconds;

	private RotatedRect rect;

	private float heightRatio;

	private float widhtRatio;

	private boolean mIsObjectRendered=false;

	private Runnable r;

	private float height;

	private float width;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//		 setContentView(R.layout.tutorial1_surface_view);
//		setContentView(R.layout.activity_lbarsample);
		try {
			AssetsManager.extractAllAssets(getApplicationContext(),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		boolean result = metaioSDK.setTrackingConfiguration("GPS");  
		boolean result = metaioSDK.setTrackingConfiguration("ORIENTATION_FLOOR");  
		Log.i(TAG, "::onCreate: Result :" + result );
		
//		TrackingValues tv=metaioSDK.getTrackingValues(1);
//		tv.setTranslation(new Vector3d(0, 0, -2540));
//		metaioSDK.setCosOffset(1, tv);
		handler=new Handler();
//		To trackd
//		TrackingValues pose = new TrackingValues();
//		pose.setTranslation(new Vector3d(0, 0, -1940));
//		metaioSDK.setCosOffset(1, pose);
//		metaioSDK.startInstantTracking("INSTANT_2D_GRAVITY");
		if (!OpenCVLoader.initDebug()) {
	        // Handle initialization error
			Log.i(TAG, "::onCreate:" + "intialise error :"+ "Handle initialization error");
	    }
		
		
//		
//		Button button = (Button)findViewById(R.id.button2);
////		button.setAlpha();
//		button.getBackground().setAlpha(125);
//		
//		button.getBackground().setColorFilter(0xAAFF0000, PorterDuff.Mode.MULTIPLY);
//		button.invalidate();
//		
////		ColorFilter filter = new LightingColorFilter(Color.RED, 1);
////		button.getPaint().setColorFilter(new PorterDuffColorFilter(Color.RED, Mode.MULTIPLY));
////		button.getPaint().setXfermode(new PorterDuffXfermode(Mode.SCREEN));
////		button.setBackgroundColor(Color.BLUE);
//		button.setBackground(getButtonBackground(button));
		
		mCallbackHandler=new IpolSDKCallBack();
		
		metaioSDK.registerCallback(mCallbackHandler);
		handler=new Handler();
		
		
	        
	}
	@Override
	public void onDrawFrame() {
		// TODO Auto-generated method stub
		super.onDrawFrame();
		
//		if(mGeomatryHotel!=null){
//			 Vector3d hotelVector= mGeomatryHotel.getTranslationLLACartesian();
////			 Vector3d hotelVector=mGeomatryHotel.getTranslation();
//			 Log.i(TAG, "::onDrawFrame:" + "vector.getX():"+hotelVector.getX());
//			 Log.i(TAG, "::onDrawFrame:" + "vector.getY():"+hotelVector.getY());
//			 hotelVector.setX(10);
//			 hotelVector.setY(10);
////				vector.setY(-vector.getZ());
//				mGeomatryHotel.setTranslation(hotelVector, false);
//		}
		  runOnUiThread(new Runnable() {
		        public void run() {
					
		        	refreshPoint(); 

		        }
		    });

		
		
		
		
	}
	private Drawable getButtonBackground(Button button){
		BitmapFactory.Options opt = new BitmapFactory.Options();
	    opt.inPreferredConfig = Config.ARGB_8888;
	    Log.i(TAG, "::getButtonBackground:" + "");
	    int width = button.getMeasuredWidth();	
	    int height = button.getMeasuredHeight();
	    Bitmap red = BitmapFactory.decodeResource(getResources(),
	            R.drawable.red, opt);
	    

	   
	    
	 Log.i(TAG, "::getButtonBackground:" + "width:"+width+" height:"+height);
	    Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	    result.eraseColor(Color.BLACK);
	    
	    Paint redP = new Paint();
	    
//	    redP.setShader(new BitmapShader(result, TileMode.CLAMP, TileMode.CLAMP));
//	    redP.setShader(null);
//	    redP.setColorFilter(new PorterDuffColorFilter(Color.RED, Mode.MULTIPLY));
	    redP.setXfermode(new PorterDuffXfermode(Mode.SCREEN));

		Canvas canvas=new Canvas(result);
		canvas.drawRect(0, 0, width, height, redP);
		Drawable d = new BitmapDrawable(getResources(),result);
		return d;
	}
//	Blending.
	static int blend(int color1, int color2, float frac)
	{
		
		int r1 = Color.red(color1);
		int g1 = Color.green(color1);
		int b1 = Color.blue(color1);
		
		int r2 = Color.red(color2);
		int g2 = Color.green(color2);
		int b2 = Color.blue(color2);
		
		
		int r3 = (int) (r1*(1.0f-frac) + r2*frac);
		int g3 = (int) (g1*(1.0f-frac) + g2*frac);
		int b3 = (int) (b1*(1.0f-frac) + b2*frac);
		
		
		return Color.rgb(r3, g3, b3);

	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		Button button = (Button)findViewById(R.id.button2);
//		ColorFilter filter = new LightingColorFilter(Color.RED, 1);
//		button.getPaint().setColorFilter(new PorterDuffColorFilter(Color.RED, Mode.MULTIPLY));
//		button.getPaint().setXfermode(new PorterDuffXfermode(Mode.SCREEN));
//		button.setBackgroundColor(Color.BLUE);
//		button.setBackground(getButtonBackground(button));
	}
	class IpolSDKCallBack extends IMetaioSDKCallback{
		
		//getting frame from metaio.
		@Override
		public void onNewCameraFrame(ImageStruct cameraFrame)
		{
			
			super.onNewCameraFrame(cameraFrame);
//			BitmapFactory.Options opt = new BitmapFactory.Options();
//		    opt.inPreferredConfig = Config.ARGB_8888;
//			Bitmap bitmap=cameraFrame.getBitmap();
			
//			bitmap.get
			Log.i(TAG, "::onNewCameraFrame:" + "getCameraFrameRate:"+metaioSDK.getCameraFrameRate());
			Log.i(TAG, "::onNewCameraFrame:" + ""+cameraFrame.getBufferSize());
			Button button = (Button)findViewById(R.id.button2);
//			ColorFilter filter = new LightingColorFilter(Color.RED, 1);
//			button.setBackgroundColor(Color.RED);
//			button.getPaint().setColorFilter(new PorterDuffColorFilter(Color.RED, Mode.MULTIPLY));
//			button.getPaint().setShader(new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP));
//			button.getPaint().setXfermode(new PorterDuffXfermode(Mode.SCREEN));
			//color blending
//			int bitmapColor=bitmap.getPixel(bitmap.getWidth()-bitmap.getWidth()/10, bitmap.getHeight()-bitmap.getHeight()/10);
////			int blendColor=blend(Color.RED, bitmapColor, 0.3f);
////			button.setBackgroundColor(blendColor);
//			Log.i(TAG, "::onNewCameraFrame:" + "bitmapColor:"+bitmapColor);
//			button.getBackground().setColorFilter(Color.BLACK,PorterDuff.Mode.MULTIPLY);
//			button.invalidate();
//			mCallbackHandler=new IpolSDKCallBack();
//			metaioSDK.setImage(cameraFrame);
//			metaioSDK.render();
			
			convertImage(cameraFrame);
			final Runnable r = new Runnable()
			{
			    

				public void run() 
			    {
					Log.i(TAG, "::run:" + "");
			    	if (metaioSDK != null)
						metaioSDK.requestCameraImage();
			       
			    }
			};
//			 handler.postDelayed(r, 1);
			
//			 if(bitmap!=null){
//				 bitmap.recycle();
//			 }
			 metaioSDK.requestCameraImage();

		}
		
		@Override
		public void onInstantTrackingEvent(boolean success, String file) {
			// TODO Auto-generated method stub
			super.onInstantTrackingEvent(success, file);
			metaioSDK.setTrackingConfiguration(file);
			
		}
	 
		@Override
		public void onSDKReady()
		{
			super.onSDKReady();
			Log.i(TAG, "::onSDKReady:" + "");
//			mSDKReady = true;
		}
	}
	
//	Heloper method for detecting shape
	double angle( Point pt1, Point pt2, Point pt0 )
	{
	    double dx1 = pt1.x - pt0.x;
	    double dy1 = pt1.y - pt0.y;
	    double dx2 = pt2.x - pt0.x;
	    double dy2 = pt2.y - pt0.y;
	    return (dx1*dx2 + dy1*dy2)/Math.sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
	}
//	Log file generation and witing log in it.
	public void appendLog(String text)
	{       
		if(!mLogging){  
			
			Log.i(TAG, "::appendLog:" + "mLogging:"+mLogging);
			return;
		}
	   File logFile = new File("sdcard/log.file");
	   if (!logFile.exists())
	   {
	      try
	      {
	         logFile.createNewFile();
	      } 
	      catch (IOException e)
	      {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	   }
	   try
	   {
	      //BufferedWriter for performance, true to set append to file flag
	      BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
	      buf.append(text);
	      buf.newLine();
	      buf.close();
	   }
	   catch (IOException e)
	   {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	   }
	}
	// Detecting Shape. 
	 public   Mat EdgeDetect(Mat src) {
		 Mat pyr=new Mat();
		 Mat timing=new Mat();
		 
		 Imgproc.pyrDown(src, pyr, new Size(src.width()/2, src.height()/2));
		 Imgproc.pyrUp(pyr, timing,src.size());
		 
         Mat blurred = new Mat();
         timing.copyTo(blurred);
//         src.copyTo(blurred);
         Log.v(TAG, "Blurred Matrix! : " + blurred.total() );
         
         Imgproc.medianBlur(src, blurred, 9);
         Log.v(TAG, "Median Blur Done!");
 
         Mat gray0 = new Mat(blurred.size(), blurred.type());
         Imgproc.cvtColor(gray0, gray0, Imgproc.COLOR_RGB2GRAY);
         Mat gray = new Mat();
         
            
         
         
//         List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
       
         
         // find squares in every color plane of the image
         pointsList.clear();
         
     for (int c = 0; c < 3; c++)
     {
                 Log.v(TAG, "Mix Channels Started! : " + gray0.total());
         int ch[] = {c, 0};
         MatOfInt fromto = new MatOfInt(ch);
         List<Mat> blurredlist = new ArrayList<Mat>();
         List<Mat> graylist = new ArrayList<Mat>();
         
//         blurredlist.add(0, blurred);
//         graylist.add(0, gray0);
//         
         blurredlist.add(0, timing);
         graylist.add(0, gray0);

         
         Core.mixChannels(blurredlist, graylist, fromto);
         gray0 = graylist.get(0);
                 Log.v(TAG, "Mix Channels Done! : " + gray0.total() );
      // try several threshold levels
         int threshold_level = 11;
         for (int l = 0; l < threshold_level; l++)
         {
             // Use Canny instead of zero threshold level!
             // Canny helps to catch squares with gradient shading
                 Log.v(TAG,"Threshold Level: " + l);
                 
             if (l ==0)
             {
                 Imgproc.Canny(gray0, gray, 50, 5); // 

                 // Dilate helps to remove potential holes between edge segments
//                 Imgproc.dilate(gray, gray, Mat.ones(new Size(3,3),0));
                 
                 Imgproc.dilate(gray, gray, Mat.ones(new Size(3,3),0));
                 
             }
             else
             {
                     int thresh = (l+1) * 255 / threshold_level;
//            	  	 int thresh = 50;
                     Imgproc.threshold(gray0, gray, thresh, 255, Imgproc.THRESH_TOZERO);
             }

                 Log.v(TAG, "Canny (or Thresholding) Done!");
                 Log.v(TAG, "Gray Matrix (after)! : " + gray.total() );
                 contours=new ArrayList<MatOfPoint>();
             // Find contours and store them in a list
             Imgproc.findContours(gray, contours, new Mat(), 1, 2);
                 Log.v(TAG, "Contours Found!");
             
             MatOfPoint2f approx = new MatOfPoint2f();
             MatOfPoint2f mMOP2f1 = new MatOfPoint2f();
             MatOfPoint mMOP = new MatOfPoint();
             for( int i = 0; i < contours.size(); i++ )
             {
                 contours.get(i).convertTo(mMOP2f1, CvType.CV_32FC2);
                 Imgproc.approxPolyDP(mMOP2f1, approx, Imgproc.arcLength(mMOP2f1, true)*0.02, true);
                 approx.convertTo(mMOP, CvType.CV_32S);
         
                 if( approx.rows()==4  && Math.abs(Imgproc.contourArea(approx)) > 1000 && Imgproc.isContourConvex(mMOP))
                 {
                         
                         Log.v(TAG,"Passes Conditions! " + approx.size().toString());
                         double maxcosine = 0;
                         Point[] list = approx.toArray();
                         
                          for (int j = 2; j < 5; j++)
                          {
                                  double cosine =Math.abs(angle(list[j%4], list[j-2], list[j-1]));
                                  maxcosine = Math.max(maxcosine, cosine);
                          }
                          
                          if( maxcosine < 0.3 )
                          {
                                  MatOfPoint temp = new MatOfPoint();
                                  approx.convertTo(temp, CvType.CV_32S);
                          appendLog("approx:"+approx);
                          
//                          DrawnContours drawnContour=new DrawnContours();
//                          drawnContour.setIndex(i);
//                          mDrawnContours.add(drawnContour);
                          double area = Math.abs(Imgproc.contourArea(temp));
                          if(!isTriangle) {
                          Imgproc.drawContours(src, contours, i, new Scalar(0,0,255));
                          }
                          else{
                        	  
                        	  RotatedRect minRect=Imgproc.minAreaRect(approx);
                        	  if(minRect.size.equals(src.size())){
                        		  continue;
                        	  }
                        	  else if((Math.abs(minRect.size.height-src.size().height)<20)){
                        		  continue;
                        	  }
                        	  else if((Math.abs(minRect.size.width-src.size().width)<20)){
                        		  continue;
                        	  }

                  			Point points[]=new Point[4];
                  			minRect.points(points);
                  			
                  			if(isInnerRect(points))
                  			{
                  				removeInnerRectangle(minRect);
                  				pointsList.add(minRect);
                  			}
             
                  			
                  	     		
                          }
                      }
                 }

             }
         }
     }
     return src;
     }
	 
	 //removing inner rectangle
	 private void removeInnerRectangle(RotatedRect minRect) {
	 	Rect rect=minRect.boundingRect();
	 	 for(int i=0;i<pointsList.size();i++){
	 		 RotatedRect rect1=pointsList.get(i);
	 		 Point[] items=new Point[4];
	 		 rect1.points(items);
	 		 for(int j=0;j<items.length;j++){
	 			 if(items[j].inside(rect)){
	 				 pointsList.remove(rect1);
	 				 break;
	 			 }
	 			}
	 	 }
	 		
	 	}
//	 Removing inner rectangle
     
     public boolean isInnerRect(Point[] point){
		 for(int i=0;i<pointsList.size();i++){
			 RotatedRect rect= pointsList.get(i);
			 
//			 Point[] items= new point
			 Log.i(TAG, "::isInnerRect:" + "point 0:"+point[0]);
			 Log.i(TAG, "::isInnerRect:" + "point 1:"+point[1]);
			 Log.i(TAG, "::isInnerRect:" + "point 2:"+point[2]);
			 Log.i(TAG, "::isInnerRect:" + "point 3:"+point[3]);
			  Rect boundingRect=rect.boundingRect();
			 for(int j=0;j<point.length;j++){
				 if(point[j].inside(boundingRect))
					 return false;
			 }
			 
//			 if( (point[0].x >= items[1].x) && (point[0].x<=items[3].x ) )
//				 return false;
//			 
		 }
		 return true;
	 }
//     converstion from metaio to open cv Frame format
     public Mat getMat(ImageStruct src){
    	 
    	 final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    	    final File file = new File(path, "cameraFrame.png");
    	    final String filePath = file.toString();
    	    int width = src.getWidth();
    	    int height = src.getHeight();
    	    Bitmap bmp = src.getBitmap();
    	    Mat mat =new Mat(new Size(width, height), CvType.CV_8UC1);
    	    if(bmp != null) 
    	    {
    	        Utils.bitmapToMat(bmp, mat, false);
    	        Highgui.imwrite(filePath, mat);
    	    }
    	    return mat;
     }
//     converstion
	public void convertImage(ImageStruct src)
	{
		
	     width = src.getWidth();
	     height = src.getHeight();
	    Log.i(TAG, "::convertImage:" + "width:"+width);
	    Log.i(TAG, "::convertImage:" + "height:"+height);
	    
	    DisplayMetrics displaymetrics = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//    	int height1 = displaymetrics.heightPixels;
//    	int wwidth1 = displaymetrics.widthPixels;
//    	heightRatio=height1/height;

    	int height1 = metaioSDK.getRenderSize().getY();
    	int wwidth1 = metaioSDK.getRenderSize().getX();
    	heightRatio=height1/height;

    	
    	widhtRatio=wwidth1/width;
    	Log.i(TAG, "::run:" + "height."+height+" ratio :"+heightRatio);
    	Log.i(TAG, "::run:" + "wwidth;"+wwidth1+" ratio :"+widhtRatio);
//	    mCameraImage = new Mat(new Size(width, height), 24);
	    
	    mCameraImage=getMat(src);
//	    mCameraImage=new Mat();
	    Log.i(TAG, "::convertImage:" + "");
//	    final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//	    final File file = new File(path, "cameraFrame.png");
//	    Log.i(TAG, "::convertImage:" + "file.exists():"+file.exists());
//	    final String filePath = file.toString();
//	    Log.i(TAG, "::convertImage:" + "filePath:"+ filePath);
	    
//	    mCameraImage.put(0, 0, src.getBuffer());
	    
//	    Imgproc.GaussianBlur(mCameraImage, mCameraImage, new Size(11,11), 0);
	    
	    Log.i(TAG, "::convertImage:" + "mCameraImage.size():"+mCameraImage.size());
	    long milliSecond=new Date().getTime();
		long second = (milliSecond / 1000) % 60;
		
//		 Mat temp=mCameraImage;
//		  EdgeDetect(mCameraImage);
		  
//		 mCameraImage= drawRectangles(temp);
//		calculate time and if its new second then we detect shape
		if(Math.abs(mSeconds-second)>1 ){
			Log.i(TAG, "::onCameraFrame:" + " 1");
//			return drawContours(inputFrame.rgba());
//			if(!isTriangle)
//			 EdgeDetect(inputFrame.rgba());
//				return debugSquares(inputFrame.rgba());
			 mSeconds=second;
			 Mat temp=mCameraImage; 
			  EdgeDetect(mCameraImage);
			  
			 mCameraImage= drawRectangles(temp);
			
			
		}
		else{
			mCameraImage= drawRectangles(mCameraImage);
		}
//	    Imgproc.cvtColor(mCameraImage, tmp, Imgproc.COLOR_GRAY2RGBA, 4);
	    runOnUiThread(new Runnable() {
	        public void run() {
	        	Bitmap bmp = Bitmap.createBitmap(mCameraImage.cols(), mCameraImage.rows(), Bitmap.Config.ARGB_8888);
	     	    Utils.matToBitmap(mCameraImage, bmp);
//	     	    bmp=blurImage(bmp);
	     	    Drawable d = new BitmapDrawable(getResources(),bmp);
	     	    
	     	   
//	     	    imageView.setImageBitmap(src.getBitmap());
//	     	    imageView.setBackground(d);
//	     	    mOpenCvCameraView.setBackground(d);
	        }
	    });
	   //	    mOpenCvCameraView.setBackgroundColor(Color.BLUE);
	    Log.i(TAG, "::convertImage:" + "src.getBuffer():");
//	    Highgui.imwrite(filePath, mCameraImage);
	}
//	Drawing rectangel which we have detected.
	 public Mat drawRectangles(Mat src) {
		 Log.i(TAG, "::drawRectangles:" + "pointsList.size():"+pointsList.size());
		 for(int i=0;i<pointsList.size();i++){
			 rect= pointsList.get(i); 
			 
			  Point[] points=new Point[4];
			  rect.points(points);
				for(int j=0;j<4;j++){
//  				Core.line(sourceImage, points[j], points[(j+1)%4], new Scalar(125,125,255));
  				Core.line(src, points[j], points[(j+1)%4], new Scalar(125,125,255), 10);
  				appendLog("points[j]:"+points[j]);
//  				Core.line(sourceImage, points[j], points[(j+1)%4], new Scalar(0,125,255), 1, 8, 1);
  			}
				 runOnUiThread(new Runnable() {
				        public void run() {

				        	Log.i(TAG, "::run:" + "rect.boundingRect():"+rect.boundingRect());
				        	Log.i(TAG, "::run:" + "rect.boundingRect().x:"+rect.boundingRect().x);
				        	Log.i(TAG, "::run:" + "");
				        	Log.i(TAG, "::run:" + "");
				        	
				        	
				        	
//				Button rectbutton = (Button)findViewById(R.id.button5);
//				 rectbutton.setTop((int) (rect.boundingRect().y *heightRatio));
//				 rectbutton.setLeft((int) (rect.boundingRect().x *widhtRatio));
//				 
//				 rectbutton.setBottom((int) (rect.boundingRect().height *heightRatio*heightRatio));
//				 rectbutton.setRight((int) (rect.boundingRect().width *widhtRatio*widhtRatio));
//				 rectbutton.setBackgroundColor(Color.BLUE);
//				 rectbutton.invalidate();
////				 mOpenCvCameraView.setBackgroundColor(Color.BLUE);
//				 mOpenCvCameraView.invalidate();
			}
				 });
		 }
		 
		 
		 return src;
	 }
	 @Override
	public boolean onTouchEvent(MotionEvent event) {
		 Log.i(TAG, "::onTouchEvent:" + "");
//		 renderObject();
		return super.onTouchEvent(event);
	}
//	 Finding  Distance to have prooper object size
	 private Vector3d findDistance(Vector2d point) {
		 Vector3d centerVec=metaioSDK.get3DPositionFromScreenCoordinates(1, point);
		 for(int i=0;i<pointsList.size();i++){
			 RotatedRect rect= pointsList.get(i); 
			 Log.i(TAG, "::findDistance:" + "heightRatio:"+heightRatio+"widhtRatio:"+widhtRatio);
			 float heightRatio=metaioSDK.getRenderSize().getY()/height;
			 float widthRatio=metaioSDK.getRenderSize().getX()/width;
			 Log.i(TAG, "::findDistance:" + "heightRatio:"+heightRatio);
			 Log.i(TAG, "::findDistance:" + "widthRatio:"+widthRatio);
//			 float width=(float) Math.sqrt(Math.pow(rect.boundingRect().x*widhtRatio-rect.boundingRect().width*widhtRatio, 2.0f)+Math.pow(rect.boundingRect().y*heightRatio-rect.boundingRect().height*heightRatio, 2.0f));
			 float width=(float) Math.sqrt(Math.pow(rect.boundingRect().x*widthRatio-rect.boundingRect().width*widthRatio, 2.0f)+Math.pow(rect.boundingRect().y*heightRatio-rect.boundingRect().height*heightRatio, 2.0f));
			 

//			 float width=(float) Math.sqrt(Math.pow(rect.boundingRect().x-rect.boundingRect().width, 2.0f)+Math.pow(rect.boundingRect().y-rect.boundingRect().y, 2.0f));
			 
			 float distance=(float) Math.sqrt(Math.pow(centerVec.getX(), 2.0f)+Math.pow(centerVec.getY(), 2.0f));
			 float originalDistance=distance;
//			 Log.i(TAG, "::findDistance:" + "metaioSDK.getRenderSize().getX():"+);
//			 Log.i(TAG, "::findDistance:" + "metaioSDK.getRenderSize().getY():"+);
			 
			 
			 
			 float normalDistance = 265.0f;
			    float normalWidth = 265.0f;
			    float normalWidthFactor = normalWidth / width;
			    float thisDistance = normalDistance * normalWidthFactor;
			    
			    float teamX=centerVec.getX(),tempY=centerVec.getY();
			    while (distance >  thisDistance) {
			        teamX *= 0.95;
			        tempY *= 0.95;
			        distance = (float) Math.sqrt((Math.pow(teamX, 2) + Math.pow(tempY , 2)));
			        
			    }
			    centerVec.setX(teamX);
			    centerVec.setY(tempY);

			    centerVec.setZ(1300.0f * (originalDistance  - distance) / originalDistance);
			    
		 }
		 return centerVec;
	 }
//	 Rendering object after convestion from 2d to 3d
	 public void renderObject(){
		 if(mIsObjectRendered){
			 return;
		 }
		 mIsObjectRendered=true;
		 Log.i(TAG, "::renderObject:" + "pointsList.size():"+pointsList.size());
//		 for(int i=0;i<pointsList.size();i++){
//			 RotatedRect rect= pointsList.get(i); 
//			 
//			 
//			 Point[] points=new Point[4];
//			 rect.points(points);
//			 
//			 
//		
//			 
////			 mOpenCvCameraView.setX(rect.boundingRect().x);
////			 mOpenCvCameraView.setY(rect.boundingRect().y);
//			 
//			 
//			 float pointX=(float) ((points[0].x+points[2].x)/2.0f);
//			 float pointY=(float) ((points[0].y+points[1].y)/2.0f);
//			 
//			 Log.i(TAG, "::renderObject:" + "0"+points[0]);
//			 Log.i(TAG, "::renderObject:" + "1"+points[1]);
//			 Log.i(TAG, "::renderObject:" + "2"+points[2]);
//			 Log.i(TAG, "::renderObject:" + "3"+points[3]);
//			 
//			 			 
//			 Vector2d vector2D=new Vector2d((float)rect.center.x, (float)rect.center.y);
////			 Vector2d vector2D=new Vector2d((float)500, (float)500);
//			 
//			 
//			 Vector3d vector=new Vector3d();
////			 vector.setX((float) points[0].x);
////			 vector.setY((float) points[0].y);
////			 mGeomatryHotel.setTranslation(vector, false);
////			 mRadar.add(mGeomatryHotel);
//			 
////			vector= metaioSDK.get3DPositionFromScreenCoordinates(1, vector2D);
//			vector=	metaioSDK.get3DPositionFromScreenCoordinates(1, vector2D,new Vector3d(1001,1001,1001));
//			
//			Log.i(TAG, "::renderObject:" + "vector x"+vector.getX()+"vector y"+vector.getY()+ "vector z"+vector.getZ());
		
//			vector.setX(0);
//			vector.setY(0);
//			vector.setZ(0);
		
			 
			 Log.i(TAG, "::renderObject:" + "mGeomatryHotel.getBoundingBox().getMin():");
			 Log.i(TAG, "::renderObject:" + " mGeomatryHotel.getBoundingBox().getMax():"+ mGeomatryHotel.getBoundingBox().getMax());
			
//			 Log.i(TAG, "::renderObject:" + " mGeomatryVFRHeilbronn.getBoundingBox().getMin():"+ mGeomatryVFRHeilbronn.getBoundingBox().getMin());
//			 Log.i(TAG, "::renderObject:" + " mGeomatryVFRHeilbronn.getBoundingBox().getMax():"+mGeomatryVFRHeilbronn.getBoundingBox().getMax());
			 
			 
//			 LLACoordinate coordinate=mGeomatryHotel.getTranslationLLA();
//			 coordinate.get
			 
//			 mGeomatryHotel.setScale(400f);
			 mRadar.add(mGeomatryHotel);
			 
			 Vector2d vector2D=new Vector2d((float)rect.center.x*2.0f, (float)rect.center.y*2.0f);
				 mGeomatryHotel.setTranslation(findDistance(vector2D), false);
				 rotateGoal();
			 
//			  mGeomatryHotel.setTranslation(translation)
			 Log.i(TAG, "::renderObject:" + "mGeomatryHotel.getBoundingBox():"+mGeomatryHotel.getBoundingBox());
			 Log.i(TAG, "::renderObject:" + " mGeomatryHotel.getBoundingBox().getMax():"+ mGeomatryHotel.getBoundingBox().getMax());
			 
		
		
			 
			 
//			 TrackingValues trackingValue=new TrackingValues();
//			 Vector3d cameraToCOS=trackingValue.getTranslation();
//			 
//			 Vector3d cameratoObject = cameraToCOS.add(mGeomatryHotel.getTranslation());
//			 
//			 float distance = FloatMath.sqrt(cameratoObject.getX()*cameratoObject.getX() + cameratoObject.getY()*cameratoObject.getY() + cameratoObject.getZ()*cameratoObject.getZ());

//			 Log.i(TAG, "::renderObject:" + "distance:"+distance);
			 
			 
			 
//				  r = new Runnable()
//				{
//				    
//
//					public void run() 
//				    {
//						Log.i(TAG, "::run:" + "");
//				    	onClick(null);
//				    	boolean postDelayed = handler.postDelayed(r, 500);
//				    }
//				};
//				
//				 handler.postDelayed(r, 500);
		
		
		 }
		 
//		 pointsList.clear();
//	 }
//	 Blur Image
	Bitmap blurImage (Bitmap input)
	{
		RenderScript rsScript = RenderScript.create(this);
		Allocation alloc = Allocation.createFromBitmap(rsScript, input);

		ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rsScript, alloc.getElement());
		blur.setRadius(12);
		blur.setInput(alloc);

		Bitmap result = Bitmap.createBitmap (input.getWidth(), input.getHeight(), input.getConfig());
		Allocation outAlloc = Allocation.createFromBitmap(rsScript, result);
		blur.forEach (outAlloc);
		outAlloc.copyTo (result);

		rsScript.destroy ();
		return result;
	}
	
//	public void convertImage(ImageStruct src)
//	{
//	    final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//	    final File file = new File(path, "cameraFrame.png");
//	    final String filePath = file.toString();
//	    Bitmap bmp = src.getBitmap();
//	    
//	    int width = src.getWidth();
//	    int height = src.getHeight();
//	    
//	    mCameraImage= new Mat(new Size(width, height), CvType.CV_8SC3);
//	    if(bmp != null) 
//	    {
//	        Utils.bitmapToMat(bmp, mCameraImage, false);
//	        Highgui.imwrite(filePath, mCameraImage);
//	    }
//	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		
		// remove callback
		if (mSensors != null)
		{
			mSensors.registerCallback(null);
		//	mSensorsManager.pause();
		}
		
		if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
	}
	  public void onDestroy() {
	        super.onDestroy();
	        if (mOpenCvCameraView != null)
	            mOpenCvCameraView.disableView();
	    }
	public void onButtonClick(View v)
	{
		finish();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		mGUIView.setBackgroundColor(Color.TRANSPARENT);
//		mGUIView.setAlpha(125F);
//		mSurfaceView.setEGLConfigChooser(8,8,8,8, 16, 0);   
//		mSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
	}
	@Override
	protected void onResume() 
	{
		super.onResume();
		
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this,
				mLoaderCallBack);
		// Register callback to receive sensor updates
		if (mSensors != null)
		{	
			mSensors.registerCallback(this);
			//mSensorsManager.resume();
		}
//		metaioSDK.stopCamera();
//		metaioSDK.render();
//		 metaioSDK.setSeeThrough(true);
//		 FrameLayout layout = (FrameLayout)findViewById(R.id.metaioframe);
//		 layout.setVisibility(View.GONE);
		
		
		
		Button button = (Button)findViewById(R.id.button2);
//		button.setAlpha();
//		button.getBackground().setAlpha(125);
		
		button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
		button.invalidate();
//		metaioSDK.setSeeThrough(true);
//		metaioSDK.stopCamera();
		
		// stop camera
		
		  mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);

	        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

	        mOpenCvCameraView.setCvCameraViewListener(this);
	        mOpenCvCameraView.setOnClickListener(this);
//	        mOpenCvCameraView.enableView();
	        

		metaioSDK.requestCameraImage();
		View paretnScreen = (View)findViewById(R.id.parentscreen);
		paretnScreen.setOnTouchListener(this);
		paretnScreen.setOnTouchListener(this);
		mGUIView.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lbarsample, menu);
		return true;
	}

	@Override
	protected int getGUILayout() {
		// TODO Auto-generated method stub
//		return R.layout.tutorial1_surface_view;
		return R.layout.activity_lbarsample;
	}
//	Rotation 
	public void rotateXNegative(View v) {
		roteateGoal(-10.0f,0,0);
	}
	public void rotateXPoisitive(View v) {
		roteateGoal(10.0f,0,0);
	}
public void rotateYPositive(View v) {
	roteateGoal(0,10.0f,0);
	}
public void rotateYNegative(View v) {
	roteateGoal(0,-10.0f,0);
}
public void rotateZPositive(View v) {
	roteateGoal(0,0.0f,10);
}
public void rotateZNegative(View v) {
	roteateGoal(0,0.0f,-10);
}
// Rotatin Goal Object
private void roteateGoal(float rotationX, float rotationY,float rotationZ){
	if(mGeomatryHotel!=null){
		Vector3d rotationVector=new Vector3d();
		rotationVector.setX(rotationX);
		rotationVector.setY(rotationY);
		rotationVector.setZ(rotationZ);
		Rotation rotation=new Rotation();
		rotation.setFromEulerAngleDegrees(rotationVector);
		mGeomatryHotel.setRotation(rotation,true);
		
	}
}
private void rotateGoal(){
	 Rotation myrotation = metaioSDK.getTrackingValues(1).getRotation();
	 mGeomatryHotel.setRotation(myrotation.inverse());
	    
}
/// Intialising Radar and 3d object
	@Override
	protected void loadContents() {
		// TODO Auto-generated method stub
		
		
			
			try
			{ 
				Log.i(TAG, "::loadContents:" + "1");
				
				String filepath = AssetsManager.getAssetPath("Tutorial5/Assets5/POI_bg.png");
				Log.i(TAG, "::loadContents: filepath:" + filepath);
				if (filepath != null) 
				{
					
//					mGeomatryHotel = metaioSDK.loadImageBillboard(createBillboardTexture("Hotel B&B"));
//					mGeomatryBistroCafe = metaioSDK.loadImageBillboard(createBillboardTexture("Bistro Cafe"));
//					mGeomatryVFRHeilbronn=metaioSDK.loadImageBillboard(createBillboardTexture("Heilbronn VFR"));
				}
				filepath=AssetsManager.getAssetPath("Tutorial5/Assets5/Tor_Vodafone_02.obj");
//				filepath=AssetsManager.getAssetPath("Tutorial5/Assets5/Ball_Sepp_Step_03a.obj");
				if(filepath!=null){
					mGeomatryHotel=metaioSDK.createGeometry(filepath);
					if(mGeomatryHotel!=null){
//						mGeomatryHotel.setScale(40f);

					}
						
				}
//				Temparary do not want metaio man
				
				Log.i(TAG, "::loadContents:" + "2");
//				filepath = AssetsManager.getAssetPath("Tutorial5/Assets5/metaioman.md2");
//				if (filepath != null) 
//				{
//					// West
//					mGeomatryVFRHeilbronn = metaioSDK.createGeometry(filepath);
//					if (mGeomatryVFRHeilbronn != null)
//					{
//						mGeomatryVFRHeilbronn.startAnimation("idle", true);
//						mGeomatryVFRHeilbronn.setScale(new Vector3d(15f,15f,15f));
//					}
//				
//				}
				Log.i(TAG, "::loadContents:" + "3");
				
//				updateGeometriesLocation(mSensors.getLocation());
				setLocation();
				Log.i(TAG, "::loadContents:" + "3.1");
				// create radar
				mRadar = metaioSDK.createRadar();
				Log.i(TAG, "::loadContents:" + "3.2");
				mRadar.setBackgroundTexture(AssetsManager.getAssetPath("Tutorial5/Assets5/radar.png"));
				Log.i(TAG, "::loadContents:" + "3.3");
				mRadar.setObjectsDefaultTexture(AssetsManager.getAssetPath("Tutorial5/Assets5/yellow.png"));
				Log.i(TAG, "::loadContents:" + "3.4");
				mRadar.setRelativeToScreen(IGeometry.ANCHOR_TL);
				Log.i(TAG, "::loadContents:" + "4");
//				// add geometries to the radar
//				mRadar.add(mGeomatryHotel);
//				mRadar.add(mGeomatryBistroCafe);
				//commenting temparary
//				mRadar.add(mGeomatryVFRHeilbronn);
				
//				metaioSDK.getTrackingValues().set
//				TrackingValues tv = new TrackingValues();
				
				metaioSDK.setLLAObjectRenderingLimits(10, 1000);
				
				
				
				
				
			}
			
			catch (Exception e)
			{
				Log.i(TAG, "::loadContents:" + "Exception:"+e.toString());
			}
	}
//	Setting Location using latitude and longitude

		private void setLocation() {
		// TODO Auto-generated method stub
			
			
//  hotel b&b
		mLocationItems=new POILocation[3]; 
		mLocationItems[0]=new POILocation();
		
		mLocationItems[0].setLatitude(49.140688f);
		mLocationItems[0].setLongitude(9.209715f);
		
//		Bistro Cafe
		mLocationItems[1]=new POILocation();
		mLocationItems[1].setLatitude(49.139902f);
		mLocationItems[1].setLongitude(9.215294f);
		
		
//		VFR Heilbronn
		mLocationItems[2]=new POILocation();
			mLocationItems[2].setLatitude(49.13489f);
			mLocationItems[2].setLongitude(9.203578f);
		
			if (mGeomatryHotel!= null)
			{
//				location.setLatitude(location.getLatitude()-OFFSET);
//				MetaioDebug.log("geometrySouth.setTranslationLLA: "+location);
//				mGeometrySouth.setTranslationLLA(location);
//				
//				location.setLatitude(location.getLatitude()+OFFSET);
//				LLACoordinate coordinate=new LLACoordinate();
//				coordinate.setLatitude(mLocationItems[0].getLatitude());
//				coordinate.setLongitude(mLocationItems[0].getLongitude());
//				mGeomatryHotel.setTranslationLLA(coordinate);
				
			}
			
//			if (mGeomatryBistroCafe != null)
//			{
//				LLACoordinate coordinate=new LLACoordinate();
//				coordinate.setLatitude(mLocationItems[1].getLatitude());
//				coordinate.setLongitude(mLocationItems[1].getLongitude());
//				mGeomatryBistroCafe.setTranslationLLA(coordinate);
//			}
			
			if (mGeomatryVFRHeilbronn != null)
			{
				Log.i(TAG, "::setLocation:" + "mSensors.getLocation().getLatitude():"+mSensors.getLocation().getLatitude()+"mSensors.getLocation().getLatitude()");
				
				LLACoordinate coordinate=new LLACoordinate();
				coordinate.setLatitude(mLocationItems[2].getLatitude());
				coordinate.setLongitude(mLocationItems[2].getLongitude());
//				coordinate.setAltitude(mSensors.getLocation().getAltitude()); 
				mGeomatryVFRHeilbronn.setTranslationLLA(coordinate);
			}
			
			
		}
		private void checkDistanceToTarget() 
		{
			// get tracking values for COS 1
			TrackingValues tv = metaioSDK.getTrackingValues(1);
			
			// Note, you can use this mechanism also to detect if something is tracking or not.
			// (e.g. for triggering an action as soon as some target is visible on screen)
			if (tv.isTrackingState())
			{			
				// calculate the distance as sqrt( x^2 + y^2 + z^2 )
				final float distance = tv.getTranslation().norm();
				
				// define a threshold distance
				final float threshold = 200;
				
			
				Log.i(TAG, "::checkDistanceToTarget:" + "distance:"+distance);
			}
		}
		
// creating bill board and write String on it.
		private String createBillboardTexture(String billBoardTitle)
	    {
	           try
	           {
	                  final String texturepath = getCacheDir() + "/" + billBoardTitle + ".png";
	                  Paint mPaint = new Paint();

	                  // Load background image (256x128), and make a mutable copy
	                  Bitmap billboard = null;
	                  
	                  //reading billboard background
	                  String filepath = AssetsManager.getAssetPath("Tutorial5/Assets5/POI_bg.png");
	                  Bitmap mBackgroundImage = BitmapFactory.decodeFile(filepath);
	                  
	                  billboard = mBackgroundImage.copy(Bitmap.Config.ARGB_8888, true);


	                  Canvas c = new Canvas(billboard);

	                  mPaint.setColor(Color.WHITE);
	                  mPaint.setTextSize(24);
	                  mPaint.setTypeface(Typeface.DEFAULT);

	                  float y = 40;
	                  float x = 30;

	                  // Draw POI name
	                  if (billBoardTitle.length() > 0)
	                  {
	                        String n = billBoardTitle.trim();

	                        final int maxWidth = 160;

	                        int i = mPaint.breakText(n, true, maxWidth, null);
	                        c.drawText(n.substring(0, i), x, y, mPaint);

	                        // Draw second line if valid
	                        if (i < n.length())
	                        {
	                               n = n.substring(i);
	                               y += 20;
	                               i = mPaint.breakText(n, true, maxWidth, null);

	                               if (i < n.length())
	                               {
	                                      i = mPaint.breakText(n, true, maxWidth - 20, null);
	                                      c.drawText(n.substring(0, i) + "...", x, y, mPaint);
	                               } else
	                               {
	                                      c.drawText(n.substring(0, i), x, y, mPaint);
	                               }
	                        }

	                  }


	                  // writing file
	                  try
	                  {
	                	  FileOutputStream out = new FileOutputStream(texturepath);
	                      billboard.compress(Bitmap.CompressFormat.PNG, 90, out);
	                      MetaioDebug.log("Texture file is saved to "+texturepath);
	                      return texturepath;
	                  } catch (Exception e) {
	                      MetaioDebug.log("Failed to save texture file");
	                	  e.printStackTrace();
	                   }
	                 
	                  billboard.recycle();
	                  billboard = null;

	           } catch (Exception e)
	           {
	                  MetaioDebug.log("Error creating billboard texture: " + e.getMessage());
	                  MetaioDebug.printStackTrace(Log.DEBUG, e);
	                  return null;
	           }
	           return null;
	    }

	@Override
	protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
		// TODO Auto-generated method stub
		return mCallbackHandler; 
	}

	
//	notification on touch event of geomatary
	@Override
	protected void onGeometryTouched(final IGeometry geometry) {
		// TODO Auto-generated method stub
		mSurfaceView.queueEvent(new Runnable()
		{

			@Override
			public void run() 
			{
//				geometry.setRelativeToScreen(IGeometry.ANCHOR_CC);
				Vector3d vector= geometry.getTranslationLLACartesian();
				vector.setX(-vector.getX());
				vector.setY(-vector.getY());
				Log.i(TAG, "::run:" + ""+vector.getX());
//				vector.setY(-vector.getZ());
				geometry.setTranslation(vector, false);
				
			
			}
		
				
		});
		
	}

	@Override
	public void onGravitySensorChanged(float[] gravity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHeadingSensorChanged(float[] orientation) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void onLocationSensorChanged(LLACoordinate location) {
		// TODO Auto-generated method stub
		Log.i(TAG, "::onLocationSensorChanged:" + "location.getLatitude():"+location.getLatitude()+"location.getLongitude():"+location.getLongitude());
		setLocation();
	}

	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		// TODO Auto-generated method stub
		Log.i(TAG, "::onCameraFrame:" + "");
		return inputFrame.rgba();
	}
//	refreshing the delected object and its place
	public void refreshPoint(){
		if(mIsObjectRendered){
		Vector2d vectorPoint= metaioSDK.getScreenCoordinatesFrom3DPosition(1, mGeomatryHotel.getBoundingBox(false).getMin());
			Log.i(TAG, "::refreshPoint:" + "vectorPoint.getX():"+vectorPoint.getX()+"vectorPoint.getY():"+vectorPoint.getY());
			Vector2d vectorMaxPoint=metaioSDK.getScreenCoordinatesFrom3DPosition(1, mGeomatryHotel.getBoundingBox(false).getMax());
			Log.i(TAG, "::refreshPoint:" + "vectorMaxPoint.getX():"+vectorMaxPoint.getX()+"vectorMaxPoint.getY():"+vectorMaxPoint.getY());
			
//			Button rectbutton = (Button)findViewById(R.id.button2);
//			 rectbutton.setTop((int) (vectorPoint.getY()));
//			 rectbutton.setLeft((int) (vectorPoint.getX()));
//			 
//			 rectbutton.setBottom((int) (vectorPoint.getY()+4));
//			 rectbutton.setRight((int) (vectorPoint.getX()+4));
//			 rectbutton.invalidate();
//			 rectbutton.setVisibility(View.VISIBLE);
//			 
//			 
//			 Button thirdButton = (Button)findViewById(R.id.button3);
//			 thirdButton.setTop((int) (vectorMaxPoint.getY()));
//			 thirdButton.setLeft((int) (vectorMaxPoint.getX()));
//			 
//			 thirdButton.setBottom((int) (vectorMaxPoint.getY()+4));
//			 thirdButton.setRight((int) (vectorMaxPoint.getX()+4));
//			 thirdButton.invalidate();
//			 thirdButton.setVisibility(View.VISIBLE);
			 
			 Log.i(TAG, "::refreshPoint:" + "pointsList.size():"+pointsList.size());
			 
				Button rectbutton1 = (Button)findViewById(R.id.button5);
				rectbutton1.setText("Rects:"+pointsList.size());
				
			 
			 for(int i=0;i<pointsList.size();i++){
			Vector2d vector2d=	new Vector2d();
			vector2d.setX((float) (pointsList.get(i).center.x*2.0f));
			vector2d.setY((float) pointsList.get(i).center.y*2.0f);
			mGeomatryHotel.setTranslation(findDistance(vector2d),false);
			rotateGoal();
					
//				mGeomatryHotel.setScale(-scaleFactor);
			 }
			 
//				mGeomatryHotel.setScale(400f);
			 
		}	

	}
	
	@Override
	public void onClick(View v) {
		Log.i(TAG, "::onClick:" + "onclick"+mIsObjectRendered);
		
		if(!mIsObjectRendered){
			renderObject();
			return;
			
		}
		
		mSurfaceView.queueEvent(new Runnable()
		{

			private Vector2d vectorPoint;
			private float mDifferenceY;
			private float mDifferentRatio;
			private float mDifferenceX;
			private float mDifferentRatioX;
			private float mDifferenceZ;
			private double factortoMinusZ;
		

			@Override
			public void run() 
			{
								
//				for(int i=0;i<size;i++){
//				TrackingValues value=	metaioSDK.getTrackingValues().get(i);
//				Log.i(TAG, "::onClick:" + "value.getTranslation().getX():"+value.getTranslation().getX()+"value.getTranslation().getY():"+value.getTranslation().getY()+"value.getTranslation().getZ():"+value.getTranslation().getZ());
//				}
//				
				float matrix[] = new float[16] ;
//				metaioSDK.getTrackingValues(1, matrix , true);
//				for(int k=0;k<matrix.length;k++){
//					Log.i(TAG, "::onClick:" + "k:"+k+"matrix[k]:"+matrix[k]);
//				}
				
				Log.i(TAG, "::onClick:" + ""+matrix);
//				geometry.setRelativeToScreen(IGeometry.ANCHOR_CC);
//				Vector3d vector= mGeomatryHotel.getTranslationLLACartesian();
				Vector3d vector= mGeomatryHotel.getTranslation();
				
				Log.i(TAG, "::onClick:" + "vector.getX():"+vector.getX());
				 Log.i(TAG, "::onClick:" + "vector.getY():"+vector.getY());
				 Log.i(TAG, "::onClick:" + "vector.getZ():"+vector.getZ());

				
//				vector.setX(-vector.getX());
//				vector.setY(-vector.getY());
//				 
//				 vector.setX(-4000);
////				 vector.setX(0);
//					vector.setY(5000);
////					vector.setY(2000);
////					mGeomatryHotel.getBoundingBox(true).
////					mGeomatryHotel.getS
//					
//				mGeomatryHotel.setTranslation(vector,false);
				Log.i(TAG, "::onClick:" + "mGeomatryHotel.getScale():"+mGeomatryHotel.getScale());
				
				Log.i(TAG, "::run:" + ""+vector.getX());
//				vector.setY(-vector.getZ());
//				geometry.setTranslation(vector, false);
				while(true){
				 vectorPoint= metaioSDK.getScreenCoordinatesFrom3DPosition(1, mGeomatryHotel.getBoundingBox(false).getMin());
				Log.i(TAG, "::onClick:" + "vectorPoint.getX():"+vectorPoint.getX()+"vectorPoint.getY():"+vectorPoint.getY());
				Vector2d vectorMaxPoint=metaioSDK.getScreenCoordinatesFrom3DPosition(1, mGeomatryHotel.getBoundingBox(false).getMax());
				
				Log.i(TAG, "::onClick:" + "metaioSDK.getRenderSize().getX():"+metaioSDK.getRenderSize().getX());
				Log.i(TAG, "::onClick:" + "metaioSDK.getRenderSize().getY():"+metaioSDK.getRenderSize().getY());
				
				
				Log.i(TAG, "::onClick:" + "vectorMaxPoint.getX():"+vectorMaxPoint.getX()+"vectorMaxPoint.getY():"+vectorMaxPoint.getY());
				  runOnUiThread(new Runnable() {
				        public void run() {
							Button rectbutton = (Button)findViewById(R.id.button2);
							 rectbutton.setTop((int) (vectorPoint.getY()));
							 rectbutton.setLeft((int) (vectorPoint.getX()));
							 
							 rectbutton.setBottom((int) (vectorPoint.getY()+4));
							 rectbutton.setRight((int) (vectorPoint.getX()+4));
//							 
//							 rectbutton.setTop((int) (100));
//							 rectbutton.setLeft((int) (100));
//							 
//							 rectbutton.setBottom((int) (100+2));
//							 rectbutton.setRight((int) (100+2));
				
							 
//							 rectbutton.setBottom((int) (rect.boundingRect().height *heightRatio));
//							 rectbutton.setRight((int) (rect.boundingRect().width *widhtRatio));
							 
							 rectbutton.setBackgroundColor(Color.BLUE);
							 rectbutton.setVisibility(View.VISIBLE);
							 rectbutton.invalidate();
							 

				        }
				    });

				

				for(int i=0;i<pointsList.size();i++){
					Log.i(TAG, "::onClick:" + "pointsList.get(i).boundingRect().x:"+pointsList.get(i).boundingRect().x);
					
					if(vectorMaxPoint.getX()<pointsList.get(i).boundingRect().x){
						Log.i(TAG, "::onClick:" + "done");
						return;
//						mGeomatryHotel.setScale(2.0f);
					}
					else{
						if(factorToMinus==0){
							factorToMinus=vector.getX()/2.0f;
							factorToMinusY=vector.getY()/2.0f;
							factortoMinusZ=vector.getZ()/2.0f;
						}
//						else{
//							mDifferentRatio=(vectorPoint.getY()-vectorMaxPoint.getY())-mDifferenceY;
//							mDifferenceY=(vectorPoint.getY()-vectorMaxPoint.getY());
//							float difference=(pointsList.get(i).boundingRect().width*2 - mDifferenceY);
//							Log.i(TAG, "::onClick:" + "difference:"+difference);
//							float finalTranslation = (( difference  * factorToMinusY)/mDifferentRatio);
//							
//							mDifferentRatioX=(vectorPoint.getX()-vectorMaxPoint.getX())-mDifferenceY;
//							mDifferenceX=(vectorPoint.getX()-vectorMaxPoint.getX());
//							float differenceX=(pointsList.get(i).boundingRect().height*2 - mDifferenceX);
//							float finalTranslationX = (( differenceX  * factorToMinus)/mDifferentRatioX);
//							
//							
//							Log.i(TAG, "::onClick:" + "finalTranslation:"+finalTranslation);
//							Log.i(TAG, "::onClick:" + "finalTranslationX:"+finalTranslationX);
//							vector.setX((finalTranslationX));
//							vector.setY(finalTranslation);
//							mGeomatryHotel.setTranslation(vector,true);
//							return;
//							
//						}
						
							mDifferenceY=(vectorPoint.getY()-vectorMaxPoint.getY());
							mDifferenceX=(vectorPoint.getX()-vectorMaxPoint.getX());
							
//							mDifferenceZ=(vectorPoint.get-vectorMaxPoint.getX());
						
						vector.setX((-factorToMinus));
						vector.setY(-factorToMinusY);
						vector.setZ(-(float) factortoMinusZ);
//						vector.setZ((float) vector.getZ()-10001);
						vector.setX(0);
						vector.setY(0);
						
//						vector.setY(-(vector.getY()/10.0f));
//					vector.setZ(0);
//						mGeomatryHotel.setTranslation(vector,true);
						mGeomatryHotel.setTranslation(vector,true);
//						mGeomatryHotel.setScale(400f);
						
						return;
					}
				}
				}
			}
		
				
		});
		
		
	}

}
