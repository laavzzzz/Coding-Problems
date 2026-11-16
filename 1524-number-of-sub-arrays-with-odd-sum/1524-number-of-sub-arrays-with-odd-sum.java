class Solution {
    public int numOfSubarrays(int[] arr) {
        int MOD = 1_000_000_007;

        long odd = 0;
        long even = 1;
        long ans = 0;
        int prefix = 0;

        for (int num : arr) {
            prefix += num;

            if (prefix % 2 == 0) {
                ans += odd;
                even++;
            } else {
                ans += even;
                odd++;
            }

            ans %= MOD;
        }

        return (int) ans;
    }
}