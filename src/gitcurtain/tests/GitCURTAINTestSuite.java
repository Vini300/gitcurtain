package gitcurtain.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	KeywordFilePathTest.class,
	MetricControllerTest.class,
	RepositoryBranchTest.class,
	RepositoryFilePathTest.class,
	GitAuthenticationTokenTest.class,
	RepositoryURITest.class,
	WaitTimeBetweenExtractionsTest.class
})

public class GitCURTAINTestSuite {
	
}
