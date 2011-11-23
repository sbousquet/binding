/**
 * 
 */
package com.netappsid.undoredo;

/**
 * @author xjodoin
 * @author NetAppsID inc.
 * 
 * @version
 * 
 */
public interface UndoSupport
{

	/**
	 * @param newValue
	 * @return
	 */
	Object getUndoableValue(Object newValue);

}
