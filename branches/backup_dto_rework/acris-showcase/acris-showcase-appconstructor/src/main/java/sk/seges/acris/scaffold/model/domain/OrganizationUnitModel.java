/**
 * 
 */
package sk.seges.acris.scaffold.model.domain;

/**
 * @author ladislav.gazo
 */
public interface OrganizationUnitModel extends DomainModel {
	String name();
	String costCenter();
	
	OrganizationUnitModel parent();
}
