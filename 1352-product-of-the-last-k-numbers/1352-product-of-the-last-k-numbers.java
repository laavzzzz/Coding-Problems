class ProductOfNumbers {
    private List<Integer> prefix;

    public ProductOfNumbers() {
        prefix = new ArrayList<>();
        prefix.add(1); // sentinel base for prefix products
    }

    public void add(int num) {
        if (num == 0) {
            // reset: no prefix product can span across a zero
            prefix.clear();
            prefix.add(1);
        } else {
            int last = prefix.get(prefix.size() - 1);
            prefix.add(last * num);
        }
    }

    public int getProduct(int k) {
        int size = prefix.size();
        if (k >= size) {
            // the window of k numbers would reach back across a zero reset
            return 0;
        }
        return prefix.get(size - 1) / prefix.get(size - 1 - k);
    }
}