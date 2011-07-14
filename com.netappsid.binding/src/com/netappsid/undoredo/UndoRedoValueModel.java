package com.netappsid.undoredo;

import java.beans.PropertyChangeListener;

import com.jgoodies.binding.beans.Observable;
import com.jgoodies.binding.value.ValueModel;

public class UndoRedoValueModel<T extends ValueModel & Observable> implements ValueModel, Observable
{
	private final T valueModel;
	private final UndoRedoManager undoRedoManager;

	public UndoRedoValueModel(UndoRedoManager manager, T valueModel)
	{
		this.undoRedoManager = manager;
		this.valueModel = valueModel;
	}

	@Override
	public void addValueChangeListener(PropertyChangeListener changeListener)
	{
		getValueModel().addValueChangeListener(changeListener);
	}

	@Override
	public Object getValue()
	{
		return getValueModel().getValue();
	}

	@Override
	public void removeValueChangeListener(PropertyChangeListener changeListener)
	{
		getValueModel().removeValueChangeListener(changeListener);
	}

	@Override
	public void setValue(Object value)
	{
		UndoRedoValue undoRedoValue = new UndoRedoValue(getValueModel().getValue(), value);
		UndoRedoValueModelOperation undoRedoValueModelOperation = new UndoRedoValueModelOperation(this, undoRedoValue);
		getUndoRedoManager().push(undoRedoValueModelOperation);
		getUndoRedoManager().beginTransaction();
		getValueModel().setValue(value);
		getUndoRedoManager().endTransaction();
	}

	public void redo(UndoRedoValue undoRedoValue)
	{
		getValueModel().setValue(undoRedoValue.getNewValue());
	}

	public void undo(UndoRedoValue undoRedoValue)
	{
		getValueModel().setValue(undoRedoValue.getOldValue());
	}

	public ValueModel getDelegate()
	{
		return getValueModel();
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener arg0)
	{
		getValueModel().addPropertyChangeListener(arg0);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener arg0)
	{
		getValueModel().removePropertyChangeListener(arg0);
	}

	protected UndoRedoManager getUndoRedoManager()
	{
		return undoRedoManager;
	}

	protected T getValueModel()
	{
		return valueModel;
	}
}
