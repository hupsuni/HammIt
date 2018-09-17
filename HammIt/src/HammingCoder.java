/**
 * A hamming code generator/checker for positive parity
 * See attached documentation for more information.
 * @author Nick Huppert - s3729119
 *
 */
public class HammingCoder {
	
	/**
	 * Takes a String input representing an integer value, converts it to a
	 * binary string, injects hamming code and returns a String representation
	 * 
	 * @param A string representing an integer
	 * @return Returns a string representing binary data with hamming code injected
	 */
	public String encodeData( String data, boolean verbose ) {
		// Convert string to int, then to binary string
		return encodeBinaryData( Integer.toBinaryString( Integer.parseInt(data) ), verbose );
	}
	public String encodeBinaryData( String data, boolean verbose ) {
		String code = data;
		int bits = calculateParityBits( code );
		
		if ( verbose ) {
			System.out.println("Data to be encoded: " + data);
			System.out.println("As binary: " + code);
			System.out.println("Requires " + bits + " parity bits to represent hamming code");
		}
		
		
		/* Create new bit string with hamming bits inserted as 0s
		 * Make and concatenate substrings based on 2^n where
		 * 2^n represents the location a parity bit would be in
		 * hamming code.
		 * Reverse string first so our references are easier
		 * for index referencing as binary is right to left and
		 * arrays etc are left to right.
		 */
		
		// Reverse string

		code = reverseString( code );
		String tempString = "";
		
		// Add 0s for temporary parity bits and expand string
		int tempIndex = 1;
		int lastIndex = 0;
		int getXBits = 0;
		
		for( int i = 0; i < bits; i++ ) {
			/* 
			 * The following stupid long statement gets a double value of 2^i, converts it to a string
			 * chops off the decimal point and parses it as an integer because reasons.
			 * Also, there is a relationship between 2^i for both where the parity bit is to be
			 * located and how many digits are tacked on.
			 * This code calculates where the parity bits are to go and appends the input string
			 * onto a blank one with a placeholder 0 and 2^i-1 characters each iteration of the loop.
			 */  
			getXBits = Integer.parseInt(Double.toString(Math.pow(2, i)).substring(0, Double.toString(Math.pow(2, i)).length()-2) ); //activeBit *2;
			
			tempString += "0" + code.substring( lastIndex, lastIndex + getXBits -1 <= code.length()? lastIndex + getXBits -1:code.length() );//lastIndex, lastIndex + tempIndex );
			
			lastIndex += getXBits -1;
		}
		if( verbose )
			System.out.println("Reversed string with 0s for parity bits: " + tempString);
		code = tempString;
		
		boolean parity = true;
		int currentBit = 0;
		
		for( int i = 0; i < bits; i++ ) {
			// Calculate the index of each parity bit and which bits it checks.
			tempIndex = Integer.parseInt(Double.toString(Math.pow(2, i)).substring(0, Double.toString(Math.pow(2, i)).length() -2));
			currentBit = tempIndex;
			lastIndex = tempIndex - 1;
			
			/*
			 *  This chops up substrings that the current parity bit is to check and checks their parity
			 *  using a bool, if the overall result is false the parity is odd and a 1 replaces the 0 placeholder
			 *  If the boolean is true the placeholder 0 remains.
			 */
			
			while( lastIndex < code.length() ) {
				if( !checkParity( code.substring( lastIndex, lastIndex + tempIndex < code.length()? lastIndex + tempIndex:code.length())) )
					parity = !parity;
				
				lastIndex += 2 * tempIndex;
			}
			
			// If the value for parity is false, overwrite the 0 parity bit with a 1
			if( !parity && currentBit < code.length() )
				code = code.substring(0, currentBit -1 ) + "1" + code.substring(currentBit , code.length());
			parity = true;
		}
		
		if( verbose )
			System.out.println("Reversed with hamming code: " + code );
		
		code = reverseString(code);
		
		if( verbose )
			System.out.println("Bit order corrected: " + code);
		
		return code;
	}
	
	public String encodeData( String data ) {
		return encodeData( data, false );
	}
	
	public String encodeBinaryData( String data ) {
		return encodeBinaryData( data, false );
	}
	
