package com.bolezhai.tutorial.hire;

/**
 * Created by colin on 16/3/2.
 */
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class QuickSum2 extends RecursiveTask<Long> {

    private static final ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime
            .getRuntime().availableProcessors());

    private static int ARRAY_SIZE = 1000000000;
    private static long BATCH_COUNT = 1200000;

    int low;
    int high;
    int[] array;

    QuickSum2(int[] arr, int lo, int hi) {
        array = arr;
        low = lo;
        high = hi;
    }

    private static int[] mockArray(int count) {
        Random r = new Random();
        int[] srcArray = new int[count];
        for (int i = 0; i < count; i++) {
            srcArray[i] = r.nextInt(1000);
        }
        return srcArray;
    }

    /**
     * 简单求和，用来做结果的比较
     */
    public static long simpleSum(int[] srcArray) {
        long sum = 0;
        for (int i : srcArray) {
            sum += i;
        }

        return sum;
    }

    @Override
    protected Long compute() {
        if (high - low <= BATCH_COUNT) {
            long sum = 0;
            for (int i = low; i < high; i++) {
                sum += array[i];
            }
            return sum;
        } else {
            int mid = low + (high - low) / 2;
            QuickSum2 left = new QuickSum2(array, low, mid);
            QuickSum2 right = new QuickSum2(array, mid, high);
            left.fork();
            right.fork();
            return left.join() + right.join();
        }
    }

    public static void main(String[] args) {
      /*
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: ARRAY_SIZE BATCH_COUNT");
        }
*/
        // ARRAY_SIZE = Integer.parseInt(args[0]);
        // BATCH_COUNT = Integer.parseInt(args[1]);

        ARRAY_SIZE = 100000000;
        BATCH_COUNT = 10000000;

        int[] srcArray = mockArray(ARRAY_SIZE);

        long start1 = System.currentTimeMillis();
        long sum1 = simpleSum(srcArray);
        long cost1 = System.currentTimeMillis() - start1;
        System.out.println(sum1 + ", " + cost1 + "ms");

        long start = System.currentTimeMillis();
        long sum2 = forkJoinPool.invoke(new QuickSum2(srcArray, 0, srcArray.length));
        long cost = System.currentTimeMillis() - start;
        System.out.println(sum2 + ", " + cost + "ms");

    }
}