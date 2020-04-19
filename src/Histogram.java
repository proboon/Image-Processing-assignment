
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Meadow
 */
public class Histogram {

    public static void main(String[] args) {

        try {
            FileInputStream myInputFile = new FileInputStream("yoda.raw");
            int value;
            int k = 0;//count arr0
            int m = 1;//count total number of each value into array
            int count = 1;//count columns
            int count1 = 0;//count total number of a value
            int runSum1 = 0;//for increment use and get the last value of runSum(array)
            int[] arr0 = new int[myInputFile.available()];
            int[] noOfPixel = new int[256];//num of pixels
            int[] runSum = new int[256];//run sum
            double[] normalized = new double[256];//normalized(run sum / 7626)
            double[] mul255 = new double[256];//multiply 7(double)
            int[] map = new int[256];//histogram-equalized value

            //store value into 1D array(int)
            while ((value = myInputFile.read()) != -1) {
                arr0[k++] = value;
            }

            //store total number of each value into array
            for (int i = 0; i < noOfPixel.length; i++) {
                m = 1;
                for (int j = 0; j < arr0.length; j++) {
                    if (arr0[j] == i) {
                        noOfPixel[i] = m++;
                    }
                }
            }

            //run sum
            for (int i = 0; i < noOfPixel.length; i++) {
                runSum[i] = runSum1 += noOfPixel[i];
            }

            //normalized
            DecimalFormat df = new DecimalFormat("0.00");
            for (int i = 0; i < runSum.length; i++) {
                normalized[i] = runSum[i] / 1.0 / runSum1;
            }

            //multiply7
            DecimalFormat df1 = new DecimalFormat("0");
            for (int i = 0; i < normalized.length; i++) {
                mul255[i] = normalized[i] * 255.0;
                mul255[i] = Math.round(mul255[i]);
            }

            //map no of pixels with multiply255
            for (int j = 0; j < mul255.length; j++) {
                map[(int) (mul255[j])] += noOfPixel[j];
            }

            FileOutputStream myOutputFile1 = new FileOutputStream("histogram.raw");

            for (int i = 0; i < arr0.length; i++) {
                myOutputFile1.write((int) mul255[arr0[i]]);
            }
            myOutputFile1.close();

         

            myInputFile.close();
        } catch (IOException ex) {
            System.out.println("File error!");
        }
    }
}
