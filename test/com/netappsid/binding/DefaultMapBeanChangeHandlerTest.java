package com.netappsid.binding;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.jgoodies.binding.value.ValueModel;

public class DefaultMapBeanChangeHandlerTest
{
	private DynamicPresentationModel dynamicPresentationModel;
	private DefaultMapBeanChangeHandler handler;
	private HashMap<String, Object> oldValue;
	private HashMap<String, Object> newValue;
	private final String propertyName = "mapProperty";
	private HashMap<String, ValueModel> namesToValueModel;
	

	@Before
	public void setUp()
	{
		oldValue = new HashMap<String, Object>();
		newValue = new HashMap<String, Object>();

		namesToValueModel = new HashMap<String, ValueModel>();
		
		namesToValueModel.put(propertyName, mock(ValueModel.class));

		dynamicPresentationModel = mock(DynamicPresentationModel.class);

		when(dynamicPresentationModel.getNamesToValueModels()).thenReturn(namesToValueModel);

		handler = new DefaultMapBeanChangeHandler(dynamicPresentationModel);
	}

	@Test
	public void test_MapBeanChanged_EnsureValueModelUpdated_WithNewMapValue()
	{
		Object propertyNewValue = new Object();
		newValue.put(propertyName, propertyNewValue);

		handler.propertyChange(new PropertyChangeEvent(dynamicPresentationModel, propertyName, oldValue, newValue));

		verify(dynamicPresentationModel.getNamesToValueModels().get(propertyName)).setValue(propertyNewValue);
	}

	@Test
	public void test_MapBeanChanged_EnsureNewUpdated_WithActualValueModelValue()
	{
		Object notPresentPropertyValue = new Object();

		ValueModel notPresentPropertyValueModel = mock(ValueModel.class);
		when(notPresentPropertyValueModel.getValue()).thenReturn(notPresentPropertyValue);

		namesToValueModel.put("notPresentProperty", notPresentPropertyValueModel);

		handler.propertyChange(new PropertyChangeEvent(dynamicPresentationModel, propertyName, oldValue, newValue));

		assertEquals(notPresentPropertyValue, dynamicPresentationModel.getNamesToValueModels().get("notPresentProperty").getValue());
	}
}
