package sk.seges.acris.generator.server.domain.twig;

import sk.seges.acris.generator.server.domain.api.FileData;

import com.vercer.engine.persist.annotation.Key;

public class TwigFile implements FileData {

	private static final long serialVersionUID = 7925430720620504077L;

	@Key
	private Long id;

	private String path;

	private String content;

	@Override
	public void setId(Long t) {
		this.id = t;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String getpath() {
		return path;
	}
 
	@Override
	public String getContent() {
		return content;
	}

	@Override
	public void setContent(String content) {
		this.content = content;
	}
}