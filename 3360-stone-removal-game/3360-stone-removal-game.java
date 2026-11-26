class Solution {
    public boolean canAliceWin(int n) {
        int remove = 10;
        boolean aliceTurn = true;

        while (n >= remove) {
            n -= remove;
            remove--;
            aliceTurn = !aliceTurn;
        }

        // aliceTurn is now true only if the player who just COULDN'T move
        // was Bob (i.e., it was Bob's turn when the loop stopped)
        return !aliceTurn;
    }
}