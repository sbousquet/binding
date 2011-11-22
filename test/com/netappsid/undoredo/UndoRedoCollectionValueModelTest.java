package com.netappsid.undoredo;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.google.common.collect.ImmutableList;
import com.jgoodies.binding.beans.Observable;
import com.netappsid.binding.beans.CollectionValueModel;
import com.netappsid.binding.beans.support.StandardChangeSupportFactory;
import com.netappsid.observable.ClearAndAddAllBatchAction;
import com.netappsid.observable.CollectionChangeEvent;
import com.netappsid.observable.CollectionChangeListener;
import com.netappsid.observable.CollectionDifference;
import com.netappsid.observable.ListDifference;
import com.netappsid.observable.ObservableCollections;
import com.netappsid.observable.ObservableList;
import com.netappsid.observable.StandardObservableCollectionSupportFactory;

public class UndoRedoCollectionValueModelTest
{
	private UndoRedoManager undoRedoManager;
	private CollectionValueModel collectionValueModel;
	private CollectionChangeListener listener;
	private UndoRedoCollectionValueModel undoRedoCollectionValueModel;
	private CollectionChangeListener undoRedoManagerPushHandler;
	private Object oldObject;
	private Object newObject;
	private ListDifference difference;
	private Object firstObject;

	@Before
	public void setUp()
	{
		undoRedoManager = mock(UndoRedoManager.class);

		firstObject = new Object();
		ObservableList<Object> newObservableArrayList = ObservableCollections.newObservableArrayList(firstObject);

		collectionValueModel = mock(CollectionValueModel.class);
		when(collectionValueModel.getValue()).thenReturn(newObservableArrayList);
		listener = mock(CollectionChangeListener.class);

		final StandardObservableCollectionSupportFactory collectionSupportFactory = new StandardObservableCollectionSupportFactory();
		final StandardChangeSupportFactory changeSupportFactory = new StandardChangeSupportFactory();
		undoRedoCollectionValueModel = new UndoRedoCollectionValueModel(undoRedoManager, collectionValueModel, collectionSupportFactory, changeSupportFactory);

		oldObject = new Object();
		newObject = new Object();

		difference = new ListDifference(ImmutableList.of(oldObject), ImmutableList.of(newObject));
	}

	@Test
	public void testEnsureImplementsObservable()
	{
		assertTrue("Must implement Observable in order to be able to bind to it via our jgoodies", undoRedoCollectionValueModel instanceof Observable);
	}

	@Test
	public void testAddCollectionChangeListener_EnsureNotRegisteredToDelegate()
	{
		undoRedoCollectionValueModel.addCollectionChangeListener(listener);

		verify(collectionValueModel, never()).addCollectionChangeListener(listener);
	}

	@Test
	public void testRemoveCollectionChangeListener_EnsureNotRegisteredToDelegate()
	{
		undoRedoCollectionValueModel.removeCollectionChangeListener(listener);

		verify(collectionValueModel, never()).removeCollectionChangeListener(listener);
	}

	@Test
	public void testUndo_EnsureOperationNotTrackedByUndoRedoManager()
	{
		// Reset since CollectionChange is registered in constructor once and we only want ton ensure no more
		// listeners are added at this point
		reset(collectionValueModel);

		ObservableList<Object> newObservableArrayList = ObservableCollections.newObservableArrayList(newObject);
		when(collectionValueModel.getValue()).thenReturn(newObservableArrayList);
		undoRedoCollectionValueModel.undo(new CollectionChangeEvent(newObservableArrayList, difference));

		InOrder inOrder = inOrder(collectionValueModel, undoRedoManager, collectionValueModel);
		inOrder.verify(collectionValueModel).removeCollectionChangeListener(undoRedoCollectionValueModel.getUndoRedoManagerPushHandler());
		inOrder.verify(undoRedoManager, never()).push(any(CollectionChangeOperation.class));
		inOrder.verify(collectionValueModel).addCollectionChangeListener(undoRedoCollectionValueModel.getUndoRedoManagerPushHandler());

		verify(collectionValueModel).unapply(any(CollectionDifference.class));
	}

