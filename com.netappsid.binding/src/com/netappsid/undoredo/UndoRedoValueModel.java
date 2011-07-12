package com.netappsid.undoredo;

import java.beans.PropertyChangeListener;

import com.jgoodies.binding.beans.Observable;
import com.jgoodies.binding.value.ValueModel;

public class UndoRedoValueModel<T extends ValueModel & Observable> implements ValueModel, Observable
{
	private final T valueModel;
	private final UndoRedoManager manager;

	public UndoRedoValueModel(UndoRedoManager manager, T valueModel)
	{
		this.manager = manager;
		this.valueModel = valueModel;
	}

	@Override
	public void addValueChangeListener(PropertyChangeListener changeListener)
	{
		valueModel.addValueChangeListener(changeListener);
	}

	@Override
	public Object getValue()
	{
		return valueModel.getValue();
	}

	@Override
	public void removeValueChangeListener(PropertyChangeListener changeListener)
	{
		valueModel.removeValueChangeListener(changeListener);
	}

	@Override
	public void setValue(Object value)
	{
		UndoRedoValue undoRedoAction = new UndoRedoValue(valueModel.getValue(), value);
		manager.push(this, undoRedoAction);
		manager.beginTransaction();
		valueModel.setValue(value);
		manager.endTransaction();
	}

	public void redo(UndoRedoValue undoRedoValue)
	{
		valueModel.setValue(undoRedoValue.getNewValue());
	}

	public void undo(UndoRedoValue undoRedoValue)
	{
		valueModel.setValue(undoRedoValue.getOldValue());
	}

	public ValueModel getDelegate()
	{
		return valueModel;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener arg0)
	{
		valueModel.addPropertyChangeListener(arg0);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener arg0)
	{
		valueModel.removePropertyChangeListener(arg0);
	}
}
