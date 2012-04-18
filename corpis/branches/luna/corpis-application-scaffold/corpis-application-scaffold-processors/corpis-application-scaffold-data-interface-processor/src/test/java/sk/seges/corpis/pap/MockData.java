package sk.seges.corpis.pap;

import java.util.HashMap;
import java.util.List;

import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.appscaffold.shared.annotation.PersistenceType;
import sk.seges.corpis.appscaffold.shared.annotation.PersistentObject;
import sk.seges.corpis.pap.model.domain.data.model.MockEnum;

public interface MockData {

	@DomainInterface
	@PersistentObject(PersistenceType.JPA)
	public interface MockDomainModel {
		MockEnum mock();
	}

	@DomainInterface
	@PersistentObject(PersistenceType.JPA)
	public interface MockiestDomainModel extends MockDomainModel {
		HashMap<Long, List<? extends MockDomainModel>> mapWithWildcard();
		MockDomainModel domain();
		List<? extends MockDomainModel> wildcardTypeList();
		List<MockDomainModel> declaredTypeList();
	}
	
}