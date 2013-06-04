/**
 * 
 */
package sk.seges.sesam.domain;

import java.beans.PropertyChangeListener;

/**
 * Interface for marking objects able to emit a property change notifications to
 * all observing participants. Defines conventional methods for object
 * observation registration.
 * 
 * Underlying implementation may vary but it is advisable to use
 * PropertyChangeSupport instance within object for property change
 * handling.
 * 
 * <pre>
 * class ObservableOne implements IObservableObject {
 *     public static final String PROPERTY_SALARY = "salary";
 *     
 *     private Float salary;
 *     private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
 *     
 *     public void addPropertyChangeListener(PropertyChangeListener listener) {
 *         pcs.addPropertyChangeListener(listener);
 *     }
 *     
 *     public void removePropertyChangeListener(PropertyChangeListener listener) {
 *         pcs.removePropertyChangeListener(listener);
 *     }
 *     
 *     public Float getSalary() {
 *         return salary;
 *     }   
 *     
 *     public void setSalary(Float salary) {
 *         Float oldValue = this.salary;
 *         this.salary = salary;
 *         pcs.firePropertyChange(PROPERTY_SALARY, oldValue, salary);
 *     }
 * }
 * </pre>
 * 
 * @author eldzi
 */
public interface IObservableObject {
	void addPropertyChangeListener(PropertyChangeListener listener);
	void removePropertyChangeListener(PropertyChangeListener listener);
}
