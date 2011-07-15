package com.netappsid.binding.beans;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.observable.ObservableCollection;

public interface CollectionValueModel<K> extends ValueModel, ObservableCollection
{
	public Object get(K key);

	public void set(K key, Object newValue);
}