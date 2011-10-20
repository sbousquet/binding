package com.netappsid.undoredo;


public class UndoRedoValueModelOperation implements UndoRedoOperation
{
	private final UndoRedoValueModel undoRedoValueModel;
	private final UndoRedoValue undoRedoValue;

	public UndoRedoValueModelOperation(UndoRedoValueModel undoRedoValueModel, UndoRedoValue undoRedoValue)
	{
		this.undoRedoValueModel = undoRedoValueModel;
		this.undoRedoValue = undoRedoValue;
	}
	
	public UndoRedoValue getUndoRedoValue()
	{
		return undoRedoValue;
	}
	
	public UndoRedoValueModel getUndoRedoValueModel()
	{
		return undoRedoValueModel;
	}

	@Override
	public void undo()
	{
		undoRedoValueModel.undo(undoRedoValue);
	}

	@Override
	public void redo()
	{
		undoRedoValueModel.redo(undoRedoValue);
	}
}
