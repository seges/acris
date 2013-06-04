package sk.seges.svn.json.data;

import java.util.ArrayList;
import java.util.List;

public class OverallInfo {

	private String repositoryPath;
	private String projectName;
	private List<RevisionInfo> revisions = new ArrayList<RevisionInfo>();

	public String getRepositoryPath() {
		return repositoryPath;
	}

	public void setRepositoryPath(String repostoryPath) {
		this.repositoryPath = repostoryPath;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public List<RevisionInfo> getRevisions() {
		return revisions;
	}

	public void setRevisions(List<RevisionInfo> revisions) {
		this.revisions = revisions;
	}

}
