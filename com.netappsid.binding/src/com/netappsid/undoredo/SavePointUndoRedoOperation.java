package com.netappsid.undoredo;

import java.util.Iterator;
import java.util.LinkedList;

import com.netappsid.undoredo.UndoRedoOperation;

public class SavePointUndoRedoOperation implements UndoRedoOperation
{
	private final LinkedList<UndoRedoOperation> undoSavePointActions;

	public SavePointUndoRedoOperation(LinkedList<UndoRedoOperation> undoSavePointActions)
	{
		this.undoSavePointActions = undoSavePointActions;
	}

	@Override
	public void undo()
	{
		Iterator<UndoRedoOperation> iterator = undoSavePointActions.descendingIterator();
		while (iterator.hasNext())
		{
			UndoRedoOperation undoRedoAction = iterator.next();
			undoRedoAction.undo();
		}
	}

	@Override
	public void redo()
	{
		Iterator<UndoRedoOperation> iterator = undoSavePointActions.iterator();
		while (iterator.hasNext())
		{
			UndoRedoOperation undoRedoAction = iterator.next();
			undoRedoAction.redo();
		}
	}

}
