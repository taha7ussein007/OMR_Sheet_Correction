/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package correct;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Omar
 */
public class funciton {
    
    public Mat highDens2(BufferedImage image , BufferedImage resImg , int count){
       System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    	try{

         byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
         Mat frame = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
         frame.put(0, 0, data);
         data = ((DataBufferByte) resImg.getRaster().getDataBuffer()).getData();
         Mat nframe = new Mat(resImg.getHeight(), resImg.getWidth(), CvType.CV_8UC3);
         nframe.put(0, 0, data);
                        //frame.reshape(300, 400);
                        //System.out.println(frame.cols() +" , "+ frame.rows());
                        //Imgcodecs.imwrite("camera.jpg", frame);
                        //Mat nframe = new Mat(frame.rows() , frame.cols() , frame.type());
                        Mat tmpframe = new Mat(frame.rows() , frame.cols() , frame.type());
                        double maxVal = 250;
                        int maxR = 0 , maxC = 0;
                        double[] val = new double[3];
                        // detecting the highest density point
                            for (int i=0; i<frame.rows(); i++){
                                for(int j=0; j<frame.cols(); j++){
                                    val = frame.get(i, j);
                                    //nframe.put(i, j, val);
                                    double britness = 0.299*val[2] + 0.587*val[1] + 0.114*val[0];
                                    //sqrt( Math.pow(0.299*val[2] , 2) + Math.pow(0.587*val[1] , 2) + Math.pow(0.114*val[0] , 2))
                                    if (maxVal < britness){
                                        maxVal = britness;
                                        maxR = i; maxC = j;
                                    }
                                }
                            }
                            double prevBrit = 0;
                            double avrRng = 15; // catching including the points less than the highest one with 15
                            // start catching the highest density area
                            //catching(maxR , maxC , frame , maxVal , avrRng , prevBrit , tmpframe , nframe);
                            Imgproc.circle(nframe, new Point(maxC , maxR) , 3, new Scalar(0 , 0 , 255) , 3);
                            // getting boundries
                            /*int minRow = getMinRow(frame, maxC, maxR, tmpframe, maxVal, avrRng);
                            int maxRow = getMaxRow(frame, maxC, maxR, tmpframe, maxVal, avrRng);
                            int minCol = tmpframe.cols()-1 , maxCol = 0;
                            for (int i=minRow; i<=maxRow; i++){
                                for (int j=0; j<tmpframe.cols(); j++){
                                    val = tmpframe.get(i, j);
                                    double britness = 0.299*val[2] + 0.587*val[1] + 0.114*val[0];
                                    if (britness >= maxVal-avrRng && britness <= maxVal+avrRng){
                                        maxCol = max(maxCol , j);
                                        minCol = min(minCol , j);
                                    }
                                }
                            }
                            val[0] = 0; val[1] = 0; val[2] = 200;
                            
                            /*for (int i=minRow-3; i<minRow; i++){
                                for (int j = minCol-3; j<=maxCol+3; j++){
                                    if (i < nframe.rows() && i >= 0 && j < nframe.cols() && j >= 0)
                                        nframe.put(i, j, val);
                                    if (maxRow+(minRow-i) < nframe.rows() && maxRow+(minRow-i) >= 0 && j < nframe.cols() && j >= 0)
                                        nframe.put(maxRow+(minRow-i), j, val);                                        
                                }
                            }
                            
                            for (int j=minCol-3; j<minCol; j++){
                                for (int i=minRow-3; i<= maxRow+3; i++){
                                    if (i < nframe.rows() && i >= 0 && j < nframe.cols() && j >= 0)
                                        nframe.put(i, j, val);
                                    if (i < nframe.rows() && i >= 0 && maxCol+(minCol-j) < nframe.cols() && maxCol+(minCol-j) >= 0)
                                        nframe.put(i, maxCol+(minCol-j), val);
                                }
                            }
                            Point st = new Point(minCol , minRow);
                            Point ed = new Point(maxCol , maxRow);
                            Rect rec = new Rect(st, ed);
                            Mat fr = new Mat(frame, rec);
                            Imgproc.rectangle(nframe , st , ed , new Scalar(255 , 0 , 0) , 2);
                            
                            
                            tmpframe.put(minRow, minCol, val);
                            tmpframe.put(maxRow, maxCol, val);*/
                            //System.out.println(maxRow);
                            //System.out.println(maxR + " , " + maxC);
                            //Imgcodecs.imwrite("camera3.jpg", tmpframe);
                            //System.out.println(tmpframe.get(0, 0)[1]);
                            //Imgcodecs.imwrite("densPoint"+count+".jpg", nframe);
                            return nframe;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return new Mat();
        }
   }
   
