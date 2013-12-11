package com.IPOL.opencvsample;

import java.io.BufferedWriter;
import java.io.File;
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
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
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
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.IPOL.opencvsample.items.DrawnContours;

public class OpenCVActivity extends Activity implements CvCameraViewListener2, OnClickListener {

	@SuppressWarnings("unused")
	private static final String TAG = "com.IPOL.opencvsample.OpenCVActivity";
	private CameraBridgeViewBase mOpenCvCameraView;
	private boolean isTriangle=false;
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				Log.i(TAG, "OpenCV loaded successfully");
				mOpenCvCameraView.enableView();
			}
				break;
			default: {
				super.onManagerConnected(status);
			}
				break;
			}
		}
	};
	private int mTotalSquare=0;
	private TextView mTextView;
	private long mSeconds;
	private Mat rgbImage;
	private Vector <DrawnContours> mDrawnContours=new Vector<DrawnContours>();
	private ArrayList<MatOfPoint> contours;
	private Button mButton;
	private boolean mLogging=false;
	  List<MatOfPoint> squares = new ArrayList<MatOfPoint>();
	  List<MatOfPoint2f> squares2f=new ArrayList<MatOfPoint2f>();
	private double maxArea=0;
	private MatOfPoint maxTempPoint;
	private MatOfPoint secondMaxPoint;
	private VideoCapture mCamera;
	private Vector<RotatedRect> pointsList=new Vector<RotatedRect>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_open_cv);
		if (!OpenCVLoader.initDebug()) {
	        // Handle initialization error
			Log.i(TAG, "::onCreate:" + "intialise error :"+ "Handle initialization error");
	    }
//		mCamera = new VideoCapture(Highgui.CV_CAP_ANDROID );
		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);
//		CvType.
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

		mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.enableView();
        mOpenCvCameraView.setMaxFrameSize(200, 200);
        mTextView = (TextView)findViewById(R.id.textView1);
        mButton = (Button)findViewById(R.id.button1);
        mButton.setOnClickListener(this);
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        
//        VideoCapture capture=new VideoCapture(1);
//        capture.gr
        
