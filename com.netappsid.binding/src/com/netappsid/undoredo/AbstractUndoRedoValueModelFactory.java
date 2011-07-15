package com.netappsid.undoredo;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.value.ValueModelFactory;

public abstract class AbstractUndoRedoValueModelFactory<T extends ValueModel> implements ValueModelFactory<T>
{
	private final ValueModelFactory delegate;
	private final UndoRedoManager undoRedoManager;

	public AbstractUndoRedoValueModelFactory(UndoRedoManager undoRedoManager, ValueModelFactory delegate)
	{
		this.undoRedoManager = undoRedoManager;
		this.delegate = delegate;
	}

	@Override
	public T getValueModel(String propertyName)
	{
		T valueModel = (T) getDelegate().getValueModel(propertyName);
		return wrap(valueModel);
	}

	protected abstract T wrap(T valueModel);

	public UndoRedoManager getUndoRedoManager()
	{
		return undoRedoManager;
	}

	public ValueModelFactory getDelegate()
	{
		return delegate;
	}
}
