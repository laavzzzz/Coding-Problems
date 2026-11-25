class Solution {
    private static final long MOD1 = 1_000_000_007L;
    private static final long MOD2 = 998_244_353L;
    private static final long BASE1 = 131;
    private static final long BASE2 = 137;

    private long[] prefix1, prefix2, pow1, pow2;
    private int n;

    public int smallestUniqueSubarray(int[] nums) {
        n = nums.length;
        prefix1 = new long[n + 1];
        prefix2 = new long[n + 1];
        pow1 = new long[n + 1];
        pow2 = new long[n + 1];
        pow1[0] = 1;
        pow2[0] = 1;

        for (int i = 0; i < n; i++) {
            prefix1[i + 1] = (prefix1[i] * BASE1 + nums[i]) % MOD1;
            prefix2[i + 1] = (prefix2[i] * BASE2 + nums[i]) % MOD2;
            pow1[i + 1] = (pow1[i] * BASE1) % MOD1;
            pow2[i + 1] = (pow2[i] * BASE2) % MOD2;
        }

        int lo = 1, hi = n;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (hasUniqueWindow(mid)) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    // true if some subarray of length L appears exactly once among all
    // subarrays of length L
    private boolean hasUniqueWindow(int L) {
        int count = n - L + 1;
        long[] hashes = new long[count];
        for (int i = 0; i < count; i++) {
            long h1 = (prefix1[i + L] - prefix1[i] * pow1[L] % MOD1 + MOD1 * MOD1) % MOD1;
            long h2 = (prefix2[i + L] - prefix2[i] * pow2[L] % MOD2 + MOD2 * MOD2) % MOD2;
            hashes[i] = h1 * MOD2 + h2; // combine two hashes into one key, collision-resistant
        }

        Map<Long, Integer> freq = new HashMap<>();
        for (long h : hashes) {
            freq.merge(h, 1, Integer::sum);
        }
        for (int f : freq.values()) {
            if (f == 1) return true;
        }
        return false;
    }
}