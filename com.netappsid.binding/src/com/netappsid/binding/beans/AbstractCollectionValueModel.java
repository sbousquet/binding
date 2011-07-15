package com.netappsid.binding.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.binding.value.AbstractValueModel;
import com.netappsid.observable.CollectionChangeEvent;
import com.netappsid.observable.CollectionChangeListener;
import com.netappsid.observable.ObservableCollection;

public abstract class AbstractCollectionValueModel<T extends ObservableCollection, K> extends AbstractValueModel implements CollectionValueModel<K>
{
	private final class CollectionChangeHandler implements CollectionChangeListener
	{
		@Override
		public void onCollectionChange(CollectionChangeEvent event)
		{
			fireCollectionChanged(event);
		}
	}

	private final class ValueChangeHandler implements PropertyChangeListener
	{
		@Override
		public void propertyChange(PropertyChangeEvent evt)
		{
			uninstall((T) evt.getOldValue());
			install((T) evt.getNewValue());
		}
	}

	private final SimplePropertyAdapter propertyAdapter;
	private final List<CollectionChangeListener> listeners;
	private final CollectionChangeHandler listener;

	public AbstractCollectionValueModel(SimplePropertyAdapter propertyAdapter, ChangeSupportFactory changeSupportFactory)
	{
		super(changeSupportFactory);

		this.propertyAdapter = propertyAdapter;
		this.listener = new CollectionChangeHandler();
		this.listeners = new ArrayList<CollectionChangeListener>();

		install((ObservableCollection) propertyAdapter.getValue());

		// Listens to the valueChanged to install/uninstall CollectionChange handler
		propertyAdapter.addValueChangeListener(new ValueChangeHandler());
	}

	protected void install(ObservableCollection newValue)
	{
		if (newValue != null)
		{
			newValue.addCollectionChangeListener(listener);
		}
	}

	protected void uninstall(ObservableCollection oldValue)
	{
		if (oldValue != null)
		{
			oldValue.removeCollectionChangeListener(listener);
		}
	}

	protected void fireCollectionChanged(CollectionChangeEvent event)
	{
		for (CollectionChangeListener listener : listeners)
		{
			listener.onCollectionChange(event);
		}
	}

	/* (non-Javadoc)
	 * @see com.netappsid.binding.beans.CollectionValueModel#addCollectionChangeListener(com.netappsid.observable.CollectionChangeListener)
	 */
	@Override
	public void addCollectionChangeListener(CollectionChangeListener listener)
	{
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see com.netappsid.binding.beans.CollectionValueModel#removeCollectionChangeListener(com.netappsid.observable.CollectionChangeListener)
	 */
	@Override
	public void removeCollectionChangeListener(CollectionChangeListener listener)
	{
		listeners.remove(listener);
	}

	@Override
	public void addValueChangeListener(PropertyChangeListener listener)
	{
		propertyAdapter.addValueChangeListener(listener);
	}

	@Override
	public void removeValueChangeListener(PropertyChangeListener listener)
	{
		propertyAdapter.removeValueChangeListener(listener);
	}

	@Override
	public Object getValue()
	{
		return propertyAdapter.getValue();
	}

	@Override
	public void setValue(Object value)
	{
		propertyAdapter.setValue(value);
	}
}
