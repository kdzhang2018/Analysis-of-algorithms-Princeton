package week8;

import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture p;
    private int w;
    private int h;
    private double[][] e;
    
    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {
        if (picture == null) throw new NullPointerException();
        p = new Picture(picture);
        w = p.width();
        h = p.height();
        
        e = new double[w][h];
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                e[col][row] = energy(col, row);
            }
        }
    }
    
    public Picture picture()                          // current picture
    {
        return new Picture(p); 
    }
    
    public     int width()                            // width of current picture
    {
        return w;
    }
    
    public     int height()                           // height of current picture
    {
        return h;
    }
    
    public  double energy(int x, int y)               // energy of pixel at column x and row y
    {
        if (x < 0 || x >= w || y < 0 || y >= h) throw new IndexOutOfBoundsException();
        if (x == 0 || x == w - 1 || y == 0 || y == h - 1) return 1000.0;
        
        double en;
        double deltaX, deltaY;
        
        Color c1 = p.get(x + 1, y);
        Color c2 = p.get(x - 1, y);
        deltaX = Math.pow(c1.getRed() - c2.getRed(), 2) + Math.pow(c1.getGreen() - c2.getGreen(), 2) + Math.pow(c1.getBlue() - c2.getBlue(), 2);
        
        Color c3 = p.get(x, y + 1);
        Color c4 = p.get(x, y - 1);
        deltaY = Math.pow(c3.getRed() - c4.getRed(), 2) + Math.pow(c3.getGreen() - c4.getGreen(), 2) + Math.pow(c3.getBlue() - c4.getBlue(), 2);
        
        en = Math.sqrt(deltaX + deltaY);
        return en;
        
    }
    
    public   int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    {
        // transpose
        int temp = h;
        h = w;
        w = temp;
        
        double[][] tempE = e;
        double[][] transE = new double[w][h];
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                transE[col][row] = e[row][col];
            }
        }
        e = transE;
        
        int[] seam = findVerticalSeam();
        
        // transpose back
        e = tempE;
        w = h;
        h = temp;
        
        return seam;
        
    }
    
    public   int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        int[] seam = new int[h];
        if (w == 1 || h == 1) return seam;
        
        int[][] edgeTo = new int[w][h];
        double[][] distTo = new double[w][h];
        
        // the dist of the pixels on the first row is 1000
        for (int col = 0; col < w; col++) {
            distTo[col][1] = 1000.0;
        }
        
        for (int row = 1; row < h; row++) {
            for (int col = 0; col < w; col++) {
                // pixels on the left edge
                if (col == 0) {
                    if (distTo[col][row - 1] <= distTo[col + 1][row - 1]) {
                        distTo[col][row] = distTo[col][row - 1] + e[col][row];
                        edgeTo[col][row] = col;
                    }
                    else if (distTo[col + 1][row - 1] <= distTo[col][row - 1]) {
                        distTo[col][row] = distTo[col + 1][row - 1] + e[col][row];
                        edgeTo[col][row] = col + 1;
                    }
                }
                // pixels on the right edge
                else if (col == w - 1) {
                    if (distTo[col][row - 1] <= distTo[col - 1][row - 1]) {
                        distTo[col][row] = distTo[col][row - 1] + e[col][row];
                        edgeTo[col][row] = col;
                    }
                    else if (distTo[col - 1][row - 1] <= distTo[col][row - 1]) {
                        distTo[col][row] = distTo[col - 1][row - 1] + e[col][row];
                        edgeTo[col][row] = col - 1;
                    }
                }
                // middle pixels
                else {
                    if (distTo[col - 1][row - 1] <= distTo[col][row - 1] && distTo[col - 1][row - 1] <= distTo[col + 1][row - 1]) {
                        distTo[col][row] = distTo[col - 1][row - 1] + e[col][row];
                        edgeTo[col][row] = col - 1;
                    }
                    else if (distTo[col][row - 1] <= distTo[col - 1][row - 1] && distTo[col][row - 1] <= distTo[col + 1][row - 1]) {
                        distTo[col][row] = distTo[col][row - 1] + e[col][row];
                        edgeTo[col][row] = col;
                    }
                    else if (distTo[col + 1][row - 1] <= distTo[col - 1][row - 1] && distTo[col + 1][row - 1] <= distTo[col][row - 1]) {
                        distTo[col][row] = distTo[col + 1][row - 1] + e[col][row];
                        edgeTo[col][row] = col + 1;
                    }
                }
            }
        }
        // find the pixel on the last row with smallest dist, which is seam[h-1]
        double min = Double.POSITIVE_INFINITY;
        for (int col = 0; col < w; col++) {
            if (distTo[col][h - 1] < min) {
                min = distTo[col][h - 1];
                seam[h - 1] = col;
            }
        }
        // trace back from the pixel on the last row to the first row
        for (int i = h - 2; i >= 0; i--) {
            seam[i] = edgeTo[seam[i+1]][i+1];
        }
        return seam; 
    }
    
    public    void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {
        transpose();
        
        removeVerticalSeam(seam);
        
        // transpose back
        transpose();
    }
    
    private void transpose() {
        int temp = h;
        h = w;
        w = temp;
        
        Picture transPic = new Picture(w, h);
        double[][] transE = new double[w][h];
        
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                transPic.set(col, row, p.get(row, col));
                transE[col][row] = e[row][col];
            }
        }
        
        p = transPic;
        e = transE;   
    }
    
    public    void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    {
        if (seam == null) throw new NullPointerException();
        if (w <= 1 || seam.length != h) throw new IllegalArgumentException();
        
        w--;
        
        // construct the new picture
        Picture temp = new Picture(w, h);
        for (int row = 0; row < h; row++) {
            int r = seam[row];
            if (r > w || r < 0) throw new IllegalArgumentException();
            if (row < h - 1 && Math.abs(r - seam[row + 1]) > 1) throw new IllegalArgumentException();
            
            for (int col = 0; col < w; col++) {
                
                if (col < r)
                    temp.set(col, row, p.get(col, row));
                else
                    temp.set(col, row, p.get(col + 1, row));
            }
        }
        p = temp; 

        // reconstruct energy 2D array
        double[][] tempE = new double[w][h];
        for (int col = 0; col < w; col++) {
            tempE[col][0] = 1000.0;
            tempE[col][h - 1] = 1000.0;
        }
        
        for (int row = 1; row < h - 1; row++) {
            int r = seam[row]; 
            
            // if the first pixel is removed, energy of all the right pixels is shifted to left
            if (r == 0) {
                tempE[0][row] = 1000.0;
                for (int col = 1; col < w; col++) {
                    tempE[col][row] = e[col + 1][row];
                }
            }
            
            // if the last pixel is removed, all the left pixels have unchanged energy
            else if (r == w) {
                tempE[w - 1][row] = 1000.0;
                //System.arraycopy(src, srcPos, dest, destPos, length);
                for (int col = 0; col < w - 1; col++) {
                    tempE[col][row] = e[col][row];
                }
            }
            
            // if the pixel in the middle is removed, recalculate the energies for the pixels on the left and on the right
            // and shift the rest right pixels
            else {
                for (int col = 0; col < r - 1; col++) {
                    tempE[col][row] = e[col][row];
                }
                for (int col = r + 1; col < w; col++) {
                    tempE[col][row] = e[col + 1][row];
                }
                tempE[r][row] = energy(r, row);
                tempE[r - 1][row] = energy(r - 1, row);
            }
            
        }
        e = tempE;
    }
}
