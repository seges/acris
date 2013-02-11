package sk.seges.corpis.shared.domain.invoice;

public enum EOrderStatus {
	NEW,			/* idx1 - Order is just created */
	PROCESSING,		/* idx4 - Order is currently processed somehow - like moving from external stock, manufactured, etc */
	PROCESSED,		/* idx5 - Order is ready to dispatch, or waiting for payment */
	CANCELLED,		/* idx9 - Order is cancelled */
	DISPATCHED,		/* idxX - Order is dispatched */
	ACCEPTED,		/* idx2 - Order is accepted by shop administrator */
	NOT_ACCEPTED,	/* idx9 - Order was not accepted by shop administrator */
	PAID,			/* idx3 - Order was paid */
	DELIVERED,		/* idx9 - Order was delivered to the customer */
	WAITING;		/* idxX - Order is waiting for products availability */
}