package sk.seges.corpis.appscaffold.jpamodel.pap.jpa;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import sk.seges.corpis.appscaffold.jpamodel.pap.api.ThemeModel;
import sk.seges.corpis.appscaffold.shared.annotation.domain.BusinessKey;
import sk.seges.corpis.appscaffold.shared.annotation.domain.JpaModel;

/**
 * @author ladislav.gazo
 */
@JpaModel
// in real scenario this is possible as well but we don't want to have compile-time dependency on meta-model processor for this project
//@Table(name = "theme", uniqueConstraints = { @UniqueConstraint(columnNames = {
//		ThemeModelMetaModel.WEB_ID, ThemeModelMetaModel.NAME }) })
@SequenceGenerator(name = "seqThemes", sequenceName = "SEQ_THEMES", initialValue = 1)
public interface JpaThemeModel extends ThemeModel<Long> {
	@Id
	@GeneratedValue(generator = "seqThemes")
	Long id();
	
	@BusinessKey
	String name();
}
