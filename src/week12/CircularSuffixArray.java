package week12;

import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private static final int CUTOFF =  15;   // cutoff to insertion sort
    private int N;
    private int[] index;
    private String s;

    public CircularSuffixArray(String s)  // circular suffix array of s
    {
        if (s == null) throw new NullPointerException();
        N = s.length();
        this.s = s;
        index = new int[N];
        for (int i = 0; i < N; i++)
            index[i] = i;
        sort(0, N-1, 0);
    }
    
    // return the dth character of s, -1 if d = length of s
    private int charAt(int i, int d) { 
        if (d == s.length()) return -1;
        return s.charAt((d + i) % N);
    }


    // 3-way string quicksort a[lo..hi] starting at dth character
    private void sort(int lo, int hi, int d) { 

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        int v = charAt(index[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(index[i], d);
            if      (t < v) exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else              i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]. 
        sort(lo, lt-1, d);
        if (v >= 0) sort(lt, gt, d+1);
        sort(gt+1, hi, d);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private void insertion(int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(index[j], index[j-1], d); j--)
                exch(j, j-1);
    }

    // exchange a[i] and a[j]
    private void exch(int i, int j) {
        int temp = index[i];
        index[i] = index[j];
        index[j] = temp;
    }
    
    // is v less than w, starting at character d
    // DEPRECATED BECAUSE OF SLOW SUBSTRING EXTRACTION IN JAVA 7
    // private static boolean less(String v, String w, int d) {
    //    assert v.substring(0, d).equals(w.substring(0, d));
    //    return v.substring(d).compareTo(w.substring(d)) < 0; 
    // }

    // is v less than w, starting at character d
    private boolean less(int v, int w, int d) {
        for (int i = d; i < N; i++) {
            if (charAt(v, i) < charAt(w, i)) return true;
            if (charAt(v, i) > charAt(w, i)) return false;
        }
        return false;
    }
    
    public int length()                   // length of s
    {
        return N;
    }
    public int index(int i)               // returns index of ith sorted suffix
    {
        if (i < 0 || i >= N) throw new IndexOutOfBoundsException();
        return index[i];
    }
    public static void main(String[] args)// unit testing of the methods (optional)
    {
        String s = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < 12; i++) {
            StdOut.println("Sorted array " + i + ":");
            StdOut.println("suffix " + csa.index(i));
            for (int j = 0; j < 12; j++) {
                StdOut.print(s.charAt((csa.index(i) + j) % 12));
            }
            StdOut.println();
        }
    }
}
