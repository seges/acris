package sk.seges.acris.widget.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

/**
 * A template panel used for fast and effective rendering of an HTML template.
 * Data source is usually a javascript object/structure/JSON.
 * 
 * Micro-template is suitable for situations where you have template and data in
 * JSON form. It uses JSNI method to fill placeholders in the template.
 * 
 * NOTE: Don't use single-quotes to quote something in the template, it throws
 * errors
 * 
 * @see http://ejohn.org/blog/javascript-micro-templating/
 * 
 * @author ladislav.gazo
 */
public class MicroTemplatePanel extends Composite implements HasMicroTemplate {
	private final HTML container;
	private String template;

	public MicroTemplatePanel() {
		container = new HTML();
		container.setStyleName("");
		initWidget(container);
	}

	@Override
	public void setViewTemplate(String viewTemplate) {
		this.template = viewTemplate;
	}

	private static native String renderTemplate(String str, JavaScriptObject data) /*-{
																					var fn = 
																					// Generate a reusable function that will serve as a template
																					// generator (and which will be cached).
																					new Function("obj",
																					"var p=[],print=function(){p.push.apply(p,arguments);};" +
																					
																					// Introduce the data as local variables using with(){}
																					"with(obj){p.push('" +
																					
																					// Convert the template into pure JavaScript
																					str
																					.replace(/[\r\t\n]/g, " ")
																					.split("<%").join("\t")
																					.replace(/((^|%>)[^\t]*)'/g, "$1\r")
																					.replace(/\t=(.*?)%>/g, "',$1,'")
																					.split("\t").join("');")
																					.split("%>").join("p.push('")
																					.split("\r").join("\\'")
																					+ "');}return p.join('');");
																					
																					// Provide some basic currying to the user
																					return data ? fn( data ) : fn;
																					}-*/;

	@Override
	public void render(String data) {
		JavaScriptObject json = fromJson(data);
		String html = renderTemplate(template, json);
		container.setHTML(html);
	}

	public static native JavaScriptObject fromJson(String jsonString) /*-{
																		return eval('(' + jsonString + ')');
																		}-*/;
}
