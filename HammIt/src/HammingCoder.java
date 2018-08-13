/**
 * A hamming code generator/checker for positive parity
 * 
 * @author Nick Huppert
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
		String code = Integer.toBinaryString( Integer.parseInt(data) );
		
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
		 * Reverse string first so out refreneces are correct
		 */
		
		// Reverse string

		code = reverseString( code );
		String tempString = "";
		
		// Add 0s for tempory parity bits and expand string
		int tempIndex = 1; //0;
		int lastIndex = 0;
		int getXBits = 0;
		
		for( int i = 0; i < bits; i++ ) {
			/* 
			 * The following stupid long statement gets a double value of 2^i, converts it to a string
			 * chops off the decimal point and parses it as an integer bevause reasons.
			 */  
			getXBits = Integer.parseInt(Double.toString(Math.pow(2, i)).substring(0, Double.toString(Math.pow(2, i)).length()-2) ); //activeBit *2;
			
			tempString += "0" + code.substring( lastIndex, lastIndex + getXBits -1 <= code.length()? lastIndex + getXBits -1:code.length() );//lastIndex, lastIndex + tempIndex );
			
			lastIndex += getXBits -1;
		}
		if( verbose )
			System.out.println("Reversed string with 0s for parity bits: " + tempString);
		code = tempString;
		
		// Figure out how to mathmatically count parity for each bit and insert said bits
		
		boolean parity = true;
		int currentBit = 0;
		
		for( int i = 0; i < bits; i++ ) {
			// Calc starting position and ammount of bits this parity bit reads
			tempIndex = Integer.parseInt(Double.toString(Math.pow(2, i)).substring(0, Double.toString(Math.pow(2, i)).length() -2));
			currentBit = tempIndex;
			lastIndex = tempIndex - 1;
			
			// 2 falses make a true. SO check parity on each substring and toggle our parity only when the result is false
			// If an even number of parities are found in substrings we will not modify the bit, if an odd number we will
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
	
	public String checkData( String data, boolean verbose ) throws HammingCodeException {
		String code = reverseString( data );
		int bits = inferParityBits( code );
		int tempIndex = 0;
		int lastIndex = 0;
		int currentBit = 0;
		boolean checkBits[] = new boolean[bits];
		
		if( verbose )
			System.out.println( "Code to be checked: " + data );
		
		// Initialize array as true;
		for( int i = 0; i < checkBits.length; i++ )
			checkBits[i] = true;
		
		for( int i = 0; i < bits; i++ ) {
			tempIndex = Integer.parseInt(Double.toString(Math.pow(2, i)).substring(0, Double.toString(Math.pow(2, i)).length() -2));
			currentBit = tempIndex;
			lastIndex = tempIndex - 1;
			
			// 2 falses make a true. SO check parity on each substring and toggle our parity only when the result is false
			// If an even number of parities are found in substrings we will not modify the bit, if an odd number we will
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
		// Ascertain which bit is in error based on check bits
		int bitInError = 0;
		for( int i = 0; i < checkBits.length; i++ )
			if( !checkBits[i] )
				bitInError += Integer.parseInt(Double.toString(Math.pow(2, i)).substring(0, Double.toString(Math.pow(2, i)).length() -2));
		
		if( bitInError > code.length() )
			throw new HammingCodeException( "Hamming code is not valid! Can not correct." );
		
		bitInError--; // Convert from real location to array appropriate reference
		// Correct bit in error
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
		return checkData( data, true ); // Operate the decoder verbosely as standard.
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
}
