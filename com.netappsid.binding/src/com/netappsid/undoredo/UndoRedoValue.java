package com.netappsid.undoredo;

public class UndoRedoValue
{

	private final Object oldValue;
	private final Object newValue;

	public UndoRedoValue(Object oldValue, Object newValue)
	{
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public Object getNewValue()
	{
		return newValue;
	}
	
	public Object getOldValue()
	{
		return oldValue;
	}
	
}
