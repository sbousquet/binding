package com.netappsid.undoredo;

import static org.mockito.Mockito.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class SavePointUndoRedoActionTest
{
	private UndoRedoOperation firstUndoRedoAction;
	private UndoRedoOperation lastUndoRedoAction;
	private SavePointUndoRedoOperation savePointAction;

	@Before
	public void setUp()
	{
		firstUndoRedoAction = mock(UndoRedoOperation.class);
		lastUndoRedoAction = mock(UndoRedoOperation.class);
		
		LinkedList<UndoRedoOperation> undoSavePointActions = new LinkedList<UndoRedoOperation>();
		undoSavePointActions.addFirst(firstUndoRedoAction);
		undoSavePointActions.addLast(lastUndoRedoAction);
		
		savePointAction = new SavePointUndoRedoOperation(undoSavePointActions);
	}

	@Test
	public void testUndo()
	{
		savePointAction.undo();
		InOrder inOrder = inOrder(lastUndoRedoAction, firstUndoRedoAction);
		inOrder.verify(lastUndoRedoAction).undo();
		inOrder.verify(firstUndoRedoAction).undo();
	}

	@Test
	public void testRedo()
	{
		savePointAction.redo();
		InOrder inOrder = inOrder(firstUndoRedoAction, lastUndoRedoAction);
		inOrder.verify(firstUndoRedoAction).redo();
		inOrder.verify(lastUndoRedoAction).redo();
	}
}
