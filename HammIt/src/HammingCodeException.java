/**
 * Exception class designed to be thrown when a hamming code cannot be validated.
 * @author Nick Huppert
 *
 */
@SuppressWarnings("serial")
public class HammingCodeException extends Exception {

	public HammingCodeException() {
		super( "Hamming code is invalid, please check the input" );
	}
	
	public HammingCodeException( String e ) {
		super( e );
	}
}
