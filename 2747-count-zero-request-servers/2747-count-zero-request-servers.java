import java.util.*;

class Solution {
    public int[] countServers(int n, int[][] logs, int x, int[] queries) {
        Arrays.sort(logs, (a, b) -> a[1] - b[1]);

        int m = queries.length;
        int[][] q = new int[m][2];
        for (int i = 0; i < m; i++) {
            q[i][0] = queries[i];
            q[i][1] = i;
        }
        Arrays.sort(q, (a, b) -> a[0] - b[0]);

        int[] freq = new int[n + 1], ans = new int[m];
        int left = 0, right = 0, active = 0;

        for (int[] query : q) {
            int start = query[0] - x, end = query[0];

            while (right < logs.length && logs[right][1] <= end) {
                if (freq[logs[right][0]]++ == 0) active++;
                right++;
            }

            while (left < right && logs[left][1] < start) {
                if (--freq[logs[left][0]] == 0) active--;
                left++;
            }

            ans[query[1]] = n - active;
        }
        return ans;
    }
}