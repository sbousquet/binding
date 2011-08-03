package com.netappsid.binding.value;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.netappsid.binding.beans.SimplePropertyAdapter;
import com.netappsid.binding.beans.support.StandardChangeSupportFactory;
import com.netappsid.observable.ClearAndAddAllBatchAction;
import com.netappsid.observable.ObservableCollections;
import com.netappsid.observable.ObservableList;
import com.netappsid.observable.StandardObservableCollectionSupportFactory;
import com.netappsid.test.tools.PropertyChangeAssertion;

public class IndexedCollectionValueModelTest
{
	private IndexedCollectionValueModel indexedCollectionValueModelWithNullBean;
	private IndexedCollectionValueModel indexedCollectionValueModel;
	private Object firstObject;
	private Object added1;
	private Object added2;

	@Before
	public void setup()
	{
		firstObject = new Object();
		added1 = new Object();
		added2 = new Object();
		List collection = ObservableCollections.newObservableArrayList(firstObject);

		SimplePropertyAdapter simplePropertyAdapter = mock(SimplePropertyAdapter.class);
		when(simplePropertyAdapter.getValue()).thenReturn(collection);

		indexedCollectionValueModel = new IndexedCollectionValueModel(simplePropertyAdapter, new StandardChangeSupportFactory(),
				new StandardObservableCollectionSupportFactory());

		SimplePropertyAdapter simplePropertyAdapterWithNullValue = mock(SimplePropertyAdapter.class);
		indexedCollectionValueModelWithNullBean = new IndexedCollectionValueModel(simplePropertyAdapterWithNullValue, new StandardChangeSupportFactory(),
				new StandardObservableCollectionSupportFactory());
	}

