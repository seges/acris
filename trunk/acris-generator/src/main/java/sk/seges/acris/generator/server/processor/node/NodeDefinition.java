package sk.seges.acris.generator.server.processor.node;


public enum NodeDefinition {
	
	KEYWORDS_TAG_NAME("keywords"),
	DESCRIPTION_TAG_NAME("description")
	;

	private String name;
	
	private NodeDefinition(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
