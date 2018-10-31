package week1;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * @author kaidizhang
 *
 */
public class Percolation {
    private int gridSize; // the size of the grid
    //private int bottom; // the virtual bottom site (stop using virtual bottom to avoid backwash)
    private WeightedQuickUnionUF uf; // weighted quick union find
    private boolean[] status; // record whether the site is open or not

    /**
     * @param N
     * the grid size
     */
    public Percolation(int N) // create N-by-N grid, with all sites blocked
    {
        if (N <= 0)
            throw new java.lang.IllegalArgumentException();
        gridSize = N;
        int ufSize = gridSize * gridSize + 1;
        //int ufSize = gridSize * gridSize + 2;
        //bottom = ufSize - 1;

        uf = new WeightedQuickUnionUF(ufSize);
        for (int i = 1; i <= N; i++)
            uf.union(0, i);
        //for (int j = gridSize * gridSize; j > gridSize * (gridSize - 1); j--)
            //uf.union(bottom, j);

        status = new boolean[ufSize];
        status[0] = true;
        //status[bottom] = true;
    }

    /**
     * @param i
     * @param j
     */
    public void open(int i, int j) // open site (row i, column j) if it is not
                                   // open already
    {
        if (i <= 0 || i > gridSize)
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (j <= 0 || j > gridSize)
            throw new IndexOutOfBoundsException("collumn index j out of bounds");

        int num = j + (i - 1) * gridSize; // the number of the site in the array
        if (!status[num]) {
            status[num] = true;
            if (i > 1 && isOpen(i - 1, j))
                uf.union(num, num - gridSize); // top site
            if (i < gridSize && isOpen(i + 1, j))
                uf.union(num, num + gridSize); // bottom site
            if (j > 1 && isOpen(i, j - 1))
                uf.union(num, num - 1); // left site
            if (j < gridSize && isOpen(i, j + 1))
                uf.union(num, num + 1); // right site
        }
    }

    /**
     * @param i
     * @param j
     * @return true if the site has been set open
     */
    public boolean isOpen(int i, int j) // is site (row i, column j) open?
    {
        if (i <= 0 || i > gridSize)
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (j <= 0 || j > gridSize)
            throw new IndexOutOfBoundsException("collumn index j out of bounds");
        int num = j + (i - 1) * gridSize;
        return status[num];
    }

    /**
     * @param i
     * @param j
     * @return true is the site is full (connected with the virtual top site)
     */
    public boolean isFull(int i, int j) // is site (row i, column j) full?
    {
        if (i <= 0 || i > gridSize)
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (j <= 0 || j > gridSize)
            throw new IndexOutOfBoundsException("collumn index j out of bounds");
        int num = j + (i - 1) * gridSize;
        if (isOpen(i, j)) // for sites on the first row, the site needs to be open to be full
            return uf.connected(0, num);
        else
            return false;
    }

    /**
     * @return true if the system percolates
     * No virtual bottom site: use for loop, not constant time  
     */
    public boolean percolates() // does the system percolate?
    {
        if (gridSize == 1 && !isOpen(1, 1))
            return false;
        for (int k = gridSize*gridSize; k > gridSize*(gridSize-1); k--) {
            if (uf.connected(0, k))
                return true;
        }
        return false;
        //return uf.connected(0, bottom);
    }

    /**
     * @param args
     */
    public static void main(String[] args) // test client (optional)
    {
        int N = StdIn.readInt();
        Percolation per = new Percolation(N);
        while (!StdIn.isEmpty()) {
            int i = StdIn.readInt();
            int j = StdIn.readInt();
            per.open(i, j);
            StdOut.println(i + ", " + j + " is full: " + per.isFull(i, j));
        }
        StdOut.println("The grid percolates: " + per.percolates());
    }
}
