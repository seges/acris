package sk.seges.acris.json.client.facebook;

import sk.seges.acris.json.client.facebook.model.Company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class CompanyPage extends Composite {

	interface CompanyPageUiBinder extends UiBinder<Widget, CompanyPage> {}

	private static CompanyPageUiBinder uiBinder = GWT.create(CompanyPageUiBinder.class);

	@UiField
	SpanElement founded;

	@UiField
	DivElement website;

	@UiField
	DivElement overview;
	
	@UiField
	SpanElement mission;
	
	@UiField
	SpanElement products;

	@UiField
	SpanElement fbpage;
	
	@UiField
	Image image;

	@UiField
	DivElement name;

	public CompanyPage(Company company) {
		initWidget(uiBinder.createAndBindUi(this));
		setCompany(company);
	}
	
	public void setCompany(Company company) {
		name.setInnerText(company.getName());
		products.setInnerHTML(company.getProducts());
		founded.setInnerHTML(company.getFounded() == null ? "" : company.getFounded().toString());
		overview.setInnerHTML(company.getOverview());
		mission.setInnerHTML(company.getMission());
		fbpage.setInnerHTML(company.getLink());
		image.setUrl(company.getPicture());
	}
}
