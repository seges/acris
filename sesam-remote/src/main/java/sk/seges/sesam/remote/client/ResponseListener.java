/**
 * 
 */
package sk.seges.sesam.remote.client;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import sk.seges.sesam.remote.domain.Response;
import sk.seges.sesam.remote.exception.RemoteInvocationException;

/**
 * @author AAlac
 * @author eldzi
 */
public class ResponseListener implements MessageListener {
	private static final Logger log = Logger.getLogger(ResponseListener.class);
	private Response ret = null;
	/**
	 * We are using lock timeouts because ActiveMQ has an issue with
	 * receive(timeout) JMS method. So we implemented it on our own.
	 */
	private final Object lock;

	public ResponseListener(Object l) {
		lock = l;
	}

	public void onMessage(Message arg0) {
		ObjectMessage m = (ObjectMessage) arg0;
		try {
			ret = (Response) m.getObject();
			if (ret == null)
				log.warn("Received null response object from message = " + m);
		} catch (JMSException e) {
			throw new RemoteInvocationException(
					"Exception while receiving response, message = " + m, e);
		} finally {
			synchronized (lock) {
				lock.notify();
			}
		}
	}

	public Response getReturnValue() {
		return ret;
	}
}