package gitcurtain.tests;

import org.junit.Assert;
import org.junit.Test;

import org.eclipse.jgit.api.errors.InvalidRemoteException;

import gitcurtain.extraction.ExtractionController;

public class RepositoryURITest extends TestParent {

	@Test
	public void testValidRepositoryURI() {
		
		String path = "test_outfiles/repository02";
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
	public void testEmptyRepositoryURI() {
		
		String path = "test_outfiles/broken_repository";
		String uri = "";
		String token = TestUtils.getTokenFromFile();
		String branch = "main";
		
		TestValueRepositoryData customRepoName = new TestValueRepositoryData(path, uri, token, branch);
		
		String dbpath = "test_outfiles/commits.db";
		
		try {
			ExtractionController.setUpRepository(customRepoName, dbpath);
			Assert.fail();
		}
		catch (InvalidRemoteException e) {
			
		}
		catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testInvalidRepositoryURI() {
		
		String path = "test_outfiles/broken_repository";
		String uri = "registro.br";
		String token = TestUtils.getTokenFromFile();
		String branch = "main";
		
		TestValueRepositoryData customRepoName = new TestValueRepositoryData(path, uri, token, branch);
		
		String dbpath = "test_outfiles/commits.db";
		
		try {
			ExtractionController.setUpRepository(customRepoName, dbpath);
			Assert.fail();
		}
		catch (InvalidRemoteException e) {
			
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
