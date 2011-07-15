package com.netappsid.binding.value;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.CollectionValueModel;

public interface ValueModelFactory
{
	ValueModel getValueModel(String propertyName);

	CollectionValueModel getCollectionValueModel(String propertyName);
}
