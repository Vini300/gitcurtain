package gitcurtain.tests;

import org.junit.Assert;
import java.io.IOException;

import org.junit.Test;

import gitcurtain.defaults.SelfAffirmedRefactoringMetric;
import gitcurtain.extraction.ExtractionController;
import gitcurtain.metrics.Metric;
import gitcurtain.metrics.MetricController;

public class KeywordFilePathTest extends TestParent {

	@Test
	public void testValidKeywordFilePath() {
		String path = "test_outfiles/repository04";

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
		
		String keywordPath = "test_outfiles/keywords.txt";
		
		SelfAffirmedRefactoringMetric metric = new SelfAffirmedRefactoringMetric(keywordPath);
		try {
			metric.loadKeywordList();
		} catch (Exception e) {
			Assert.fail();
		}
		Metric enteredMetric = new Metric(1, metric);
		MetricController.addMetric(enteredMetric);
	}
	
	@Test
	public void testEmptyKeywordFilePath() {
		String path = "test_outfiles/repository05";

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
		
		String keywordPath = "";
		
		SelfAffirmedRefactoringMetric metric = new SelfAffirmedRefactoringMetric(keywordPath);
		try {
			metric.loadKeywordList();
			Assert.fail();
		} catch (IOException e) {
			
		} catch (Exception e) {
			Assert.fail();
		}
		Metric enteredMetric = new Metric(1, metric);
		MetricController.addMetric(enteredMetric);
	}

	@Test
	public void testInvalidKeywordFilePath() {
		String path = "test_outfiles/repository06";

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
		
		String keywordPath = "aeiou";
		
		SelfAffirmedRefactoringMetric metric = new SelfAffirmedRefactoringMetric(keywordPath);
		try {
			metric.loadKeywordList();
			Assert.fail();
		} catch (IOException e) {
			
		} catch (Exception e) {
			Assert.fail();
		}
		Metric enteredMetric = new Metric(1, metric);
		MetricController.addMetric(enteredMetric);
	}
}
