package week3;

import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> lines = new ArrayList<LineSegment>();
    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
        if (points == null) 
            throw new NullPointerException();
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) 
                throw new NullPointerException(); // check null
            for (int j = i+1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("repeated points");
            }    
        }
        

        for (int i = 0; i < points.length-3; i++) { // only iterate when there are 4 or more points

            for (int j = i+1; j < points.length-2; j++) {

                for (int k = j+1; k < points.length-1; k++) {
                    if (points[k].slopeTo(points[i]) != points[j].slopeTo(points[i])) continue; 
                    // continue to the next point if the first three points are not collinear

                    for (int l = k+1; l < points.length; l++) {
                        if (points[l].slopeTo(points[i]) == points[j].slopeTo(points[i])) {
                            //StdOut.println(points[i] + ", " + points[j] + ", " + points[k] + ", " + points[l]);
                            Point[] temp = new Point[4];
                            temp[0] = points[i];
                            temp[1] = points[j];
                            temp[2] = points[k];
                            temp[3] = points[l];
                            Arrays.sort(temp); // start to end: ascending order
                            LineSegment ls = new LineSegment(temp[0], temp[3]);
                            lines.add(ls);
                            //StdOut.println(ls);
                        }
                    }
                }
            }
        }
    }
    public int numberOfSegments() {       // the number of line segments
        return lines.size();
    }
    public LineSegment[] segments() {               // the line segments
        return lines.toArray(new LineSegment[lines.size()]);
        /*LineSegment[] segments = new LineSegment[lines.size()];
        for (int i = 0; i < lines.size(); i++)
            segments[i] = lines.get(i);
        return segments; */
    }
    
    public static void main(String[] args) {

        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
 }

