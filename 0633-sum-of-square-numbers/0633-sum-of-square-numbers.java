class Solution {
    public boolean judgeSquareSum(int c) {
        int sum=0;
        for(long a=0;a*a<=c;a++){
            
            long b=(long)Math.sqrt(c - a*a);
            if(a*a+b*b==c){
                return true;
            }
                }
        return false;
    }
}