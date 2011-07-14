package com.netappsid.undoredo;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.google.common.collect.ImmutableList;
import com.netappsid.binding.beans.CollectionValueModel;
import com.netappsid.observable.CollectionChangeEvent;
import com.netappsid.observable.CollectionChangeListener;
import com.netappsid.observable.ListDifference;
import com.netappsid.observable.ObservableCollections;
import com.netappsid.observable.ObservableList;

public class UndoRedoCollectionValueModelTest
{
	private UndoRedoManager undoRedoManager;
	private CollectionValueModel collectionValueModel;
	private CollectionChangeListener listener;
	private UndoRedoCollectionValueModel valueModel;
	private CollectionChangeListener undoRedoManagerPushHandler;
	private Object oldObject;
	private Object newObject;
	private ListDifference difference;


	@Before
	public void setUp()
	{
		undoRedoManager = mock(UndoRedoManager.class);
		collectionValueModel = mock(CollectionValueModel.class);
		listener = mock(CollectionChangeListener.class);

		valueModel = new UndoRedoCollectionValueModel(undoRedoManager, collectionValueModel);

		oldObject = new Object();
		newObject = new Object();

		difference = new ListDifference(ImmutableList.of(oldObject), ImmutableList.of(newObject));
	}

	@Test
	public void testAddCollectionChangeListener()
	{
		valueModel.addCollectionChangeListener(listener);

		verify(collectionValueModel).addCollectionChangeListener(listener);
	}

	@Test
	public void testRemoveCollectionChangeListener()
	{
		valueModel.removeCollectionChangeListener(listener);

		verify(collectionValueModel).removeCollectionChangeListener(listener);
	}

	@Test
	public void testUndo()
	{
		ObservableList<Object> newObservableArrayList = ObservableCollections.newObservableArrayList(newObject);
		when(collectionValueModel.getValue()).thenReturn(newObservableArrayList);
		valueModel.undo(new CollectionChangeEvent(newObservableArrayList, difference));

		InOrder inOrder = inOrder(collectionValueModel, undoRedoManager, collectionValueModel);
		inOrder.verify(collectionValueModel).removeCollectionChangeListener(valueModel.getUndoRedoManagerPushHandler());
		inOrder.verify(undoRedoManager, never()).push(any(CollectionChangeOperation.class));
		inOrder.verify(collectionValueModel).addCollectionChangeListener(valueModel.getUndoRedoManagerPushHandler());
		
		assertTrue(newObservableArrayList.contains(oldObject));
		assertFalse(newObservableArrayList.contains(newObject));
	}

	@Test
	public void testRedo()
	{
		ObservableList<Object> newObservableArrayList = ObservableCollections.newObservableArrayList(oldObject);
		when(collectionValueModel.getValue()).thenReturn(newObservableArrayList);
		valueModel.redo(new CollectionChangeEvent(newObservableArrayList, difference));

		InOrder inOrder = inOrder(collectionValueModel, undoRedoManager, collectionValueModel);
		inOrder.verify(collectionValueModel).removeCollectionChangeListener(valueModel.getUndoRedoManagerPushHandler());
		inOrder.verify(undoRedoManager, never()).push(any(CollectionChangeOperation.class));
		inOrder.verify(collectionValueModel).addCollectionChangeListener(valueModel.getUndoRedoManagerPushHandler());

		assertFalse(newObservableArrayList.contains(oldObject));
		assertTrue(newObservableArrayList.contains(newObject));
	}
}
