class Solution {
    public int minimumChanges(String s, int k) {
        int n = s.length();
        int INF = 1000000;

        // cost[i][j] = minimum changes to make s[i...j] semi-palindrome
        int[][] cost = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cost[i][j] = INF;
            }
        }

        // Precompute the cost for every substring
        for (int len = 2; len <= n; len++) {
            for (int start = 0; start + len <= n; start++) {
                int end = start + len - 1;

                // Try every proper divisor of the substring length
                for (int d = 1; d < len; d++) {
                    if (len % d != 0) {
                        continue;
                    }

                    int changes = 0;

                    // Check each group formed using divisor d
                    for (int r = 0; r < d; r++) {
                        int left = r;
                        int right = len - d + r;

                        while (left < right) {
                            if (s.charAt(start + left) != s.charAt(start + right)) {
                                changes++;
                            }

                            left += d;
                            right -= d;
                        }
                    }

                    cost[start][end] = Math.min(cost[start][end], changes);
                }
            }
        }

        // DP: dp[p][i] = minimum changes to divide first i characters
        // into exactly p semi-palindromes
        int[][] dp = new int[k + 1][n + 1];

        for (int p = 0; p <= k; p++) {
            for (int i = 0; i <= n; i++) {
                dp[p][i] = INF;
            }
        }

        dp[0][0] = 0;

        for (int p = 1; p <= k; p++) {
            for (int i = 2 * p; i <= n; i++) {
                for (int start = 2 * (p - 1); start <= i - 2; start++) {
                    if (dp[p - 1][start] == INF) {
                        continue;
                    }

                    dp[p][i] = Math.min(
                        dp[p][i],
                        dp[p - 1][start] + cost[start][i - 1]
                    );
                }
            }
        }

        return dp[k][n];
    }
}