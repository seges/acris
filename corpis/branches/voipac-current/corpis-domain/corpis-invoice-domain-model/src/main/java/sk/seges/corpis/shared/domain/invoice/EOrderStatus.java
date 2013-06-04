package sk.seges.corpis.shared.domain.invoice;

public enum EOrderStatus {
	NEW,			/* Order is just created */
	PROCESSING,		/* Order is currently processed somehow - like moving from external stock, manufactured, etc */
	PROCESSED,		/* Order is ready to dispatch, or waiting for payment */
	CANCELLED,		/* Order is cancelled */
	DISPATCHED;		/* Order is dispatched */
}