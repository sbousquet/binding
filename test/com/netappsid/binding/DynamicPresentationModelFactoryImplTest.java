package com.netappsid.binding;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.netappsid.binding.DefaultPresentationModelTest.TestSubModel1;
import com.netappsid.binding.beans.support.StandardChangeSupportFactory;
import com.netappsid.observable.StandardObservableCollectionSupportFactory;

public class DynamicPresentationModelFactoryImplTest
{
	private PresentationModel rootModel;

	@Before
	public void setUp()
	{
		rootModel = new DefaultPresentationModel(new StandardChangeSupportFactory(), new StandardObservableCollectionSupportFactory(), TestSubModel1.class);
	}

	@Test
	public void test()
	{
		DynamicPresentationModelFactoryImpl factory = new DynamicPresentationModelFactoryImpl();
		DynamicPresentationModel newDynamicPresentationModel = factory.newDynamicPresentationModel(rootModel, "property1");

		assertTrue(newDynamicPresentationModel.getValueModelFactory() instanceof DynamicPresentationModelValueModelFactoryImpl);
		assertTrue(newDynamicPresentationModel.getMapBeanChangeHandler() instanceof DefaultMapBeanChangeHandler);
		assertTrue(newDynamicPresentationModel.getMappedValueChangeHandler() instanceof DefaultMappedValueChangeHandler);
	}
}
