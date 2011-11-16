package sk.seges.corpis.pap.service.hibernate.service;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.corpis.pap.model.entity.MockEntity;
import sk.seges.corpis.service.annotation.TransactionPropagation;
import sk.seges.corpis.service.annotation.TransactionPropagation.PropagationTarget;
import sk.seges.corpis.service.annotation.TransactionPropagation.PropagationType;
import sk.seges.corpis.service.annotation.TransactionPropagations;
import sk.seges.sesam.pap.service.annotation.LocalService;

@LocalService
public class TransactionalMockService implements TransactionalMockLocalService {

	@TransactionPropagation
	@Transactional
	@Override
	public MockEntity findInTransaction() {
		return null;
	}

	@TransactionPropagation
	@Override
	public MockEntity findWithoutTransaction() {
		return null;
	}

	@TransactionPropagation(value = PropagationType.ISOLATE, target = PropagationTarget.RETURN_VALUE, fields = "blob")
	@Transactional
	@Override
	public MockEntity findWithoutBlob() {
		return null;
	}

	@TransactionPropagation(value = PropagationType.ISOLATE, target = { PropagationTarget.RETURN_VALUE, PropagationTarget.ARGUMENTS }, fields = "blob")
	@Transactional
	@Override
	public MockEntity findWithoutBlobBothWays(MockEntity entity) {
		return null;
	}

	@TransactionPropagations({
			@TransactionPropagation(value = PropagationType.PROPAGATE, target = PropagationTarget.RETURN_VALUE, fields = "blob"),
			@TransactionPropagation(value = PropagationType.ISOLATE, target = PropagationTarget.ARGUMENTS, fields = "blob")})
	@Transactional
	@Override
	public MockEntity findWithReturnBlob(MockEntity entity) {
		return null;
	}
}