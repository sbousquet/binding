package com.netappsid.binding;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.netappsid.binding.beans.support.StandardChangeSupportFactory;
import com.netappsid.binding.value.ValueHolder;
import com.netappsid.observable.StandardObservableCollectionSupportFactory;

public class DynamicPresentationModelTest
{
	@Test
	public void testAssertStateModelNotNull()
	{
		DynamicPresentationModel dynamicPresentationModel = new DynamicPresentationModel(new StandardChangeSupportFactory(),
				new StandardObservableCollectionSupportFactory());

		assertNotNull(dynamicPresentationModel.getStateModel());
	}

	@Test
	public void testSetMappedBeanChangeHandler_EnsureNewListenerAddedOnMapChannel()
	{
		ValueHolder mapChannel = new ValueHolder(new StandardChangeSupportFactory(), new HashMap<String, Object>());
		DynamicPresentationModel dynamicPresentationModel = new DynamicPresentationModel(new StandardChangeSupportFactory(),
				new StandardObservableCollectionSupportFactory(), mapChannel, Map.class);

		DynamicPresentationModelMapBeanChangeHandler listener = mock(DynamicPresentationModelMapBeanChangeHandler.class);
		dynamicPresentationModel.setMapBeanChangeHandler(listener);

		// Can't equals listener because it's wrapped by jgoodies
		assertEquals(1, mapChannel.getPropertyChangeListeners().length);
	}
}
