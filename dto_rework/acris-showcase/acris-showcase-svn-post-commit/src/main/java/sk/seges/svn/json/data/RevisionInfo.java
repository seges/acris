package sk.seges.svn.json.data;

import java.util.List;

public class RevisionInfo {
	
	/**
	 * A list of String paths added by this revision.
	 */
	private List<String> addedPaths;

	/**
	 * Username responsible for commit.
	 */
	private String author;

	/**
	 * URL to browse repository history for this revision.
	 */
	private String url;
	
	/**
	 * Repository commit timestamp.
	 */
	private Long timestamp;
	
	/**
	 * A list of String paths modified by this revision.
	 */
	private List<String> modifiedPaths;
	
	/**
	 * Commit log message.
	 */
	private String message;
	
	/**
	 * A list of String paths removed by this revision.
	 */
	private List<String> removedPaths;
	
	/**
	 * Repository identifier for this commit.
	 */
	private Long revisionNr;
	
	public List<String> getAddedPaths() {
		return addedPaths;
	}
	public void setAddedPaths(List<String> addedPaths) {
		this.addedPaths = addedPaths;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public List<String> getModifiedPaths() {
		return modifiedPaths;
	}
	public void setModifiedPaths(List<String> modifiedPaths) {
		this.modifiedPaths = modifiedPaths;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<String> getRemovedPaths() {
		return removedPaths;
	}
	public void setRemovedPaths(List<String> removedPaths) {
		this.removedPaths = removedPaths;
	}
	public Long getRevisionNr() {
		return revisionNr;
	}
	public void setRevisionNr(Long revisionNr) {
		this.revisionNr = revisionNr;
	}
}
