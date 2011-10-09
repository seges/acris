package sk.seges.acris.recorder.server.service;

import javax.servlet.http.HttpSession;

import org.gwtwidgets.server.spring.ServletUtils;

import de.novanic.eventservice.service.EventExecutorService;
import de.novanic.eventservice.service.EventExecutorServiceFactory;

public class RemoteEventServiceServlet extends de.novanic.eventservice.service.RemoteEventServiceServlet implements EventExecutorService {

	private static final long serialVersionUID = 4038898693978895043L;

	@Override
	protected EventExecutorService getEventExecutorService() {
        final EventExecutorServiceFactory theEventExecutorServiceFactory = EventExecutorServiceFactory.getInstance();

        HttpSession theSession = ServletUtils.getRequest().getSession();
        return theEventExecutorServiceFactory.getEventExecutorService(theSession);
    }
}