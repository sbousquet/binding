package com.netappsid.undoredo;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.netappsid.binding.beans.CollectionValueModel;
import com.netappsid.observable.CollectionChangeListener;

public class UndoRedoCollectionValueModelTest
{
	private UndoRedoManager undoRedoManager;
	private CollectionValueModel collectionValueModel;
	private CollectionChangeListener listener;
	private UndoRedoCollectionValueModel valueModel;

	@Before
	public void setUp()
	{
		undoRedoManager = mock(UndoRedoManager.class);
		collectionValueModel = mock(CollectionValueModel.class);
		listener = mock(CollectionChangeListener.class);

		valueModel = new UndoRedoCollectionValueModel(undoRedoManager, collectionValueModel);
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
}
