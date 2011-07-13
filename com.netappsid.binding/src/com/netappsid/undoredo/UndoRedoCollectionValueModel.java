package com.netappsid.undoredo;

import com.jgoodies.binding.beans.Observable;
import com.netappsid.binding.beans.CollectionValueModel;
import com.netappsid.observable.CollectionChangeListener;

public class UndoRedoCollectionValueModel<T extends CollectionValueModel & Observable> extends UndoRedoValueModel<T> implements CollectionValueModel,
		Observable
{
	public UndoRedoCollectionValueModel(UndoRedoManager manager, T valueModel)
	{
		super(manager, valueModel);
	}

	@Override
	public void addCollectionChangeListener(CollectionChangeListener listener)
	{
		getValueModel().addCollectionChangeListener(listener);
	}

	@Override
	public void removeCollectionChangeListener(CollectionChangeListener listener)
	{
		getValueModel().removeCollectionChangeListener(listener);
	}
}
