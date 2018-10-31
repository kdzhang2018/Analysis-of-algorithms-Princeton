package week5;

import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private SET<Point2D> pointSet;
    public PointSET()                               // construct an empty set of points 
    {
        pointSet = new SET<Point2D>();
    }
    public boolean isEmpty()                      // is the set empty? 
    {
        return pointSet.size() == 0;
        
    }
    public int size()                         // number of points in the set 
    {
        return pointSet.size();
        
    }
    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new NullPointerException();
        if (!contains(p)) {
            pointSet.add(p);
        }
    }
    public boolean contains(Point2D p)            // does the set contain point p? 
    {
        if (p == null) throw new NullPointerException();
        return pointSet.contains(p);
        
    }
    public void draw()                         // draw all points to standard draw 
    {
        Iterator<Point2D> it = pointSet.iterator();
        while (it.hasNext()) {
            Point2D p = it.next();
            StdDraw.setPenRadius(.01);
            StdDraw.setPenColor(StdDraw.BLACK);
            p.draw();
        }
    }
    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle 
    {
        if (rect == null) throw new NullPointerException();
        Queue<Point2D> q = new Queue<Point2D>();
        Iterator<Point2D> it = pointSet.iterator();
        while (it.hasNext()) {
            Point2D p = it.next();
            if (rect.contains(p))
                q.enqueue(p);
        }
        return q;
        
    }
    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 
    {
        if (p == null) throw new NullPointerException(); 
        Iterator<Point2D> it = pointSet.iterator();
        double d = Double.POSITIVE_INFINITY;
        Point2D neighbor = null;
        while (it.hasNext()) {
            Point2D point = it.next();
            if (point.distanceSquaredTo(p) < d) {
                d = point.distanceSquaredTo(p);
                neighbor = point;
            }     
        }
        return neighbor;
        
    }

    public static void main(String[] args)                  // unit testing of the methods (optional) 
    {
        String filename = args[0];
        In in = new In(filename);

        StdDraw.show(0);

        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        //StdOut.println(brute.contains(new Point2D(0.5, 0.5)));
        //StdOut.println(brute.contains(new Point2D(0.5, 0.4)));
        
        StdDraw.clear();
        brute.draw();
        StdDraw.show(0);
        StdDraw.show(40);
    }
}
