package com.netappsid.binding.value;

import com.jgoodies.binding.value.ValueModel;

public interface BoundValueModel extends ValueModel
{
	public static final String UNKNOWN_PROPERTY = "UNKNOWN_PROPERTY";

	String getPropertyName();
}
