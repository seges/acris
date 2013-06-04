/**
 * 
 */
package sk.seges.acris.widget.client.action;

public class ActionEvent<T> {
	private T source;
	
	public ActionEvent(T source) {
		this.source = source;
	}
	
	public T getSource() {
		return source;
	}
}