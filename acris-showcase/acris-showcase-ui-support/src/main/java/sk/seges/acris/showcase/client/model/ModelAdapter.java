package sk.seges.acris.showcase.client.model;

import java.io.Serializable;
import java.util.Collection;


public interface ModelAdapter {

	<S extends Serializable, T> T[] convertDataForGrid(Collection<S> data);

}