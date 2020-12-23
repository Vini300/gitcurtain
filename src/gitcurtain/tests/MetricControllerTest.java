package gitcurtain.tests;

import org.junit.Assert;
import org.junit.Test;

import gitcurtain.defaults.SelfAffirmedRefactoringMetric;
import gitcurtain.metrics.Metric;
import gitcurtain.metrics.MetricController;

public class MetricControllerTest extends TestParent {

	@Test
	public void testCorrectlyAddRemoveMetric() {
		String keywordPath = "test_outfiles/keywords.txt";
		SelfAffirmedRefactoringMetric metric = new SelfAffirmedRefactoringMetric(keywordPath);
		try {
			metric.loadKeywordList();
		} catch (Exception e) {
			Assert.fail();
		}
		Metric enteredMetric = new Metric(1, metric);
		MetricController.addMetric(enteredMetric);
		Assert.assertNotNull(MetricController.removeMetric(1));
	}
	
	@Test
	public void testRemoveInvalidMetricID() {
		String keywordPath = "test_outfiles/keywords.txt";
		SelfAffirmedRefactoringMetric metric = new SelfAffirmedRefactoringMetric(keywordPath);
		try {
			metric.loadKeywordList();
		} catch (Exception e) {
			Assert.fail();
		}
		Metric enteredMetric = new Metric(1, metric);
		MetricController.addMetric(enteredMetric);
		Assert.assertNull(MetricController.removeMetric(-1));
	}
	
	@Test
	public void testRemoveWithEmptyMetricArray() {
		Assert.assertNull(MetricController.removeMetric(0));
	}

}