	@Test
	public void testRedo_EnsureOperationNotTrackedByUndoRedoManager()
	{
		// Reset since CollectionChange is registered in constructor once and we only want ton ensure no more
		// listeners are added at this point
		reset(collectionValueModel);

		ObservableList<Object> newObservableArrayList = ObservableCollections.newObservableArrayList(oldObject);
		when(collectionValueModel.getValue()).thenReturn(newObservableArrayList);
		undoRedoCollectionValueModel.redo(new CollectionChangeEvent(newObservableArrayList, difference));

		InOrder inOrder = inOrder(collectionValueModel, undoRedoManager, collectionValueModel);
		inOrder.verify(collectionValueModel).removeCollectionChangeListener(undoRedoCollectionValueModel.getUndoRedoManagerPushHandler());
		inOrder.verify(undoRedoManager, never()).push(any(CollectionChangeOperation.class));
		inOrder.verify(collectionValueModel).addCollectionChangeListener(undoRedoCollectionValueModel.getUndoRedoManagerPushHandler());

		verify(collectionValueModel).apply(any(CollectionDifference.class));
	}

	@Test
	public void testSize()
	{
		undoRedoCollectionValueModel.size();
		verify(collectionValueModel).size();
	}

	@Test
	public void testAdd()
	{
		Object added = new Object();
		undoRedoCollectionValueModel.add(added);
		verify(collectionValueModel).add(added);
	}

	@Test
	public void testAddAll()
	{
		List added = Arrays.asList(new Object(), new Object());
		undoRedoCollectionValueModel.addAll(added);
		verify(collectionValueModel).addAll(added);
	}

	@Test
	public void testClear()
	{
		undoRedoCollectionValueModel.clear();
		verify(collectionValueModel).clear();
	}

	@Test
	public void testContains()
	{
		undoRedoCollectionValueModel.contains(firstObject);
		verify(collectionValueModel).contains(firstObject);
	}

	@Test
	public void testContainsAll()
	{
		List added = Arrays.asList(new Object(), new Object());
		undoRedoCollectionValueModel.containsAll(added);
		verify(collectionValueModel).containsAll(added);
	}

	@Test
	public void testIsEmpty()
	{
		undoRedoCollectionValueModel.isEmpty();
		verify(collectionValueModel).isEmpty();
	}

	@Test
	public void testIterator()
	{
		undoRedoCollectionValueModel.iterator();
		verify(collectionValueModel).iterator();
	}

	@Test
	public void testRemove()
	{
		undoRedoCollectionValueModel.remove(firstObject);
		verify(collectionValueModel).remove(firstObject);
	}

	@Test
	public void testRemoveAll()
	{
		List removed = Arrays.asList(new Object(), new Object());
		undoRedoCollectionValueModel.removeAll(removed);
		verify(collectionValueModel).removeAll(removed);
	}

	@Test
	public void testRetainAll()
	{
		List retained = Arrays.asList(new Object(), new Object());
		undoRedoCollectionValueModel.retainAll(retained);
		verify(collectionValueModel).retainAll(retained);
	}

	@Test
	public void testExecuteBatchAction()
	{
		ClearAndAddAllBatchAction action = new ClearAndAddAllBatchAction(Arrays.asList(new Object()));
		undoRedoCollectionValueModel.executeBatchAction(action);
		verify(collectionValueModel).executeBatchAction(action);
	}

	@Test
	public void testToArray()
	{
		undoRedoCollectionValueModel.toArray();
		verify(collectionValueModel).toArray();
	}

	@Test
	public void testToArray_Overload()
	{
		Object[] targetArrayType = new Object[] {};
		undoRedoCollectionValueModel.toArray(targetArrayType);
		verify(collectionValueModel).toArray(targetArrayType);
	}

	@Test
	public void testGetCollectionChangeListeners_EnsureNotNull()
	{
		assertTrue(undoRedoCollectionValueModel.getCollectionChangeListeners().isEmpty());
	}
}
