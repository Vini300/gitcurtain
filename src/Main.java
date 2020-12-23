


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import gitcurtain.defaults.RepositoryByTerminal;
import gitcurtain.defaults.SelfAffirmedRefactoringMetric;
import gitcurtain.defaults.SelfAffirmedRefactoringPieChart;
import gitcurtain.exceptions.InvalidBranchException;
import gitcurtain.exceptions.InvalidTimerValueException;
import gitcurtain.extraction.ExtractionController;
import gitcurtain.metrics.Metric;
import gitcurtain.metrics.MetricController;
import gitcurtain.visualization.VisualizationController;

public class Main {
	
	public static void main(String[] args) {
		
		RepositoryByTerminal customRepoName = new RepositoryByTerminal();
		String dbpath = getDBPath();
		try {
			ExtractionController.setUpRepository(customRepoName, dbpath);
		} catch (InvalidRemoteException e) {
			System.out.println("Error. Invalid repository URI.");
			e.printStackTrace();
			System.exit(0);
		} catch (TransportException e) {
			System.out.println("Error. Invalid GitHub token.");
			e.printStackTrace();
			System.exit(0);
		} catch (InvalidBranchException e) {
			System.out.println("Error. Invalid repository branch.");
			e.printStackTrace();
			System.exit(0);
		}
		
		SelfAffirmedRefactoringMetric metric = new SelfAffirmedRefactoringMetric();
		try {
			metric.loadKeywordList();
		} catch (IOException e) {
			System.out.println("Error in I/O.");
			e.printStackTrace();
			System.exit(0);
		}
		Metric enteredMetric = new Metric(1, metric);
		MetricController.addMetric(enteredMetric);
		
		SelfAffirmedRefactoringPieChart pie = new SelfAffirmedRefactoringPieChart();
		VisualizationController.setVis(pie);
		
		long timer = getTimer();
		
		if (timer == -1) {
			ExtractionController.beginExtraction();
		}
		else {
			try {
				ExtractionController.beginExtraction(timer);
			} catch (InvalidTimerValueException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
			
		MetricController.beginMetricCalculation();
		VisualizationController.startVisualization();
	}
	
	public static String getDBPath() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String path = "";
		boolean done = false;
		
		while (!done) {
			try {
				System.out.println("Please enter the dabase path (i.e., the filename for the database, either existing or to be created):");
	        	path = reader.readLine();
	        	done = true;
			}
	        catch (IOException e) {
	        	System.out.println("An unexpected error occurred while reading data. Retrying...");
				e.printStackTrace();
			}
		}
		
        return path;
	}
	
	public static long getTimer() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String value = "";
		long timer = -1;
		boolean done = false;
		
		while (!done) {
			try {
				System.out.println("Please enter the time between repository update checks, in milliseconds"
						+ " (leave empty to use the default time of 3 hours).  Note that very low values (<1000) might result in a"
						+ " high usage of resources.:");
	        	value = reader.readLine();
	        	if (value.equals("")) {
	        		return -1;
	        	}
	        	timer = Long.parseLong(value);
				if (timer < 0) {
					System.out.println("Error. Please use a value above 0. Note that very low values (<60000) might result in a high"
							+ "usage of resources.");
					continue;
				} else if (timer < 60000) {
					System.out.println("This value will cause a high resource usage, are you sure (Y to continue, anything else to return)");
					String answer = reader.readLine();
					if (!answer.equals("Y")) {
						continue;
					}
				}
				done = true;
			}
	        catch (IOException e) {
	        	System.out.println("An unexpected error occurred while reading data. Retrying...");
				e.printStackTrace();
			}
		}
        
        return timer;
	}
}
