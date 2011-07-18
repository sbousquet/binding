package com.netappsid.binding.value;

import com.jgoodies.binding.value.ValueModel;

public interface ValueModelFactory<T extends ValueModel>
{
	T getValueModel(String propertyName);
}
