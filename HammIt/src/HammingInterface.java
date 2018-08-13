import java.util.Scanner;

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
		
		
		while( !input.equals("1") && !input.equals("2") && !input.equals("3") ) {
			System.out.println("**********************************");
			System.out.println("1: Encode data to hamming code");
			System.out.println("2: Check code for error");
			System.out.println("3: Run example with verbose steps");
			System.out.println("**********************************");
			System.out.print("Enter Option: ");
			input = sc.nextLine();
		}
		
		switch( input ) {
		case "1":
			encode();
			break;
		case "2":
			check();
			break;
		case "3":
			test();
		}
		
	}
	
	private void encode() {
		System.out.print("Enter number: ");
		String input = sc.nextLine();
		hammingCoder.encodeData(input);
	}
	
	private void check() {
		System.out.print("Enter binary string: ");
		String input = sc.nextLine();
		hammingCoder.checkData(input);
	}
	
	private void test() {
		
	}
}
