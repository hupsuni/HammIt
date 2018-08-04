
public class HammingCoder {
	
	
	public String encodeData( String data ) {
		// Convert string to int, then to binary string
		String code = Integer.toBinaryString( Integer.parseInt(data) );
		
		System.out.println( "Data in: " + data );
		System.out.println( "As binary: " + code );
		
		int bits = calculateParityBits( code );
		
		System.out.println( "Requires " + bits + " parity bits to represent hamming code");
		
		/* Create new bit string with hamming bits inserted as 0s
		 * Make and concatenate substrings based on 2^n where
		 * 2^n represents the location a parity bit would be in
		 * hamming code.
		 * Reverse string first so out refreneces are correct
		 */
		
		// Reverse string
		String tempString = "";
		for( int i = 1; i <= code.length(); i++ ) {
			tempString += code.charAt(code.length()-i);
		}
		System.out.println("Reversed String: " + tempString);
		code = tempString;
		tempString = "";
		
		int tempIndex = 0;
		int lastIndex = 0;
		for( int i = 0; i < bits; i++ ) {
			tempIndex = Integer.parseInt(Double.toString(Math.pow(2, i)).substring(0, Double.toString(Math.pow(2, i)).length()-2))-1;
			tempString += "0" + code.substring(lastIndex, lastIndex + tempIndex < code.length()? tempIndex:code.length());
			lastIndex = tempIndex;
			System.out.println(tempString);
		}
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
			
			if( !parity && currentBit +1 < code.length() )
				code = code.substring(0, currentBit -1 ) + "1" + code.substring(currentBit , code.length());
			
		}
		
		System.out.println("Reversed with hamming code: " + code );
		
		return code;
	}
	
	public String checkData( String data ) {
		
		return data;
	}
	
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
		System.out.println(data);
		boolean parity = true;
		char dataAsChars[] = data.toCharArray();
		for( char c: dataAsChars ) {
			if( c == '1' )
				parity = !parity;
		}
		
		return parity;
	}
}