//        capture.set(Highgui.cv_)
//        CV_CAP_PROP_FORMAT
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
				Log.i(TAG, "::onTouchEvent:" + "");
			break;
		}
		return super.onTouchEvent(event);
	}
	
	private Mat debugSquares(Mat sourceImage){
		RotatedRect maxRect;
		Log.i(TAG, "::debugSquares:" + "squares2f.size():"+squares2f.size());
		appendLog("::debugSquares:" + "squares2f.size():"+squares2f.size());
		for(int i=0;i<squares2f.size();i++){
			Log.i(TAG, "::debugSquares:" + "squares2f.get(i);"+squares2f.get(i));
			appendLog("squares2f.get(i):"+squares2f.get(i));
			RotatedRect minRect=Imgproc.minAreaRect(squares2f.get(i));
//			minRect.si
//			Core.rec
			Point points[]=new Point[4];
			
//			Imgproc.drawContours(image, contours, contourIdx, color)
			
//		Rect rect=	Imgproc.boundingRect(squares.get(i));
//		Core.rectangle(sourceImage, new Point(rect.x, rect.y) , new Point(rect.width, rect.height), new Scalar(0,125,255));
			
			minRect.points(points);
			for(int j=0;j<4;j++){
//				Core.line(sourceImage, points[j], points[(j+1)%4], new Scalar(125,125,255));
				Core.line(sourceImage, points[j], points[(j+1)%4], new Scalar(125,125,255), 1);
				appendLog("points[j]:"+points[j]);
//				Core.line(sourceImage, points[j], points[(j+1)%4], new Scalar(0,125,255), 1, 8, 1);
			}
////			Core.rectangle(sourceImage, points[0], points[points.length-1], new Scalar(125,125,255));
			
		}
		return sourceImage;
	}
	/*
	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		// TODO Auto-generated method stub
		Mat gray=new Mat();
		Imgproc.cvtColor(inputFrame.rgba(), gray, Imgproc.COLOR_BGR2GRAY);
	
		Mat edges=new Mat();
		Imgproc.Canny(gray, edges, 50, 5);	
        Mat hierarchy=new Mat();
        contours = new ArrayList<MatOfPoint>();
        contours.clear();
		Imgproc.findContours(gray, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		
		  MatOfPoint2f approxCurve = new MatOfPoint2f();
	        mTotalSquare=0;
//	         rgbImage=new Mat(output.size(),CvType.CV_8UC3);
//	        Imgproc.cvtColor(output, rgbImage, Imgproc.COLOR_GRAY2RGB);
	        rgbImage=inputFrame.rgba();
	        mDrawnContours.clear();
	        mTotalSquare=0;
//	        output=Imgproc.cvt
	        for(int i=0;i< contours.size();i++){
	        	MatOfPoint tempContour=contours.get(i);
	        	MatOfPoint2f newMat = new MatOfPoint2f( tempContour.toArray() );
	        	int contourSize = (int)tempContour.total();
	            Imgproc.approxPolyDP(newMat, approxCurve, contourSize*0.02, true);
	            MatOfPoint points=new MatOfPoint(approxCurve.toArray());
	            
	            if((Math.abs(Imgproc.contourArea(tempContour))<100) || !Imgproc.isContourConvex(points)){
	            	continue;
	            }
	            else if(points.toArray().length >= 4 && points.toArray().length <= 6){
	            	int vtc = points.toArray().length;
	            	Vector<Double> cosList=new Vector<Double>();
	            	for (int j = 2; j < vtc+1; j++){
	            		
	            		cosList.add(angle(points.toArray()[j%vtc], points.toArray()[j-2], points.toArray()[j-1]));
	            		
	            	}	
	            	   double mincos = getMin(cosList);
	                   double maxcos = getMax(cosList);
	                   if (vtc == 4 && mincos >= -0.1 && maxcos <= 0.3)
	                   {
	                	   mTotalSquare++;
	                	  
	                		Imgproc.drawContours(rgbImage, contours, i, new Scalar(0,0,255));
	                		DrawnContours contours2=new DrawnContours();
	                		contours2.setIndex(i);
	                		mDrawnContours.add(contours2);
	                   }

	            }
	        }
	            
	        Log.i(TAG, "::onCameraFrame:" + "");
		Log.i(TAG, "::onCameraFrame:" + "Total Sqare:"+mTotalSquare);
//		Imgproc.Canny(gray, edges, threshold1, threshold2, apertureSize, L2gradient)
		return rgbImage;
	}

*/
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
	
	double angle( Point pt1, Point pt2, Point pt0 )
		{
		    double dx1 = pt1.x - pt0.x;
		    double dy1 = pt1.y - pt0.y;
		    double dx2 = pt2.x - pt0.x;
		    double dy2 = pt2.y - pt0.y;
		    return (dx1*dx2 + dy1*dy2)/Math.sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
		}
	private double getMin(Vector<Double> list){
		double temp=0;
		if(list.size()>0){
			temp=list.get(0);
			for(int i=1;i<list.size();i++){
				if(temp<list.get(i))
					temp=list.get(i);
			}
		}
		return temp;
		
	}
	private double getMax(Vector<Double> list){
		double temp=0;
		if(list.size()>0){
			temp=list.get(0);
			for(int i=1;i<list.size();i++){
				if(temp>list.get(i))
					temp=list.get(i);
			}
		}
		return temp;
	}
	private Mat drawContours(Mat source){
		for(int i=0;i<mDrawnContours.size();i++){
			Imgproc.drawContours(source, contours, mDrawnContours.get(i).getIndex(), new Scalar(0,0,255));
		}
		return source;
	}
	
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
	