   private static int getMinRow(Mat frame , int maxC , int maxR , Mat tmpframe , double maxVal , double avrRng ){
        int minRow = frame.rows()-1;
        double[] val = new double[3];
                            for(int j=maxC; j>=0; j--){
                                for (int i=maxR; i>=0; i--){
                                    val = tmpframe.get(i, maxC);
                                    double britness = 0.299*val[2] + 0.587*val[1] + 0.114*val[0];
                                    //val[0] = 0; val[1] = 0; val[2] = 200;
                                    //System.out.println(britness);
                                    if (britness >= maxVal-avrRng && britness <= maxVal+avrRng){
                                        minRow = min(minRow , i);
                                    }
                                    else{
                                        break;
                                    }
                                }
                            }
                            for(int j=maxC; j<tmpframe.cols(); j++){
                                for (int i=maxR; i>=0; i--){
                                    val = tmpframe.get(i, maxC);
                                    double britness = 0.299*val[2] + 0.587*val[1] + 0.114*val[0];
                                    //val[0] = 0; val[1] = 0; val[2] = 200;
                                    //System.out.println(britness);
                                    if (britness >= maxVal-avrRng && britness <= maxVal+avrRng){
                                        minRow = min(minRow , i);
                                    }
                                    else{
                                        break;
                                    }
                                }
                            }
                            return minRow;
    }
    
    private static int getMaxRow(Mat frame , int maxC , int maxR , Mat tmpframe , double maxVal , double avrRng ){
        int maxRow = 0;
        double[] val = new double[3];
                            for(int j=maxC; j>=0; j--){
                                for (int i=maxR; i<tmpframe.rows(); i++){
                                    val = tmpframe.get(i, maxC);
                                    double britness = 0.299*val[2] + 0.587*val[1] + 0.114*val[0];
                                    //val[0] = 0; val[1] = 0; val[2] = 200;
                                    //System.out.println(britness);
                                    if (britness >= maxVal-avrRng && britness <= maxVal+avrRng){
                                        maxRow = max(maxRow , i);
                                    }
                                    else{
                                        break;
                                    }
                                }
                            }
                            for(int j=maxC; j<tmpframe.cols(); j++){
                                for (int i=maxR; i<tmpframe.rows(); i++){
                                    val = tmpframe.get(i, maxC);
                                    double britness = 0.299*val[2] + 0.587*val[1] + 0.114*val[0];
                                    //val[0] = 0; val[1] = 0; val[2] = 200;
                                    //System.out.println(britness);
                                    if (britness >= maxVal-avrRng && britness <= maxVal+avrRng){
                                        maxRow = max(maxRow , i);
                                    }
                                    else{
                                        break;
                                    }
                                }
                            }
                            return maxRow;
    }
    
