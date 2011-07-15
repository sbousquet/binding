package com.netappsid.binding.value;

import java.util.List;

import com.netappsid.binding.beans.AbstractCollectionValueModel;
import com.netappsid.binding.beans.SimplePropertyAdapter;
import com.netappsid.binding.beans.support.ChangeSupportFactory;

public class IndexedCollectionValueModel extends AbstractCollectionValueModel
{
	public IndexedCollectionValueModel(SimplePropertyAdapter propertyAdapter, ChangeSupportFactory changeSupportFactory)
	{
		super(propertyAdapter, changeSupportFactory);
	}

	public Object get(Integer index)
	{
		Object value = getValue();

		if (value != null && List.class.isAssignableFrom(value.getClass()))
		{
			List list = (List) value;
			return list.get(index);
		}

		return null;
	}
}
