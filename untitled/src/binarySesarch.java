public class binarySesarch {

    public int search(int[] nums, int target) {
           int left =0 ;
           int right= nums.length-1;

           while (left<=right) {
               int mid = left+ (right-left)/2;

               if (nums[mid]==target) {
                   return mid;
               } else if (nums[mid]<target) {
                   left=mid+1;
               } else {
                   right=mid-1;
               }
           }
           return -1;
    }

    public int searchsemisort(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        //[1,2,3,4,7,6,4]
        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return mid;
            } // []
            //can be left oriennted
             if (nums[left]<=nums[mid]) {

                if (target >= nums[left] && target < nums[mid]) {
                    right = mid - 1;   // search left
                } else {
                    left = mid + 1;    // search right
                }
            } else {
               if( target > nums[mid] && target<nums[right]) {
                   left=mid+1;
               }else {
                   right=mid-1;
               }
            }
        }

        return -1;
    }

    //nums = [4,5,6,7,0,1,2] → 0
    public static int findMinInArray(int nums[]) {
        int left=0;
        int right=nums.length-1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if(nums[mid]>nums[right]) {
                left=mid+1;
            } else {
               right=mid;
            }

        }
return nums[left];
    }


    public int Insertsearch(int[] nums, int target) {
        int left =0 ;
        int right= nums.length-1;

        while (left<=right) {
            int mid = left+ (right-left)/2;

            if (nums[mid]==target) {
                return mid;
            } else if (nums[mid]<target) {
                left=mid+1;
            } else {
                right=mid-1;
            }
        }

        return left;
    }

    private int findFirst(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        int result = -1;
//[1,3,3,4]
        while (left <= right) {
            int mid = left+ (right-left)/2;
            if(target==nums[mid]) {
                result=mid;
                right=mid-1;
            }
           else if(nums[mid]<target) {
                left=mid+1;
            } else {
                right=mid-1;
            }
        }
        return result;
    }

    public int findLast(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        int result = -1;
//[1,3,3,4]
        while (left <= right) {
            int mid = left+ (right-left)/2;

            if(target==nums[mid]) {
                result=mid;
                left=mid+1;
            }
            else if(nums[mid]<target) {
                left=mid+1;
            } else {
                right=mid-1;
            }
        }
        return result;
    }


    public int[] searchRange(int[] nums, int target) {
        return new int[]{findFirst(nums, target), findLast(nums, target)};
    }

    public int findPeakElement(int[] nums) {
        int left = 0, right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;
//[1,2,3,4,5,6]
            if(nums[mid] < nums[mid + 1]) {
                left = mid + 1;
            } else {
                right = mid;
            }

        }
        return nums[right];

    }
}
