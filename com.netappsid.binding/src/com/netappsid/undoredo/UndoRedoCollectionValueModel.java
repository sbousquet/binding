package com.netappsid.undoredo;

import java.util.Collection;
import java.util.Iterator;

import com.jgoodies.binding.beans.Observable;
import com.netappsid.binding.beans.CollectionValueModel;
import com.netappsid.observable.BatchAction;
import com.netappsid.observable.CollectionChangeEvent;
import com.netappsid.observable.CollectionChangeListener;
import com.netappsid.observable.CollectionDifference;
import com.netappsid.observable.DefaultObservableCollectionSupport;
import com.netappsid.observable.ObservableCollection;

public class UndoRedoCollectionValueModel<T extends CollectionValueModel & Observable> extends UndoRedoValueModel<T> implements CollectionValueModel

{
	private final class DelegateCollectionValueModelCollectionChangeListener implements CollectionChangeListener
	{
		@Override
		public void onCollectionChange(CollectionChangeEvent event)
		{
			getUndoRedoManager().push(new CollectionChangeOperation(UndoRedoCollectionValueModel.this, event));
			CollectionChangeEvent newCollectionChangeEvent = observableCollectionSupport.newCollectionChangeEvent(event.getDifference());
			observableCollectionSupport.fireCollectionChangeEvent(newCollectionChangeEvent);
		}
	}

	private final DefaultObservableCollectionSupport observableCollectionSupport;
	private final CollectionChangeListener listener;

	public UndoRedoCollectionValueModel(UndoRedoManager manager, T valueModel)
	{
		super(manager, valueModel);

		listener = new DelegateCollectionValueModelCollectionChangeListener();

		observableCollectionSupport = new DefaultObservableCollectionSupport(this);
		getValueModel().addCollectionChangeListener(listener);
	}

	protected CollectionChangeListener getUndoRedoManagerPushHandler()
	{
		return listener;
	}

	@Override
	public void addCollectionChangeListener(CollectionChangeListener listener)
	{
		observableCollectionSupport.addCollectionChangeListener(listener);
	}

	@Override
	public void removeCollectionChangeListener(CollectionChangeListener listener)
	{
		observableCollectionSupport.removeCollectionChangeListener(listener);
	}

	public void undo(CollectionChangeEvent event)
	{
		try
		{
			observableCollectionSupport.removeCollectionChangeListener(listener);

			CollectionDifference difference = event.getDifference();

			// Always use the ValueModel's value since when an entity is reloaded, a new collection
			// containing the same objects is recreated
			ObservableCollection source = (ObservableCollection) getValueModel().getValue();

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
			observableCollectionSupport.addCollectionChangeListener(listener);
		}
	}

	public void redo(CollectionChangeEvent event)
	{
		try
		{
			observableCollectionSupport.removeCollectionChangeListener(listener);

			CollectionDifference difference = event.getDifference();

			// Always use the ValueModel's value since when an entity is reloaded, a new collection
			// containing the same objects is recreated
			ObservableCollection source = (ObservableCollection) getValueModel().getValue();

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
			observableCollectionSupport.addCollectionChangeListener(listener);
		}
	}

	@Override
	public Object get(Object key)
	{
		return getValueModel().get(key);
	}

	@Override
	public void set(Object key, Object newValue)
	{
		getValueModel().set(key, newValue);
	}

	@Override
	public boolean add(Object e)
	{
		return getValueModel().add(e);
	}

	@Override
	public boolean addAll(Collection c)
	{
		return getValueModel().addAll(c);
	}

	@Override
	public void clear()
	{
		getValueModel().clear();
	}

	@Override
	public boolean contains(Object e)
	{
		return getValueModel().contains(e);
	}

	@Override
	public boolean containsAll(Collection c)
	{
		return getValueModel().containsAll(c);
	}

	@Override
	public boolean isEmpty()
	{
		return getValueModel().isEmpty();
	}

	@Override
	public Iterator iterator()
	{
		return getValueModel().iterator();
	}

	@Override
	public boolean remove(Object e)
	{
		return getValueModel().remove(e);
	}

	@Override
	public boolean removeAll(Collection c)
	{
		return getValueModel().removeAll(c);
	}

	@Override
	public boolean retainAll(Collection c)
	{
		return getValueModel().retainAll(c);
	}

	@Override
	public int size()
	{
		return getValueModel().size();
	}

	@Override
	public void executeBatchAction(BatchAction action)
	{
		getValueModel().executeBatchAction(action);
	}

	@Override
	public Object[] toArray()
	{
		return getValueModel().toArray();
	}

	@Override
	public Object[] toArray(Object[] a)
	{
		return getValueModel().toArray(a);
	}
}
