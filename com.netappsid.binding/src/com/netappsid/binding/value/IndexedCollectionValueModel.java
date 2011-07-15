package com.netappsid.binding.value;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.netappsid.binding.beans.AbstractCollectionValueModel;
import com.netappsid.binding.beans.SimplePropertyAdapter;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.observable.ObservableCollection;

public class IndexedCollectionValueModel extends AbstractCollectionValueModel<ObservableCollection, Integer>
{
	public IndexedCollectionValueModel(SimplePropertyAdapter propertyAdapter, ChangeSupportFactory changeSupportFactory)
	{
		super(propertyAdapter, changeSupportFactory);
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

	protected List getList()
	{
		List list;
		Object value = getValue();

		if (value != null && List.class.isAssignableFrom(value.getClass()))
		{
			list = (List) value;
		}
		else
		{
			throw new NullPointerException("The collection must not be null");
		}

		return list;
	}
}
