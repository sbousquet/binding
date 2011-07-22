package com.netappsid.binding.value;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.AbstractCollectionValueModel;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.observable.BatchAction;
import com.netappsid.observable.ObservableList;

public class IndexedCollectionValueModel<T> extends AbstractCollectionValueModel<T, ObservableList<T>> implements ObservableList<T>
{

	@Override
	public void add(int index, T element)
	{
		getValue().add(index, element);
	}

	@Override
	public boolean add(T e)
	{
		return getValue().add(e);
	}

	@Override
	public boolean addAll(Collection<? extends T> c)
	{
		return getValue().addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c)
	{
		return getValue().addAll(index, c);
	}

	@Override
	public void clear()
	{
		getValue().clear();
	}

	@Override
	public boolean contains(Object o)
	{
		return getValue().contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return getValue().containsAll(c);
	}

	@Override
	public boolean equals(Object o)
	{
		return getValue().equals(o);
	}

	@Override
	public void executeBatchAction(BatchAction action)
	{
		getValue().executeBatchAction(action);
	}

	@Override
	public T get(int index)
	{
		return getValue().get(index);
	}

	@Override
	public int hashCode()
	{
		return getValue().hashCode();
	}

	@Override
	public int indexOf(Object o)
	{
		return getValue().indexOf(o);
	}

	@Override
	public boolean isEmpty()
	{
		return getValue().isEmpty();
	}

	@Override
	public Iterator<T> iterator()
	{
		return getValue().iterator();
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return getValue().lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator()
	{
		return getValue().listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index)
	{
		return getValue().listIterator(index);
	}

	@Override
	public T remove(int index)
	{
		return getValue().remove(index);
	}

	@Override
	public boolean remove(Object o)
	{
		return getValue().remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return getValue().removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return getValue().retainAll(c);
	}

	@Override
	public T set(int index, T element)
	{
		return getValue().set(index, element);
	}

	@Override
	public int size()
	{
		return getValue().size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex)
	{
		return getValue().subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray()
	{
		return getValue().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return getValue().toArray(a);
	}

	public IndexedCollectionValueModel(ValueModel valueModel, ChangeSupportFactory changeSupportFactory)
	{
		super(valueModel, changeSupportFactory);
	}

}
