import java.util.*;

class Solution {
    public int maximumSaleItems(int[][] items, int budget) {
        int n = items.length;
        int[] factor = new int[n];
        int[] price = new int[n];
        for (int i = 0; i < n; i++) {
            factor[i] = items[i][0];
            price[i] = items[i][1];
        }

        // cnt[i] = number of other items j whose factor is divisible by factor[i]
        // (buying item i grants one free copy of each such j)
        int[] cnt = new int[n];
        int minPrice = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            minPrice = Math.min(minPrice, price[i]);
            for (int j = 0; j < n; j++) {
                if (i != j && factor[j] % factor[i] == 0) {
                    cnt[i]++;
                }
            }
        }

        // 0/1 knapsack: choosing a SET of distinct items to buy exactly one
        // copy of each (to trigger their free bonuses). value = 1 (the
        // purchased copy itself) + cnt[i] (its free copies); weight = price[i]
        int[] dp = new int[budget + 1];
        for (int i = 0; i < n; i++) {
            int w = price[i];
            int v = 1 + cnt[i];
            if (w > budget) continue;
            for (int c = budget; c >= w; c--) {
                dp[c] = Math.max(dp[c], dp[c - w] + v);
            }
        }
        // dp[c] is now the best value achievable spending at most c on the
        // knapsack selection (monotonic non-decreasing in c)

        // Any leftover budget after the knapsack selection is best spent
        // buying more copies of the globally cheapest item, each extra
        // copy adding 1 to the total count with no further free bonus.
        int ans = 0;
        for (int spent = 0; spent <= budget; spent++) {
            int leftover = budget - spent;
            int extra = leftover / minPrice;
            ans = Math.max(ans, dp[spent] + extra);
        }

        return ans;
    }
}