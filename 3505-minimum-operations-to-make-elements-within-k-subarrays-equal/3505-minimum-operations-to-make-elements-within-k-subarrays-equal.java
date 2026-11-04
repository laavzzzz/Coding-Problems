import java.util.*;

class Solution {
    TreeMap<Integer, Integer> lo = new TreeMap<>(), hi = new TreeMap<>();
    long sumLo = 0, sumHi = 0;
    int sizeLo = 0, sizeHi = 0;

    void add(int v) {
        if (sizeLo == 0 || v <= lo.lastKey()) {
            lo.merge(v, 1, Integer::sum);
            sumLo += v;
            sizeLo++;
        } else {
            hi.merge(v, 1, Integer::sum);
            sumHi += v;
            sizeHi++;
        }
        balance();
    }

    void remove(int v) {
        if (lo.containsKey(v)) {
            dec(lo, v);
            sumLo -= v;
            sizeLo--;
        } else {
            dec(hi, v);
            sumHi -= v;
            sizeHi--;
        }
        balance();
    }

    void dec(TreeMap<Integer, Integer> map, int v) {
        if (map.get(v) == 1) map.remove(v);
        else map.put(v, map.get(v) - 1);
    }

    void balance() {
        while (sizeLo > sizeHi + 1) {
            int v = lo.lastKey();
            dec(lo, v);
            sumLo -= v;
            sizeLo--;
            hi.merge(v, 1, Integer::sum);
            sumHi += v;
            sizeHi++;
        }
        while (sizeLo < sizeHi) {
            int v = hi.firstKey();
            dec(hi, v);
            sumHi -= v;
            sizeHi--;
            lo.merge(v, 1, Integer::sum);
            sumLo += v;
            sizeLo++;
        }
    }

    long cost() {
        int m = lo.lastKey();
        return (long) m * sizeLo - sumLo + sumHi - (long) m * sizeHi;
    }

    public long minOperations(int[] nums, int x, int k) {
        int n = nums.length;
        long[] cost = new long[n - x + 1];

        lo.clear();
        hi.clear();
        sumLo = sumHi = 0;
        sizeLo = sizeHi = 0;

        for (int i = 0; i < n; i++) {
            add(nums[i]);

            if (i >= x) remove(nums[i - x]);

            if (i >= x - 1)
                cost[i - x + 1] = cost();
        }

        long INF = Long.MAX_VALUE / 4;
        long[][] dp = new long[k + 1][n + 1];

        for (int j = 1; j <= k; j++)
            Arrays.fill(dp[j], INF);

        for (int j = 1; j <= k; j++) {
            for (int i = 1; i <= n; i++) {
                dp[j][i] = dp[j][i - 1];

                if (i >= x && dp[j - 1][i - x] != INF) {
                    dp[j][i] = Math.min(dp[j][i],
                        dp[j - 1][i - x] + cost[i - x]);
                }
            }
        }

        return dp[k][n];
    }
}