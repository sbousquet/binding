package com.netappsid.binding.value;

import com.jgoodies.binding.value.ValueModel;

public interface ValueModelFactory
{
	ValueModel getValueModel(String propertyName);
}
