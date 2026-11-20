class Solution {
    public int maxSelectedElements(int[] nums) {
        Arrays.sort(nums);
        Map<Integer, Integer> dp = new HashMap<>();
        int ans = 0;

        for (int num : nums) {
            int extendKeep = dp.getOrDefault(num - 1, 0) + 1; // use num unchanged
            int extendInc  = dp.getOrDefault(num, 0) + 1;     // use num as num + 1

            dp.put(num,     Math.max(dp.getOrDefault(num, 0), extendKeep));
            dp.put(num + 1, Math.max(dp.getOrDefault(num + 1, 0), extendInc));

            ans = Math.max(ans, Math.max(dp.get(num), dp.get(num + 1)));
        }

        return ans;
    }
}