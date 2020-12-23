package gitcurtain.tests;
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
 
public class LoggingTestWatcher extends TestWatcher {
 
    private final Logger logger;
 
    public LoggingTestWatcher() {
        logger = LogManager.getLogger();
    }
 
    public LoggingTestWatcher(String loggerName) {
        logger = LogManager.getLogger(loggerName);
    }
 
    @Override
    protected void failed(Throwable e, Description description) {
        logger.error(description + "| Status: Failed", e);
    }
 
    @Override
    protected void succeeded(Description description) {
        logger.info(description + "| Status: Succeeded");
    }
}