	public String checkData( String data, boolean verbose ) throws HammingCodeException {
		String code = reverseString( data );
		int bits = inferParityBits( code );
		int tempIndex = 0;
		int lastIndex = 0;
		int currentBit = 0;
		boolean checkBits[] = new boolean[bits];
		
		if( verbose )
			System.out.println( "Code to be checked: " + data );
		
		// Initialize array as true, each element corresponds to a parity bit at 2^i
		for( int i = 0; i < checkBits.length; i++ )
			checkBits[i] = true;
		
		for( int i = 0; i < bits; i++ ) {
			tempIndex = Integer.parseInt(Double.toString(Math.pow(2, i)).substring(0, Double.toString(Math.pow(2, i)).length() -2));
			currentBit = tempIndex;
			lastIndex = tempIndex - 1;
			
			/*
			 *  This chops up substrings that the current parity bit is to check and checks their parity
			 *  which is stored in a boolean array where each index represents the parity bit at 2^i
			 *  As each substring returns false the value is toggled meaning an even amount of odd parity
			 *  resolves to an overall even parity.
			 */
			while( lastIndex < code.length() ) {
				if( !checkParity( code.substring( lastIndex, lastIndex + tempIndex < code.length()? lastIndex + tempIndex:code.length())) )
					checkBits[i] = !checkBits[i];
				
				lastIndex += 2 * tempIndex;
			}
			
			// If the penultimate result is false, the bits covered by this check bit 
			// are not even parity so set the overall value to false
			if( !checkBits[i] && currentBit < code.length() ) {
				if( verbose )
					System.out.println( "Bit #" + i + " is not correct");
				checkBits[i] = false;
			}
						
		}
		/*
		 *  Iterate through the array of booleans, if a false is recovered then
		 *  that bit is showing a parity error. The location of the changed bit
		 *  will be them sum of 2^i for each index i that returns false.
		 */
		int bitInError = 0;
		for( int i = 0; i < checkBits.length; i++ )
			if( !checkBits[i] )
				bitInError += Integer.parseInt(Double.toString(Math.pow(2, i)).substring(0, Double.toString(Math.pow(2, i)).length() -2));
		
		// If the error bit is a location outside the given string the code is invalid
		if( bitInError > code.length() )
			throw new HammingCodeException( "Hamming code is not valid! Can not correct." );
		
		bitInError--; // Convert from real location to array appropriate reference
		// Correct bit in error, -1 value means no error was found
		if( bitInError >= 0 ) {
			if( verbose )
				System.out.println( "The incorrect bit is in location " + (bitInError +1) );
			if( code.charAt( bitInError ) == '1' ) {
				code = code.substring(0, bitInError ) + "0" + code.substring(bitInError +1, code.length());
			} else {
				code = code.substring(0, bitInError ) + "1" + code.substring(bitInError +1 , code.length());
			}
				
		} else {
			if( verbose ) 
				System.out.println("The hamming code has detected no errors");
		}
		
		code = reverseString( code );
		return code;
	}
	
	public String checkData( String data ) throws HammingCodeException {
		return checkData( data, false ); // Operate the decoder non-verbosely as standard.
	}
	
	/*
	 * Take a string input, assume it contains hamming code and
	 * calculate how many hamming bits must be present based
	 * on length of input string.
	 */
	public int inferParityBits( String binaryData ) {
		int bits = 0;
		for( int i = 0; Math.pow(2, bits ) <= binaryData.length(); i++ ){
			bits = i +1;
		}
		return bits;
	}
	
	/*
	 * Take a string input, assume it contains no hamming code
	 * and calculate the required ammount of bits needed to
	 * implement a hamming code.
	 */
	public int calculateParityBits( String binaryData ) {
		int bits = 0;
		for( int i = 0; Math.pow(2, bits ) <= binaryData.length() + bits; i++ ){
			bits = i;
		}
		return bits;
	}
	
	/**
	 * Checks a binary string to see if it is even or odd parity
	 * @param Binary String
	 * @return True: Even parity, False: Odd parity
	 */
	public boolean checkParity( String data ) {
		//System.out.println(data); // Prints our received string for debugging
		boolean parity = true;
		char dataAsChars[] = data.toCharArray();
		for( char c: dataAsChars ) {
			if( c == '1' )
				parity = !parity;
		}
		
		return parity;
	}
	
	// Its easier to work with a reversed string for arrays than a regular binary string
	public String reverseString(  String data ) {
		String tempString = "";
		for( int i = 1; i <= data.length(); i++ ) {
			tempString += data.charAt(data.length()-i);
		}
		return tempString;
	}
	
	// Return a string of binary data with the hamming code removed
	public String removeParityBits( String data ) {
		String code = reverseString( data );
		String tempString = "";
		int bits = inferParityBits( data );

		int[] parityLocations = new int[bits];
		// Calculate where each parity bit is as an index reference
		for( int i = 0; i < bits; i++ ) {
			parityLocations[i] = Integer.parseInt(Double.toString(Math.pow(2, i)).substring(0, Double.toString(Math.pow(2, i)).length() -2)) -1;
		}
		// Strip out the bits
		int lastIndex = 0;
		for( int i:parityLocations ) {
			tempString += code.substring(lastIndex, i);
			lastIndex = i +1;
		}
		if( lastIndex <= code.length() )
			tempString += code.substring(lastIndex, code.length() );
		
		return reverseString(tempString);
	}
	
	/*
	 * Take an input string and, character by character, convert to binary.
	 * Append each binary representation to a string and return this binary
	 * representation.
	 */
	public String stringToUnicodeBinary( String data ) {
		
		String coded = "";
		int character;
		String outMessage = "";
		
		for( int i = 0; i < data.length(); i++ ) {
			// Get unicode char value and convert to binary string
			character = (int) data.charAt(i);
			coded = Integer.toBinaryString(character);
			
			// Pad the string length so it is of uniform length (8 bits)
			for( int j = 0; j <= ( 8 - coded.length() ); j++ )
				coded = "0" + coded;

			
			outMessage += coded;
		}
		return outMessage;
	}
	
	public String unicodeBinaryToString( String data ) {
		String tempString = "";
		for( int i = 0; i < data.length(); i+=8 ) {
			tempString += (char)Integer.parseInt(data.substring(i, i+8),2);
		}
		return tempString;
	}
}
