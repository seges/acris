package sk.seges.acris.mvp.server.dispatch;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;

import com.philbeaudoin.gwtp.dispatch.server.Dispatch;
import com.philbeaudoin.gwtp.dispatch.server.spring.DefaultDispatchServlet;

//@Component
public class RemoteDispatchServlet extends DefaultDispatchServlet {

	private static final long serialVersionUID = 942525344309880460L;

	@Autowired
	public RemoteDispatchServlet(final Dispatch dispatch/*, ServletConfig servletConfig*/) throws ServletException {
		super(dispatch);
//		init(servletConfig);
	}

//	@Override
//	protected HttpServletRequest getRequest() {
//		return ServletUtils.getRequest();
//	}
}