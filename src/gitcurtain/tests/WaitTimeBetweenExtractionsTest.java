package gitcurtain.tests;

import org.junit.Assert;
import org.junit.Test;

import gitcurtain.defaults.SelfAffirmedRefactoringMetric;
import gitcurtain.defaults.SelfAffirmedRefactoringPieChart;
import gitcurtain.exceptions.InvalidTimerValueException;
import gitcurtain.extraction.ExtractionController;
import gitcurtain.metrics.Metric;
import gitcurtain.metrics.MetricController;
import gitcurtain.visualization.VisualizationController;

public class WaitTimeBetweenExtractionsTest extends TestParent {
	
	@Test
	public void testDefaultWaitTimeBetweenExtractions() {
		
		String path = "test_outfiles/repository07";
		String uri = "https://github.com/Vini300/test-repository.git";
		String token = "4ee24ae02ad35d3a4a0de490e3ad3184314fb29b";
		String branch = "main";
		
		TestValueRepositoryData customRepoName = new TestValueRepositoryData(path, uri, token, branch);
		String dbpath = "test_outfiles/commits_final.db";
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
		
		SelfAffirmedRefactoringPieChart pie = new SelfAffirmedRefactoringPieChart();
		VisualizationController.setVis(pie);
		
		ExtractionController.beginExtraction();
		MetricController.beginMetricCalculation();
		VisualizationController.startVisualization();
		
		Thread thread = new Thread() {
			@Override
			public void run() {
				while(true) {
					
				}
			}
		};

		thread.start();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			Assert.fail();
		}

		Assert.assertTrue(thread.isAlive());
	}
	
	@Test
	public void testValidWaitTimeBetweenExtractions() {
		
		String path = "test_outfiles/repository08";
		String uri = "https://github.com/Vini300/test-repository.git";
		String token = "4ee24ae02ad35d3a4a0de490e3ad3184314fb29b";
		String branch = "main";
		
		TestValueRepositoryData customRepoName = new TestValueRepositoryData(path, uri, token, branch);
		String dbpath = "test_outfiles/commits_final.db";
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
		
		SelfAffirmedRefactoringPieChart pie = new SelfAffirmedRefactoringPieChart();
		VisualizationController.setVis(pie);
		
		try {
			ExtractionController.beginExtraction(5000);
		} catch (Exception e) {
			Assert.fail();
		}
		MetricController.beginMetricCalculation();
		VisualizationController.startVisualization();
		
		Thread thread = new Thread() {
			@Override
			public void run() {
				while(true) {
					
				}
			}
		};

		thread.start();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			Assert.fail();
		}

		Assert.assertTrue(thread.isAlive());
	}
	
	@Test
	public void testInvalidWaitTimeBetweenExtractions() {
		
		String path = "test_outfiles/repository09";
		String uri = "https://github.com/Vini300/test-repository.git";
		String token = TestUtils.getTokenFromFile();
		String branch = "main";
		
		TestValueRepositoryData customRepoName = new TestValueRepositoryData(path, uri, token, branch);
		String dbpath = "test_outfiles/commits_final.db";
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
		
		SelfAffirmedRefactoringPieChart pie = new SelfAffirmedRefactoringPieChart();
		VisualizationController.setVis(pie);
		
		try {
			ExtractionController.beginExtraction(-1);
			Assert.fail();
		} catch (InvalidTimerValueException e) {
			
		}
	}

}
