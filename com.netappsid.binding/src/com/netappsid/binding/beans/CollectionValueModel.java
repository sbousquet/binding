package com.netappsid.binding.beans;

import java.util.Collection;
import java.util.Iterator;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.observable.CollectionChangeListener;

public interface CollectionValueModel<K> extends ValueModel, Iterable
{
	public void addCollectionChangeListener(CollectionChangeListener listener);

	public void removeCollectionChangeListener(CollectionChangeListener listener);

	public Object get(K key);

	public void set(K key, Object newValue);

	public boolean add(Object e);

	public boolean addAll(Collection c);

	public void clear();

	public boolean contains(Object e);

	public boolean containsAll(Collection c);

	public boolean isEmpty();

	@Override
	public Iterator iterator();

	public boolean remove(Object e);

	public boolean removeAll(Collection c);

	public boolean retainAll(Collection c);

	public int size();
}