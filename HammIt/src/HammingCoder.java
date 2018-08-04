
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
		
		code = tempString;
		tempString = "";
		
		int tempIndex = 0;
		int lastIndex = 0;
		for( int i = 0; i < bits; i++ ) {
			tempIndex = Integer.parseInt(Double.toString(Math.pow(2, i)).substring(0, Double.toString(Math.pow(2, i)).length()-2))-1;
			tempString += "0" + code.substring(lastIndex, tempIndex < code.length()? tempIndex:code.length());
			lastIndex = tempIndex;
			System.out.println(tempString);
		}
		System.out.println(tempString);
		// Figure out how to mathmatically count parity for each bit and insert said bits
		
		
		
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
		boolean parity = true;
		char dataAsChars[] = data.toCharArray();
		for( char c: dataAsChars ) {
			if( c == '1' )
				parity = !parity;
		}
		
		return parity;
	}
}