	@Test
	public void test_GetValidIndex()
	{
		assertEquals(firstObject, indexedCollectionValueModel.get(0));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void test_GetInvalidIndex()
	{
		indexedCollectionValueModel.get(3);
	}

	@Test
	public void test_SetValidIndex()
	{
		Object newObject = new Object();
		indexedCollectionValueModel.set(0, newObject);

		assertEquals(newObject, indexedCollectionValueModel.get(0));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void test_SetInvalidIndex()
	{
		indexedCollectionValueModel.set(3, null);
	}

	@Test
	public void testSize()
	{
		assertEquals(1, indexedCollectionValueModel.size());
	}

	@Test(expected = RuntimeException.class)
	public void testSize_NullBean()
	{
		indexedCollectionValueModelWithNullBean.size();
	}

	@Test
	public void testAdd()
	{
		indexedCollectionValueModel.add(added1);
		assertTrue(indexedCollectionValueModel.contains(added1));
	}

	@Test(expected = RuntimeException.class)
	public void testAdd_NullBean()
	{
		indexedCollectionValueModelWithNullBean.add(added1);
	}

	@Test
	public void testAddAll()
	{
		List added = Arrays.asList(added1, added2);
		indexedCollectionValueModel.addAll(added);
		assertTrue(indexedCollectionValueModel.contains(added1));
		assertTrue(indexedCollectionValueModel.contains(added2));
	}

	@Test(expected = RuntimeException.class)
	public void testAddAll_NullBean()
	{
		List added = Arrays.asList(added1, added2);
		indexedCollectionValueModelWithNullBean.addAll(added);
	}

	@Test
	public void testClear()
	{
		indexedCollectionValueModel.clear();
		assertFalse(indexedCollectionValueModel.contains(firstObject));
	}

	@Test
	public void testContains()
	{
		assertTrue("FirsObject must be found", indexedCollectionValueModel.contains(firstObject));
	}

	@Test
	public void testContainsAll()
	{
		List added = Arrays.asList(added1, added2);
		indexedCollectionValueModel.clear();
		indexedCollectionValueModel.addAll(added);
		assertTrue("All added must be found", indexedCollectionValueModel.containsAll(added));
	}

	@Test
	public void testIsEmpty()
	{
		indexedCollectionValueModel.clear();
		assertTrue(indexedCollectionValueModel.isEmpty());
	}

	@Test(expected = RuntimeException.class)
	public void testIsEmpty_NullBean()
	{
		indexedCollectionValueModelWithNullBean.isEmpty();
	}

	@Test
	public void testIterator()
	{
		Iterator iterator = indexedCollectionValueModel.iterator();
		assertTrue("Should have an element", iterator.hasNext());
		assertEquals("First element must be returned", firstObject, iterator.next());
	}

	@Test(expected = RuntimeException.class)
	public void testIterator_NullBean()
	{
		indexedCollectionValueModelWithNullBean.iterator();
	}

	@Test
	public void testRemove()
	{
		indexedCollectionValueModel.remove(firstObject);
		assertFalse(indexedCollectionValueModel.contains(firstObject));
	}

	@Test(expected = RuntimeException.class)
	public void testRemove_NullBean()
	{
		indexedCollectionValueModelWithNullBean.remove(firstObject);
	}

	@Test
	public void testRemoveAll()
	{
		List added = Arrays.asList(added1, added2);
		indexedCollectionValueModel.addAll(added);
		indexedCollectionValueModel.removeAll(Arrays.asList(firstObject));
		assertTrue("Only first object must be found", indexedCollectionValueModel.containsAll(added));
	}

	@Test(expected = RuntimeException.class)
	public void testRemoveAll_NullBean()
	{
		indexedCollectionValueModelWithNullBean.removeAll(Arrays.asList(firstObject));
	}

	@Test
	public void testRetainAll()
	{
		List added = Arrays.asList(added1, added2);
		indexedCollectionValueModel.addAll(added);
		indexedCollectionValueModel.retainAll(Arrays.asList(firstObject));
		assertFalse(indexedCollectionValueModel.containsAll(added));
		assertTrue(indexedCollectionValueModel.contains(firstObject));
	}

	@Test
	public void testExecuteBatchAction()
	{
		ClearAndAddAllBatchAction action = new ClearAndAddAllBatchAction(Arrays.asList(added1));
		indexedCollectionValueModel.executeBatchAction(action);
		assertTrue(indexedCollectionValueModel.contains(added1));
	}

	@Test
	public void testToArray()
	{
		Object[] array = indexedCollectionValueModel.toArray();
		assertArrayEquals(new Object[] { firstObject }, array);
	}

	@Test
	public void testToArray_Overload()
	{
		Object[] array = indexedCollectionValueModel.toArray(new Object[] {});
		assertArrayEquals(new Object[] { firstObject }, array);
	}

	@Test
	public void testValueChange_EnsureSourceIsValueModel()
	{
		StandardChangeSupportFactory changeSupportFactory = new StandardChangeSupportFactory();

		ObservableList<Object> oldList = ObservableCollections.newObservableArrayList(added1);
		ObservableList<Object> newList = ObservableCollections.newObservableArrayList(added2);

		ValueHolder valueHolder = new ValueHolder(changeSupportFactory, oldList);

		IndexedCollectionValueModel indexedCollectionValueModel = new IndexedCollectionValueModel(valueHolder, changeSupportFactory,
				new StandardObservableCollectionSupportFactory());

		PropertyChangeAssertion eventSpy = new PropertyChangeAssertion();
		indexedCollectionValueModel.addValueChangeListener(eventSpy);
		indexedCollectionValueModel.setValue(newList);

		eventSpy.assertEventFired(AbstractValueModel.PROPERTYNAME_VALUE, indexedCollectionValueModel);
	}

	@Test
	public void testEnsureListenersOnNewCollection_AndSourceIsValueModel()
	{
		StandardChangeSupportFactory changeSupportFactory = new StandardChangeSupportFactory();

		ObservableList<Object> oldList = ObservableCollections.newObservableArrayList();
		ObservableList<Object> newList = ObservableCollections.newObservableArrayList();

		ValueHolder valueHolder = new ValueHolder(changeSupportFactory, oldList);

		IndexedCollectionValueModel indexedCollectionValueModel = new IndexedCollectionValueModel(valueHolder, changeSupportFactory,
				new StandardObservableCollectionSupportFactory());

		CollectionChangeEventSpy eventSpy = new CollectionChangeEventSpy();
		indexedCollectionValueModel.addCollectionChangeListener(eventSpy);

		Object addedInOldList = new Object();
		indexedCollectionValueModel.add(addedInOldList);
		eventSpy.assertEvent(indexedCollectionValueModel, ImmutableList.of(addedInOldList), ImmutableList.of(), -1);

		indexedCollectionValueModel.setValue(newList);

		Object addedInNewList = new Object();
		indexedCollectionValueModel.add(addedInNewList);
		eventSpy.assertEvent(indexedCollectionValueModel, ImmutableList.of(addedInNewList), ImmutableList.of(), -1);

		assertTrue("oldList must contain addedInOldList", oldList.contains(addedInOldList));
		assertTrue("newList must contain addedInNewList", newList.contains(addedInNewList));
	}
}
