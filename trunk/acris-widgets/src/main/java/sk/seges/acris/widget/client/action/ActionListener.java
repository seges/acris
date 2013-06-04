/**
 * 
 */
package sk.seges.acris.widget.client.action;

public interface ActionListener<T> {
	void actionPerformed(ActionEvent<T> event);
}