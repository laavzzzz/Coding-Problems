
class Solution {
    public int minOperations(int[] nums) {
        int n = nums.length;

        boolean sortedRotation = true;
        boolean reverseRotation = true;

        int first = nums[0];

        for (int i = 0; i < n; i++) {
            if (nums[i] != (first + i) % n) {
                sortedRotation = false;
            }

            if (nums[i] != (first - i + n) % n) {
                reverseRotation = false;
            }
        }

        int ans = Integer.MAX_VALUE;

        // Case 1: nums follows the sorted cyclic order
        if (sortedRotation) {
            int k = (n - first) % n;
            ans = Math.min(ans, Math.min(k, n - k + 2));
        }

        // Case 2: nums follows the reverse cyclic order
        if (reverseRotation) {
            int k = (first + 1) % n;
            ans = Math.min(ans, 1 + Math.min(k, n - k));
        }

        return ans == Integer.MAX_VALUE ? -1 : ans;
    }
}
