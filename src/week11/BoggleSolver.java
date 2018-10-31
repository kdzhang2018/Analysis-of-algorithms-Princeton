package week11;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class BoggleSolver {
    private static final int R = 26;        // ALPHABET
    private Node root;      // root of 26-way trie
    
    private int M, N; // number of rows and columns in the board
    private boolean[] marked; // track if the dice has been visited
    private char[] letters; // board letters (convert from 2D to 1D)
    private StringBuilder curr; // current string
    private Bag<String> words; // words found
    private Bag<Node> nodes; // nodes corresponding to words found
    
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
        root = new Node();
        for (String s: dictionary)
            add(s);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        M = board.rows();
        N = board.cols();
        marked = new boolean[M*N];
        letters = new char[M*N];
        curr = new StringBuilder();
        words = new Bag<String>();
        nodes = new Bag<Node>();
        // construct letters: convert 2D board into 1D array
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                letters[i*N + j] = board.getLetter(i, j);
                //StdOut.println(letters[i*N + j]);
            }
        }
        
        for (int v = 0; v < M*N; v++) {
            //StdOut.println("DFS starts with " + v);
            dfs(root, v);
        }
        // reset visited to false for all the nodes visited
        for (Node n: nodes) 
            n.visited = false;
        return words;
    }

    /**
     * @param v
     * @param curr
     */
    private void dfs(Node currNode, int v) 
    {
        currNode = currNode.next[letters[v] - 'A'];
        //StdOut.println("DFS: " + v + " " + letters[v]);
        
        // when the current path corresponds to a string that is not a prefix of any word in the dictionary
        // there is no need to expand the path further
        if (currNode == null)
            return;
        if (letters[v] == 'Q')
            currNode = currNode.next['U' - 'A'];
        if (currNode == null)
            return;
        
        marked[v] = true;
        curr.append(letters[v]);
        if (letters[v] == 'Q')
            curr.append('U');
        //StdOut.println(curr);
        
        // add the curr string only when currNode isString is true and has not been visited before
        if (curr.length() > 2 && currNode.isString && !currNode.visited) {
            words.add(curr.toString());
            currNode.visited = true;
            nodes.add(currNode);
            //StdOut.println(word);
        }
        
        for (int w: adjacent(v)) {
            if (!marked[w]) {
                //StdOut.println(w);
                dfs(currNode, w);
            }
        }
        
        curr.deleteCharAt(curr.length() - 1);
        if (curr.length() > 0 && curr.charAt(curr.length() - 1) == 'Q') 
            curr.deleteCharAt(curr.length() - 1);
        marked[v] = false;
    }
    
    
    /**
     * @param v
     * @return
     */
    private Bag<Integer> adjacent(int v) 
    {
        Bag<Integer> adj = new Bag<Integer>();
        int r = v / N;
        int c = v % N;
        
        if (c > 0)
            adj.add(v - 1); // the left dice
        if (c < N - 1)
            adj.add(v + 1); // the right dice
        if (r > 0) {
            adj.add(v - N); // the top dice
            if (c > 0)
                adj.add(v - N - 1); // the top left dice
            if (c < N - 1)
                adj.add(v - N + 1); // the top right dice
        }
        if (r < M - 1) {
            adj.add(v + N); // the bottom dice
            if (c > 0)
                adj.add(v + N - 1); // the bottom left dice
            if (c < N - 1)
                adj.add(v + N + 1); // the bottom right dice
        }
        return adj;
    }
    
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
        if (!contains(word)) 
            return 0;
        int l = word.length();
        if (l < 3)
            return 0;
        else if (l <= 4)
            return 1;
        else if (l == 5)
            return 2;
        else if (l == 6)
            return 3;
        else if (l == 7)
            return 5;
        else
            return 11;
    }

    // R-way trie node
    private static class Node {
        private Node[] next = new Node[R];
        private boolean visited;
        private boolean isString;
    }
    /**
     * Does the set contain the given key?
     * @param key the key
     * @return <tt>true</tt> if the set contains <tt>key</tt> and
     *     <tt>false</tt> otherwise
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    private boolean contains(String key) {
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c - 'A'], key, d+1);
    }

    /**
     * Adds the key to the set if it is not already present.
     * @param key the key to add
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    private void add(String key) {
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.isString = true;
        }
        else {
            char c = key.charAt(d);
            x.next[c - 'A'] = add(x.next[c - 'A'], key, d+1);
        }
        return x;
    }

    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        /*StdOut.println("WORD: " + solver.scoreOf("WORD"));
        StdOut.println("WRONG: " + solver.scoreOf("WRONG"));
        StdOut.println("WORL: " + solver.scoreOf("WORL"));*/
        
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        Stopwatch sw = new Stopwatch();
        Iterable<String> it = solver.getAllValidWords(board);
        StdOut.println(sw.elapsedTime()*1000);
        //StdOut.println(1/sw.elapsedTime());
        
        for (String word : it)
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        
        board = new BoggleBoard(args[2]);
        score = 0;
        sw = new Stopwatch(); it = solver.getAllValidWords(board);
        StdOut.println(sw.elapsedTime()*1000);
        //StdOut.println(1/sw.elapsedTime());
        
        for (String word : it)
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
