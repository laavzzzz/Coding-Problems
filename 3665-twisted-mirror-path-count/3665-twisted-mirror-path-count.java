class Solution {
    public int uniquePaths(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int size = m * n;
        int MOD = 1_000_000_007;

        int[] right = new int[size];
        int[] down = new int[size];

        // -1 means the move goes outside the grid.
        java.util.Arrays.fill(right, -1);
        java.util.Arrays.fill(down, -1);

        // Precompute final landing positions after reflections.
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                int p = i * n + j;

                if (grid[i][j] == 0) {
                    right[p] = down[p] = p;
                } else {
                    // Entering a mirror from the left redirects down.
                    if (i + 1 < m) {
                        right[p] = down[p + n];
                    }

                    // Entering a mirror from above redirects right.
                    if (j + 1 < n) {
                        down[p] = right[p + 1];
                    }
                }
            }
        }

        int[] dp = new int[size];
        dp[0] = 1;

        // Count paths in topological order.
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int p = i * n + j;
                int ways = dp[p];

                if (ways == 0 || p == size - 1) {
                    continue;
                }

                // Try moving right.
                if (j + 1 < n) {
                    int target = right[p + 1];

                    if (target != -1) {
                        dp[target] = (int) (
                            (dp[target] + (long) ways) % MOD
                        );
                    }
                }

                // Try moving down.
                if (i + 1 < m) {
                    int target = down[p + n];

                    if (target != -1) {
                        dp[target] = (int) (
                            (dp[target] + (long) ways) % MOD
                        );
                    }
                }
            }
        }

        return dp[size - 1];
    }
}