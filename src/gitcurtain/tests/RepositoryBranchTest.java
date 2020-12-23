package gitcurtain.tests;

import org.junit.Assert;
import org.junit.Test;

import gitcurtain.exceptions.InvalidBranchException;
import gitcurtain.extraction.ExtractionController;

public class RepositoryBranchTest extends TestParent {

	@Test
	public void testValidRepositoryBranch() {
		
		String path = "test_outfiles/repository10";

		String uri = "https://github.com/Vini300/test-repository.git";
		String token = TestUtils.getTokenFromFile();
		String branch = "main";
		
		TestValueRepositoryData customRepoName = new TestValueRepositoryData(path, uri, token, branch);
		
		String dbpath = "test_outfiles/commits.db";
		
		try {
			ExtractionController.setUpRepository(customRepoName, dbpath);
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testEmptyRepositoryBranch() {
		
		String path = "test_outfiles/repository11";

		String uri = "https://github.com/Vini300/test-repository.git";
		String token = TestUtils.getTokenFromFile();
		String branch = "";
		
		TestValueRepositoryData customRepoName = new TestValueRepositoryData(path, uri, token, branch);
		
		String dbpath = "test_outfiles/commits.db";
		
		try {
			ExtractionController.setUpRepository(customRepoName, dbpath);
			Assert.fail();
		}
		catch (InvalidBranchException e) {	
		}
		catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testInvalidRepositoryBranch() {
		
		String path = "test_outfiles/repository12";

		String uri = "https://github.com/Vini300/test-repository.git";
		String token = TestUtils.getTokenFromFile();
		String branch = "aeiou";
		
		TestValueRepositoryData customRepoName = new TestValueRepositoryData(path, uri, token, branch);
		
		String dbpath = "test_outfiles/commits.db";
		
		try {
			ExtractionController.setUpRepository(customRepoName, dbpath);
			Assert.fail();
		} catch (InvalidBranchException e) {	
		}
		catch (Exception e) {
			Assert.fail();
		}
	}

}
