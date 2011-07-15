package com.netappsid.undoredo;

import com.netappsid.binding.beans.CollectionValueModel;
import com.netappsid.observable.CollectionChangeEvent;
import com.netappsid.observable.CollectionChangeListener;
import com.netappsid.observable.CollectionDifference;
import com.netappsid.observable.ObservableCollection;

public class UndoRedoCollectionValueModel<T extends CollectionValueModel> extends UndoRedoValueModel<T> implements CollectionValueModel

{
	private final CollectionChangeListener listener;

	public UndoRedoCollectionValueModel(UndoRedoManager manager, T valueModel)
	{
		super(manager, valueModel);

		listener = new CollectionChangeListener()
			{
				@Override
				public void onCollectionChange(CollectionChangeEvent event)
				{
					getUndoRedoManager().push(new CollectionChangeOperation(UndoRedoCollectionValueModel.this, event));
				}
			};

		getValueModel().addCollectionChangeListener(listener);
	}

	protected CollectionChangeListener getUndoRedoManagerPushHandler()
	{
		return listener;
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

	public void undo(CollectionChangeEvent event)
	{
		getValueModel().removeCollectionChangeListener(listener);

		CollectionDifference difference = event.getDifference();

		// Always use the ValueModel's value since when an entity is reloaded, a new collection
		// containing the same objects is recreated
		ObservableCollection source = (ObservableCollection) getValueModel().getValue();

		try
		{
			for (Object added : difference.getAdded())
			{
				source.remove(added);
			}

			for (Object removed : difference.getRemoved())
			{
				source.add(removed);
			}
		}
		finally
		{
			getValueModel().addCollectionChangeListener(listener);
		}
	}

	public void redo(CollectionChangeEvent event)
	{
		getValueModel().removeCollectionChangeListener(listener);

		CollectionDifference difference = event.getDifference();

		// Always use the ValueModel's value since when an entity is reloaded, a new collection
		// containing the same objects is recreated
		ObservableCollection source = (ObservableCollection) getValueModel().getValue();

		try
		{
			for (Object added : difference.getAdded())
			{
				source.add(added);
			}

			for (Object removed : difference.getRemoved())
			{
				source.remove(removed);
			}
		}
		finally
		{
			getValueModel().addCollectionChangeListener(listener);
		}
	}
}
