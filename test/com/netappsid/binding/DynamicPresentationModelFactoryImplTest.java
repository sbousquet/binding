package com.netappsid.binding;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.netappsid.binding.DefaultPresentationModelTest.TestSubModel1;
import com.netappsid.binding.beans.support.StandardChangeSupportFactory;
import com.netappsid.observable.StandardObservableCollectionSupportFactory;

public class DynamicPresentationModelFactoryImplTest
{
	private PresentationModel rootModel;
	private DynamicPresentationModelFactoryImpl factory;

	@Before
	public void setUp()
	{
		rootModel = spy(new DefaultPresentationModel(new StandardChangeSupportFactory(), new StandardObservableCollectionSupportFactory(), TestSubModel1.class));
		factory = new DynamicPresentationModelFactoryImpl();
	}

	@Test
	public void testEnsureRightValueModel()
	{
		DynamicPresentationModel newDynamicPresentationModel = factory.newDynamicPresentationModel(rootModel, "property1");
		verify(rootModel).getValueModel("property1");
	}

	@Test
	public void testEnsureBeanClass()
	{
		DynamicPresentationModel newDynamicPresentationModel = factory.newDynamicPresentationModel(rootModel, "property1");
		assertEquals(Map.class, newDynamicPresentationModel.getBeanClass());
	}

	@Test
	public void testEnsureValueModelFactory()
	{
		DynamicPresentationModel newDynamicPresentationModel = factory.newDynamicPresentationModel(rootModel, "property1");

		assertTrue(newDynamicPresentationModel.getValueModelFactory() instanceof DynamicPresentationModelValueModelFactoryImpl);
	}

	@Test
	public void testEnsureMapBeanChangeHandler()
	{
		DynamicPresentationModel newDynamicPresentationModel = factory.newDynamicPresentationModel(rootModel, "property1");

		assertTrue(newDynamicPresentationModel.getMapBeanChangeHandler() instanceof DefaultMapBeanChangeHandler);
	}

	@Test
	public void testEnsureMappedValueChangeHandler()
	{
		DynamicPresentationModel newDynamicPresentationModel = factory.newDynamicPresentationModel(rootModel, "property1");

		assertTrue(newDynamicPresentationModel.getMappedValueChangeHandler() instanceof DefaultMappedValueChangeHandler);
	}
}
