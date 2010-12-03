package sk.seges.acris.generator.server.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import sk.seges.acris.generator.shared.domain.dto.CommentDto;

@Entity
public class JpaComment extends CommentDto  {

	private static final long serialVersionUID = -1407943825717309374L;

	@Id
	@Override
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return super.getId();
	}
	
	@Override
	@Column
	public String getValue() {
		return super.getValue();
	}	
}