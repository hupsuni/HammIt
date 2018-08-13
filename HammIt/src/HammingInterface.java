import java.util.Scanner;
import java.util.*;

/**
 * Interface to drive the hamming code program
 * @author Nick Huppert
 *
 */
public class HammingInterface {
	private HammingCoder hammingCoder;
	private Scanner sc = new Scanner( System.in );
	
	public HammingInterface() {
		hammingCoder = new HammingCoder();
	}
	
	public void run() {
		
		String input = "";
		
		
		while ( !input.equals("4") ) {
			input = "";
			while (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("4")) {
				System.out.println("**********************************");
				System.out.println("1: Encode data to hamming code");
				System.out.println("2: Check code for error");
				System.out.println("3: Run example with verbose steps");
				System.out.println("4: Exit");
				System.out.println("**********************************");
				System.out.print("Enter Option: ");
				input = sc.nextLine();
			}
			switch (input) {
			case "1":
				encode();
				break;
			case "2":
				check();
				break;
			case "3":
				test();
				break;
			case "4":
				return;
			}
			System.out.println("Press enter to continue:");
			sc.nextLine();
		}
		
	}
	
	private void encode() {
		System.out.print("Enter number: ");
		String input = sc.nextLine();
		System.out.println( "Data returned: " + hammingCoder.encodeData(input) );
	}
	
	private void check() {
		System.out.print("Enter binary string: ");
		String input = sc.nextLine();
		try{
			System.out.println( "Data was checked and corrected, result: " + hammingCoder.checkData(input) );
		}
		catch( HammingCodeException e ) {
			System.out.println( e.getMessage() );
		}
	}
	
	private void test() {
		test( "23" );
		System.out.println( "\nLets test with a larger value for fun - press Enter to continue... " );
		sc.nextLine();
		test( "1031326" );
	}
	
	// Generate 2 examples, encode them, check the encoded values then break the codes on purpose and try to check again
	private void test( String input ) {
		try {
			System.out.println("First we will test the hamming code generator for the number " + input + "\n");
			String data = hammingCoder.encodeData( input, true );
			System.out.println( "We have received the returned string: " + data + 
					"\n\nNow we will send this to the hamming code checker and see if it is correct\n");
			System.out.println( "The result returned to us is: " + hammingCoder.checkData( data, true ) );
			System.out.println("\nNow we will alter 1 random bit in our original string to simulate an error and see if the checker will detect it\n");
			System.out.println( "Original code: " + data );
			data = breakTheCode( data );
			System.out.println( "Tampered code: " + data );
			System.out.println( "Now checking our modified code to see if it is detected and repaired" );
			data = hammingCoder.checkData( data, true );
			System.out.println( "The result returned to us is: " + data );
		} catch (HammingCodeException e) {
			System.out.println( e.getMessage() );
		}
		
	}
	
	// Toggle 1 randomly chosen character from the input string
	private String breakTheCode( String data ) {
		String brokenData = data;
		Random rng = new Random();
		String brokenBit = "";
		int randomIndex = rng.nextInt( brokenData.length() );
		
		if( brokenData.charAt(randomIndex) == '1' )
			brokenBit = "0";
		else
			brokenBit = "1";
		
		brokenData = brokenData.substring( 0, randomIndex ) + brokenBit + brokenData.substring( randomIndex +1, brokenData.length() );
		return brokenData;
	}
}
