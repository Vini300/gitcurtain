package gitcurtain.tests;

import org.junit.Assert;
import org.junit.Test;

import org.eclipse.jgit.api.errors.JGitInternalException;


import gitcurtain.extraction.ExtractionController;

public class RepositoryFilePathTest extends TestParent {

	@Test
	public void testValidRepositoryFilePath() {
		
		String path = "test_outfiles/repository01";

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
	public void testRepositoryPathWithFilesInside() {
		String path = "test_outfiles/existing_repository";
		String uri = "https://github.com/Vini300/test-repository.git";
		String token = TestUtils.getTokenFromFile();
		String branch = "main";
		
		TestValueRepositoryData customRepoName = new TestValueRepositoryData(path, uri, token, branch);
		
		String dbpath = "test_outfiles/commits.db";
		
		try {
			ExtractionController.setUpRepository(customRepoName, dbpath);
			Assert.fail();
		}
		catch (JGitInternalException e) {
			
		}
		catch (Exception e) {
			Assert.fail();
		}
	}
}
