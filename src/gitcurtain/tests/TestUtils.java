package gitcurtain.tests;

import java.io.BufferedReader;
import java.io.FileReader;

public class TestUtils {

	public static String getTokenFromFile() {
		String tokenFile = "test_outfiles/token.txt";
		String token = "";
		
		try(BufferedReader br = new BufferedReader(new FileReader(tokenFile))) {
		    token = br.readLine();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return token;
	}
}
