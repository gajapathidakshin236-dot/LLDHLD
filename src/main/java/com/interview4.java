package com;

import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;

public class interview4 {

    List<Integer> n = new ArrayList<>();
    //1 ,2,3 -> find even adn then you have to square and sum the squares
    /**
     * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
     * elements of this stream, using the provided identity value and an
     * <a href="package-summary.html#Associativity">associative</a>
     * accumulation function, and returns the reduced value.  This is equivalent
     * to:
     * <pre>{@code
     *     T result = identity;
     *     for (T element : this stream)
     *         result = accumulator.apply(result, element)
     *     return result;
     * }</pre>
     *
     * but is not constrained to execute sequentially.
     *
     * <p>The {@code identity} value must be an identity for the accumulator
     * function. This means that for all {@code t},
     * {@code accumulator.apply(identity, t)} is equal to {@code t}.
     * The {@code accumulator} function must be an
     * <a href="package-summary.html#Associativity">associative</a> function.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">terminal
     * operation</a>.
     *
     * @apiNote Sum, min, max, average, and string concatenation are all special
     * cases of reduction. Summing a stream of numbers can be expressed as:
     *
     * <pre>{@code
     *     Integer sum = integers.reduce(0, (a, b) -> a+b);
     * }</pre>
     *
     * or:
     *
     * <pre>{@code
     *     Integer sum = integers.reduce(0, Integer::sum);
     * }</pre>
     *
     * <p>While this may seem a more roundabout way to perform an aggregation
     * compared to simply mutating a running total in a loop, reduction
     * operations parallelize more gracefully, without needing additional
     * synchronization and with greatly reduced risk of data races.
     *
     * @param identity the identity value for the accumulating function
     * @param accumulator an <a href="package-summary.html#Associativity">associative</a>,
     *                    <a href="package-summary.html#NonInterference">non-interfering</a>,
     *                    <a href="package-summary.html#Statelessness">stateless</a>
     *                    function for combining two values
     * @return the result of the reduction
     */
     Integer ans = n.stream().filter(k->k%2==0).map(k->k*k).collect(Collectors.toList()).stream().reduce(0,(a,b)->a+b);


    public static int result() {
        // Max sum subarray of size k
        int[] arr = {2, 1, 5, 1, 3, 2};
                   // l
                   //      r             r
        int k =2;
        int l=0, r=0, maxValue=Integer.MIN_VALUE;
//1-0+1
      int sum =0;
        while(l<arr.length && r < arr.length && l<r) {
            sum=+arr[r];

            if (r-l+1>=k) {
                sum=sum-arr[l];
                l++;
            }
            if(sum>=maxValue) {
                maxValue=sum;
            }

            r++;
        }

return maxValue;
    }

    public static void main(String[] args) {

    }




}
