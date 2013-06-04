package sk.seges.acris.showcase.client.model.smartgwt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.showcase.client.model.ModelAdapter;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public abstract class AbstractSmartGWTModelAdapter implements ModelAdapter {

	@SuppressWarnings("unchecked")
	public <S extends Serializable, T> T[] convertDataForGrid(Collection<S> data) {
		List<ListGridRecord> result = new ArrayList<ListGridRecord>();

		for (S d : data) {
			ListGridRecord record = generateRecord(d.getClass());
			((BeanWrapper<S>) record).setBeanWrapperContent(d);
			result.add(record);
		}
		return (T[]) result.toArray(new ListGridRecord[] {});
	}
	
	public abstract ListGridRecord generateRecord(Class<?> clazz);

}
