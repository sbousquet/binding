package com.netappsid.undoredo;

import com.netappsid.undoredo.UndoRedoOperation;
import com.netappsid.undoredo.UndoRedoValueModel;

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

	/* (non-Javadoc)
	 * @see com.netappsid.gui.components.model.factory.UndoRedoAction#undo()
	 */
	@Override
	public void undo()
	{
		undoRedoValueModel.undo(undoRedoValue);
	}

	/* (non-Javadoc)
	 * @see com.netappsid.gui.components.model.factory.UndoRedoAction#redo()
	 */
	@Override
	public void redo()
	{
		undoRedoValueModel.redo(undoRedoValue);
	}

}
