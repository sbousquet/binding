package com.netappsid.binding.value;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.netappsid.binding.beans.SimplePropertyAdapter;
import com.netappsid.binding.beans.support.StandardChangeSupportFactory;
import com.netappsid.observable.ObservableCollections;

public class IndexedCollectionValueModelTest
{
	private IndexedCollectionValueModel indexedCollectionValueModel;
	private Object firstObject;

	@Before
	public void setup()
	{
		firstObject = new Object();
		List collection = ObservableCollections.newObservableArrayList(firstObject);

		SimplePropertyAdapter simplePropertyAdapter = mock(SimplePropertyAdapter.class);
		when(simplePropertyAdapter.getValue()).thenReturn(collection);

		indexedCollectionValueModel = new IndexedCollectionValueModel(simplePropertyAdapter, new StandardChangeSupportFactory());
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

	@Test
	public void testAdd()
	{
		Object added = new Object();
		indexedCollectionValueModel.add(added);
		assertEquals(2, indexedCollectionValueModel.size());
	}

	@Test
	public void testAddAll()
	{
		List added = Arrays.asList(new Object(), new Object());
		indexedCollectionValueModel.addAll(added);
		assertEquals(3, indexedCollectionValueModel.size());
	}

	@Test
	public void testClear()
	{
		indexedCollectionValueModel.clear();
		assertEquals(0, indexedCollectionValueModel.size());
	}

	@Test
	public void testContains()
	{
		assertTrue("FirsObject must be found", indexedCollectionValueModel.contains(firstObject));
	}

	@Test
	public void testContainsAll()
	{
		List added = Arrays.asList(new Object(), new Object());
		indexedCollectionValueModel.clear();
		indexedCollectionValueModel.addAll(added);
		assertTrue("FirsObject must be found", indexedCollectionValueModel.containsAll(added));
	}

	@Test
	public void testIsEmpty()
	{
		indexedCollectionValueModel.clear();
		assertTrue(indexedCollectionValueModel.isEmpty());
	}

	@Test
	public void testIterator()
	{
		Iterator iterator = indexedCollectionValueModel.iterator();
		assertTrue("Should have an element", iterator.hasNext());
		assertEquals("First element must be returned", firstObject, iterator.next());
	}

	@Test
	public void testRemove()
	{
		indexedCollectionValueModel.remove(firstObject);
		assertEquals(0, indexedCollectionValueModel.size());
	}

	@Test
	public void testRemoveAll()
	{
		List added = Arrays.asList(new Object(), new Object());
		indexedCollectionValueModel.addAll(added);
		indexedCollectionValueModel.removeAll(Arrays.asList(firstObject));
		assertTrue(indexedCollectionValueModel.containsAll(added));
	}

	@Test
	public void testRetainAll()
	{
		List added = Arrays.asList(new Object(), new Object());
		indexedCollectionValueModel.addAll(added);
		indexedCollectionValueModel.retainAll(Arrays.asList(firstObject));
		assertEquals(1, indexedCollectionValueModel.size());
	}
}
