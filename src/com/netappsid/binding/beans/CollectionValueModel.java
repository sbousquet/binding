package com.netappsid.binding.beans;

import com.netappsid.binding.value.BoundValueModel;
import com.netappsid.observable.ObservableList;

public interface CollectionValueModel<T> extends BoundValueModel, ObservableList<T>
{
	void dispose();
}