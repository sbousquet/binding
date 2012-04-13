package com.netappsid.binding;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.binding.value.ValueHolder;

public class DynamicPresentationModelValueModelFactoryImpl implements DynamicPresentationModelValueModelFactory
{
	@Override
	public ValueModel createValueModelForMapValue(String propertyName, Object mapValue, ChangeSupportFactory changeSupportFactory)
	{
		return new ValueHolder(changeSupportFactory, mapValue);
	}
}
