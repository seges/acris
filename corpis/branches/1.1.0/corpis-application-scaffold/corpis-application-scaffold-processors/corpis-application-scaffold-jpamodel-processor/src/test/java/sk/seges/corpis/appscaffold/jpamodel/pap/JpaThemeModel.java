package sk.seges.corpis.appscaffold.jpamodel.pap;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.corpis.appscaffold.shared.annotation.domain.BusinessKey;
import sk.seges.corpis.appscaffold.shared.annotation.domain.JpaModel;

/**
 * @author ladislav.gazo
 */
@JpaModel
@Table(name = "theme", uniqueConstraints = { @UniqueConstraint(columnNames = {
		ThemeModelMetaModel.WEB_ID, ThemeModelMetaModel.NAME }) })
@SequenceGenerator(name = "seqThemes", sequenceName = "SEQ_THEMES", initialValue = 1)
public interface JpaThemeModel extends ThemeModel<Long> {
	@Id
	@GeneratedValue(generator = "seqThemes")
	Long id();
	
	@BusinessKey
	String name();
}
