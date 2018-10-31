package week5;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int N;
    public KdTree()                               // construct an empty set of points 
    {
        N = 0;
    }
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
      
        public Node(Point2D p) {
            this.p = p;
        }
     }
    
    public boolean isEmpty()                      // is the set empty? 
    {
        return N == 0;
        
    }
    public int size()                         // number of points in the set 
    {
        return N;
        
    }
    /**
     * @param p
     */
    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new NullPointerException();
        if (!contains(p)) {
            RectHV r = new RectHV(0, 0, 1, 1);
            root = insert(root, p, false, r);
            N++;
        }
    }
    private Node insert(Node n, Point2D p, boolean horizontal, RectHV r) {
        if (n == null) {
            Node node = new Node(p);
            node.rect = r;
            return node;
        }
        if (horizontal) { // horizontal splits: compare y
            if (p.y() < n.p.y()) { // put p into the bottom subdivision
                if (n.lb != null) r = n.lb.rect;
                else r = new RectHV(r.xmin(), r.ymin(), r.xmax(), n.p.y());
                n.lb = insert(n.lb, p, false, r); 
            }
            else { // put p into the top division
                if (n.rt != null) r = n.rt.rect;
                else r = new RectHV(r.xmin(), n.p.y(), r.xmax(), r.ymax());
                n.rt = insert(n.rt, p, false, r); 
            }
        }
        else { // vertical splits: compare x
            if (p.x() < n.p.x()) { // put p into the left subdivision
                if (n.lb != null) r = n.lb.rect;
                else r = new RectHV(r.xmin(), r.ymin(), n.p.x(), r.ymax());
                n.lb = insert(n.lb, p, true, r);
            }
            else { // put p into the right subdivision
                if (n.rt != null) r = n.rt.rect;
                else r = new RectHV(n.p.x(), r.ymin(), r.xmax(), r.ymax());
                n.rt = insert(n.rt, p, true, r);
            }
        }
        return n;
    }
    
    /**
     * @param p
     * @return
     */
    public boolean contains(Point2D p)            // does the set contain point p? 
    {
        if (p == null) throw new NullPointerException();
        return contains(root, p, false);
        
    }
    private boolean contains(Node n, Point2D p, boolean horizontal) 
    {
        if (n == null) return false; // reach the end of the tree
        
        if (p.equals(n.p)) return true;
        
        if (horizontal) { // horizontal splits: compare y
            if (p.y() < n.p.y()) return contains(n.lb, p, false);
            else return contains(n.rt, p, false);
        }
        else { // vertical splits: compare x
            if (p.x() < n.p.x()) return contains(n.lb, p, true);
            else return contains(n.rt, p, true);
        }
        
    }
    public void draw()                         // draw all points to standard draw 
    {
        draw(root, false); 
    }
    private void draw(Node n, boolean horizontal) {
        if (n == null) return;
        
        StdDraw.setPenRadius(.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        n.p.draw();
        
        StdDraw.setPenRadius(.002);
        if (horizontal) { // horizontal splits in blue: draw a horizontal line within the rectangle
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
            draw(n.lb, false);
            draw(n.rt, false);
        }
        else { // vertical splits in red: draw a vertical line within the rectangle
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
            draw(n.lb, true);
            draw(n.rt, true);
        } 
    }
    
    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle 
    {
        if (rect == null) throw new NullPointerException();
        Queue<Point2D> q = new Queue<Point2D>();
        range(root, q, rect);
        return q;
        
    }
    private void range(Node n, Queue<Point2D> q, RectHV rect) 
    {
        if (n == null) return;
        if (rect.contains(n.p)) q.enqueue(n.p);
        if (n.rect.intersects(rect)) {
            range(n.lb, q, rect);
            range(n.rt, q, rect);
        }
    }
    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 
    {
        if (p == null) throw new NullPointerException();
        if (root == null) return null; // null if the set is empty
        return nearest(root, p, root.p); 
    }
    private Point2D nearest(Node n, Point2D p, Point2D neighbor) 
    {
        // if (n == null) return neighbor; // unnecessary check
        // update the neighbor if n is closer to the query point
        if (n.p.distanceSquaredTo(p) < neighbor.distanceSquaredTo(p)) {
            neighbor = n.p;
        }
        
        double dist = neighbor.distanceSquaredTo(p);
        if (n.lb != null && n.rt != null) {
            double lbDist = n.lb.rect.distanceSquaredTo(p);
            double rtDist = n.rt.rect.distanceSquaredTo(p);
            // choose the division closer to the query point
            if (lbDist < rtDist) {
                // search the node when the rectangle distance to p is smaller than the current neighbor
                if (lbDist < dist) 
                    neighbor = nearest(n.lb, p, neighbor);
                if (rtDist < neighbor.distanceSquaredTo(p))
                    neighbor = nearest(n.rt, p, neighbor);
            }
            else {
                if (rtDist < dist)
                    neighbor = nearest(n.rt, p, neighbor);
                if (lbDist < neighbor.distanceSquaredTo(p))
                    neighbor = nearest(n.lb, p, neighbor);
            }
        }
        else if (n.lb != null && n.rt == null) {
            double lbDist = n.lb.rect.distanceSquaredTo(p);
            if (lbDist < dist) 
                neighbor = nearest(n.lb, p, neighbor);
        }
        else if (n.lb == null && n.rt != null) {
            double rtDist = n.rt.rect.distanceSquaredTo(p);
            if (rtDist < dist) 
                neighbor = nearest(n.rt, p, neighbor);
        }
        return neighbor;
    }
    
    public static void main(String[] args)                  // unit testing of the methods (optional) 
    {
        String filename = args[0];
        In in = new In(filename);

        StdDraw.show(0);

        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        //StdOut.println(kdtree.contains(new Point2D(0.5, 0.5)));
        //StdOut.println(kdtree.contains(new Point2D(0.5, 0.4)));
        
        StdDraw.clear();
        kdtree.draw();
        StdDraw.show(0);
        StdDraw.show(40);
    }
}
