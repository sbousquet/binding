package com.netappsid.undoredo;

import com.netappsid.observable.CollectionChangeEvent;

public class CollectionChangeOperation implements UndoRedoOperation
{
	private final CollectionChangeEvent event;
	private final UndoRedoCollectionValueModel undoRedoCollectionValueModel;

	public CollectionChangeOperation(UndoRedoCollectionValueModel undoRedoCollectionValueModel, CollectionChangeEvent event)
	{
		this.undoRedoCollectionValueModel = undoRedoCollectionValueModel;
		this.event = event;
	}

	@Override
	public void undo()
	{
		this.undoRedoCollectionValueModel.undo(event);
	}

	@Override
	public void redo()
	{
		this.undoRedoCollectionValueModel.redo(event);
	}
}
