package com.company.DSA;

import java.util.Arrays;

public final class MergeSortSolution {
    private MergeSortSolution() {
    }

    public static void MergeSort(int[] arr,int left,int right) {
        if(left >= right) return;

        int mid = left  +  (right-left)/2;

        MergeSort(arr,left,mid);
        MergeSort(arr,mid+1,right);
        merge(arr,left,mid,right);
    }

    public static void merge(int[] arr, int left, int mid, int right) {
        int n1=mid-left+1;
        int n2=right-mid;

        int[] L = new int[n1];
        int[] R = new int[n2];


        for (int i = 0; i < n1; i++) {
            L[i] = arr[left + i];
        }


        for (int j = 0; j < n2; j++) {
            R[j] = arr[mid + 1 + j];
        }

        int i = 0;
        int j = 0;
        int k = left;

        while (i<n1 && j<n2) {
            if (L[i]<=R[j]) {
             arr[k++]=L[i++];
            }else {
                arr[k++]=R[j++];
            }
        }

        while (i < n1) {
            arr[k++] = L[i++];
        }

        // Remaining right elements
        while (j < n2) {
            arr[k++] = R[j++];
        }

    }

    public static void main(String[] args) {
        int[] arr = {5, 1, 4, 2, 8, 0, -3, 9, 9};
        MergeSort(arr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));
    }
}

