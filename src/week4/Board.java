package week4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int[][] tiles;
    private final int N;    // board dimension
    
    public Board(int[][] blocks) {          // construct a board from an N-by-N array of blocks
                                           // (where blocks[i][j] = block in row i, column j)
        N = blocks.length;
        if (N <= 1) throw new IllegalArgumentException();
        tiles = new int[N][N];
        for (int i =  0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = blocks[i][j];
            }
        }
    
    }
    public int dimension() {                // board dimension N
        return N;
    }
    public int hamming() {                  // number of blocks out of place
        int ham = 0;
        for (int i =  0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != 0) {
                    if (tiles[i][j] != i*N + j + 1)
                        ham++;
                }
            }
        }
        return ham;
    }
    public int manhattan() {                // sum of Manhattan distances between blocks and goal
        int man = 0;
        for (int i =  0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int num = tiles[i][j];
                if (num != 0) {
                    // (num-1)/N, (num-1)%N should be the right position
                    int h = Math.abs((num-1)/N - i);
                    int v = Math.abs((num-1) % N - j);
                    man += h + v;
                    //StdOut.println(tiles[i][j] + " " + h + " " + v);
                }
            }
        }
        return man;
    }
    public boolean isGoal() {               // is this board the goal board?
        for (int i =  0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == N-1 && j == N-1) return true; // all blocks except the last blank block are at the right position
                if (tiles[i][j] != i*N + j + 1) return false;
            }
        }
        return true;
    }
    public Board twin() {                   // a board that is obtained by exchanging any pair of blocks
        int[][] twtiles = new int[N][N];
        for (int i =  0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                twtiles[i][j] = tiles[i][j];
            }
        }
        if (tiles[0][0] != 0) {
            if (tiles[0][1] != 0) {
                // exchange [0][0] and [0][1]
                twtiles[0][0] = tiles[0][1];
                twtiles[0][1] = tiles[0][0];
            }
            else {
                // exchange [0][0] and [1][0] if [0][1] is blank
                twtiles[0][0] = tiles[1][0];
                twtiles[1][0] = tiles[0][0];
            }
        }
        else {
            // exchange [1][0] and [0][1] if [0][0] is blank
            twtiles[1][0] = tiles[0][1];
            twtiles[0][1] = tiles[1][0];
        }
        return new Board(twtiles);
    }
    public boolean equals(Object y) {       // does this board equal y?
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        
        Board that = (Board) y;
        if (this.N != that.N) return false; // boards have different dimensions
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.tiles[i][j] != that.tiles[i][j])
                    return false;
            }
        }
        return true;
    }
    public Iterable<Board> neighbors() {    // all neighboring boards
        Queue<Board> q = new Queue<Board>();
        // find the blank block
        int blanki = 0;
        int blankj = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) {
                    blanki = i;
                    blankj = j;
                    break;
                }
            }
        }
        // move the top block
        if (blanki - 1 >= 0) {
            int[][] ntiles = new int[N][N];
            for (int i =  0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    ntiles[i][j] = tiles[i][j];
                }
            }
            ntiles[blanki][blankj] = ntiles[blanki - 1][blankj];
            ntiles[blanki - 1][blankj] = 0;
            q.enqueue(new Board(ntiles));
        }
        // move the bottom block
        if (blanki + 1 < N) {
            int[][] ntiles = new int[N][N];
            for (int i =  0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    ntiles[i][j] = tiles[i][j];
                }
            }
            ntiles[blanki][blankj] = ntiles[blanki + 1][blankj];
            ntiles[blanki + 1][blankj] = 0;
            q.enqueue(new Board(ntiles));
        }
        // move the left block
        if (blankj - 1 >= 0) {
            int[][] ntiles = new int[N][N];
            for (int i =  0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    ntiles[i][j] = tiles[i][j];
                }
            }
            ntiles[blanki][blankj] = ntiles[blanki][blankj - 1];
            ntiles[blanki][blankj - 1] = 0;
            q.enqueue(new Board(ntiles));
        }
        // move the right block
        if (blankj + 1 < N) {
            int[][] ntiles = new int[N][N];
            for (int i =  0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    ntiles[i][j] = tiles[i][j];
                }
            }
            ntiles[blanki][blankj] = ntiles[blanki][blankj + 1];
            ntiles[blanki][blankj + 1] = 0;
            q.enqueue(new Board(ntiles));
        }
        return q;
    }
    public String toString() {              // string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) { // unit tests (not graded)
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial);
        StdOut.println("Hamming: " + initial.hamming());
        StdOut.println("Manhattan: " + initial.manhattan());
        StdOut.println("Is the block the goal block: " + initial.isGoal());
        StdOut.println(initial.twin());
        StdOut.println(initial.neighbors());
    }
}
