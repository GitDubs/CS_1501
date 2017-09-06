/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class MyLZW {
    private static final int R = 256;        // number of input chars
    private static final int MAX_BITS = 16;       // max width of a codeword
    private static String mode;     //Specifies Do Nothing, Reset, Or Monitor (n,r,m)
    private static final double THRESHOLD = 1.1;
    
    public static void compress() { 
        int W = 9;
        int L = (int)Math.pow(2,W);
        double originalSize;
        double compressionRatio = 0;
        double compressedData = 0;
        
        
        String input = BinaryStdIn.readString();
        originalSize = input.length();
        
        TST<Integer> st = new TST<Integer>();                   //Symbol table
        for (int i = 0; i < R; i++)                             //Fill the st with the first 256 chars
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF, code will tell us where to place the new pattern

        BinaryStdOut.write(st.get(mode), W);                    //Write the mode to the beginning of the file
        if(mode.equals("m"))
        	 BinaryStdOut.write(originalSize);		//Write the original file size, so expansion can monitor compression ratio
        
        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);               //Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);                   //Print s's encoding.
            int t = s.length();
            
            if (t < input.length()){                             //Add s to symbol table.
                if(code < L)
                        st.put(input.substring(0, t + 1), code++);
                    else if(W < MAX_BITS){
                        W++;
                        L = (int)Math.pow(2,W);
                        st.put(input.substring(0, t + 1), code++);
                    }else if(mode .equals("r")){
                        st = new TST<Integer>();
                        for (int i = 0; i < R; i++)
                            st.put("" + (char) i, i);
                        code = R+1;
                        W = 9;
                        L = (int)Math.pow(2,W);
                    }else if(mode.equals("m")){
                        if(compressionRatio == 0){
                            compressionRatio = originalSize / compressedData; 
                        }else{
                            double newRatio = originalSize / compressedData;
                            if(compressionRatio / newRatio > THRESHOLD){
                                st = new TST<Integer>();
                                for (int i = 0; i < R; i++)
                                    st.put("" + (char) i, i);
                                code = R+1;
                                W = 9;
                                L = (int)Math.pow(2,W);
                            }
                        }
                        
                    }
            }
            input = input.substring(t);                         // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 


    public static void expand() {
        int W = 9;
        int L = (int)Math.pow(2,W);
        double originalSize = 0;
        double compressionRatio = 0;
        double compressedData = 0;
        String[] st = new String[(int)Math.pow(2,MAX_BITS)];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF
        
        mode = st[BinaryStdIn.readInt(W)];
        if(mode.equalsIgnoreCase("m"))
            originalSize = BinaryStdIn.readDouble();
            
        int codeword = BinaryStdIn.readInt(W);
        compressedData += (W/8);
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];

        while (true) {
            if(i == L){
                if(W < MAX_BITS){
                    W++;
                    L = (int)Math.pow(2,W);
                }else if(mode.equals("r")){
                    W = 9;
                    L = (int)Math.pow(2,W);       	
                    st = new String[(int)Math.pow(2,MAX_BITS)];

                    for (i = 0; i < R; i++)
                    st[i] = "" + (char) i;
                    st[i++] = "";
                }else if(mode.equals("m")){
                    if(compressionRatio == 0){
                        compressionRatio = originalSize / compressedData; 
                    }else{
                        double newRatio = originalSize / compressedData;
                        if(compressionRatio / newRatio > THRESHOLD){
                            compressionRatio = originalSize / compressedData;
                            W = 9;
                            L = (int)Math.pow(2,W);       	
                            st = new String[(int)Math.pow(2,MAX_BITS)];

                            for (i = 0; i < R; i++)
                            st[i] = "" + (char) i;
                            st[i++] = "";
                        }
                    }
                }
            }
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            compressedData += (W/8);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
        if(args[0].equals("-")){
            mode = args[1];
            compress();
        }
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
