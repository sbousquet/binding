package com.netappsid.binding;

import static org.junit.Assert.*;

import org.junit.Test;

import com.netappsid.binding.beans.support.StandardChangeSupportFactory;
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
}
