package com.netappsid.binding.value;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Iterator;

import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import com.google.common.collect.Lists;
import com.netappsid.binding.beans.support.StandardChangeSupportFactory;
import com.netappsid.observable.CollectionChangeEvent;
import com.netappsid.observable.CollectionChangeListener;
import com.netappsid.observable.ObservableCollections;
import com.netappsid.observable.ObservableList;
import com.netappsid.observable.StandardObservableCollectionSupportFactory;

@RunWith(JukitoRunner.class)
public class CollectionValueHolderTest
{
	private CollectionValueHolder<Integer> holder;
	private StandardChangeSupportFactory supportFactory;
	private StandardObservableCollectionSupportFactory collectionSupportFactory;
	private ObservableList<Integer> observableList;

	@Before
	public void before()
	{
		supportFactory = new StandardChangeSupportFactory();
		collectionSupportFactory = new StandardObservableCollectionSupportFactory();
		observableList = ObservableCollections.newObservableArrayList();
		holder = new CollectionValueHolder<Integer>(supportFactory, collectionSupportFactory, observableList);
	}

	@Test
	public void test_firesRedirectedEventWhenOriginalCollectionModified(CollectionChangeListener<Integer> listener,
			ArgumentCaptor<CollectionChangeEvent<Integer>> eventCaptor)
	{
		holder.addCollectionChangeListener(listener);
		observableList.add(1);

		verify(listener).onCollectionChange(eventCaptor.capture());
		assertEquals(holder, eventCaptor.getValue().getSource());
		assertEquals(Lists.newArrayList(1), eventCaptor.getValue().getAdded());
	}

	@Test
	public void test_firesRedirectedEventWhenIteratorModified(CollectionChangeListener<Integer> listener,
			ArgumentCaptor<CollectionChangeEvent<Integer>> eventCaptor)
	{
		observableList.add(1);
		observableList.add(2);
		holder.addCollectionChangeListener(listener);

		final Iterator<Integer> iterator = holder.iterator();
		iterator.next();
		iterator.remove();

		verify(listener).onCollectionChange(eventCaptor.capture());
		assertEquals(holder, eventCaptor.getValue().getSource());
		assertEquals(Lists.newArrayList(1), eventCaptor.getValue().getRemoved());
	}

	@Test
	public void test_firesRedirectedEventWhenSubListModified(CollectionChangeListener<Integer> listener,
			ArgumentCaptor<CollectionChangeEvent<Integer>> eventCaptor)
	{
		observableList.add(1);
		holder.addCollectionChangeListener(listener);
		holder.subList(0, holder.size() - 1).add(2);

		verify(listener).onCollectionChange(eventCaptor.capture());
		assertEquals(holder, eventCaptor.getValue().getSource());
		assertEquals(Lists.newArrayList(2), eventCaptor.getValue().getAdded());
	}

	@Test
	public void test_setsAnEmptyObservableListWhenSettingNullValue()
	{
		holder.setValue(null);
		assertTrue(holder.isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_throwsWhenValueIsNotObservableList()
	{
		holder.setValue(1);
	}

	@Test
	public void dispose()
	{
		assertEquals(1, observableList.getCollectionChangeListeners().size());
		holder.dispose();
		assertEquals(0, observableList.getCollectionChangeListeners().size());
	}
}
