package gitcurtain.tests;

import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TestWatcher;

import gitcurtain.metrics.MetricController;

public class TestParent {
	
	@Rule
    public TestWatcher watcher = new LoggingTestWatcher();

	@After
	public void cleanUpStaticVariables() {
		MetricController.removeAllMetrics();
	}
}
