package com.netappsid.binding.value;

import java.util.List;

import com.netappsid.binding.beans.AbstractCollectionValueModel;
import com.netappsid.binding.beans.SimplePropertyAdapter;

public class IndexedCollectionValueModel extends AbstractCollectionValueModel
{
	public IndexedCollectionValueModel(SimplePropertyAdapter propertyAdapter)
	{
		super(propertyAdapter);
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
