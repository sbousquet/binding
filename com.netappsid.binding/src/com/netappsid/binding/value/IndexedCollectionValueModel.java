package com.netappsid.binding.value;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.AbstractCollectionValueModel;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.observable.BatchAction;
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
		return validateNotNull(getValue()).isEmpty();
	}

	@Override
	public Iterator<T> iterator()
	{
		return validateNotNull(getValue()).iterator();
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return validateNotNull(getValue()).lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator()
	{
		return validateNotNull(getValue()).listIterator();
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
		return validateNotNull(getValue()).size();
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
}
