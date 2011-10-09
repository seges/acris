package sk.seges.sesam.shared.model.mock;

import sk.seges.sesam.model.metadata.annotation.MetaModel;
import sk.seges.sesam.model.metadata.strategy.MetamodelMethodStrategy;

/**
 * @author ladislav.gazo
 */
@MetaModel(methodStrategy = MetamodelMethodStrategy.PURE)
public interface MockBirdInterface {
	String name();
	Integer type();
}
