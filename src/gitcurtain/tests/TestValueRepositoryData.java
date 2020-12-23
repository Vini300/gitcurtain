package gitcurtain.tests;

import gitcurtain.extraction.RepoSetupStrategy;

class TestValueRepositoryData implements RepoSetupStrategy {
	
	private String path;
	private String uri;
	private String token;
	private String branch;
	
	public TestValueRepositoryData(String path, String uri, String token, String branch) {
		this.path = path;
		this.uri = uri;
		this.token = token;
		this.branch = branch;
	}

	public String getRepositoryPath() {
		return path;
	}

	public String getRepositoryURI() {
		return uri;
	}

	public String getToken() {
		return token;
	}
	
	public String getBranch() {
		return branch;
	}
	
}