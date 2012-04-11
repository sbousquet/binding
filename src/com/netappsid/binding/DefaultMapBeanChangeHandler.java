package com.netappsid.binding;

import java.beans.PropertyChangeEvent;
import java.util.Map;
import java.util.Map.Entry;

import com.jgoodies.binding.value.ValueModel;

public class DefaultMapBeanChangeHandler implements DynamicPresentationModelMapBeanChangeHandler
{
	private final DynamicPresentationModel dynamicPresentationModel;

	public DefaultMapBeanChangeHandler(DynamicPresentationModel dynamicPresentationModel)
	{
		this.dynamicPresentationModel = dynamicPresentationModel;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getNewValue() instanceof Map)
		{
			Map newMap = (Map) evt.getNewValue();

			for (Entry<String, ValueModel> entry : dynamicPresentationModel.getNamesToValueModels().entrySet())
			{
				String key = entry.getKey();
				ValueModel valueModel = entry.getValue();

				if (newMap.containsKey(key))
				{
					valueModel.setValue(newMap.get(key));
				}
				else
				{
					newMap.put(key, valueModel.getValue());
				}
			}
		}
	}
}