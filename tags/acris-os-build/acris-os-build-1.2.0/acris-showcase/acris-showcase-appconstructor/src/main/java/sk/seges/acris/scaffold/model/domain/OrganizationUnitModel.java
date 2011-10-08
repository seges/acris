/**
 * 
 */
package sk.seges.acris.scaffold.model.domain;

/**
 * @author ladislav.gazo
 */
public interface OrganizationUnitModel {
	String name();
	String costCenter();
	
	OrganizationUnitModel parent();
}
