package week2;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int size;
    
    public RandomizedQueue()                 // construct an empty randomized queue
    {
        q = (Item[]) new Object[2];
        size = 0;
    }
    public boolean isEmpty()                 // is the queue empty?
    {
        return size == 0;
        
    }
    public int size()                        // return the number of items on the queue
    {
        return size;
        
    }
    private void resize(int max)
    {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < size; i++)
        {
            temp[i] = q[i]; 
        }
        q = temp;
    }
    
    public void enqueue(Item item)           // add the item
    {
        if (item == null)
            throw new java.lang.NullPointerException();
        if (size == q.length)
            resize(size*2); // double array size
        q[size] = item;
        size++;
    }
    public Item dequeue()                    // remove and return a random item
    {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");
        int i = StdRandom.uniform(size);
        Item item = q[i];
        q[i] = q[size-1];
        q[size-1] = null; // to avoid loitering
        size--;
        if (size > 0 && size == q.length/4)
            resize(q.length/2); // shrink array size
        return item;
        
    }
    public Item sample()                     // return (but do not remove) a random item
    {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");
        int i = StdRandom.uniform(size);
        Item item = q[i];
        return item;
        
    }
    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new RandomizedQueueIterator();
        
    }
    
    private class RandomizedQueueIterator implements Iterator<Item>
    {
        private Item[] temp;
        private int tempSize;

        public RandomizedQueueIterator() {
            temp = (Item[]) new Object[size];
            for (int i = 0; i < size; i++)
                temp[i] = q[i];
            // temp = q; // the second iterator will not work
            tempSize = size;
        }

        public boolean hasNext() {
            return tempSize != 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) 
                throw new NoSuchElementException();
            int i = StdRandom.uniform(tempSize);
            Item item = temp[i];
            temp[i] = temp[tempSize-1];
            temp[tempSize-1] = null;
            tempSize--;
            return item;
        }
    }
    public static void main(String[] args)   // unit testing
    {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) q.enqueue(item);
            else if (!q.isEmpty()) StdOut.print(q.dequeue() + " ");
        }
        Iterator<String> i = q.iterator();
        while (i.hasNext())
        {
            String s = i.next();
            StdOut.println(s);
        }
        Iterator<String> i2 = q.iterator();
        while (i2.hasNext())
        {
            String s = i2.next();
            StdOut.println(s);
        }
        StdOut.println("(" + q.size() + " left on queue)");
    }
 }
