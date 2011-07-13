package com.netappsid.binding.value;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.CollectionValueModel;

public interface ValueModelFactory
{
	boolean isCachingRequired();
	
	ValueModel getValueModel(String propertyName);

	CollectionValueModel getCollectionValueModel(String propertyName);
}
