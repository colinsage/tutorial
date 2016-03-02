package com.bolezhai.tutorial.hire;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by colin on 16/3/2.
 */
public class QuickSum {
    String name;

    public void say(){
        System.out.println("hi, " + name);
    }


    public static void main(String[] args) throws Exception {

        Random r = new Random(10);
        int n = 1000000;
        int[] array = new int[n];
        for(int i=0; i<n; i++){
            array[i] = r.nextInt(100);
        }

        long sum = 0;
        for(int i=0; i<n; i++){
            sum += array[i] ;
        }
        System.out.println(sum);
        sum(array);
    }

    public static void sum(final int[] array) throws Exception{

        long startTime = System.currentTimeMillis();

        int sum = 0;
        int numThreads = Runtime.getRuntime().availableProcessors() + 1;//使用系统核心数+1作为线程数。

        final int[] subSums = new int[numThreads ];

        final CountDownLatch latch = new CountDownLatch(numThreads -1);

        final int subArrayLen = array.length / (numThreads-1);
        final int lastLen = array.length %(numThreads-1);//余数部分

        for(int t=0;t<numThreads -1;t++) {
            final int threadNum = t;
            new Thread(new Runnable() {
                public void run() {
                    int start = threadNum *subArrayLen;
                    int end = (threadNum +1)*subArrayLen;
                    for(int i = start ; i < end; i++) {
                        subSums[threadNum] += array [i];
                    }
                    latch .countDown();
                }
            }).start();
        }

        for(int i=(numThreads-1) *subArrayLen ;i<array.length;i++) {
            subSums[numThreads-1] += array[i];
        }

        latch.await();

        for(int t=0;t<numThreads;t++) {
            sum += subSums[t];
        }

        long endTime = System.currentTimeMillis();

        System.out.println(sum + ","+ ((endTime -startTime )) +"ms");
    }

}
