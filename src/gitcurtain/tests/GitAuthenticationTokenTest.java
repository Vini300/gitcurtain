package gitcurtain.tests;

import org.junit.Assert;
import org.junit.Test;

import org.eclipse.jgit.api.errors.TransportException;

import gitcurtain.extraction.ExtractionController;

public class GitAuthenticationTokenTest extends TestParent {

	@Test
	public void testValidAuthenticationToken() {
		
		String path = "test_outfiles/repository03";
		String uri = "https://github.com/Vini300/test-repository.git";
		String token = TestUtils.getTokenFromFile();
		String branch = "main";
		
		TestValueRepositoryData customRepoName = new TestValueRepositoryData(path, uri, token, branch);
		
		String dbpath = "test_outfiles/commits.db";
		
		try {
			ExtractionController.setUpRepository(customRepoName, dbpath);
		}
		catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testEmptyAuthenticationToken() {
		
		String path = "test_outfiles/broken_repository";
		String uri = "https://github.com/Vini300/test-repository.git";
		String token = "";
		String branch = "main";
		
		TestValueRepositoryData customRepoName = new TestValueRepositoryData(path, uri, token, branch);
		
		String dbpath = "test_outfiles/commits.db";
		
		try {
			ExtractionController.setUpRepository(customRepoName, dbpath);
			Assert.fail();
		}
		catch (TransportException e) {
		}
		catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testInvalidAuthenticationToken() {
		
		String path = "test_outfiles/broken_repository";
		String uri = "https://github.com/Vini300/test-repository.git";
		String token = "AEIOU";
		String branch = "main";
		
		TestValueRepositoryData customRepoName = new TestValueRepositoryData(path, uri, token, branch);
		
		String dbpath = "test_outfiles/commits.db";
		
		try {
			ExtractionController.setUpRepository(customRepoName, dbpath);
			Assert.fail();
		}
		catch (TransportException e) {
		}
		catch (Exception e) {
			Assert.fail();
		}
	}
}
