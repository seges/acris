package sk.seges.acris.generator.server.domain.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import sk.seges.acris.generator.server.domain.api.FileData;

@Entity
public class JpaFile implements FileData {

	private static final long serialVersionUID = -85528865636910575L;

	private String id;
	private String content;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public void setId(String t) {
		this.id = t;
	}

	@Override
	public String getId() {
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
}