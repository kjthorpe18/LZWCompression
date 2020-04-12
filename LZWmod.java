
public class LZWmod {
    private static int R = 256;        // number of input chars
    private static int W = 9;         // codeword width
    private static int L = (int) Math.pow(2, W);       // number of codewords = 2^W

    public static void compress(boolean reset) { 
    	BinaryStdOut.write(reset);
        // Initialize st with alphabet
        TrieST<Integer> st = new TrieST<Integer>();
        for (int i = 0; i < R; i++)         
        {
            StringBuilder s = new StringBuilder("" + (char) i);
            st.put(s, i);
        }
		int code = R + 1;

        StringBuilder word = new StringBuilder();
        char c;
        
            while(true)
            {	
                c = BinaryStdIn.readChar();
                word.append(c);
                if (!st.contains(word))     // If a word is not found, add it to dictionary and output code of longest prefix
                {   
                	word.deleteCharAt(word.length()-1);
                	BinaryStdOut.write(st.get(word), W);   // output code
                	word.append(c);

                	if ((code == L) && (W == 16) && (reset == true))	// Reset the dictionary
                	{
                		W = 9;
                		L = (int) Math.pow(2, W);
                		code = R+1;
                		st = new TrieST<Integer>();
                        for (int i = 0; i < R; i++)         
        				{
            				StringBuilder s = new StringBuilder("" + (char) i);
            				st.put(s, i);
        				}
                	}
                	if ((code == L) && (W < 16))  // Increment the codeword
                    {
                        W++;
                        L = (int) Math.pow(2, W);
                    }
                    if (code < L)
                    {   
                        st.put(word, code++);
                    }

					word.setLength(0);
            		word.append(c);
                }

                if (BinaryStdIn.isEmpty())  // Checks if there is a next character, breaks if not
                {
                	BinaryStdOut.write(st.get(word), W);
                    break;
                }
            }

        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 


    public static void expand() {
        boolean reset = BinaryStdIn.readBoolean();  // Gets the reset bit
        String[] st = new String[65536];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
        	BinaryStdOut.write(val);				// Print the character
        	codeword = BinaryStdIn.readInt(W);		// Get the next codeword
        	if (codeword == R) break;				// If end of file stop
            String s = st[codeword];				// Get the string associated w/ the codeword
            if (i == codeword) s = val + val.charAt(0);   	// special case hack
            if (i < L) st[i++] = val + s.charAt(0);			// Put string/codeword pair into array
            val = s;        								// Get the string for next iteration

            if ((i == L) && (W == 16) && (reset == true))
            {
            	//System.out.println("RESETTING");
            	W = 9;
            	L = (int) Math.pow(2, W);
            	// initialize symbol table with all 1-character strings
            	st = new String[65536];
        		for (i = 0; i < R; i++)
            		st[i] = "" + (char) i;
       		 	st[i++] = "";                 // (unused) lookahead for EOF
            }     
			// If the last codeword is reached, increment W (size) and resize L (# of codewords)
            if ((i == L) && (W < 16))  
            {
            	//System.out.println("RESIZING");
                W++;
                L = (int) Math.pow(2, W);
            }
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
    	if (args.length == 2)
    	{
    		if      (args[0].equals("-") && args[1].equals("r")) compress(true);
       		else if (args[0].equals("-") && args[1].equals("n")) compress(false);
    	}
        else
        {
        	if (args[0].equals("-")) compress(false);
        	else if (args[0].equals("+")) expand();
        	else throw new RuntimeException("Illegal command line argument");
        }
    }
}
