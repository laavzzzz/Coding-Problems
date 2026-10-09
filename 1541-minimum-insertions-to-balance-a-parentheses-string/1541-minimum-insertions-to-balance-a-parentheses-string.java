class Solution {
    public int minInsertions(String s) {
        int open=0;
        int insertions=0;
        for (int i = 0; i < s.length(); i++) {

            if (s.charAt(i) == '(') {
                open++;
            } else {

                // If next character is also ')',
                // consume the pair together
                if (i + 1 < s.length() && s.charAt(i + 1) == ')') {
                    i++;
                } else {
                    // Only one ')' found, insert another ')'
                    insertions++;
                }

                // If no '(' is available, insert one '('
                if (open == 0) {
                    insertions++;
                } else {
                    open--;
                }
            }
        }

        // Each remaining '(' needs two ')'
        insertions += open * 2;

        return insertions;
    }
}