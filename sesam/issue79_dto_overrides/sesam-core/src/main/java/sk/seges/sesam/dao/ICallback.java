/**
 * 
 */
package sk.seges.sesam.dao;

/**
 * Support for asynchronous calls.
 * 
 * <p>
 * As an example, suppose the service interface defines a method called
 * <code>getShapes</code> as follows:
 * 
 * <pre>
 * Shape[] getShapes(String databaseName) throws ShapeException, DbException;
 * </pre>
 * 
 * Its asynchronous counterpart method be declared as:
 * 
 * <pre>
 * void getShapes(String databaseName, Callback<Shape[]> callback);
 * </pre>
 * 
 * Note that <code>throws</code> declaration is not repeated in the async
 * version.
 * </p>
 * 
 * <p>
 * A call with a typical use of <code>Callback</code> might look like
 * this:
 * 
 * <pre>
 * service.getShapes(dbName, new AsyncCallback<Shape[]>() {
 *   public void onSuccess(Shape[] result) {
 *     controller.processShapes(result);
 *   }
 * 
 *   public void onFailure(Throwable caught) {
 *     // Convenient way to find out which exception was thrown.
 *     try {
 *       throw caught;
 *     } catch (ShapeException e) {
 *       // one of the 'throws' from the original method
 *     } catch (DbException e) {
 *       // one of the 'throws' from the original method
 *     } catch (Throwable e) {
 *       // last resort -- a very unexpected exception
 *     }
 *   }
 * });
 * </pre>
 * 
 * @author ladislav.gazo
 */
public interface ICallback<T> {
	  /**
	   * Called when an asynchronous call fails to complete normally.
	   * 
	   * @param caught failure encountered while executing a remote procedure call
	   */
	 void onFailure(Throwable caught);
	 /**
	   * Called when an asynchronous call completes successfully. It is always safe
	   * to downcast the parameter (of type <code>Object</code>) to the return
	   * type of the original method for which this is a callback. Note that if the
	   * return type of the synchronous service interface method is a primitive then
	   * the parameter will be the boxed version of the primitive (for example, an
	   * <code>int</code> return type becomes an {@link Integer}.
	   */
	 void onSuccess(T result);
}
