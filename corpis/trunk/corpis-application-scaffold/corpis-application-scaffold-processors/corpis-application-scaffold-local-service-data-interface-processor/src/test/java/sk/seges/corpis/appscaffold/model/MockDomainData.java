package sk.seges.corpis.appscaffold.model;

import javax.annotation.Generated;

import sk.seges.corpis.appscaffold.shared.annotation.DomainData;

@DomainData
@Generated(value = "sk.seges.corpis.appscaffold.model.pap.DomainDataInterfaceProcessor")
public interface MockDomainData<T> {

		public static final String CHANNELTYPE = "channelType";

		String getType();
		
		void setType(String type);

}
