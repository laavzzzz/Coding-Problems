import java.util.BitSet;

class Solution {
    public int minCost(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        BitSet[][] dp = new BitSet[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = new BitSet(1024);
            }
        }

        dp[0][0].set(grid[0][0]);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                int value = grid[i][j];

                if (i > 0) {
                    for (int x = dp[i - 1][j].nextSetBit(0);
                         x >= 0;
                         x = dp[i - 1][j].nextSetBit(x + 1)) {
                        dp[i][j].set(x ^ value);
                    }
                }

                if (j > 0) {
                    for (int x = dp[i][j - 1].nextSetBit(0);
                         x >= 0;
                         x = dp[i][j - 1].nextSetBit(x + 1)) {
                        dp[i][j].set(x ^ value);
                    }
                }
            }
        }

        return dp[m - 1][n - 1].nextSetBit(0);
    }
}