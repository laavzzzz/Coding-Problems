import java.util.*;

class Solution {

    private List<Integer>[] graph;
    private int[] tin, tout, depth;
    private int[][] up;
    private int LOG;
    private FenwickXor fenwick;
    private int[] charMask;

    public List<Boolean> palindromePath(
            int n, int[][] edges, String s, String[] queries) {

        // Step 1: Build the tree
        graph = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];

            graph[u].add(v);
            graph[v].add(u);
        }

        // Step 2: Initialize binary lifting
        LOG = 1;

        while ((1 << LOG) <= n) {
            LOG++;
        }

        tin = new int[n];
        tout = new int[n];
        depth = new int[n];
        up = new int[n][LOG];

        // Step 3: Convert characters into bitmasks
        charMask = new int[n];

        for (int i = 0; i < n; i++) {
            charMask[i] = 1 << (s.charAt(i) - 'a');
        }

        // Step 4: Iterative DFS to calculate Euler Tour order,
        // depths, parents, and binary lifting ancestors.
        int[] parent = new int[n];
        int[] order = new int[n];
        int[] stack = new int[n];

        Arrays.fill(parent, -2);

        int top = 0;
        int timer = 0;

        stack[top++] = 0;
        parent[0] = 0;

        while (top > 0) {
            int u = stack[--top];

            tin[u] = timer;
            order[timer++] = u;

            for (int v : graph[u]) {
                if (parent[v] != -2) {
                    continue;
                }

                parent[v] = u;
                depth[v] = depth[u] + 1;

                up[v][0] = u;

                for (int j = 1; j < LOG; j++) {
                    up[v][j] = up[up[v][j - 1]][j - 1];
                }

                stack[top++] = v;
            }
        }

        // The root is its own ancestor.
        for (int j = 0; j < LOG; j++) {
            up[0][j] = 0;
        }

        // Step 5: Calculate subtree sizes in reverse DFS order.
        int[] subtreeSize = new int[n];
        Arrays.fill(subtreeSize, 1);

        for (int i = n - 1; i > 0; i--) {
            int u = order[i];
            subtreeSize[parent[u]] += subtreeSize[u];
        }

        // Each subtree occupies [tin[u], tout[u]].
        for (int u = 0; u < n; u++) {
            tout[u] = tin[u] + subtreeSize[u] - 1;
        }

        // Step 6: Initialize Fenwick Tree.
        // The XOR at tin[u] will represent the root-to-u path.
        fenwick = new FenwickXor(n);

        for (int u = 0; u < n; u++) {
            fenwick.rangeXor(tin[u], tout[u], charMask[u]);
        }

        // Step 7: Process queries.
        List<Boolean> answer = new ArrayList<>();

        for (String query : queries) {
            String[] parts = query.trim().split("\\s+");

            if (parts[0].equals("update")) {

                int u = Integer.parseInt(parts[1]);
                char newChar = parts[2].charAt(0);

                int newMask = 1 << (newChar - 'a');

                // XOR delta removes the old character and adds the new one.
                int delta = charMask[u] ^ newMask;

                // Updating u changes the root-path parity of every
                // node in u's subtree.
                fenwick.rangeXor(tin[u], tout[u], delta);

                charMask[u] = newMask;

            } else {

                int u = Integer.parseInt(parts[1]);
                int v = Integer.parseInt(parts[2]);

                int lca = findLCA(u, v);

                // XOR parity masks of the root-to-u and root-to-v paths.
                int maskU = fenwick.pointQuery(tin[u]);
                int maskV = fenwick.pointQuery(tin[v]);

                // The LCA must be included exactly once.
                int pathMask = maskU ^ maskV ^ charMask[lca];

                // A palindrome is possible if at most one character
                // has an odd frequency.
                boolean canFormPalindrome =
                        (pathMask & (pathMask - 1)) == 0;

                answer.add(canFormPalindrome);
            }
        }

        return answer;
    }

    // Lowest Common Ancestor using binary lifting.
    private int findLCA(int u, int v) {

        // Make u the deeper node.
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }

        // Bring both nodes to the same depth.
        int diff = depth[u] - depth[v];

        for (int j = 0; j < LOG; j++) {
            if ((diff & (1 << j)) != 0) {
                u = up[u][j];
            }
        }

        if (u == v) {
            return u;
        }

        // Lift both nodes while their ancestors differ.
        for (int j = LOG - 1; j >= 0; j--) {
            if (up[u][j] != up[v][j]) {
                u = up[u][j];
                v = up[v][j];
            }
        }

        return up[u][0];
    }

    // Fenwick Tree supporting range XOR updates and point queries.
    private static class FenwickXor {

        private final int[] bit;

        FenwickXor(int n) {
            bit = new int[n + 2];
        }

        private void add(int index, int value) {
            for (int i = index; i < bit.length; i += i & -i) {
                bit[i] ^= value;
            }
        }

        private int prefixXor(int index) {
            int result = 0;

            for (int i = index; i > 0; i -= i & -i) {
                result ^= bit[i];
            }

            return result;
        }

        // Apply XOR value to every position in [left, right].
        // The input positions are zero-indexed and inclusive.
        void rangeXor(int left, int right, int value) {
            add(left + 1, value);
            add(right + 2, value);
        }

        // Get the current XOR value at a zero-indexed position.
        int pointQuery(int index) {
            return prefixXor(index + 1);
        }
    }
}