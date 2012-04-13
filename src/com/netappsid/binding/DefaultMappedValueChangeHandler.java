package com.netappsid.binding;

import java.beans.PropertyChangeEvent;
import java.util.Map;

import com.jgoodies.binding.value.ValueModel;

class DefaultMappedValueChangeHandler implements DynamicPresentationModelMappedValueChangedHandler
{
	private final DynamicPresentationModel dynamicPresentationModel;

	public DefaultMappedValueChangeHandler(DynamicPresentationModel dynamicPresentationModel)
	{
		this.dynamicPresentationModel = dynamicPresentationModel;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void propertyChange(PropertyChangeEvent evt)
	{
		Map<ValueModel, String> valueModelNames = dynamicPresentationModel.getValueModelToNames();

		if (valueModelNames.containsKey(evt.getSource()))
		{
			((Map) dynamicPresentationModel.getBean()).put(valueModelNames.get(evt.getSource()), evt.getNewValue());
		}
	}
}