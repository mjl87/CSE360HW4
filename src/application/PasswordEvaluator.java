package application;

/**
 * This class's main purpose is to check whether an inputted password is valid, meaning the password
 * must pass various password checkers to see if the password works or not, so this class checks for
 * things like upper case, lower case, number, special character, and so on...
 * 
 */
public class PasswordEvaluator {
	/**
	 * <p> Title: Directed Graph-translated Password Assessor. </p>
	 * 
	 * <p> Description: A demonstration of the mechanical translation of Directed Graph 
	 * diagram into an executable Java program using the Password Evaluator Directed Graph. 
	 * The code detailed design is based on a while loop with a cascade of if statements</p>
	 * 
	 * <p> Copyright: Lynn Robert Carter © 2022 </p>
	 * 
	 * @author Lynn Robert Carter
	 * 
	 * @version 0.00		2018-02-22	Initial baseline 
	 * 
	 */

	/**********************************************************************************************
	 * 
	 * Result attributes to be used for GUI applications where a detailed error message and a 
	 * pointer to the character of the error will enhance the user experience.
	 * 
	 */

	public static String passwordErrorMessage = "";		// The error message text
	public static String passwordInput = "";			// The input being processed
	public static int passwordIndexofError = -1;		// The index where the error was located
	public static boolean foundUpperCase = false;
	public static boolean foundLowerCase = false;
	public static boolean foundNumericDigit = false;
	public static boolean foundSpecialChar = false;
	public static boolean foundLongEnough = false;
	private static String inputLine = "";				// The input line
	private static char currentChar;					// The current character in the line
	private static int currentCharNdx;					// The index of the current character
	private static boolean running;						// The flag that specifies if the FSM is 
														// running

	/**********
	 * This private method display the input line and then on a line under it displays an up arrow
	 * at the point where an error should one be detected.  This method is designed to be used to 
	 * display the error message on the console terminal.
	 * 
	 * @param input				The input string
	 * @param currentCharNdx	The location where an error was found
	 * @return					Two lines, the entire input line followed by a line with an up arrow
	 */
	private static void displayInputState() {
		// Display the entire input line
		System.out.println(inputLine);
		System.out.println(inputLine.substring(0,currentCharNdx) + "?");
		System.out.println("The password size: " + inputLine.length() + "  |  The currentCharNdx: " + 
				currentCharNdx + "  |  The currentChar: \"" + currentChar + "\"");
	}

	/**********
	 * This method is a mechanical transformation of a Directed Graph diagram into a Java
	 * method.
	 * 
	 * @param input		The input string for directed graph processing
	 * @return			An output string that is empty if every things is okay or it will be
	 * 						a string with a help description of the error follow by two lines
	 * 						that shows the input line follow by a line with an up arrow at the
	 *						point where the error was found.
	 */
	public static String evaluatePassword(String input) {
		// The following are the local variable used to perform the Directed Graph simulation
		passwordErrorMessage = "";
		passwordIndexofError = 0;			// Initialize the IndexofError
		inputLine = input;					// Save the reference to the input line as a global
		currentCharNdx = 0;					// The index of the current character
		
		if(input.length() <= 0) return "Please enter a Password";
		
		// The input is not empty, so we can access the first character
		currentChar = input.charAt(0);		// The current character from the above indexed position

		// The Directed Graph simulation continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state
		
		passwordInput = input;				// Save a copy of the input
		foundUpperCase = false;				// Reset the Boolean flag
		foundLowerCase = false;				// Reset the Boolean flag
		foundNumericDigit = false;			// Reset the Boolean flag
		foundSpecialChar = false;			// Reset the Boolean flag
		foundNumericDigit = false;			// Reset the Boolean flag
		foundLongEnough = false;			// Reset the Boolean flag
		running = true;						// Start the loop
		
		//new boolean variable checking for invalid characters
		boolean inv = false;
		// The Directed Graph simulation continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition
		while (running) {
			displayInputState();
			// The cascading if statement sequentially tries the current character against all of the
			// valid transitions
			
			//if statement checks for invalid characters which was not listed in the FSM
			if(currentChar == '>' || currentChar == '<' || currentChar == ';' || currentChar == '\\' || currentChar == '=') {
				System.out.println("invalid character found");
				inv = true;
			}
			else if (currentChar >= 'A' && currentChar <= 'Z') {
				System.out.println("Upper case letter found");
				foundUpperCase = true;
			} else if (currentChar >= 'a' && currentChar <= 'z') {
				System.out.println("Lower case letter found");
				foundLowerCase = true;
			} else if (currentChar >= '0' && currentChar <= '9') {
				System.out.println("Digit found");
				foundNumericDigit = true;
			} else if ("~`!@#$%^&*()_-+={}[]|:\"',.?/".indexOf(currentChar) >= 0) {
				System.out.println("Special character found");
				foundSpecialChar = true;
			} else {
				passwordIndexofError = currentCharNdx;
				return "An invalid character has been found!";
			}
			//if any of the invalid characters are found in the password then it doesnt accept the password
			if (currentCharNdx >= 7 && !inv) {
				System.out.println("At least 8 characters found");
				foundLongEnough = true;
			}
			
			// Go to the next character if there is one
			currentCharNdx++;
			if (currentCharNdx >= inputLine.length())
				running = false;
			else
				currentChar = input.charAt(currentCharNdx);
			
			System.out.println();
		}
		
		
		//CHANGE HEREEEEEEEEEEEE
		
		//edited some of the error messages and removed the += to make error message less cluttered and easy to read
		String errMessage = "";
		//new error message for invalid character print statement
		if (inv) {
			errMessage = "Invalid character found";
		}
		else if (!foundUpperCase)
			errMessage = "Missing Upper case Letter";
		
		else if (!foundLowerCase)
			errMessage = "Missing Lower case Letter";
		
		else if (!foundNumericDigit)
			errMessage = "Missing Numeric digit";
			
		else if (!foundSpecialChar)
			errMessage = "Missing Special character";
			
		else if (!foundLongEnough)
			errMessage = "Needs to be at least 8 characters long";
		
		else if (errMessage == "")
			return "";
		
		passwordIndexofError = currentCharNdx;
		return errMessage;

	}
}