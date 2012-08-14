package com.netappsid.binding.value;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.BeanAdapter;
import com.netappsid.binding.beans.CollectionValueModel;
import com.netappsid.binding.beans.SimplePropertyAdapter;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.binding.beans.support.StandardChangeSupportFactory;
import com.netappsid.observable.ObservableCollectionSupportFactory;
import com.netappsid.observable.StandardObservableCollectionSupportFactory;
import com.netappsid.test.beans.TestBean;
import com.netappsid.undoredo.UndoRedoCollectionValueModel;
import com.netappsid.undoredo.UndoRedoManager;
import com.netappsid.undoredo.UndoRedoValueModel;

public class BoundValueModelTest
{
	private static final String EXPECTED_PROPERTY_NAME = "expectedPropertyName";
	private CollectionValueModel boundValueModel;
	private ChangeSupportFactory changeSupportFactory;
	private ObservableCollectionSupportFactory observableCollectionSupportFactory;
	private UndoRedoManager undoRedoManager;

	@Before
	public void setUp()
	{
		// Using CollectionValueModel since it implements BoundValueModel and
		boundValueModel = mock(CollectionValueModel.class);
		when(boundValueModel.getPropertyName()).thenReturn(EXPECTED_PROPERTY_NAME);

		changeSupportFactory = new StandardChangeSupportFactory();
		observableCollectionSupportFactory = new StandardObservableCollectionSupportFactory();
		undoRedoManager = new UndoRedoManager();
	}

	@Test
	public void testGetPropertyName_IndexedCollectionValueModel()
	{
		IndexedCollectionValueModel valueModel = new IndexedCollectionValueModel(boundValueModel, changeSupportFactory, observableCollectionSupportFactory);
		assertEquals(EXPECTED_PROPERTY_NAME, valueModel.getPropertyName());
	}

	@Test
	public void testGetPropertyName_SimplePropertyAdapter()
	{
		BeanAdapter beanAdapter = new BeanAdapter(changeSupportFactory, observableCollectionSupportFactory, new TestBean("1"), TestBean.class);

		SimplePropertyAdapter valueModel = new SimplePropertyAdapter(beanAdapter, TestBean.PROPERTYNAME_PROPERTY1);
		assertEquals(TestBean.PROPERTYNAME_PROPERTY1, valueModel.getPropertyName());
	}

	@Test
	public void testGetPropertyName_UndoRedoValueModel()
	{
		UndoRedoValueModel valueModel = new UndoRedoValueModel(undoRedoManager, boundValueModel, changeSupportFactory);
		assertEquals(EXPECTED_PROPERTY_NAME, valueModel.getPropertyName());
	}

	@Test
	public void testGetPropertyName_UndoRedoCollectionValueModel()
	{
		UndoRedoCollectionValueModel valueModel = new UndoRedoCollectionValueModel(undoRedoManager, boundValueModel, observableCollectionSupportFactory,
				changeSupportFactory);
		assertEquals(EXPECTED_PROPERTY_NAME, valueModel.getPropertyName());
	}

	@Test
	public void testGetPropertyName_BufferedValueModel()
	{
		BufferedValueModel valueModel = new BufferedValueModel(changeSupportFactory, boundValueModel, mock(ValueModel.class));
		assertEquals(EXPECTED_PROPERTY_NAME, valueModel.getPropertyName());
	}

	@Test
	public void testGetPropertyName_ComponentValueModel()
	{
		ComponentValueModel valueModel = new ComponentValueModel(changeSupportFactory, boundValueModel);
		assertEquals(EXPECTED_PROPERTY_NAME, valueModel.getPropertyName());
	}
}
