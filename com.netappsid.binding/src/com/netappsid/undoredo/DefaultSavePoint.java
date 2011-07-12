package com.netappsid.undoredo;



public class DefaultSavePoint implements SavePoint
{
	private final UndoRedoOperation action;

	public DefaultSavePoint(UndoRedoOperation action)
	{
		this.action = action;
	}

	@Override
	public UndoRedoOperation getOrigin()
	{
		return action;
	}
}
