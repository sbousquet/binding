package com.netappsid.binding;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.support.ChangeSupportFactory;

public interface DynamicPresentationModelValueModelFactory
{
	ValueModel createValueModelForMapValue(String propertyName, Object mapValue, ChangeSupportFactory changeSupportFactory);
}
