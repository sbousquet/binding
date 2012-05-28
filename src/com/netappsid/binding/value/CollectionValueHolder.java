package com.netappsid.binding.value;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.common.collect.ImmutableList;
import com.netappsid.binding.beans.CollectionValueModel;
import com.netappsid.observable.BatchAction;
import com.netappsid.observable.CollectionChangeListener;
import com.netappsid.observable.CollectionDifference;
import com.netappsid.observable.ObservableList;

public class CollectionValueHolder implements CollectionValueModel
{
	private final ObservableList collectionValue;

	public CollectionValueHolder(ObservableList collectionValue)
	{
		this.collectionValue = collectionValue;
	}

	@Override
	public void addValueChangeListener(PropertyChangeListener arg0)
	{}

	@Override
	public Object getValue()
	{
		return this.collectionValue;
	}

	@Override
	public void removeValueChangeListener(PropertyChangeListener arg0)
	{}

	@Override
	public void setValue(Object arg0)
	{
		throw new RuntimeException("Setting Collection of a CollectionValueHolder is not supported");
	}

	@Override
	public void addCollectionChangeListener(CollectionChangeListener listener)
	{}

	@Override
	public void removeCollectionChangeListener(CollectionChangeListener listener)
	{}

	@Override
	public ImmutableList getCollectionChangeListeners()
	{
		return this.collectionValue.getCollectionChangeListeners();
	}

	@Override
	public void executeBatchAction(BatchAction action)
	{
		this.collectionValue.executeBatchAction(action);
	}

	@Override
	public boolean add(Object arg0)
	{
		return this.collectionValue.add(arg0);
	}

	@Override
	public boolean addAll(Collection arg0)
	{
		return this.collectionValue.addAll(arg0);
	}

	@Override
	public void clear()
	{}

	@Override
	public boolean contains(Object arg0)
	{
		return this.collectionValue.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection arg0)
	{
		return this.collectionValue.containsAll(arg0);
	}

	@Override
	public boolean isEmpty()
	{
		return this.collectionValue.isEmpty();
	}

	@Override
	public Iterator iterator()
	{
		return this.collectionValue.iterator();
	}

	@Override
	public boolean remove(Object arg0)
	{
		return this.collectionValue.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection arg0)
	{
		return this.collectionValue.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection arg0)
	{
		return this.collectionValue.retainAll(arg0);
	}

	@Override
	public int size()
	{
		return this.collectionValue.size();
	}

	@Override
	public Object[] toArray()
	{
		return this.collectionValue.toArray();
	}

	@Override
	public Object[] toArray(Object[] arg0)
	{
		return this.collectionValue.toArray(arg0);
	}

	@Override
	public void add(int index, Object element)
	{
		this.collectionValue.add(index, element);
	}

	@Override
	public boolean addAll(int index, Collection c)
	{
		return this.collectionValue.addAll(index, c);
	}

	@Override
	public Object get(int index)
	{
		return this.collectionValue.get(index);
	}

	@Override
	public int indexOf(Object o)
	{
		return this.collectionValue.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return this.collectionValue.lastIndexOf(o);
	}

	@Override
	public ListIterator listIterator()
	{
		return this.collectionValue.listIterator();
	}

	@Override
	public ListIterator listIterator(int index)
	{
		return this.collectionValue.listIterator(index);
	}

	@Override
	public Object remove(int index)
	{
		return null;
	}

	@Override
	public Object set(int index, Object element)
	{
		return this.collectionValue.set(index, element);
	}

	@Override
	public List subList(int fromIndex, int toIndex)
	{
		return this.collectionValue.subList(fromIndex, toIndex);
	}

	/**
	 * @param difference
	 * @see com.netappsid.observable.ObservableCollection#apply(com.netappsid.observable.CollectionDifference)
	 */
	@Override
	public void apply(CollectionDifference difference)
	{
		collectionValue.apply(difference);
	}

	/**
	 * @param difference
	 * @see com.netappsid.observable.ObservableCollection#unapply(com.netappsid.observable.CollectionDifference)
	 */
	@Override
	public void unapply(CollectionDifference difference)
	{
		collectionValue.unapply(difference);
	}

	@Override
	public void dispose()
	{
	}
}
