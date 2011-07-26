package com.netappsid.undoredo;

import java.util.HashMap;
import java.util.Map;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.CollectionValueModel;
import com.netappsid.binding.value.CollectionValueModelFactory;
import com.netappsid.binding.value.ValueModelFactory;

public class UndoRedoValueModelFactory extends AbstractUndoRedoValueModelFactory implements CollectionValueModelFactory
{
	private final Map<String, CollectionValueModel> collectionValueModelCache = new HashMap<String, CollectionValueModel>();

	public UndoRedoValueModelFactory(UndoRedoManager undoRedoManager, ValueModelFactory delegate)
	{
		super(undoRedoManager, delegate);
	}

	@Override
	protected UndoRedoValueModel wrap(ValueModel valueModel)
	{
		return new UndoRedoValueModel(getUndoRedoManager(), valueModel);
	}

	@Override
	public CollectionValueModel getCollectionValueModel(String propertyName)
	{
		CollectionValueModel returnedCollectionValueModel = collectionValueModelCache.get(propertyName);

		if (returnedCollectionValueModel == null)
		{
			CollectionValueModel newCollectionValueModel = ((CollectionValueModelFactory) getDelegate()).getCollectionValueModel(propertyName);
			returnedCollectionValueModel = new UndoRedoCollectionValueModel(getUndoRedoManager(), newCollectionValueModel);
			
			collectionValueModelCache.put(propertyName, returnedCollectionValueModel);
		}

		return returnedCollectionValueModel;
	}
}
