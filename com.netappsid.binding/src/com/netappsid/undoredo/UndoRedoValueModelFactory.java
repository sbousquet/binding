package com.netappsid.undoredo;

import com.netappsid.binding.beans.CollectionValueModel;
import com.netappsid.binding.value.CollectionValueModelFactory;
import com.netappsid.binding.value.ValueModelFactory;

public class UndoRedoValueModelFactory extends AbstractUndoRedoValueModelFactory implements CollectionValueModelFactory
{
	public UndoRedoValueModelFactory(UndoRedoManager undoRedoManager, ValueModelFactory delegate)
	{
		super(undoRedoManager, delegate);
	}

	@Override
	public CollectionValueModel getCollectionValueModel(String propertyName)
	{
		CollectionValueModel collectionValueModel = ((CollectionValueModelFactory) getDelegate()).getCollectionValueModel(propertyName);
		return new UndoRedoCollectionValueModel(getUndoRedoManager(), collectionValueModel);
	}
}
