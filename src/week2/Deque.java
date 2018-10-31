package week2;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;
    private class Node 
    {
        private Item item;
        private Node previous;
        private Node next;
    }
    public Deque()                           // construct an empty deque
    {
        first = null;
        last = null;
        size = 0;
        
    }
    public boolean isEmpty()                 // is the deque empty?
    {
        return size == 0;
        
    }
    public int size()                        // return the number of items on the deque
    {
        return size;
    }
    public void addFirst(Item item)          // add the item to the front
    {
        if (item == null)
            throw new java.lang.NullPointerException();
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        if (isEmpty())
            last = first;
        else
            oldfirst.previous = first;
        size++;
    }
    public void addLast(Item item)           // add the item to the end
    {
        if (item == null)
            throw new java.lang.NullPointerException();
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.previous = oldlast;
        if (isEmpty()) 
            first = last;
        else
            oldlast.next = last;
        size++;
    }
    public Item removeFirst()                // remove and return the item from the front
    {
        if (isEmpty()) 
            throw new NoSuchElementException("Queue underflow");
        Item item = first.item;
        first = first.next;
        size--;
        if (isEmpty())
            last = first;
        else
            first.previous = null;
        return item;  
    }
    
    public Item removeLast()                 // remove and return the item from the end
    {
        if (isEmpty()) 
            throw new NoSuchElementException("Queue underflow");
        Item item = last.item;
        last = last.previous;
        size--;
        if (isEmpty())
            first = last;
        else
            last.next = null;
        return item;  
    }
    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        return new ListIterator();  
    }
    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext()
        {
            return current != null; 
        }
        public void remove()
        {
            throw new UnsupportedOperationException();  
        }

        public Item next() 
        {
            if (!hasNext()) 
                throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }
    public static void main(String[] args)   // unit testing
    {
        Deque<String> q = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) q.addFirst(item);
            //if (!item.equals("-")) q.addLast(item);
            else if (!q.isEmpty()) StdOut.print(q.removeLast() + " ");
            //else if (!q.isEmpty()) StdOut.print(q.removeFirst() + " ");
        }
        Iterator<String> i = q.iterator();
        while (i.hasNext())
        {
            String s = i.next();
            StdOut.println(s);
        }
        StdOut.println("(" + q.size() + " left on queue)");
    }
 }