    private static void catching(int maxR , int maxC , Mat frame , double maxVal , double avrRng , double prevBrit , Mat tmpframe , Mat nframe){
        double[] val = new double[3];
                            for (int i=maxR; i<frame.rows(); i++){
                                for(int j=maxC; j<frame.cols(); j++){
                                    val = frame.get(i, j);
                                    double britness = 0.299*val[2] + 0.587*val[1] + 0.114*val[0];
                                    //val[0] = 0; val[1] = 0; val[2] = 200;
                                    //System.out.println(britness);
                                    if (britness >= maxVal-avrRng && britness <= maxVal+avrRng){
                                        prevBrit = britness;
                                        tmpframe.put(i, j, val);
                                        //nframe.put(i, j, val);
                                    }
                                    else{
                                        if (prevBrit >= maxVal-avrRng && prevBrit <= maxVal+avrRng ){
                                            //nframe.put(i, j, val);
                                        }
                                        prevBrit = britness;
                                        break;
                                    }
                                }
                            }
                            /*for (int i=maxR; i>=0; i--){
                                for(int j=maxC; j>=0; j--){
                                    val = frame.get(i, j);
                                    double britness = 0.299*val[2] + 0.587*val[1] + 0.114*val[0];
                                    //val[0] = 0; val[1] = 0; val[2] = 200;
                                    //System.out.println(britness);
                                    if (britness >= maxVal-avrRng && britness <= maxVal+avrRng){
                                        prevBrit = britness;
                                        tmpframe.put(i, j, val);
                                        nframe.put(i, j, val);
                                    }
                                    else{
                                        if (prevBrit >= maxVal-avrRng && prevBrit <= maxVal+avrRng ){
                                            nframe.put(i, j, val);
                                        }
                                        prevBrit = britness;
                                        break;
                                    }
                                }
                            }
                            
                            for (int i=maxR; i<frame.rows(); i++){
                                for(int j=maxC; j>=0; j--){
                                    val = frame.get(i, j);
                                    double britness = 0.299*val[2] + 0.587*val[1] + 0.114*val[0];
                                    //val[0] = 0; val[1] = 0; val[2] = 200;
                                    //System.out.println(britness);
                                    if (britness >= maxVal-avrRng && britness <= maxVal+avrRng){
                                        prevBrit = britness;
                                        tmpframe.put(i, j, val);
                                        nframe.put(i, j, val);
                                    }
                                    else{
                                        if (prevBrit >= maxVal-avrRng && prevBrit <= maxVal+avrRng ){
                                            nframe.put(i, j, val);
                                        }
                                        prevBrit = britness;
                                        break;
                                    }
                                }
                            }
                            for (int i=maxR; i>=0; i--){
                                for(int j=maxC; j<frame.cols(); j++){
                                    val = frame.get(i, j);
                                    double britness = 0.299*val[2] + 0.587*val[1] + 0.114*val[0];
                                    //val[0] = 0; val[1] = 0; val[2] = 200;
                                    //System.out.println(britness);
                                    if (britness >= maxVal-avrRng && britness <= maxVal+avrRng){
                                        prevBrit = britness;
                                        tmpframe.put(i, j, val);
                                        //nframe.put(i, j, val);
                                    }
                                    else{
                                        if (prevBrit >= maxVal-avrRng && prevBrit <= maxVal+avrRng ){
                                            //nframe.put(i, j, val);
                                        }
                                        prevBrit = britness;
                                        break;
                                    }
                                }
                            }*/
                            //System.out.println(maxVal);
    }
    
    static public boolean toGray( String imgname , int count ) { 
   
      try {
         System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
         File input = new File(imgname);
         BufferedImage image = ImageIO.read(input);	

         byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
         Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
         mat.put(0, 0, data);

         Mat mat1 = new Mat(image.getHeight(),image.getWidth(),CvType.CV_8UC1);
         Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);

         byte[] data1 = new byte[mat1.rows() * mat1.cols() * (int)(mat1.elemSize())];
         mat1.get(0, 0, data1);
         BufferedImage image1 = new BufferedImage(mat1.cols(),mat1.rows(), BufferedImage.TYPE_BYTE_GRAY);
         image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);

