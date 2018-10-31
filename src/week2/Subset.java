package week2;

import java.util.Iterator;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        int k = Integer.parseInt(args[0]); // the number of strings that will be print out: 0 <= k <= N
        
        /* // one RandomizedQueue object of size at k+1
        for (int j = 0; j < k; j++) {
            String item = StdIn.readString();
            q.enqueue(item);
        }
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            q.enqueue(item); 
            q.dequeue(); // not uniformly random
        }
        Iterator<String> i = q.iterator();
        while (i.hasNext())
        {
            String s = i.next();
            StdOut.println(s);
        }*/
        
         // one RandomizedQueue object of size at N
         while (!StdIn.isEmpty()) { 
             String item = StdIn.readString(); 
             q.enqueue(item); 
         }
         Iterator<String> i = q.iterator();
         for (int j = 0; j < k; j++) {
             String s = i.next();
             StdOut.println(s);
         }
         
        // StdOut.println("(" + q.size() + " left on queue)");

        

    }
}
