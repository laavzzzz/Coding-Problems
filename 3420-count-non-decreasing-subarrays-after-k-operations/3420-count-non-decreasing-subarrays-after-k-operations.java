class Solution {
    public long countNonDecreasingSubarrays(int[] nums, int k) {
        int n = nums.length;
        long limit = k;
        
        // P[i] stores the prefix sum of nums array to compute range sums in O(1)
        long[] P = new long[n + 1];
        for (int i = 0; i < n; i++) {
            P[i + 1] = P[i] + nums[i];
        }
        
        int[] stack = new int[n];
        long[] stack_M_sum = new long[n];
        int size = 0;
        
        long totalSubarrays = 0;
        
        // Iterate backwards to compute block properties dynamically
        for (int i = n - 1; i >= 0; i--) {
            // Maintain a strictly decreasing stack of values to form prefix maximums bounds
            while (size > 0 && nums[stack[size - 1]] <= nums[i]) {
                size--;
            }
            stack[size] = i;
            
            // R is the right boundary index for the newly established maximum block
            int R = (size == 0) ? n - 1 : stack[size - 1] - 1;
            long cur_block_sum = (long) nums[i] * (R - i + 1);
            stack_M_sum[size] = (size == 0 ? 0 : stack_M_sum[size - 1]) + cur_block_sum;
            size++;
            
            // First Binary Search: Find the block where cost crosses the threshold `limit`
            int l = 0, r = size - 1;
            int k_target = -1;
            while (l <= r) {
                int m = l + (r - l) / 2;
                int R_m = (m == 0) ? n - 1 : stack[m - 1] - 1;
                long cost = stack_M_sum[size - 1] - (m == 0 ? 0L : stack_M_sum[m - 1]) - (P[R_m + 1] - P[i]);
                if (cost > limit) {
                    k_target = m;     // end of this block is invalid, look closely on leftward side (closer to i)
                    l = m + 1;
                } else {
                    r = m - 1;
                }
            }
            
            int max_valid_j = i;
            if (k_target == -1) {   // Cost up to the array's absolute end does not exceed `k`
                max_valid_j = n - 1;
            } else {
                int ans_j = stack[k_target] - 1;
                int low = stack[k_target];
                int high = (k_target == 0) ? n - 1 : stack[k_target - 1] - 1;
                
                long base = stack_M_sum[size - 1] - stack_M_sum[k_target];
                
                // Second Binary Search: Zero-in exactly on `j` transitioning boundary situated in block `k_target`
                while (low <= high) {
                    int mid = low + (high - low) / 2;
                    long M = base + (long) nums[stack[k_target]] * (mid - stack[k_target] + 1);
                    long cost = M - (P[mid + 1] - P[i]);
                    if (cost <= limit) {
                        ans_j = mid;
                        low = mid + 1;
                    } else {
                        high = mid - 1;
                    }
                }
                max_valid_j = ans_j;
            }
            
            // Collect answers for every valid `j` ending at or after `i`
            totalSubarrays += (max_valid_j - i + 1);
        }
        
        return totalSubarrays;
    }
}