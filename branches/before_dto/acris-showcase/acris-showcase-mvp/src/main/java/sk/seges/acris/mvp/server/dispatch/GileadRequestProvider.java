package sk.seges.acris.mvp.server.dispatch;

import javax.servlet.http.HttpServletRequest;

import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.RequestProvider;

@Component
public class GileadRequestProvider implements RequestProvider {

	private static final long serialVersionUID = 942525344309880460L;

	@Override
	public HttpServletRequest getServletRequest() {
		return ServletUtils.getRequest();
	}
}
