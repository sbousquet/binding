package com.netappsid.binding.beans;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.observable.CollectionChangeListener;

public interface CollectionValueModel extends ValueModel
{

	public void addCollectionChangeListener(CollectionChangeListener listener);

	public void removeCollectionChangeListener(CollectionChangeListener listener);

}