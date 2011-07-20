package com.netappsid.undoredo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jgoodies.binding.beans.Observable;
import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.support.SwingIdentityPropertyChangeSupport;
import com.netappsid.observable.ObservableByName;

public class UndoRedoValueModel<T extends ValueModel & Observable> implements ValueModel, ObservableByName
{
	private final class DelegateValueModelValueChangeListener implements PropertyChangeListener
	{
		@Override
		public void propertyChange(PropertyChangeEvent event)
		{
			propertyChangeSupport.firePropertyChange(new PropertyChangeEvent(UndoRedoValueModel.this, event.getPropertyName(), event.getOldValue(), event
					.getNewValue()));
		}
	}

	private final T valueModel;
	private final UndoRedoManager undoRedoManager;
	private final SwingIdentityPropertyChangeSupport propertyChangeSupport;

	public UndoRedoValueModel(UndoRedoManager manager, T valueModel)
	{
		this.undoRedoManager = manager;
		this.valueModel = valueModel;
		this.propertyChangeSupport = new SwingIdentityPropertyChangeSupport(this);
		valueModel.addValueChangeListener(new DelegateValueModelValueChangeListener());
	}

	@Override
	public void addValueChangeListener(PropertyChangeListener listener)
	{
		addPropertyChangeListener("value", listener);
	}

	@Override
	public Object getValue()
	{
		return getValueModel().getValue();
	}

	@Override
	public void removeValueChangeListener(PropertyChangeListener listener)
	{
		removePropertyChangeListener("value", listener);
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

	protected UndoRedoManager getUndoRedoManager()
	{
		return undoRedoManager;
	}

	protected T getValueModel()
	{
		return valueModel;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	@Override
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
	}
}
