package com.netappsid.undoredo;

import com.jgoodies.binding.value.ValueModel;

/**
 * An interface which {@link ValueModel}s can opt to implement to make it possible to replace their default initial value with a different one that is also
 * considered initial.
 * 
 * @author ftaillefer
 * 
 */
public interface ReinitializableValueModel
{

	/**
	 * <p>
	 * Attempts to reinitialize the value in this ValueModel without triggering the normal effects and events. If the ValueModel depends on inner elements which
	 * do not support silent setting, this method will tell them to set non silently (rather than giving up and doing nothing).
	 * </p>
	 * This should be called <b>only</b> when the ValueModel is expected to have its initial value <b>and</b> the value we want to set is also considered an
	 * initial value. This is <b>not</b> a reset for a ValueModel that has been used.
	 * 
	 * @param value
	 */
	public void reinitialize(Object value);
}