         File ouptut = new File("grayscale"+(count)+".jpg");
         ImageIO.write(image1, "jpg", ouptut);
         return true;
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
         return false;
      }
   }
    
    static public boolean toAnotherRange( String imgname , int count ) { 
   
      try {
         System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
         File input = new File(imgname);
         BufferedImage image = ImageIO.read(input);	

         byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
         Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
         mat.put(0, 0, data);

         Mat mat1 = new Mat(image.getHeight(),image.getWidth(),CvType.CV_8UC3);
         Imgproc.threshold(mat, mat1, 100, 255, Imgproc.THRESH_BINARY);

         byte[] data1 = new byte[mat1.rows() * mat1.cols() * (int)(mat1.elemSize())];
         mat1.get(0, 0, data1);
         BufferedImage image1 = new BufferedImage(mat1.cols(),mat1.rows(), BufferedImage.TYPE_BYTE_GRAY);
         image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);

         File ouptut = new File("grayscale"+(count)+".jpg");
         ImageIO.write(image1, "jpg", ouptut);
         return true;
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
         return false;
      }
   }
    
    static public int[] Correction(String img_path , int numOfQues , int[] rightAnswers) throws IOException{
        String exam_path = getExam(img_path);
        System.out.println(exam_path);
        if ("".equals(exam_path))
            return new int[1];
        int[] answers = getAnswers(numOfQues, exam_path);
        int[] results = new int[3];
        for (int i=0; i<numOfQues; i++){
            System.out.println(answers[i]+" , "+rightAnswers[i]);
            if (answers[i] == 0)
                results[2]++;
            else if (answers[i] == rightAnswers[i])
                results[0]++;
            else
                results[1]++;
        }
        return results;
    }
    
    private static boolean valid(Mat mat){
        double[] val00 = {0.0,0.0,0.0}; double[] val01 = {50,0.0,0.0};
        //double[] val10 = {100,100,100}; double[] val11 = {100,100,100};
        Imgproc.circle(mat, new Point(0 , 0), 10, new Scalar(0 , 0 , 255) , 5);
        Imgcodecs.imwrite("tst.jpg" , mat);
        
        //for(int i=0; i<10; i++)
            //System.out.println(mat.get(0, i)[0]+" "+mat.get(0, i)[1]+" "+mat.get(0, i)[2]);
        
        /*System.out.println((mat.get(0, 0)[0]+" "+mat.get(0, 0)[1]+" "+mat.get(0, 0)[2])
        +"\n"+(mat.get(0, mat.cols()-1)[0]+" "+mat.get(0, mat.cols()-1)[1]+" "+mat.get(0, mat.cols()-1)[2])
                +"\n"+(mat.get(mat.rows()-1, 0)[0]+" "+mat.get(mat.rows()-1, 0)[1]+" "+mat.get(mat.rows()-1, 0)[2])
        +"\n"+(mat.get(mat.rows()-1, mat.cols()-1)[0]+" "+mat.get(mat.rows()-1, mat.cols()-1)[1]+" "+mat.get(mat.rows()-1, mat.cols()-1)[2]));
        
        System.out.println((mat.get(0, 0)[0] == val00[0] && mat.get(0, 0)[1] == val00[1] && mat.get(0, 0)[2] == val00[2])
        +"\n"+(mat.get(0, mat.cols()-1)[0] == val01[0] && mat.get(0, mat.cols()-1)[1] == val01[1] && mat.get(0, mat.cols()-1)[2] == val01[2])
                +"\n"+(mat.get(mat.rows()-1, 0)[0] == val10[0] && mat.get(mat.rows()-1, 0)[1] == val10[1] && mat.get(mat.rows()-1, 0)[2] == val10[2])
        +"\n"+(mat.get(mat.rows()-1, mat.cols()-1)[0] == val11[0] && mat.get(mat.rows()-1, mat.cols()-1)[1] == val11[1] && mat.get(mat.rows()-1, mat.cols()-1)[2] == val11[2]));*/
        boolean b1 = (mat.get(0, 0)[0] <= 5 && mat.get(0, 0)[1] <= 5 && mat.get(0, 0)[2] <= 5);
        boolean b2 = (mat.get(0, mat.cols()-1)[0] <= 5 && mat.get(0, mat.cols()-1)[1] <= 5 && mat.get(0, mat.cols()-1)[2] <= 5);
        //boolean b3 = (mat.get(mat.rows()-1, 0)[0] == mat.get(mat.rows()-1, 0)[1] && mat.get(mat.rows()-1, 0)[0] == mat.get(mat.rows()-1, 0)[2] && mat.get(mat.rows()-1, 0)[0] <= val10[0]+10 && mat.get(mat.rows()-1, 0)[0] >= val10[0]-10);
        //boolean b4 = (mat.get(mat.rows()-1, mat.cols()-1)[0] == mat.get(mat.rows()-1, mat.cols()-1)[1] && mat.get(mat.rows()-1, mat.cols()-1)[0] == mat.get(mat.rows()-1, mat.cols()-1)[2] && mat.get(mat.rows()-1, mat.cols()-1)[0] <= val11[0]+10 && mat.get(mat.rows()-1, mat.cols()-1)[0] >= val11[0]-10);
        
        //System.out.println(b1+"\n"+b2+"\n"+b3+"\n"+b4);
        if (b1&&b2)
            System.out.println("valid");
        return b1&&b2;//&&b3&&b4;
    }
    
    private static String getExam(String img_path) throws IOException{
        
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        File input = new File(img_path);
        BufferedImage image = ImageIO.read(input);	

        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        Mat newMat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        mat.put(0, 0, data);
        newMat.put(0, 0, data);
        
        if (valid(mat)){
            return img_path;
        }
        
        Point tl = new Point() , tr = new Point() , bl = new Point() , br = new Point();
        Point[] all = new Point[4];
        /*System.out.println((mat.get(0, 0)[0]+" "+mat.get(0, 0)[1]+" "+mat.get(0, 0)[2])
        +"\n"+(mat.get(0, mat.cols()-1)[0]+" "+mat.get(0, mat.cols()-1)[1]+" "+mat.get(0, mat.cols()-1)[2])
                +"\n"+(mat.get(mat.rows()-1, 0)[0]+" "+mat.get(mat.rows()-1, 0)[1]+" "+mat.get(mat.rows()-1, 0)[2])
        +"\n"+(mat.get(mat.rows()-1, mat.cols()-1)[0]+" "+mat.get(mat.rows()-1, mat.cols()-1)[1]+" "+mat.get(mat.rows()-1, mat.cols()-1)[2]));*/
        //System.out.println("hi");
        int stop = (int)Math.ceil(0.0057*mat.rows())*2;int tmp = 0;
        int progress = 0; boolean found = false;
        int minX = mat.cols() , minYX = minX , minYX2 = mat.rows() , maxX = 0;
        
        int rng = 15;
        
        for (int i=0; i<mat.rows(); i++){
            for (int j=0; j<mat.cols(); j++){
                if (mat.get(i, j)[0] <= rng && mat.get(i, j)[1] <= rng && mat.get(i, j)[2] <= rng){
                    found = true;
                    //tmp++;
//                    System.out.println("I found : "+j+" "+i);
//                    double[] val = {0 , 0 , 255};
//                    newMat.put(i, j, val);
//                    if ((i==0 || mat.get(i-1 , j)[0] > rng && mat.get(i-1 , j)[1] > rng && mat.get(i-1 , j)[2] > rng)
//                            && (j==0 || mat.get(i , j-1)[0] > rng && mat.get(i , j-1)[1] > rng && mat.get(i , j-1)[2] > rng)
//                            && j <= minX){
//                        minYX = j==minX ? minYX : mat.cols();
//                        if (i < minYX){
//                            System.out.println("first point = "+i+" "+j);
//                            tl.x = j; tl.y = i;
//                            minX = j;
//                            minYX = i;
//                        }
//                    }
//                    if ((i==0 || mat.get(i-1 , j)[0] > rng && mat.get(i-1 , j)[1] > rng && mat.get(i-1 , j)[2] > rng)
//                            && (j == mat.cols()-1 ||mat.get(i , j+1)[0] > rng && mat.get(i , j+1)[1] > rng && mat.get(i , j+1)[2] > rng)
//                            && j >= maxX){
//                        minYX2 = j == maxX ? minYX2 : mat.rows();
//                        if (i < minYX2){
//                            System.out.println("second point = "+i+" "+j);
//                            tr.x = j; tr.y = i;
//                            minYX2 = i;
//                            maxX = j;
//                        }
//                    }
//                    /*if (j <= minX){
//                        minYX = j==minX ? minYX : mat.cols();
//                        if (i < minYX){
//                            System.out.println("first point = "+i+" "+j);
//                            tl.x = j; tl.y = i;
//                            minX = j;
//                            minYX = i;
//                        }
//                    }
//                    if (j >= maxX){
//                        minYX2 = j == maxX ? minYX2 : mat.rows();
//                        if (i < minYX2){
//                            System.out.println("second point = "+i+" "+j);
//                            tr.x = j; tr.y = i;
//                            minYX2 = i;
//                            maxX = j;
//                        }
//                    }*/
                }
            }
            if (found){
                progress++;
                if (progress > stop)
                    break;
                found = false;
            }
        }
        
        //System.out.println("tmp = "+tmp);
        System.out.println(tl.x+" "+tl.y+" , "+tr.x+" "+tr.y);
        System.out.println(progress + " stop = "+stop/2);
        if (progress < stop/2)
            return "";        

        //System.out.println(tl.x+" "+tl.y);
        //System.out.println(tr.x+" "+tr.y);
        //System.out.println(bl.x+" "+bl.y);
        double[] val = {255 , 0 , 0};
        if (Math.abs(tr.y-tl.y) <= 3){
            tl.y = Math.min(tr.y , tl.y);
            tr.y = tl.y;
        }
        double wm = (tr.y - tl.y)/(tr.x-tl.x);
        double hm = (-1)/wm;
        if (wm == 0)
            hm = 0;
        System.out.println(wm+" "+hm);
        double x,y=tl.y,y0,x0=tl.x;
        
        
        Mat tst = new Mat((int)Math.round((tr.x-tl.x)/0.707), (int)Math.round(tr.x-tl.x), CvType.CV_8UC3);
        int tmpy = (int)tl.y;
        /*for (int col = (int)tl.x; col<= tr.x; col++){
            newMat.put(tmpy+=wm , col , val);
        }*/
        
        int i=0,j=0;
        if (wm < 1){
            for (y0=tl.y; /*y0<=(tr.x-tl.x)/0.70744*/i<tst.rows(); y0++,i++){
                y = y0;
                for (x = x0; /*x <= tr.x*/j<tst.cols(); x++,j++){
                    val = mat.get((int) Math.round(y), (int)Math.round(x));
                    tst.put(i , j , val);
                    y += wm;
                    val[0] = 0; val[1] = 0; val[2] = 255;
                    newMat.put((int) Math.round(y) , (int)Math.round(x) , val);
                    //System.out.println(y+" "+x);
                    //tmp++;
                }
                j = 0;
                x0 += hm == 0 ? 0 : 1/hm;
            }
        }
        //System.out.println(tmp+" x0 = "+x0);
        Imgcodecs.imwrite("tst.jpg" , tst);
        Imgcodecs.imwrite("newTry.jpg" , newMat);
        
        
        return "tst.jpg";
    }
    
    public static String generateSuitedExam(String img_path) throws IOException{
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        File input = new File(img_path);
        BufferedImage image = ImageIO.read(input);	

        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        mat.put(0, 0, data);
        Imgproc.rectangle(mat, new Point(0 , 0), new Point(40 , 40) , new Scalar(0 , 0 , 0) , 40);
        Imgproc.rectangle(mat, new Point(mat.cols()-40 , 0), new Point(mat.cols() , 40) , new Scalar(0 , 0 , 0) , 40);
        double[] val = {0 , 0 , 0};
        //mat.put(0 , 0 , val);
        //val[0] = 50; val[1] = 50; val[2] = 50;
        //mat.put(0, mat.cols()-1, val);
        //val[0] = 100; val[1] = 100; val[2] = 100;
        //mat.put(mat.rows()-1, 0, val);
        //mat.put(mat.rows()-1, mat.cols()-1, val);
        //Imgproc.circle(mat, new Point(0 , 0), 5, new Scalar(255 , 0 , 0) , 15);
        //Imgproc.line(mat, new Point(0 , 0), new Point(10 , 0), new Scalar(255 , 0 , 0));
        String part1 = img_path.substring(0 , img_path.lastIndexOf("."));
        String part2 = img_path.substring(img_path.lastIndexOf("."));
        String res = part1+"Edited"+part2;
        Imgcodecs.imwrite(res , mat);
        return res;
    }
    
    static public int[] getAnswers(int numOfQues , String exam_path){
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            //toAnotherRange("C:\\Users\\Omar\\Documents\\NetBeansProjects\\swarSection\\Task3Answersheet.jpg" , 1);
            File input = new File(exam_path);
            BufferedImage image = ImageIO.read(input);
            byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            Mat frame = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
            frame.put(0, 0, data);
            
            //Imgproc.circle(frame, new Point(504+100+100 , 2247+240), 35, new Scalar(0 , 0 , 255) , 3);
            double[] val = new double[3];
            val[0] = 0; val[1] = 0; val[2] = 255;
            int max , count = 0 , phase = 0;
            int[] answers = new int[numOfQues];
            int topTend = (int)Math.round(0.32035*frame.rows()) , leftTend = (int)Math.round(0.10157*frame.cols());
            int choicesTend = (int)Math.round(0.02015*frame.cols());
            int choiceRadus = (int)Math.round(0.00541*frame.rows());
            int choiceTends = (int)Math.round(0.02013*frame.cols());
            int quisTends = (int)Math.round(0.03452*frame.rows());
            int forHandle = 0;
            max = (int)Math.round(0.05*(4*choiceRadus*choiceRadus));
            boolean ed = false;
            
            for(int top = 0; top < 2; top++){
                for(int left = 0; left < 5; left++){
                    for(int quis=0; quis<10; quis++){
                        if (phase + quis >= numOfQues){
                            ed = true;
                            break;
                        }
                        int answer = -1;
                        forHandle = quis >= 5 ? choiceRadus/2 : 0;
                        for(int chis=0; chis<5; chis++){
                            Imgproc.circle(frame, new Point(leftTend+chis*choicesTend , topTend+quis*quisTends-forHandle), choiceRadus, new Scalar(0 , 0 , 255));
                            for (int row = topTend+quis*quisTends-choiceRadus-forHandle; row < topTend+quis*quisTends+choiceRadus-forHandle; row++){
                                for (int col = leftTend+chis*choiceTends-choiceRadus; col < leftTend+chis*choiceTends+choiceRadus; col++){
                                    //System.out.println(row+" "+col);
                                    if (frame.get(row, col)[2] < 180){
                                        count++;
                                    }
                                }
                            }
                            
                            System.out.print(count + " ");
                            if (max <= count){
                                max = count;
                                answer = chis;
                            }
                            count = 0;
                        }
                        System.out.println("--> "+max+" , "+answer);
                        answers[phase+quis] = answer+1;
                        //System.out.println("\n"+max);
                        max = (int)Math.round(0.05*(4*choiceRadus*choiceRadus));
                        //System.out.println("Answer of question #"+(phase+quis+1)+" : "+answers[phase+quis]);
                    }
                    leftTend += 0.18178*frame.cols();
                    if (left == 3)
                        leftTend -= choiceTends;
                    phase += 10;
                }
                if (ed)
                    break;
                topTend += (int)Math.round(0.34288*frame.rows());
                leftTend = (int)Math.round(0.10157*frame.cols());
            }            
            
            
            //Imgproc.circle(frame, new Point(leftTend+4*100+502 , topTend+0*240.85+2405), 37, new Scalar(0 , 0 , 255) , 3);
            //topTend += 2405;
            //System.out.println(frame.get(topTend, leftTend+5*100+38)[2]);
            //System.out.println(count);
            Imgcodecs.imwrite("CorrectionTry.jpg", frame);
            return answers;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(funciton.class.getName()).log(Level.SEVERE, null, ex);
            return new int[100];
        }
    }
    
}
