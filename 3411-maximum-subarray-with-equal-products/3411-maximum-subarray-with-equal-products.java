class Solution {
    public int maxLength(int[] nums) {
        int n = nums.length;
        int ans = 0;

        for (int i = 0; i < n; i++) {
            long product = 1;
            int gcd = 0;
            long lcm = 1;

            for (int j = i; j < n; j++) {
                int num = nums[j];
                product *= num;
                gcd = gcd(gcd, num);
                lcm = lcm(lcm, num);

                // product grows monotonically; once it blows past a safe
                // bound it can never equal lcm*gcd again for this start,
                // so stop extending from this i
                if (product > (long) 1e15) break;

                if (product == lcm * gcd) {
                    ans = Math.max(ans, j - i + 1);
                }
            }
        }

        return ans;
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private long lcm(long a, long b) {
        return a / gcd((int) a, (int) b) * b;
    }
}