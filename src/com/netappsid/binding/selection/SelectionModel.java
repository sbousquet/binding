package com.netappsid.binding.selection;

import java.beans.PropertyChangeListener;
import java.util.SortedSet;

/**
 * 
 * 
 * @author Eric Belanger
 * @author NetAppsID Inc.
 * @version $Revision: 1.5 $
 */
public interface SelectionModel
{
	void addSelectionChangeListener(PropertyChangeListener listener);
	SortedSet<Integer> getSelection();
	boolean hasSelection();
	void removeSelectionChangeListener(PropertyChangeListener listener);
	void setSelection();
	void setSelectedItem(Object selection);

	void setSelectedItems(Object[] selections);
	void setSelection(Integer index);
	void setSelection(SortedSet<Integer> indexes);

	void setSelectionInterval(Integer index0, Integer index1);
}
