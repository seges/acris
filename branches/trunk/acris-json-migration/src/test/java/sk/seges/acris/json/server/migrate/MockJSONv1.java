/**
 * 
 */
package sk.seges.acris.json.server.migrate;

/**
 * @author ladislav.gazo
 */
public class MockJSONv1 {
	private String name;
	private String[] tags;
	private Integer lenght;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
