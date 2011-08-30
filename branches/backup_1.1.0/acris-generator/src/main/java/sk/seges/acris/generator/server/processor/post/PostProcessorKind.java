package sk.seges.acris.generator.server.processor.post;

public interface PostProcessorKind {

	public static enum Kind {
		ALTER, ANNIHILATOR, APPENDER;
	}
	
	Kind getKind();
}
