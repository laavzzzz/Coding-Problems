import java.util.*;

class Solution {
    public int[] assignTasks(int[] servers, int[] tasks) {
        PriorityQueue<int[]> free = new PriorityQueue<>(
            (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]
        );

        PriorityQueue<long[]> busy = new PriorityQueue<>(
            (a, b) -> a[0] != b[0] ? Long.compare(a[0], b[0])
                                  : Long.compare(a[1], b[1])
        );

        for (int i = 0; i < servers.length; i++)
            free.offer(new int[]{servers[i], i});

        int[] ans = new int[tasks.length];
        long time = 0;

        for (int i = 0; i < tasks.length; i++) {
            time = Math.max(time, i);

            while (!busy.isEmpty() && busy.peek()[0] <= time) {
                long[] s = busy.poll();
                int id = (int) s[1];
                free.offer(new int[]{servers[id], id});
            }

            if (free.isEmpty()) {
                time = busy.peek()[0];

                while (!busy.isEmpty() && busy.peek()[0] <= time) {
                    long[] s = busy.poll();
                    int id = (int) s[1];
                    free.offer(new int[]{servers[id], id});
                }
            }

            int[] s = free.poll();
            ans[i] = s[1];

            busy.offer(new long[]{time + tasks[i], s[1]});
        }

        return ans;
    }
}