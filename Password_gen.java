import java.util.*; 

public class Password_gen 
{ 
	public static void main(String[] args) 
	{ 
		int length = 10; 
		System.out.println(Password(length)); 
	} 

	static char[] Password(int len) 
	{ 
		System.out.println("Generating password using random() : "); 
		System.out.print("Your new password is : "); 

		// A strong password has Cap_chars, Lower_chars, 
		// numeric value and symbols. So we are using all of 
		// them to generate our password 
		
		String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
		
		String Small_chars = "abcdefghijklmnopqrstuvwxyz"; 
		
		String numbers = "0123456789"; 
		
		String symbols = "!@#$%^&*_=+-/.?<>)"; 


		String values = Capital_chars + Small_chars + 
						numbers + symbols; 

		// Using random method 
		Random rndm_method = new Random(); 

		char[] password = new char[len]; 

		for (int i = 0; i < len; i++) 
		{ 
			password[i] = values.charAt(rndm_method.nextInt(values.length())); 
		} 
		
		return password; 
	} 
} 
