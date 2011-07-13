package com.netappsid.undoredo;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.CollectionValueModel;
import com.netappsid.binding.value.ValueModelFactory;

public class UndoRedoValueModelFactory implements ValueModelFactory
{

	private final ValueModelFactory delegate;
	private final UndoRedoManager undoRedoManager;

	public UndoRedoValueModelFactory(UndoRedoManager undoRedoManager, ValueModelFactory delegate)
	{
		this.undoRedoManager = undoRedoManager;
		this.delegate = delegate;
	}
	
	@Override
	public boolean isCachingRequired()
	{
		return delegate.isCachingRequired();
	}

	@Override
	public ValueModel getValueModel(String propertyName)
	{
		ValueModel valueModel = delegate.getValueModel(propertyName);
		return wrap(valueModel);
	}

	public UndoRedoValueModel wrap(ValueModel valueModel)
	{
		return new UndoRedoValueModel(undoRedoManager, valueModel);
	}

	@Override
	public CollectionValueModel getCollectionValueModel(String propertyName)
	{
		CollectionValueModel collectionValueModel = delegate.getCollectionValueModel(propertyName);
		return new UndoRedoCollectionValueModel(undoRedoManager, collectionValueModel);
	}
}
