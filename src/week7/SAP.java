package week7;

import java.util.ArrayList;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph d;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G)
    {
        d = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w)
    {
        if (v >= d.V() || w >= d.V()) throw new IndexOutOfBoundsException();
        if (v < 0 || w < 0) throw new IndexOutOfBoundsException();
        
        DeluxeBFS vBFS = new DeluxeBFS(d, v);
        DeluxeBFS wBFS = new DeluxeBFS(d, w);
        int min = Integer.MAX_VALUE;
        
        // If you run the two breadth-first searches from v and w in lockstep 
        // (alternating back and forth between exploring vertices in each of the two searches), 
        // then you can terminate the BFS from v (or w) 
        // as soon as the distance exceeds the length of the best ancestral path found so far.
        
        ArrayList<Integer> vConnected = vBFS.connected();
        ArrayList<Integer> wConnected = wBFS.connected();
        ArrayList<Integer> vDistances = vBFS.distances();
        ArrayList<Integer> wDistances = wBFS.distances();
        /*StdOut.println(vConnected);
        StdOut.println(vDistances);
        StdOut.println(wConnected);
        StdOut.println(wDistances);*/
        
        
        int i = 0;
        int j = 0;
        
        while (i < vConnected.size() || j < wConnected.size()) {
            
            // look at the ith item in vConnected
            if (j >= wConnected.size() || (i < vConnected.size() && j < wConnected.size() && vDistances.get(i) <= wDistances.get(j))) {
                //StdOut.println("v connected: " + i + "th item: " + vConnected.get(i));
                if (vDistances.get(i) >= min) break;
                //int wIndex = wConnected.indexOf(vConnected.get(i)); // indexOf: linear time operation
                //if (wIndex >= 0) {
                if (wBFS.hasPathTo(vConnected.get(i))) {
                    int dist = vDistances.get(i) + wBFS.distTo(vConnected.get(i));
                    //int dist = vDistances.get(i) + wDistances.get(wIndex);
                    //StdOut.println("dist: " + dist);
                    if (dist < min) {
                        min = dist;
                    }
                }
                i++;
            }
            
            // look at the jth item in wConnected
            else if (i >= vConnected.size() || (i < vConnected.size() && j < wConnected.size() && vDistances.get(i) > wDistances.get(j))) {
                //StdOut.println("w connected: " + j + "th item: " + wConnected.get(j));
                if (wDistances.get(j) >= min) break;
                //int vIndex = vConnected.indexOf(wConnected.get(j));
                //if (vIndex >= 0) {
                if (vBFS.hasPathTo(wConnected.get(j))) {
                    int dist = wDistances.get(j) + vBFS.distTo(wConnected.get(j));
                    //int dist = wDistances.get(j) + vDistances.get(vIndex);
                    //StdOut.println("dist: " + dist);
                    if (dist < min) {
                        min = dist;
                    }
                }
                j++;
            }
        }
        
        if (min == Integer.MAX_VALUE) return -1;
        return min;
        
        
        
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w)
    {
        if (v >= d.V() || w >= d.V()) throw new IndexOutOfBoundsException();
        if (v < 0 || w < 0) throw new IndexOutOfBoundsException();
        
        DeluxeBFS vBFS = new DeluxeBFS(d, v);
        DeluxeBFS wBFS = new DeluxeBFS(d, w);
        int min = Integer.MAX_VALUE;
        int ancestor = -1;
        
        ArrayList<Integer> vConnected = vBFS.connected();
        ArrayList<Integer> wConnected = wBFS.connected();
        ArrayList<Integer> vDistances = vBFS.distances();
        ArrayList<Integer> wDistances = wBFS.distances();
        
        int i = 0;
        int j = 0;
        
        while (i < vConnected.size() || j < wConnected.size()) {
            
            // look at the ith item in vConnected
            if (j >= wConnected.size() || (i < vConnected.size() && j < wConnected.size() && vDistances.get(i) <= wDistances.get(j))) {
                //StdOut.println("v connected: " + i + "th item: " + vConnected.get(i));
                if (vDistances.get(i) >= min) break;
                //int wIndex = wConnected.indexOf(vConnected.get(i)); // indexOf: linear time operation
                //if (wIndex >= 0) {
                if (wBFS.hasPathTo(vConnected.get(i))) {
                    int dist = vDistances.get(i) + wBFS.distTo(vConnected.get(i));
                    //int dist = vDistances.get(i) + wDistances.get(wIndex);
                    //StdOut.println("dist: " + dist);
                    if (dist < min) {
                        min = dist;
                        ancestor = vConnected.get(i);
                    }
                }
                i++;
            }
            
            // look at the jth item in wConnected
            else if (i >= vConnected.size() || (i < vConnected.size() && j < wConnected.size() && vDistances.get(i) > wDistances.get(j))) {
                //StdOut.println("w connected: " + j + "th item: " + wConnected.get(j));
                if (wDistances.get(j) >= min) break;
                //int vIndex = vConnected.indexOf(wConnected.get(j));
                //if (vIndex >= 0) {
                if (vBFS.hasPathTo(wConnected.get(j))) {
                    int dist = wDistances.get(j) + vBFS.distTo(wConnected.get(j));
                    //int dist = wDistances.get(j) + vDistances.get(vIndex);
                    //StdOut.println("dist: " + dist);
                    if (dist < min) {
                        min = dist;
                        ancestor = wConnected.get(j);
                    }
                }
                j++;
            }
        }
        
        /*v3: while (i < vConnected.size() && j < wConnected.size()) { // should be or, not and
            if (Integer.min(vDistances.get(i), wDistances.get(j)) >= min) break;
            // look at the ith item in vConnected
            if (vDistances.get(i) <= wDistances.get(j)) {
                if (wBFS.hasPathTo(vConnected.get(i))) {
                    int dist = vDistances.get(i) + wBFS.distTo(vConnected.get(i));
                    if (dist < min) {
                        min = dist;
                        ancestor = vConnected.get(i);
                    }
                }
                i++;
            }
            // look at the jth item in wConnected
            else {
                if (vBFS.hasPathTo(wConnected.get(j))) {
                    int dist = wDistances.get(j) + vBFS.distTo(wConnected.get(j));
                    if (dist < min) {
                        min = dist;
                        ancestor = wConnected.get(j);
                    }
                }
                j++;
            }
        }*/
        
        /*v2: Queue<Integer> vConnected = vBFS.connected();
        Queue<Integer> wConnected = wBFS.connected();
        
        int connectedNum = vConnected.size() + wConnected.size();
        
        for (int i = 0; i < connectedNum; i++) {
            if (vConnected.isEmpty() && wConnected.isEmpty()) break;
            
            if (i%2 == 0 && !vConnected.isEmpty()) {
                int j = vConnected.dequeue();
                if (vBFS.distTo(j) >= min) {
                    vConnected = new Queue<Integer>();
                    continue;
                }
                if (wBFS.hasPathTo(j)) {
                    int dist = vBFS.distTo(j) + wBFS.distTo(j);
                    if (dist < min) {
                        min = dist;
                        ancestor = j;
                    }
                }
            }
            else if (i%2 == 1 && !wConnected.isEmpty()) {
                int j = wConnected.dequeue();
                if (wBFS.distTo(j) >= min) {
                    wConnected = new Queue<Integer>();
                    continue;
                }
                if (vBFS.hasPathTo(j)) {
                    int dist = vBFS.distTo(j) + wBFS.distTo(j);
                    if (dist < min) {
                        min = dist;
                        ancestor = j;
                    }
                }
            }
        }*/
        
        /*v1: int[] lengths = new int[d.V()];
        for (int i = 0; i < d.V(); i++) {
            if (vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
                lengths[i] = vBFS.distTo(i) + wBFS.distTo(i);
                if (lengths[i] < min) {
                    min = lengths[i];
                    ancestor = i;
                }
            }
        }*/
        
        return ancestor;
        
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
        for (int i: v) {
            if (i < 0 || i >= d.V()) throw new IndexOutOfBoundsException();
        }
        for (int i: w) {
            if (i < 0 || i >= d.V()) throw new IndexOutOfBoundsException();
        }
        
        DeluxeBFS vBFS = new DeluxeBFS(d, v);
        DeluxeBFS wBFS = new DeluxeBFS(d, w);
        int min = Integer.MAX_VALUE;
        
        ArrayList<Integer> vConnected = vBFS.connected();
        ArrayList<Integer> wConnected = wBFS.connected();
        ArrayList<Integer> vDistances = vBFS.distances();
        ArrayList<Integer> wDistances = wBFS.distances();
        
        int i = 0;
        int j = 0;
        
        while (i < vConnected.size() || j < wConnected.size()) {
            
            // look at the ith item in vConnected
            if (j >= wConnected.size() || (i < vConnected.size() && j < wConnected.size() && vDistances.get(i) <= wDistances.get(j))) {
                //StdOut.println("v connected: " + i + "th item: " + vConnected.get(i));
                if (vDistances.get(i) >= min) break;
                //int wIndex = wConnected.indexOf(vConnected.get(i)); // indexOf: linear time operation
                //if (wIndex >= 0) {
                if (wBFS.hasPathTo(vConnected.get(i))) {
                    int dist = vDistances.get(i) + wBFS.distTo(vConnected.get(i));
                    //int dist = vDistances.get(i) + wDistances.get(wIndex);
                    //StdOut.println("dist: " + dist);
                    if (dist < min) {
                        min = dist;
                    }
                }
                i++;
            }
            
            // look at the jth item in wConnected
            else if (i >= vConnected.size() || (i < vConnected.size() && j < wConnected.size() && vDistances.get(i) > wDistances.get(j))) {
                //StdOut.println("w connected: " + j + "th item: " + wConnected.get(j));
                if (wDistances.get(j) >= min) break;
                //int vIndex = vConnected.indexOf(wConnected.get(j));
                //if (vIndex >= 0) {
                if (vBFS.hasPathTo(wConnected.get(j))) {
                    int dist = wDistances.get(j) + vBFS.distTo(wConnected.get(j));
                    //int dist = wDistances.get(j) + vDistances.get(vIndex);
                    //StdOut.println("dist: " + dist);
                    if (dist < min) {
                        min = dist;
                    }
                }
                j++;
            }
        }
        
        if (min == Integer.MAX_VALUE) return -1;
        return min;
        
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
        for (int i: v) {
            if (i < 0 || i >= d.V()) throw new IndexOutOfBoundsException();
        }
        for (int i: w) {
            if (i < 0 || i >= d.V()) throw new IndexOutOfBoundsException();
        }
        
        DeluxeBFS vBFS = new DeluxeBFS(d, v);
        DeluxeBFS wBFS = new DeluxeBFS(d, w);
        int min = Integer.MAX_VALUE;
        int ancestor = -1;
        
        ArrayList<Integer> vConnected = vBFS.connected();
        ArrayList<Integer> wConnected = wBFS.connected();
        ArrayList<Integer> vDistances = vBFS.distances();
        ArrayList<Integer> wDistances = wBFS.distances();
        
        int i = 0;
        int j = 0;
        
        while (i < vConnected.size() || j < wConnected.size()) {
            
            // look at the ith item in vConnected
            if (j >= wConnected.size() || (i < vConnected.size() && j < wConnected.size() && vDistances.get(i) <= wDistances.get(j))) {
                //StdOut.println("v connected: " + i + "th item: " + vConnected.get(i));
                if (vDistances.get(i) >= min) break;
                //int wIndex = wConnected.indexOf(vConnected.get(i)); // indexOf: linear time operation
                //if (wIndex >= 0) {
                if (wBFS.hasPathTo(vConnected.get(i))) {
                    int dist = vDistances.get(i) + wBFS.distTo(vConnected.get(i));
                    //int dist = vDistances.get(i) + wDistances.get(wIndex);
                    //StdOut.println("dist: " + dist);
                    if (dist < min) {
                        min = dist;
                        ancestor = vConnected.get(i);
                    }
                }
                i++;
            }
            
            // look at the jth item in wConnected
            else if (i >= vConnected.size() || (i < vConnected.size() && j < wConnected.size() && vDistances.get(i) > wDistances.get(j))) {
                //StdOut.println("w connected: " + j + "th item: " + wConnected.get(j));
                if (wDistances.get(j) >= min) break;
                //int vIndex = vConnected.indexOf(wConnected.get(j));
                //if (vIndex >= 0) {
                if (vBFS.hasPathTo(wConnected.get(j))) {
                    int dist = wDistances.get(j) + vBFS.distTo(wConnected.get(j));
                    //int dist = wDistances.get(j) + vDistances.get(vIndex);
                    //StdOut.println("dist: " + dist);
                    if (dist < min) {
                        min = dist;
                        ancestor = wConnected.get(j);
                    }
                }
                j++;
            }
        }
        
        //StdOut.println(vBFS.pathTo(ancestor));
        //StdOut.println(wBFS.pathTo(ancestor));
        return ancestor;
        
        
    }

    // do unit testing of this class
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            
            /*ArrayList<Integer> v = new ArrayList<Integer>(); 
            ArrayList<Integer> w = new ArrayList<Integer>();
            
            v.add(StdIn.readInt());
            v.add(StdIn.readInt());
            w.add(StdIn.readInt());
            w.add(StdIn.readInt());*/
            
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            //StdOut.printf("length = %d", length);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
