package com.netappsid.binding.value;

import java.util.Collection;
import java.util.Collections;
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
		ObservableList<T> value = getValue();

		if (value == null)
		{
			throw new RuntimeException("Trying to add an object when ValueModel's value is null");
		}

		value.add(index, element);
	}

	@Override
	public boolean add(T e)
	{
		ObservableList<T> value = getValue();
		if (value == null)
		{
			throw new RuntimeException("Trying to add an object when ValueModel's value is null");
		}

		return value.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends T> c)
	{
		ObservableList<T> value = getValue();

		if (value == null)
		{

			throw new RuntimeException("Trying to add an object when ValueModel's value is null");
		}

		return value.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c)
	{
		ObservableList<T> value = getValue();
		if (value == null)
		{
			throw new RuntimeException("Trying to add an object when ValueModel's value is null");
		}

		return value.addAll(index, c);
	}

	@Override
	public void clear()
	{
		ObservableList<T> value = getValue();
		if (value != null)
		{
			getValue().clear();
		}
	}

	@Override
	public boolean contains(Object o)
	{
		ObservableList<T> value = getValue();
		return (value == null) ? false : value.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		ObservableList<T> value = getValue();
		return (value == null) ? false : value.containsAll(c);
	}

	@Override
	public boolean equals(Object o)
	{
		return getValue().equals(o);
	}

	@Override
	public void executeBatchAction(BatchAction action)
	{
		ObservableList<T> value = getValue();
		if (value != null)
		{
			value.executeBatchAction(action);
		}
	}

	@Override
	public T get(int index)
	{
		ObservableList<T> value = getValue();
		if (value == null)
		{
			throw new RuntimeException("Trying to get an item when ValueModel's value is null");
		}

		return value.get(index);
	}

	@Override
	public int hashCode()
	{
		return getValue().hashCode();
	}

	@Override
	public int indexOf(Object o)
	{
		ObservableList<T> value = getValue();
		return (value == null) ? -1 : value.indexOf(o);
	}

	@Override
	public boolean isEmpty()
	{
		ObservableList<T> value = getValue();
		return (value == null) ? true : value.isEmpty();
	}

	@Override
	public Iterator<T> iterator()
	{
		ObservableList<T> value = getValue();
		return (value == null) ? Collections.EMPTY_LIST.iterator() : value.iterator();
	}

	@Override
	public int lastIndexOf(Object o)
	{
		ObservableList<T> value = getValue();
		return (value == null) ? -1 : value.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator()
	{
		ObservableList<T> value = getValue();
		return (value == null) ? Collections.EMPTY_LIST.listIterator() : value.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index)
	{
		ObservableList<T> value = getValue();
		return (value == null) ? Collections.EMPTY_LIST.listIterator() : value.listIterator(index);
	}

	@Override
	public T remove(int index)
	{
		ObservableList<T> value = getValue();

		if (value == null)
		{
			throw new RuntimeException("Trying to remove an object when ValueModel's value is null");
		}

		return value.remove(index);
	}

	@Override
	public boolean remove(Object o)
	{
		ObservableList<T> value = getValue();

		if (value == null)
		{
			throw new RuntimeException("Trying to remove an object when ValueModel's value is null");
		}

		return value.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		ObservableList<T> value = getValue();
		if (value == null)
		{
			throw new RuntimeException("Trying to remove an object when ValueModel's value is null");
		}

		return value.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		ObservableList<T> value = getValue();
		return (value == null) ? false : value.retainAll(c);
	}

	@Override
	public T set(int index, T element)
	{
		ObservableList<T> value = getValue();

		if (value == null)
		{
			throw new RuntimeException("Trying to set an object when ValueModel's value is null");
		}

		return value.set(index, element);
	}

	@Override
	public int size()
	{
		ObservableList<T> value = getValue();
		return (value == null) ? 0 : value.size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex)
	{
		ObservableList<T> value = getValue();
		return (value == null) ? Collections.EMPTY_LIST : value.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray()
	{
		ObservableList<T> value = getValue();
		return (value == null) ? new Object[] {} : value.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		ObservableList<T> value = (ObservableList<T>) getValue();
		return ((value == null) ? (T[]) new Object[] {} : value.toArray(a));
	}
}
