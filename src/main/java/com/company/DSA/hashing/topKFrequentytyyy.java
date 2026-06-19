package com.company.DSA.hashing;

import java.util.HashMap;

public class topKFrequentytyyy {


    public int[] topKFrequent(int[] nums, int k) {

        HashMap<Integer, Integer> map = new HashMap<>();

        // count frequency
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        // unique elements
        int n = map.size();

        int[] arr = new int[n];

        int index = 0;

        for (int key : map.keySet()) {
            arr[index++] = key;
        }



        // custom merge sort
        mergeSort(arr, 0, n - 1, map);

        // answer
        int[] result = new int[k];

        for (int i = 0; i < k; i++) {
            result[i] = arr[i];
        }

        return result;
    }

    public void mergeSort(int[] arr, int left, int right,
                          HashMap<Integer, Integer> map) {
        if(left>=right){
            return;
        }
        int mid = left +(right-left)/2;
        mergeSort(arr,left,mid,map);
        mergeSort(arr,mid+1,right,map);
        merge(arr, left, mid, right, map);

    }

    public void merge(int[] arr, int left, int mid, int right,
                      HashMap<Integer, Integer> map) {

        int n1=  mid-left+1;
        int n2 = right-mid;

        int L[] = new int [n1];
        int R[] = new int [n2];



        for (int i=0;i<n1;i++) {
            L[i]=arr[left+i];//0,1,2,3,4
        }

        for (int i=0;i<n1;i++) {
            R[i]=arr[mid+1+i];
        }


        int i = 0;
        int j = 0;
        int k = left;

        // descending frequency
        while (i < n1 && j < n2) {

            if(map.get(L[i]) >= map.get(R[j])) {
               arr[k++]=L[i++];
            } else {
                arr[k++]=R[j++];
            }
        }


        while (i < n1) {
            arr[k++] = L[i++];
        }

        while (j < n2) {
            arr[k++] = R[j++];
        }



    }
}
