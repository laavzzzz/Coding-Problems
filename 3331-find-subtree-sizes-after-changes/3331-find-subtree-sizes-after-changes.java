class Solution {
    public int[] findSubtreeSizes(int[] parent, String s) {
        int n = parent.length;

        // Build children list for the ORIGINAL tree
        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < n; i++) children.add(new ArrayList<>());
        for (int i = 1; i < n; i++) children.get(parent[i]).add(i);

        int[] newParent = new int[n];
        newParent[0] = -1;

        // Per-character stack of ancestors (by char 'a'..'z') on current root-to-node path
        Deque<Integer>[] charStack = new Deque[26];
        for (int i = 0; i < 26; i++) charStack[i] = new ArrayDeque<>();

        // Iterative pre-order DFS to assign new parents.
        // Each stack frame: [node, childIndexToProcessNext]
        Deque<int[]> frame = new ArrayDeque<>();
        frame.push(new int[]{0, 0});
        charStack[s.charAt(0) - 'a'].push(0);

        while (!frame.isEmpty()) {
            int[] top = frame.peek();
            int node = top[0];
            int idx = top[1];
            List<Integer> kids = children.get(node);

            if (idx < kids.size()) {
                top[1]++; // advance for when we come back to this frame
                int child = kids.get(idx);
                int c = s.charAt(child) - 'a';

                // Closest ancestor with same character, or keep original parent
                if (!charStack[c].isEmpty()) {
                    newParent[child] = charStack[c].peek();
                } else {
                    newParent[child] = node;
                }

                charStack[c].push(child);
                frame.push(new int[]{child, 0});
            } else {
                // done with this node's children: pop it off its char stack, backtrack
                charStack[s.charAt(node) - 'a'].pop();
                frame.pop();
            }
        }

        // Build the NEW tree's children list from newParent
        List<List<Integer>> newChildren = new ArrayList<>();
        for (int i = 0; i < n; i++) newChildren.add(new ArrayList<>());
        for (int i = 1; i < n; i++) newChildren.get(newParent[i]).add(i);

        // Iterative post-order DFS to compute subtree sizes
        int[] answer = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();
        Deque<Integer> order = new ArrayDeque<>(); // will hold post-order via reversed pre-order
        stack.push(0);
        while (!stack.isEmpty()) {
            int node = stack.pop();
            order.push(node); // push to front conceptually; we'll process via this as a stack
            for (int child : newChildren.get(node)) {
                stack.push(child);
            }
        }
        // order, when popped, gives nodes in reverse of visitation (children before parents)
        while (!order.isEmpty()) {
            int node = order.pop();
            answer[node] = 1;
            for (int child : newChildren.get(node)) {
                answer[node] += answer[child];
            }
        }

        return answer;
    }
}