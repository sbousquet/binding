package com.netappsid.binding.value;

import com.netappsid.binding.beans.CollectionValueModel;
import com.netappsid.observable.CollectionChangeListener;

public interface CollectionValueModelFactory
{
	CollectionValueModel getCollectionValueModel(String propertyName, CollectionChangeListener collectionChangeListener);
}
