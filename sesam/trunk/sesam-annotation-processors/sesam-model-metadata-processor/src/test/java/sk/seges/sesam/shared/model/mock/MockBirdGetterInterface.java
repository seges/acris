package sk.seges.sesam.shared.model.mock;

import sk.seges.sesam.model.metadata.annotation.MetaModel;
import sk.seges.sesam.model.metadata.strategy.MetamodelMethodStrategy;

/**
 * @author ladislav.gazo
 */
@MetaModel(methodStrategy = MetamodelMethodStrategy.GETTER_SETTER)
public interface MockBirdGetterInterface {
	String getName();
	void setName(String name);
	Integer getType();
	void setType(Integer type);
}