/*
	@Override
	public  Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		// TODO Auto-generated method stub
		long milliSecond=new Date().getTime();
		long second = (milliSecond / 1000) % 60;
		
		if(Math.abs(mSeconds-second)<1 ){
			return drawContours(inputFrame.rgba());
		}
		Log.i(TAG, "::onCameraFrame:" + "second:"+second);
		mSeconds=second;
		
		Mat  output= getGray(inputFrame.rgba(),inputFrame.rgba());
		Imgproc.medianBlur(output, output, 5);
		Imgproc.erode(output, output, new Mat());
		Imgproc.dilate(output, output, new Mat());
		 Mat edges = new Mat();
		Imgproc.Canny(output, output, 50, 150);
//		Vector<MatOfPoint> vector=new Vector<MatOfPoint>();
//		Imgproc.findContours(output, points, output, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		 contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        contours.clear();
        Imgproc.findContours(output, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Log.i(TAG, "::onCameraFrame:" + "contours.size():"+contours.size());
        Log.i(TAG, "::onCameraFrame:" + "output.size():"+output.size());
        Log.i(TAG, "::onCameraFrame:" + "output.rows():"+output.rows());
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        mTotalSquare=0;
//         rgbImage=new Mat(output.size(),CvType.CV_8UC3);
//        Imgproc.cvtColor(output, rgbImage, Imgproc.COLOR_GRAY2RGB);
        rgbImage=inputFrame.rgba();
        mDrawnContours.clear();
        appendLog("contours.size():"+contours.size());
//        output=Imgproc.cvt
        for(int i=0;i< contours.size();i++){
        	MatOfPoint tempContour=contours.get(i);
        	MatOfPoint2f newMat = new MatOfPoint2f( tempContour.toArray() );
        	int contourSize = (int)tempContour.total();
            Imgproc.approxPolyDP(newMat, approxCurve, contourSize*0.20, true);
            MatOfPoint points=new MatOfPoint(approxCurve.toArray());
            int tempCounter=mDrawnContours.size();
            
//            check and draw it
            
            double area1=Math.abs(Imgproc.contourArea(tempContour));
            RotatedRect rect1=Imgproc.minAreaRect(newMat);
            double area2=rect1.size.width*rect1.size.height;
            appendLog("Math.abs(area1-area2);"+Math.abs(area1-area2));
            if(Math.abs(area1-area2)<100){
            mTotalSquare++;
              	  
            Imgproc.drawContours(rgbImage, contours, i, new Scalar(0,255,255));
            DrawnContours contours2=new DrawnContours();
            contours2.setIndex(i);
            mDrawnContours.add(contours2);
            Log.i(TAG, "::onCameraFrame:" + "found");
            appendLog("found IN 2:");
           
            }
//            if((Math.abs(Imgproc.contourArea(tempContour))<100) || !Imgproc.isContourConvex(points)){
            if((Math.abs(Imgproc.contourArea(tempContour))<100) || !Imgproc.isContourConvex(points)){
            	
            	Log.i(TAG, "::onCameraFrame:" + " too small");
            	appendLog("Too small Math.abs(Imgproc.contourArea(tempContour))<100 :"+ (Math.abs(Imgproc.contourArea(tempContour))<100));
            	appendLog("!Imgproc.isContourConvex(points):"+!Imgproc.isContourConvex(points));
            	    	continue;
            }
             if(points.toArray().length >= 4 && points.toArray().length <= 6){
//            	   else if(points.toArray().length >= 4 && points.toArray().length <= 6){
            	int vtc = points.toArray().length;
            	Vector<Double> cosList=new Vector<Double>();
            	for (int j = 2; j < vtc+1; j++){
            		
            		cosList.add(angle(points.toArray()[j%vtc], points.toArray()[j-2], points.toArray()[j-1]));
            		
            	}	
            	   double mincos = getMin(cosList);
                   double maxcos = getMax(cosList);
                   Log.i(TAG, "::onCameraFrame:" + "mincos:"+mincos+"maxcos:"+maxcos);
                   if (vtc == 4 && mincos >= -0.1 && maxcos <= 0.3)
                   {
                	   mTotalSquare++;
                	  
                		Imgproc.drawContours(rgbImage, contours, i, new Scalar(0,0,255));
                		DrawnContours contours2=new DrawnContours();
                		contours2.setIndex(i);
                		mDrawnContours.add(contours2);
                		Log.i(TAG, "::onCameraFrame:" + "found");
                		appendLog("found:");
                		appendLog("contours.get(i).total():"+contours.get(i).total());
                		
                   }
                   else{
                	   Log.i(TAG, "::onCameraFrame:" +" not found " +"mincos:"+mincos+"maxcos:"+maxcos);
                	   appendLog("not found 1"+"mincos:"+mincos+"maxcos:"+maxcos);
                	   appendLog("contours.get(i).total():"+contours.get(i).total());
                   }

            }
//         /    else if(tempCounter==mDrawnContours.size()){
//            	 double area1=Math.abs(Imgproc.contourArea(tempContour));
//                 RotatedRect rect1=Imgproc.minAreaRect(newMat);
//                 double area2=rect1.size.width*rect1.size.height;
//                 appendLog("Math.abs(area1-area2);"+Math.abs(area1-area2));
//                 if(Math.abs(area1-area2)<10){
//                 mTotalSquare++;
//                   	  
//                 Imgproc.drawContours(rgbImage, contours, i, new Scalar(0,255,255));
//                 DrawnContours contours2=new DrawnContours();
//                 contours2.setIndex(i);
//                 mDrawnContours.add(contours2);
//                 Log.i(TAG, "::onCameraFrame:" + "found");
//                 appendLog("found IN 2:");
//                 
             
//                 }
             
             }
            
//            Log.i(TAG, "::onCameraFrame:" + "points.toArray().length:"+points.toArray().length);
//            working but not so accureately
//            if( (points.toArray().length==4) && Math.abs(Imgproc.contourArea(approxCurve)) >1000 && Imgproc.isContourConvex(points)){
//            	
//            	 double maxCosine = 0;
//            	 for( int j = 2; j < 5; j++ )
//                 {
//                     double cosine = Math.abs(angle(points.toArray()[j%4], points.toArray()[j-2], points.toArray()[j-1]));
//                     maxCosine = Math.max(maxCosine, cosine);
//                     Log.i(TAG, "::onCameraFrame:" + "maxCosine:"+maxCosine);
//                     if( maxCosine < 0.3 ){
//                    	 Log.i(TAG, "::onCameraFrame:" + "found");
//                    	 mTotalSquare++;
//                     	Imgproc.drawContours(rgbImage, contours, i, new Scalar(0,0,255));
//                     	Log.i(TAG, "::onCameraFrame:" + "contours.get(i):"+contours.get(i));
//                     }
//                    	 
//                 }
//            
           
            


            	
//            }
//        Log.i(TAG, "::onCameraFrame:" + "mTotalSquare:"+mTotalSquare);
//        Mat destMat = new Mat();
//        
//        Size newSize=new Size( output.height()/2.0,output.width()/2.0);
//        
//        /=new Mat(output.rows()/2, output.cols()/2, output.type());
//        Imgproc.resize(output, destMat, destMat.size());
        
//        Imgproc.resize(output, destMat, new Size(destMat.rows(),destMat.cols()), 1.0, 1.0, Imgproc.INTER_AREA);
//        Imgproc.resize(output, destMat, new Size(output.cols()/2, output.rows()/2), 0, 0, Imgproc.INTER_CUBIC);
        
        //last one commented
//        Imgproc.resize(output, destMat, newSize, 0, 0, Imgproc.INTER_CUBIC);
//        output.setTo(new Scalar(0.5));
//        output.copyTo(output, destMat);
        
//        Log.i(TAG, "::onCameraFrame:" + "output.size();"+output.size());
        
//        Imgproc.resize(output, destMat, new Size(), 0.5, 0.5, Imgproc.INTER_AREA);
//        Imgproc.resize(output, destMat, new Size(),0,0,Imgproc.INTER_NEAREST);
//        Log.i(TAG, "::onCameraFrame:" + "destMat.size():"+destMat.size());
//        Core.convertScaleAbs(output, destMat, 1/10, 0);
//        Imgproc.resize(output, destMat, newSize);
//        Imgproc.resize(output, output, output.new Size(1,1));
//        output.re
		return rgbImage;
	}
	
	
	*/
	
	
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
         
         squares.clear();
         squares2f.clear();
         Log.v(TAG, "Gray0 Matrix! : " + gray0.total() );
         Log.v(TAG, "Gray Matrix! : " + gray.total() );
         
         
         
