/**
 * 
 */
package sk.seges.acris.bpm.client.engine;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.bpmn.Condition;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/**
 * @author ladislav.gazo
 *
 */
public class JSCondition implements Condition {
	private static final String END = "}";
	private static final String BEGIN = "${";
	private final String expression;
	
	public JSCondition(String expression) {
		super();
		this.expression = expression;
	}

	@Override
	public boolean evaluate(DelegateExecution execution) {
//		JsMap<JsArrayString, JavaScriptObject> vars = (JsMap<JsArrayString, JavaScriptObject>) JavaScriptObject.createObject();
		String jsExpression = expression;
		int index = expression.indexOf(BEGIN);
		int endIndex;
		String variable;
		Object value;
		while(index != -1) {
			endIndex = expression.indexOf(END, index);
			if(endIndex == -1) {
				throw new ActivitiException("Expression variable not closed with a bracket = " + expression);
			}
			
			variable = expression.substring(index + BEGIN.length(), endIndex);
			value = execution.getVariable(variable);
//			set(vars, variable, value);
			
			jsExpression = expression.substring(0, index);
			if(value instanceof String) {
				jsExpression += "\"" + value + "\"";
			} else {
				jsExpression += value;
			}
//			jsExpression += "vars['" + variable + "']";
			jsExpression += expression.substring(endIndex + END.length());
			
			index = expression.indexOf(BEGIN, index + BEGIN.length());
		}
//		return evaluate(jsExpression, vars);
		boolean result = evaluate(jsExpression);
		return result;
	}

//	public final native void set(JsMap<JsArrayString, JavaScriptObject> map, String key, Object value) /*-{
//     map[key] = value;
// 	}-*/;
	
	private native boolean evaluate(String jsExpression)/*-{
		return eval(jsExpression);
	}-*/;

	
//	private native boolean evaluate(String jsExpression, JsMap<JsArrayString, JavaScriptObject> vars)/*-{
//		function evalWithVariables(func, params) {
//    		return new Function("v", "with (v) { return (" + func +")}")(params);
//		}
//	
//		return evalWithVariables(jsExpression, vars);
//	
////		$wnd.alert(vars['wizardOption']);
//// 		var variables = vars;
////		return eval("with (variables) {" + jsExpression + "}"); 
//	}-*/;

	
}
