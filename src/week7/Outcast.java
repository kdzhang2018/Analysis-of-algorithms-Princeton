package week7;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wn;
    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
        wn = wordnet;
    }
    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
        int max = 0;
        String oc = null;
        for (String v: nouns) {
            int dist = 0;
            for (String w: nouns) {
                dist += wn.distance(v, w);
            }
            //StdOut.println(v + dist);
            if (dist > max) {
                max = dist;
                oc = v;
            }
        }
        return oc;
        
    }
    public static void main(String[] args)  // see test client below
    {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
