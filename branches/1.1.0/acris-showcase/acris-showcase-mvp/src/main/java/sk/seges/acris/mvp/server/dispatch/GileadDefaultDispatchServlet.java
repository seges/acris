package sk.seges.acris.mvp.server.dispatch;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.Dispatch;

@Component
public class GileadDefaultDispatchServlet extends DefaultDispatchServlet {

	private static final long serialVersionUID = 942525344309880460L;

	@Autowired
	public GileadDefaultDispatchServlet(final Dispatch dispatch/*, ServletConfig servletConfig*/) throws ServletException {
		super(dispatch);
//		init(servletConfig);
	}

	@Override
	protected HttpServletRequest getRequest() {
		return ServletUtils.getRequest();
	}
}
