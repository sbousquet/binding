package com.netappsid.undoredo;

import java.util.HashMap;
import java.util.Map;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.CollectionValueModel;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.binding.value.CollectionValueModelFactory;
import com.netappsid.binding.value.ValueModelFactory;
import com.netappsid.observable.ObservableCollectionSupportFactory;

public class UndoRedoValueModelFactory extends AbstractUndoRedoValueModelFactory implements CollectionValueModelFactory
{
	private final Map<String, CollectionValueModel> collectionValueModelCache = new HashMap<String, CollectionValueModel>();
	private final ObservableCollectionSupportFactory observableCollectionSupportFactory;
	private final ChangeSupportFactory changeSupportFactory;

	public UndoRedoValueModelFactory(UndoRedoManager undoRedoManager, ValueModelFactory delegate,
			ObservableCollectionSupportFactory observableCollectionSupportFactory, ChangeSupportFactory changeSupportFactory)
	{
		super(undoRedoManager, delegate);
		this.observableCollectionSupportFactory = observableCollectionSupportFactory;
		this.changeSupportFactory = changeSupportFactory;
	}

	@Override
	protected UndoRedoValueModel wrap(ValueModel valueModel)
	{
		return new UndoRedoValueModel(getUndoRedoManager(), valueModel, changeSupportFactory);
	}

	@Override
	public CollectionValueModel getCollectionValueModel(String propertyName)
	{
		CollectionValueModel returnedCollectionValueModel = collectionValueModelCache.get(propertyName);

		if (returnedCollectionValueModel == null)
		{
			CollectionValueModel newCollectionValueModel = ((CollectionValueModelFactory) getDelegate()).getCollectionValueModel(propertyName);

			returnedCollectionValueModel = new UndoRedoCollectionValueModel(getUndoRedoManager(), newCollectionValueModel, observableCollectionSupportFactory,
					changeSupportFactory);

			collectionValueModelCache.put(propertyName, returnedCollectionValueModel);
		}

		return returnedCollectionValueModel;
	}
}
