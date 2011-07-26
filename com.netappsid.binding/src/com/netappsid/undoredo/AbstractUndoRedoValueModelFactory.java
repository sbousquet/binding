package com.netappsid.undoredo;

import java.util.HashMap;
import java.util.Map;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.value.ValueModelFactory;

public abstract class AbstractUndoRedoValueModelFactory<T extends ValueModel> implements ValueModelFactory<T>
{
	private final ValueModelFactory delegate;
	private final UndoRedoManager undoRedoManager;
	private final Map<String, T> valueModelCache = new HashMap<String, T>();

	public AbstractUndoRedoValueModelFactory(UndoRedoManager undoRedoManager, ValueModelFactory delegate)
	{
		this.undoRedoManager = undoRedoManager;
		this.delegate = delegate;
	}

	@Override
	public T getValueModel(String propertyName)
	{
		T returnedValueModel = valueModelCache.get(propertyName);

		if (returnedValueModel == null)
		{
			T valueModel = (T) getDelegate().getValueModel(propertyName);
			returnedValueModel = wrap(valueModel);
			valueModelCache.put(propertyName, returnedValueModel);
		}
		return returnedValueModel;
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
