package com.netappsid.binding.value;

import java.util.Collection;
import java.util.Iterator;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.AbstractCollectionValueModel;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.observable.BatchAction;
import com.netappsid.observable.ObservableCollection;
import com.netappsid.observable.ObservableList;

public class IndexedCollectionValueModel extends AbstractCollectionValueModel<ObservableCollection, Integer>
{
	public IndexedCollectionValueModel(ValueModel valueModel, ChangeSupportFactory changeSupportFactory)
	{
		super(valueModel, changeSupportFactory);
	}

	@Override
	public Object get(Integer index)
	{
		return getList().get(index);
	}

	@Override
	public void set(Integer key, Object newValue)
	{
		getList().set(key, newValue);
	}

	@Override
	public boolean add(Object e)
	{
		return getList().add(e);
	}

	@Override
	public boolean addAll(Collection c)
	{
		return getList().addAll(c);
	}

	@Override
	public void clear()
	{
		getList().clear();
	}

	@Override
	public boolean contains(Object e)
	{
		return getList().contains(e);
	}

	@Override
	public boolean containsAll(Collection c)
	{
		return getList().containsAll(c);
	}

	@Override
	public boolean isEmpty()
	{
		return getList().isEmpty();
	}

	@Override
	public Iterator iterator()
	{
		return getList().iterator();
	}

	@Override
	public boolean remove(Object e)
	{
		return getList().remove(e);
	}

	@Override
	public boolean removeAll(Collection c)
	{
		return getList().removeAll(c);
	}

	@Override
	public boolean retainAll(Collection c)
	{
		return getList().retainAll(c);
	}

	@Override
	public int size()
	{
		return getList().size();
	}

	protected ObservableList getList()
	{
		ObservableList list;
		Object value = getValue();

		if (value != null && ObservableList.class.isAssignableFrom(value.getClass()))
		{
			list = (ObservableList) value;
		}
		else
		{
			throw new NullPointerException("The collection must not be null");
		}

		return list;
	}

	@Override
	public void executeBatchAction(BatchAction action)
	{
		getList().executeBatchAction(action);
		
	}

	@Override
	public Object[] toArray()
	{
		return getList().toArray();
	}

	@Override
	public Object[] toArray(Object[] a)
	{
		return getList().toArray(a);
	}
}
