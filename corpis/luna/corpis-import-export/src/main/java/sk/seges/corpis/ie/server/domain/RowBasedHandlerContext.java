/**
 * 
 */
package sk.seges.corpis.ie.server.domain;


/**
 * @author ladislav.gazo
 */
public class RowBasedHandlerContext extends HandlerContext {
	private static final String ROW = "row";
	
	public Integer getRow() {
		return (Integer) contextMap.get(ROW);
	}
	
	public void setRow(Integer row) {
		contextMap.put(ROW, row);
	}
}