//         List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
       
         
         // find squares in every color plane of the image
         mDrawnContours.clear();
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
                          squares.add(temp);
                          squares2f.add(approx);
                          appendLog("approx:"+approx);
                          
                          DrawnContours drawnContour=new DrawnContours();
                          drawnContour.setIndex(i);
                          mDrawnContours.add(drawnContour);
                          double area = Math.abs(Imgproc.contourArea(temp));
//                          if(maxArea<area) {
//                        	  maxArea=area;
//                        	  secondMaxPoint=maxTempPoint;
//                        	  maxTempPoint=temp;
//                        	  
//                          }
//                          Rect rect=	Imgproc.boundingRect(temp);
                        	
//                    		Core.rectangle(src, rect.tl() , new Point(rect.width, rect.height), new Scalar(0,125,255));
                          if(!isTriangle) {
                          Imgproc.drawContours(src, contours, i, new Scalar(0,0,255));
                          }
                          else{
                        	  
                        	  RotatedRect minRect=Imgproc.minAreaRect(approx);
                        	  if(minRect.size.equals(src.size())){
                        		  continue;
                        	  }
                        	  else if((Math.abs(minRect.size.height-src.size().height)<10)){
                        		  continue;
                        	  }
                        	  else if((Math.abs(minRect.size.width-src.size().width)<10)){
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
                 Log.v(TAG, "Squares Added to List! : " + squares.size());
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


	//	 public void removeInnerRectangles(){
//		 for(int i=0;i<pointsList.size();i++){
//			 pointsList.get
//		 }
//	 }
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
	 
	 public Mat drawRectangles(Mat src) {
		 Log.i(TAG, "::drawRectangles:" + "pointsList.size():"+pointsList.size());
		 for(int i=0;i<pointsList.size();i++){
			RotatedRect rect= pointsList.get(i); 
			 
			  Point[] points=new Point[4];
			  rect.points(points);
				for(int j=0;j<4;j++){
//  				Core.line(sourceImage, points[j], points[(j+1)%4], new Scalar(125,125,255));
  				Core.line(src, points[j], points[(j+1)%4], new Scalar(125,125,255), 10);
  				appendLog("points[j]:"+points[j]);
//  				Core.line(sourceImage, points[j], points[(j+1)%4], new Scalar(0,125,255), 1, 8, 1);
  			}
		 }
		 return src;
	 }
	 // This is back up method to use other algoritham
	 public Mat EdgeDetect1(Mat src) {
		 
		 Mat pyr=new Mat();
		 Mat timing=new Mat();
		 
		 Imgproc.pyrDown(src, pyr, new Size(src.width()/2, src.height()/2));
		 Imgproc.pyrUp(pyr, timing,src.size());
		 
         Mat blurred = new Mat();
//         src.copyTo(blurred);
         timing.copyTo(blurred);
         Log.v(TAG, "Blurred Matrix! : " + blurred.total() );
         
         Imgproc.medianBlur(src, blurred, 9);
         Log.v(TAG, "Median Blur Done!");
 
         Mat gray0 = new Mat(blurred.size(), blurred.type());
         Imgproc.cvtColor(gray0, gray0, Imgproc.COLOR_RGB2GRAY);
         Mat gray = new Mat();

         Log.v(TAG, "Gray0 Matrix! : " + gray0.total() );
         Log.v(TAG, "Gray Matrix! : " + gray.total() );
         
         List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
         List<MatOfPoint> squares = new ArrayList<MatOfPoint>();
         
         // find squares in every color plane of the image
     for (int c = 0; c < 3; c++)
     {
                 Log.v(TAG, "Mix Channels Started! : " + gray0.total());
         int ch[] = {c, 0};
         MatOfInt fromto = new MatOfInt(ch);
         List<Mat> blurredlist = new ArrayList<Mat>();
         List<Mat> graylist = new ArrayList<Mat>();
         blurredlist.add(0, blurred);
         graylist.add(0, gray0);
         Core.mixChannels(blurredlist, graylist, fromto);
         gray0 = graylist.get(0);
                 Log.v(TAG, "Mix Channels Done! : " + gray0.total() );
      // try several threshold levels
         int threshold_level = 2;
         for (int l = 0; l < threshold_level; l++)
         {
             // Use Canny instead of zero threshold level!
             // Canny helps to catch squares with gradient shading
                 Log.v(TAG,"Threshold Level: " + l);
                 
             if (l >=0)
             {
                 Imgproc.Canny(gray0, gray, 20, 30); // 

                 // Dilate helps to remove potential holes between edge segments
                 Imgproc.dilate(gray, gray, Mat.ones(new Size(3,3),0));
                 
             }
             else
             {
                     int thresh = (l+1) * 255 / threshold_level;
                     Imgproc.threshold(gray0, gray, thresh, 255, Imgproc.THRESH_TOZERO);
             }

                 Log.v(TAG, "Canny (or Thresholding) Done!");
                 Log.v(TAG, "Gray Matrix (after)! : " + gray.total() );
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
         
                 if( approx.rows()==4 && Math.abs(Imgproc.contourArea(approx)) > 1000 && Imgproc.isContourConvex(mMOP))
                 {
                         
                         Log.v(TAG,"Passes Conditions! " + approx.size().toString());
                         double maxcosine = 0;
                         Point[] list = approx.toArray();
                         
                          for (int j = 2; j < 5; j++)
                          {
                                  double cosine =Math.abs(angle(list[j%4], list[j-2], list[j-1]));
                                  maxcosine = Math.max(maxcosine, cosine);
                  }
                          
//                          if( maxcosine < 0.3 ) 
                          {
                                  MatOfPoint temp = new MatOfPoint();
                                  approx.convertTo(temp, CvType.CV_32S);
                          squares.add(temp);
                          Imgproc.drawContours(src, contours, i, new Scalar(125,255,125));
                      }
                 }

             }
                 Log.v(TAG, "Squares Added to List! : " + squares.size());
         }
     }
     return src;
	 }

     
	//Josef code
	private void findSqaures(Mat sourceImage){
		Vector<Point> sqares;
		Mat pyr,timing ,gry =new Mat();
		pyr=new Mat();
		sourceImage.copyTo(pyr);
		timing=new Mat();
		sourceImage.copyTo(timing);
		int thresh = 50, N = 11;
		List<Mat> graylist=new ArrayList<Mat>();
		List<Mat> blurredlist=new ArrayList<Mat>();
		Imgproc.pyrDown(sourceImage, pyr,new Size(sourceImage.cols()/2.0, sourceImage.rows()/2.0));
		Imgproc.pyrUp(pyr, timing,sourceImage.size());
//		Vector<Point> contours=new Vector<Point>();
		blurredlist.add(0,pyr);
		graylist.add(0,timing);
//		grayO.add(0,timing);
		for(int c=0;c<3;c++){
			int ch[]={c,0};
			
			MatOfInt fromto = new MatOfInt(ch);
			Core.mixChannels(blurredlist, graylist, fromto);
//			Core.mixChannels(src, dst, fromTo)
			for(int i=0;i<N;i++){
				Mat output=graylist.get(0);
				if(i==0){
					
					Imgproc.Canny(output, gry, 5, thresh);
					Imgproc.dilate(gry, gry, new Mat(), new Point(-1,-1), 1);
				}
				 else {	
//					 output = output >= (i+1)*255/N;
		           }
//				sourceImage=gry;
				contours=new ArrayList<MatOfPoint>();
				Imgproc.findContours(gry, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
				 MatOfPoint2f approxCurve = new MatOfPoint2f();
				 mDrawnContours.clear();
				 Log.i(TAG, "::findSqaures:" + "contours.size():"+contours.size());
				for(int j=0;i<contours.size();j++){
					MatOfPoint tempContour=contours.get(i);
			    	MatOfPoint2f newMat = new MatOfPoint2f( tempContour.toArray() );
		        	int contourSize = (int)tempContour.total();
		    
					Imgproc.approxPolyDP(newMat, approxCurve, contourSize*0.02, true);
					MatOfPoint points=new MatOfPoint(approxCurve.toArray());
//				      if( approx.size() == 4 && fabs(contourArea(cv::Mat(approx))) > 1000 && cv::isContourConvex(cv::Mat(approx))) {
				    if(points.toArray().length==4 && (Math.abs(approxCurve.total())>1000) && Imgproc.isContourConvex(points)){
				    	double maxCosine=0;
				    	int k;
				    	for( k=2;k<5;k++){
				    		double cosine=Math.abs(angle(points.toArray()[k%4], points.toArray()[k-2], points.toArray()[k-1]));
				    		if(maxCosine>cosine){
				    			maxCosine=cosine;
				    		}
				    	}
				    	Log.i(TAG, "::findSqaures:" + "maxCosine:"+maxCosine);
				    	if(maxCosine<0.3){
				    		DrawnContours drawnContours=new DrawnContours();
					    	drawnContours.setIndex(k);
					    	mDrawnContours.add(drawnContours);
			
				    	}
				    			    	
				    }
					
				}
				Log.i(TAG, "::findSqaures:" + "mDrawnContours.size():"+mDrawnContours.size());
			}
		}
			
		
//		Core.mixChannels(src, dst, fromTo)
		
	}
//	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
//		
//		long milliSecond=new Date().getTime();
//		long second = (milliSecond / 1000) % 60;
//		
//		if(Math.abs(mSeconds-second)<1 ){
//			return drawContours(inputFrame.rgba());
//		}
//		Mat mGray= inputFrame.gray();
//		Mat output=inputFrame.rgba();
//		rgbImage=inputFrame.rgba();
//		Imgproc.cvtColor(inputFrame.rgba(), mGray, Imgproc.COLOR_BGR2GRAY);
//
//	    // doing a gaussian blur prevents getting a lot of false hits
////	    Imgproc.GaussianBlur(mGray, mGray, new Size(5, 5), 2, 2);
//	  
//	    int iCannyLowerThreshold = 35;
//	    int iCannyUpperThreshold = 75;
//	     Mat bw=new Mat();   
//	    Imgproc.Canny(mGray, bw, iCannyLowerThreshold, iCannyUpperThreshold);
//	    contours=new ArrayList<MatOfPoint>();
//	    Imgproc.findContours(bw.clone(), contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//	    MatOfPoint2f approxCurve = new MatOfPoint2f();
//	    mDrawnContours.clear();
////	    rgbImage=new Mat(mGray.size(),CvType.CV_8UC3);
//	    for(int i=0;i<contours.size();i++){
//	    	MatOfPoint tempContour=contours.get(i);
//	    	MatOfPoint2f newMat = new MatOfPoint2f( tempContour.toArray() );
//        	int contourSize = (int)tempContour.total();
//            Imgproc.approxPolyDP(newMat, approxCurve, contourSize*0.30, true);
//            MatOfPoint points=new MatOfPoint(approxCurve.toArray());
//            if((Math.abs(Imgproc.contourArea(tempContour))<100) || !Imgproc.isContourConvex(points)){
//            	continue;
//            }
//            else if(points.toArray().length >= 4 && points.toArray().length <= 6){
//            	int vtc = points.toArray().length;
//            	Vector<Double> cosList=new Vector<Double>();
//            	for (int j = 2; j < vtc+1; j++){
//            		
//            		cosList.add(angle(points.toArray()[j%vtc], points.toArray()[j-2], points.toArray()[j-1]));
//            		
//            	}	
//            	   double mincos = getMin(cosList);
//                   double maxcos = getMax(cosList);
//                   if (vtc == 4 && mincos >= -0.1 && maxcos <= 0.3)
//                   {
//                	   mTotalSquare++;
//                	  
//                		Imgproc.drawContours(rgbImage, contours, i, new Scalar(0,255,255));
//                		DrawnContours contours2=new DrawnContours();
//                		contours2.setIndex(i);
//                		mDrawnContours.add(contours2);
//                   }
//
//            }
//	    }
            

	    

//		return rgbImage;
//	}
	public Mat getGray(Mat input, Mat output){
		Log.i(TAG, "::getGray:" + "output.channels():"+output.channels());
//		Imgproc.
		int channels=input.channels();
		if(channels==4){
			Imgproc.cvtColor(input, output, Imgproc.COLOR_BGRA2GRAY, channels);
		}
		else if(channels==3){
			Imgproc.cvtColor(input, output, Imgproc.COLOR_BGR2GRAY);
		}
		else if(channels==1){
			output=input;
		}
		return output;
	}

	@Override
	public void onResume() {
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this,
				mLoaderCallback);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mLogging=!mLogging;
		isTriangle=!isTriangle;
	}
//	josef code
	
	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		long milliSecond=new Date().getTime();
		long second = (milliSecond / 1000) % 60;
		Log.i(TAG, "::onCameraFrame:" + "type():"+inputFrame.rgba().type());
		
		if(Math.abs(mSeconds-second)>1 ){
			Log.i(TAG, "::onCameraFrame:" + " 1");
//			return drawContours(inputFrame.rgba());
//			if(!isTriangle)
//			 EdgeDetect(inputFrame.rgba());
//				return debugSquares(inputFrame.rgba());
			 mSeconds=second;
				return EdgeDetect(inputFrame.rgba());
			
			
		}
//		else{
//			findSqaures(inputFrame.rgba());
//		}
		
//
//		
//		return drawContours(inputFrame.rgba());
		// TODO Auto-generated method stub
//		return EdgeDetect(inputFrame.rgba());
//		return EdgeDetect1(inputFrame.rgba());
//		return EdgeDetect(inputFrame.rgba());
		
		//temparary block
//		if(isTriangle){
//			return EdgeDetect(inputFrame.rgba());
////			 return debugSquares(inputFrame.rgba());
//		}
//		else{
//			return EdgeDetect(inputFrame.rgba());
//		}
		
		return drawRectangles(inputFrame.rgba());
//		 return debugSquares(inputFrame.rgba());
		
//		return find
	}
	

}
