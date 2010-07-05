package sk.seges.acris.security.showcase.client;

import sk.seges.acris.security.client.annotations.Secured;
import sk.seges.acris.security.client.ui.SecuredComposite;
import sk.seges.acris.security.rpc.user_management.domain.Permission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ladislav.gazo
 */
public class CustomerPanel extends SecuredComposite {
	interface CustomerViewUiBinder extends UiBinder<Widget, CustomerPanel> {}

	private static CustomerViewUiBinder uiBinder = GWT.create(CustomerViewUiBinder.class);

	@UiField
	@Secured(value = Authorities.SECURITY_MANAGEMENT, permission = Permission.CREATE)
	protected TextBox name;
	
	@UiField
	@Secured(Authorities.SECURITY_MANAGEMENT)
	protected TextBox securityID;

	public CustomerPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
