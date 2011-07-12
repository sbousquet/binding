package com.netappsid.binding.value;

import com.jgoodies.binding.value.ValueModel;

public interface ValueModelFactory
{
	boolean isCachingRequired();
	
	ValueModel getValueModel(String propertyName);
}
