package week1;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * @author kaidizhang
 *
 */
public class PercolationStats {
    private double[] fractions; // the list of open site fraction in each
                                // experiment
    private int expNum; // the number of independent experiments

    public PercolationStats(int N, int T) // perform T independent experiments
                                          // on an N-by-N grid
    {
        if (N <= 0 || T <= 0)
            throw new java.lang.IllegalArgumentException();
        expNum = T;
        fractions = new double[T];
        for (int k = 0; k < T; k++) {
            Percolation per = new Percolation(N);
            double openSites = 0;
            while (!per.percolates()) {
                int i = StdRandom.uniform(1, N + 1);
                int j = StdRandom.uniform(1, N + 1);
                if (!per.isOpen(i, j)) {
                    openSites++;
                    per.open(i, j);
                    // StdOut.println(i+","+j);
                }
            }
            fractions[k] = openSites / (N * N);
            // StdOut.println("fraction of open sites: " + fractions[k]);
        }
    }

    /**
     * @return sample mean of percolation threshold
     */
    public double mean() // sample mean of percolation threshold
    {
        return StdStats.mean(fractions);
    }

    /**
     * @return sample standard deviation of percolation threshold
     */
    public double stddev() // sample standard deviation of percolation threshold
    {
        return StdStats.stddev(fractions);
    }

    /**
     * @return low endpoint of 95% confidence interval
     */
    public double confidenceLo() // low endpoint of 95% confidence interval
    {
        double lo = mean() - 1.96 * stddev() / Math.sqrt(expNum);
        return lo;
    }

    /**
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi() // high endpoint of 95% confidence interval
    {
        double hi = mean() + 1.96 * stddev() / Math.sqrt(expNum);
        return hi;
    }

    /**
     * @param args
     */
    public static void main(String[] args) // test client (described below)
    {
        int N = StdIn.readInt();
        int T = StdIn.readInt();
        PercolationStats perSt = new PercolationStats(N, T);
        StdOut.println(T + " independend experiments on an " + N + "-by-" + N + " grid:");
        StdOut.println("Percolation threshold mean: " + perSt.mean());
        StdOut.println("Standard deviation: " + perSt.stddev());
        StdOut.println("95% confidence interval: " + perSt.confidenceLo() + ", " + perSt.confidenceHi());
    }
}
