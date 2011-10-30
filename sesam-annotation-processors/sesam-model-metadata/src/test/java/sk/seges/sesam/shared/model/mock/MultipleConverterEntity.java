package sk.seges.sesam.shared.model.mock;

import sk.seges.sesam.model.metadata.annotation.MetaModel;
import sk.seges.sesam.model.metadata.strategy.DBPropertyConverter;
import sk.seges.sesam.model.metadata.strategy.PojoPropertyConverter;

@MetaModel(beanPropertyConverter = {PojoPropertyConverter.class, DBPropertyConverter.class})
public class MultipleConverterEntity {
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
