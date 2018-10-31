package week12;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256; // extended ASCII
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() 
    {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        StringBuilder t = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int idx = csa.index(i);
            if (idx == 0) {
                BinaryStdOut.write(i);
                idx += s.length();
            }
            t.append(s.charAt(idx - 1));
        }
        BinaryStdOut.write(t.toString());
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() 
    {
        int first = BinaryStdIn.readInt();
        StringBuilder sb = new StringBuilder();
        while (!BinaryStdIn.isEmpty())
            sb.append(BinaryStdIn.readChar());
        char[] t = sb.toString().toCharArray();
        int N = t.length;
        int[] count = new int[R+1];
        int[] next = new int[N];
        for (int i = 0; i < N; i++)
            count[t[i] + 1]++; // count frequencies
        for (int i = 0; i < R; i++)
            count[i+1] += count[i]; // compute cumulates
        for (int i = 0; i < N; i++)
            next[count[t[i]]++] = i; // construct next array
        /*for (int idx = next[first]; idx != first; idx = next[idx])
            BinaryStdOut.write(t[idx]);
        BinaryStdOut.write(t[first]);*/
        int idx = next[first];
        for (int i = 0; i < N; i++) {
            BinaryStdOut.write(t[idx]);
            idx = next[idx];
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) 
    {
        if (args[0].equals("-")) 
            encode();
        else if (args[0].equals("+"))
            decode();    
    }
}
