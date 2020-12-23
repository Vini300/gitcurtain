package gitcurtain.defaults;


import java.util.ArrayList;
import java.util.HashMap;

import gitcurtain.utils.MetricResult;

@SuppressWarnings("serial")
public class SelfAffirmedRefactoringSet implements MetricResult {
	
	private HashMap<String, ArrayList<String>> selfAffirmedRefactoringMap;
	
	public SelfAffirmedRefactoringSet(HashMap<String, ArrayList<String>> newMap) {
		selfAffirmedRefactoringMap = newMap;
	}

	public Object getValues() {
		return selfAffirmedRefactoringMap;
	}

	public ArrayList<String> getCommitHashes() {
		return new ArrayList<String>(selfAffirmedRefactoringMap.keySet());
	}

}
