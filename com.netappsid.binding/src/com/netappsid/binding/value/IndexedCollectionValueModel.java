package com.netappsid.binding.value;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.common.collect.ImmutableList;
import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.AbstractCollectionValueModel;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.observable.BatchAction;
import com.netappsid.observable.CollectionChangeListener;
import com.netappsid.observable.ObservableCollectionSupportFactory;
import com.netappsid.observable.ObservableList;

public class IndexedCollectionValueModel<T> extends AbstractCollectionValueModel<T, ObservableList<T>> implements ObservableList<T>
{
	public IndexedCollectionValueModel(ValueModel valueModel, ChangeSupportFactory changeSupportFactory,
			ObservableCollectionSupportFactory observableCollectionSupportFactory)
	{
		super(valueModel, changeSupportFactory, observableCollectionSupportFactory);
	}

	@Override
	public void add(int index, T element)
	{
		validateNotNull(getValue()).add(index, element);
	}

	@Override
	public boolean add(T e)
	{
		return validateNotNull(getValue()).add(e);
	}

	@Override
	public boolean addAll(Collection<? extends T> c)
	{
		return validateNotNull(getValue()).addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c)
	{
		return validateNotNull(getValue()).addAll(index, c);
	}

	@Override
	public void clear()
	{
		validateNotNull(getValue()).clear();
	}

	@Override
	public boolean contains(Object o)
	{
		return validateNotNull(getValue()).contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return validateNotNull(getValue()).containsAll(c);
	}

	@Override
	public boolean equals(Object o)
	{
		return validateNotNull(getValue()).equals(o);
	}

	@Override
	public void executeBatchAction(BatchAction action)
	{
		validateNotNull(getValue()).executeBatchAction(action);
	}

	@Override
	public T get(int index)
	{
		return validateNotNull(getValue()).get(index);
	}

	@Override
	public int hashCode()
	{
		return validateNotNull(getValue()).hashCode();
	}

	@Override
	public int indexOf(Object o)
	{
		return validateNotNull(getValue()).indexOf(o);
	}

	@Override
	public boolean isEmpty()
	{
		return (getValue() == null) ? true : getValue().isEmpty();
	}

	@Override
	public Iterator<T> iterator()
	{
		return (getValue() == null) ? Collections.EMPTY_LIST.iterator() : getValue().iterator();
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return validateNotNull(getValue()).lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator()
	{
		return (getValue() == null) ? Collections.EMPTY_LIST.listIterator() : getValue().listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index)
	{
		return validateNotNull(getValue()).listIterator(index);
	}

	@Override
	public T remove(int index)
	{
		return validateNotNull(getValue()).remove(index);
	}

	@Override
	public boolean remove(Object o)
	{
		return validateNotNull(getValue()).remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return validateNotNull(getValue()).removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return validateNotNull(getValue()).retainAll(c);
	}

	@Override
	public T set(int index, T element)
	{
		return validateNotNull(getValue()).set(index, element);
	}

	@Override
	public int size()
	{
		return (getValue() == null) ? 0 : getValue().size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex)
	{
		return validateNotNull(getValue()).subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray()
	{
		return validateNotNull(getValue()).toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return validateNotNull(getValue()).toArray(a);
	}

	private ObservableList<T> validateNotNull(ObservableList<T> value)
	{
		if (value == null)
		{
			throw new RuntimeException("Cannot operate on a null ValueModel target.");
		}

		return value;
	}

	@Override
	public ImmutableList<CollectionChangeListener<T>> getCollectionChangeListeners()
	{
		return ImmutableList.copyOf(getSupport().getCollectionChangeListeners());
	}
}
