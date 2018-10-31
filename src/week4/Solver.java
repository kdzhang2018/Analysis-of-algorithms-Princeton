package week4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final boolean solvable;
    private final int solutionMoves;
    private final Stack<Board> solution;
    
    public Solver(Board initial) {          // find a solution to the initial board (using the A* algorithm)
        if (initial == null) 
            throw new NullPointerException();
        
        SearchNode initialSN = new SearchNode(initial, 0, null, false);
        SearchNode initialSNtwin = new SearchNode(initial.twin(), 0, null, true); // the twin node
        MinPQ<SearchNode> PQ = new MinPQ<SearchNode>();
        solution = new Stack<Board>();
        
        PQ.insert(initialSN);
        PQ.insert(initialSNtwin);
        SearchNode current = null;
        
        while (!PQ.isEmpty()) {
            current = PQ.delMin();
            //StdOut.println(current.board);
            //StdOut.println("priority: " + current.priority + " moves: " + current.moves + " twin: " + current.twin);
            if (current.board.isGoal()) break;
            
            for (Board b: current.board.neighbors()) {
                if (current.previous == null || !b.equals(current.previous.board)) { 
                    // for initial SearchNode, add all its neighbors
                    // for the following SearchNodes, perform critical optimization
                    SearchNode sn = new SearchNode(b, current.moves + 1, current, current.twin);
                    PQ.insert(sn);
                    //StdOut.println("Neighbor: " + b);
                }
            }
        }
        if (!current.twin) { // current derives from the initial board: solvable
            solvable = true;
            solutionMoves = current.moves;
            // solution is last-in-first-out: the initial node is the last item
            while (current != null) {
                solution.push(current.board);
                current = current.previous;
            }  
        }
        else { // current derives from the twin board: unsolvable
            solvable = false;
            solutionMoves = -1;
        }   
    }
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private int priority;
        private SearchNode previous;
        private boolean twin; // false if the node derives from the initial board
        
        public SearchNode(Board board, int moves, SearchNode previous, boolean twin) {
            this.board = board;
            this.moves = moves;
            this.priority = moves + board.manhattan();
            this.previous = previous;
            this.twin = twin;
        }

        @Override
        public int compareTo(SearchNode that) {
            // TODO Auto-generated method stub
            return (this.priority - that.priority);
        }    
    }
    
    public boolean isSolvable() {            // is the initial board solvable?
        return solvable;
    }
    public int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
        return solutionMoves;
    }
    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if unsolvable
        if (solvable)
            return solution;
        else
            return null;
    }
    public static void main(String[] args) { // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        } 
    }
}
