package week3;

import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lines = new ArrayList<LineSegment>();
    
    
    public FastCollinearPoints(Point[] points) { // finds all line segments containing 4 or more points
        if (points == null) 
            throw new NullPointerException();
        Point[] temp = new Point[points.length]; // temporary Point array for sorting
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new NullPointerException();
            temp[i] = points[i];
        }

        for (int i = 0; i < points.length; i++) { // iterate through the points to check duplicates
            //StdOut.println("point " + i + ": " + points[i]);
            if (points.length == 1) break;
            Arrays.sort(temp, points[i].slopeOrder()); 
            if (points[i].compareTo(temp[1]) == 0) // repeated points (slope -infinity) will be temp[1]  
                throw new IllegalArgumentException("repeated points");

            int j = 1;
            while (j < temp.length-2) { // only iterate when there are 4 or more points
                //StdOut.println(j + ": " + temp[j]);
                int k = j+1;
                while (k < temp.length) {
                    // point k is not collinear with i and j
                    if (points[i].slopeTo(temp[j]) != points[i].slopeTo(temp[k])) break;
                    k++;
                }
                if (k-j > 2) { // j, j+1, ..., j+k-1 are collinear with i
                    Arrays.sort(temp, j, k); // sort j to j+k-1 in an ascending order
                    //StdOut.println(j + " to " + (k-1));
                    Point start = temp[j];
                    Point end = temp[k-1];
                    if (points[i].compareTo(start) < 0) start = points[i];
                    if (points[i].compareTo(end) > 0) end = points[i];
                    //StdOut.println(start + " -> " + end);
                    if (points[i].compareTo(start) == 0) { // only add the segment if i is start
                        LineSegment ls = new LineSegment(start, end);
                        //StdOut.println("LineSegment added: " + ls);
                        lines.add(ls);
                    }
                }
                j = k;
            }
        }
        
        // lines with 5 or more points appear more than once in different combinations:
        /*for (int i = 0; i < points.length-1; i++) { // iterate through the points to check duplicate
            //StdOut.println();
            //StdOut.println("point " + i + ": " + points[i]);
            // sort from i to the array end
            // for every iteration, temp[i] is points[i]
            // in the next iteration, temp[i] will not be considered to avoid duplicate lines
            Arrays.sort(temp, i, temp.length, points[i].slopeOrder()); 
            if (points[i].compareTo(temp[i+1]) == 0) // repeated points (slope -infinity) will be temp[i+1]  
                throw new IllegalArgumentException("repeated points");
            
            int j = i+1;
            while (j < temp.length-2) {
                //StdOut.println(j + ": " + temp[j]);
                int k = j+1;
                while (k < temp.length) {
                    // point k is not collinear with i and j
                    if (points[i].slopeTo(temp[j]) != points[i].slopeTo(temp[k])) break;
                    k++;
                }
                if (k-j > 2) { // j, j+1, ..., j+k-1 are collinear with i
                    Arrays.sort(temp, j, k); // sort j to j+k-1 in an ascending order
                    //StdOut.println(temp[j] + ", " + temp[j+1] + ", " + temp[j+2]);
                    Point start = temp[j];
                    Point end = temp[k-1];
                    if (points[i].compareTo(start) < 0) start = points[i];
                    if (points[i].compareTo(end) > 0) end = points[i];
                    LineSegment ls = new LineSegment(start, end);
                    //StdOut.println(ls);
                    lines.add(ls);
                }
                j = k;
            }
        }*/
        
        // Did not pass the timing tests:
        /*ArrayList<Point> lineEnds = new ArrayList<Point>(); // record line ends to avoid duplicate lines
        Point[] temp = new Point[points.length]; // temporary Point array for sorting
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new NullPointerException();
            temp[i] = points[i];
        }
       
        for (int i = 0; i < points.length; i++) { // iterate through all the points

            //StdOut.println();
            //StdOut.println(points[i]);
            Arrays.sort(temp, points[i].slopeOrder()); // NlogN operations
            if (points[i].compareTo(temp[1]) == 0) // repeated points (slope -infinity) will be temp[1]  
                throw new IllegalArgumentException("repeated points");

            int j = 1;
            while (j < temp.length-2) {
                //StdOut.println(j + ": " + temp[j]);
                if (points[i].slopeTo(temp[j]) == points[i].slopeTo(temp[j+1]) 
                        && points[i].slopeTo(temp[j]) == points[i].slopeTo(temp[j+2])) {
                    int k = 3;
                    while (j + k < temp.length) { // k-1 is the total number of points on one line
                        if (points[i].slopeTo(temp[j]) != points[i].slopeTo(temp[j+k])) break;
                        k++;
                    }
                    Arrays.sort(temp, j, j+k); // sort j to j+k-1 in an ascending order
                    //StdOut.println(temp[j] + ", " + temp[j+1] + ", " + temp[j+2]);
                    Point start = temp[j];
                    Point end = temp[j+k-1];
                    if (points[i].compareTo(start) < 0) start = points[i];
                    if (points[i].compareTo(end) > 0) end = points[i];

                    Boolean addLine = true;
                    for (int l = 0; l < lineEnds.size() - 1; l = l + 2) {
                        if (lineEnds.get(l).compareTo(start) == 0 && lineEnds.get(l+1).compareTo(end) == 0) {
                            // the same line has already been added to lines
                            addLine = false; 
                            break;
                        }
                    }
                    if (addLine) { // only add the line if the line has not been added to lines
                        lineEnds.add(start);
                        lineEnds.add(end);
                        LineSegment ls = new LineSegment(start, end);
                        //StdOut.println(ls);
                        lines.add(ls);
                    }
                    j = j + k;
                }
                else j++;
            }
        }*/
        
    }
    
    /**
     * @return
     */
    public int numberOfSegments() {       // the number of line segments
        return lines.size();
    }
    
    /**
     * @return
     */
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
 }