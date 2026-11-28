import java.util.*;

class Solution {
    public int maximumSaleItems(int[][] items, int budget) {
        int n = items.length;

        // freq[f] = how many items have exactly this factor value
        int[] freq = new int[n + 1];
        for (int[] item : items) {
            freq[item[0]]++;
        }

        // multiples[f] = how many items have a factor that is a multiple of f
        // (harmonic-series loop: O(n log n) total)
        int[] multiples = new int[n + 1];
        for (int f = 1; f <= n; f++) {
            int count = 0;
            for (int m = f; m <= n; m += f) {
                count += freq[m];
            }
            multiples[f] = count;
        }

        int minPrice = Integer.MAX_VALUE;
        for (int[] item : items) {
            minPrice = Math.min(minPrice, item[1]);
        }

        int limit = 2 * minPrice;
        // (price, gain) pairs worth considering as "double value" copies
        List<int[]> entries = new ArrayList<>();
        for (int[] item : items) {
            int factor = item[0], price = item[1];
            int gain = multiples[factor] - 1; // exclude itself
            // a double-value copy only beats buying 2 cheapest-item copies
            // if its price is strictly less than 2 * minPrice
            if (gain > 0 && price < limit) {
                entries.add(new int[]{price, gain});
            }
        }

        entries.sort((a, b) -> a[0] - b[0]);

        long ans = 0;
        long remaining = budget;

        for (int[] e : entries) {
            int price = e[0], gain = e[1];
            long canBuy = remaining / price;
            long take = Math.min(canBuy, gain);

            ans += 2 * take; // each such copy = 1 purchased + 1 free
            remaining -= take * price;

            if (take < gain) break; // ran out of budget mid-batch
        }

        // leftover budget: buy single-value copies of the cheapest item
        ans += remaining / minPrice;

        return (int) ans;
    }
}