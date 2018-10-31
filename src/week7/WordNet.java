package week7;

import java.util.ArrayList;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
    private Digraph d;
    // the set of nouns and their synset IDs
    private final SET<Noun> s = new SET<Noun>();
    // the list of synsets
    private final ArrayList<String> a = new ArrayList<String>();
    // the number of synsets
    private int dSize = 0;
    private SAP sap;
    
    private class Noun implements Comparable<Noun>
    {
        private String noun;
        private ArrayList<Integer> id;
        
        public Noun(String n) {
            if (n == null) throw new NullPointerException();
            noun = n;
            id = new ArrayList<Integer>();
        }
        
        public ArrayList<Integer> getId() {
            return id;
        }
        
        public void addId(Integer i) {
            if (i == null) throw new NullPointerException();
            id.add(i);
        }

        @Override
        public int compareTo(Noun that) {
            // TODO Auto-generated method stub
            return this.noun.compareTo(that.noun);
        }
    }
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms)
    {
        if (synsets == null || hypernyms == null) throw new NullPointerException();
        In inSynsets = new In(synsets);
        In inHypernyms = new In(hypernyms);
        
        constructSynsets(inSynsets);
        constructHypernyms(inHypernyms);
        
        if (!rootedDAG(d)) throw new IllegalArgumentException();
        
        sap = new SAP(d);
        
    }
    
    private void constructSynsets(In inSynsets)
    {
        
        String sLine = inSynsets.readLine();
        
        while (sLine != null) {
            dSize++;
            String[] sLineSplit = sLine.split(",");
            Integer id = Integer.parseInt(sLineSplit[0]);
            a.add(sLineSplit[1]);
            String[] synset = sLineSplit[1].split(" ");
            
            for (String n: synset) {
                Noun noun = new Noun(n);
                if (s.contains(noun)) {
                    s.ceiling(noun).addId(id);
                }
                else {
                    noun.addId(id);
                    s.add(noun);
                }
            }
            sLine = inSynsets.readLine();
        }
    }
    
    private void constructHypernyms(In inHypernyms)
    {
        d = new Digraph(dSize);
        String hLine = inHypernyms.readLine();
        
        while (hLine != null) {
            String[] hLineSplit = hLine.split(",");
            int v = Integer.parseInt(hLineSplit[0]);
            for (int i = 1; i < hLineSplit.length; i++) {
                d.addEdge(v, Integer.parseInt(hLineSplit[i]));
            }
            hLine = inHypernyms.readLine();
        }
    }
    
    private boolean rootedDAG(Digraph g) {
        // test if the digraph has only one root
        int root = 0;
        for (int i = 0; i < dSize; i++) {
            if (d.outdegree(i) == 0) root++;
        }
        if (root != 1) return false;
        
        // test if the digraph has cycles
        DirectedCycle dc = new DirectedCycle(d);
        if (dc.hasCycle()) return false;
        
        return true;
    }
    
    // returns all WordNet nouns
    public Iterable<String> nouns()
    {
        Queue<String> q = new Queue<String>();
        for (Noun n: s) {
            q.enqueue(n.noun);
        }
        return q;
        
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        if (word == null) throw new NullPointerException();
        return s.contains(new Noun(word));
        
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        Noun nA = s.ceiling(new Noun(nounA));
        Noun nB = s.ceiling(new Noun(nounB));
        Iterable<Integer> iA = nA.getId();
        //StdOut.println(iA);
        Iterable<Integer> iB = nB.getId();
        //StdOut.println(iB);
        return sap.length(iA, iB);
        
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        Noun nA = s.ceiling(new Noun(nounA));
        Noun nB = s.ceiling(new Noun(nounB));
        Iterable<Integer> iA = nA.getId();
        Iterable<Integer> iB = nB.getId();
        int ancestor = sap.ancestor(iA, iB);
        //StdOut.println(ancestor);
        return a.get(ancestor);
        
    }

    // do unit testing of this class
    public static void main(String[] args)
    {
        WordNet wn = new WordNet(args[0], args[1]);
        
        //StdOut.println(wn.nouns());
        StdOut.println(wn.s.size());
        StdOut.println(wn.d.V());
        StdOut.println(wn.d.E());
        //StdOut.println(wn.a.get(3));
        //StdOut.println(wn.isNoun("z"));
        //StdOut.println(wn.isNoun("c"));
        
        StdOut.println("length = " + wn.distance("fish", "person"));
        StdOut.println("ancestor = " + wn.sap("fish", "person"));
    }
}
