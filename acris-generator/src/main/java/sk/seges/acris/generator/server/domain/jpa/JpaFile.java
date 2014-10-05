package sk.seges.acris.generator.server.domain.jpa;

import sk.seges.acris.generator.server.domain.api.FileData;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JpaFile implements FileData {

	private static final long serialVersionUID = -85528865636910575L;

	private Long id;
	private String content;
	private String path;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public void setId(Long t) {
		this.id = t;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String getpath() {
		return path;
	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}
}