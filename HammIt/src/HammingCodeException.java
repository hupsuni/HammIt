
@SuppressWarnings("serial")
public class HammingCodeException extends Exception {

	public HammingCodeException() {
		super( "Hamming code is invalid, please check the input" );
	}
	
	public HammingCodeException( String e ) {
		super( e );
	}
}
