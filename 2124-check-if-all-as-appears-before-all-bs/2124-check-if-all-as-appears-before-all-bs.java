class Solution {
    public boolean checkString(String s) {
        boolean seenB = false;
        for (char c : s.toCharArray()) {
            if (c == 'b') {
                seenB = true;
            } else if (seenB) {
                // we're at an 'a' after already having seen a 'b'
                return false;
            }
        }
        return true;
    }
}