package week12;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256; // extended ASCII
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() 
    {
        char[] alphabet = new char[R];
        for (char i = 0; i < 256; i++)
            alphabet[i] = i;
        while (!BinaryStdIn.isEmpty()) {
            char input = BinaryStdIn.readChar();
            int i;
            for (i = 0; i < R; i++) {
                if (input == alphabet[i])
                    break;    
            }
            BinaryStdOut.write(i, 8);
            /*for (int j = i; j > 0; j--)
                alphabet[j] = alphabet[j-1];*/
            System.arraycopy(alphabet, 0, alphabet, 1, i);
            alphabet[0] = input;
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode()
    {
        char[] alphabet = new char[R];
        for (char i = 0; i < 256; i++)
            alphabet[i] = i;
        while (!BinaryStdIn.isEmpty()) {
            int input = BinaryStdIn.readChar();
            char temp = alphabet[input];
            BinaryStdOut.write(temp);
            /*for (int j = input; j > 0; j--)
                alphabet[j] = alphabet[j-1];*/
            System.arraycopy(alphabet, 0, alphabet, 1, input);
            alphabet[0] = temp;
        }
        BinaryStdOut.flush(); // close()   
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args)
    {
        /*while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            BinaryStdOut.write(c);
        }
        BinaryStdOut.close();*/
        
        if (args[0].equals("-")) 
            encode();
        else if (args[0].equals("+"))
            decode();
    }
}
