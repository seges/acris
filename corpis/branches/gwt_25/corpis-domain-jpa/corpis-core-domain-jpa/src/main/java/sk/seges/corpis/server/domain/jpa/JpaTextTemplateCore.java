package sk.seges.corpis.server.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import sk.seges.corpis.server.domain.server.model.base.TextTemplateCoreBase;

@Entity
@Table(name = "text_template")
@SequenceGenerator(name = "seqTextTemplates", sequenceName = "seq_text_templates", initialValue = 1) //$NON-NLS-1$ //$NON-NLS-2$
public class JpaTextTemplateCore extends TextTemplateCoreBase {

	private static final long serialVersionUID = -4826191940267204555L;
	public static final int TEXT_LENGTH = 255;

	private Integer version;

	@Override
	@Id
	@GeneratedValue(generator = "seqTextTemplates")
	public Long getId() {
		return super.getId();
	}
	
	@Override
	@Column(length = TEXT_LENGTH)
	public String getText() {
		return super.getText();
	}
	
	@Version
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}