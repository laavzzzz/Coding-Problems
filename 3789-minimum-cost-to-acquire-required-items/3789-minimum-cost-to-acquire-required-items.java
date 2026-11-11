
class Solution {
    public long minimumCost(int cost1, int cost2, int costBoth, int need1, int need2) {
        long both = Math.min(need1, need2);
        long max = Math.max(need1, need2);

        long option1 = (long) need1 * cost1 + (long) need2 * cost2;

        long option2 = both * costBoth
                     + (need1 - both) * (long) cost1
                     + (need2 - both) * (long) cost2;

        long option3 = max * costBoth;

        return Math.min(option1, Math.min(option2, option3));
    }
}
