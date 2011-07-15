package com.netappsid.binding.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.binding.value.AbstractValueModel;
import com.netappsid.observable.CollectionChangeEvent;
import com.netappsid.observable.CollectionChangeListener;
import com.netappsid.observable.DefaultObservableCollectionSupport;
import com.netappsid.observable.ObservableCollection;

public abstract class AbstractCollectionValueModel<T extends ObservableCollection, K> extends AbstractValueModel implements CollectionValueModel<K>
{
	private final class ModelCollectionChangeHandler implements CollectionChangeListener
	{
		@Override
		public void onCollectionChange(CollectionChangeEvent event)
		{
			CollectionChangeEvent collectionChangeEvent = defaultObservableCollectionSupport.newCollectionChangeEvent(event.getDifference(), event.getIndex());
			defaultObservableCollectionSupport.fireCollectionChangeEvent(collectionChangeEvent);
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

	private final ValueModel valueModel;
	private final List<CollectionChangeListener> listeners;
	private final ModelCollectionChangeHandler modelCollectionListener;
	private final DefaultObservableCollectionSupport defaultObservableCollectionSupport;

	public AbstractCollectionValueModel(ValueModel valueModel, ChangeSupportFactory changeSupportFactory)
	{
		super(changeSupportFactory);

		this.defaultObservableCollectionSupport = new DefaultObservableCollectionSupport(this);
		this.valueModel = valueModel;
		this.modelCollectionListener = new ModelCollectionChangeHandler();
		this.listeners = new ArrayList<CollectionChangeListener>();

		install((ObservableCollection) valueModel.getValue());

		// Listens to the valueChanged to install/uninstall CollectionChange handler
		valueModel.addValueChangeListener(new ValueChangeHandler());
	}

	protected void install(ObservableCollection newValue)
	{
		if (newValue != null)
		{
			newValue.addCollectionChangeListener(modelCollectionListener);
		}
	}

	protected void uninstall(ObservableCollection oldValue)
	{
		if (oldValue != null)
		{
			oldValue.removeCollectionChangeListener(modelCollectionListener);
		}
	}

	@Override
	public void addCollectionChangeListener(CollectionChangeListener listener)
	{
		defaultObservableCollectionSupport.addCollectionChangeListener(listener);
	}

	@Override
	public void removeCollectionChangeListener(CollectionChangeListener listener)
	{
		defaultObservableCollectionSupport.removeCollectionChangeListener(listener);
	}

	@Override
	public void addValueChangeListener(PropertyChangeListener listener)
	{
		valueModel.addValueChangeListener(listener);
	}

	@Override
	public void removeValueChangeListener(PropertyChangeListener listener)
	{
		valueModel.removeValueChangeListener(listener);
	}

	@Override
	public Object getValue()
	{
		return valueModel.getValue();
	}

	@Override
	public void setValue(Object value)
	{
		valueModel.setValue(value);
	}
}
