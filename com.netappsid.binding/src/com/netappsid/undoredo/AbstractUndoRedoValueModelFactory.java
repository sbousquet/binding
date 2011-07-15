package com.netappsid.undoredo;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.value.ValueModelFactory;

public abstract class AbstractUndoRedoValueModelFactory implements ValueModelFactory
{

	private final ValueModelFactory delegate;
	private final UndoRedoManager undoRedoManager;

	public AbstractUndoRedoValueModelFactory(UndoRedoManager undoRedoManager, ValueModelFactory delegate)
	{
		this.undoRedoManager = undoRedoManager;
		this.delegate = delegate;
	}

	@Override
	public ValueModel getValueModel(String propertyName)
	{
		ValueModel valueModel = getDelegate().getValueModel(propertyName);
		return wrap(valueModel);
	}

	public UndoRedoValueModel wrap(ValueModel valueModel)
	{
		return new UndoRedoValueModel(getUndoRedoManager(), valueModel);
	}

	public UndoRedoManager getUndoRedoManager()
	{
		return undoRedoManager;
	}

	public ValueModelFactory getDelegate()
	{
		return delegate;
	}
}
