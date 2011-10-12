/**
 * 
 */
package sk.seges.acris.json.server.migrate;

/**
 * @author ladislav.gazo
 */
public class MockJSONv2 {
	private String title;
	private String[] tags;
	private Integer lenght;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String name) {
		this.title = name;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	public Integer getLenght() {
		return lenght;
	}
	public void setLenght(Integer lenght) {
		this.lenght = lenght;
	}
}
