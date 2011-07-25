package com.netappsid.binding.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.binding.value.AbstractValueModel;
import com.netappsid.observable.CollectionChangeEvent;
import com.netappsid.observable.CollectionChangeListener;
import com.netappsid.observable.DefaultObservableCollectionSupport;
import com.netappsid.observable.ObservableCollection;
import com.netappsid.observable.ObservableCollections;
import com.netappsid.observable.ObservableList;

public abstract class AbstractCollectionValueModel<E, T extends ObservableList<E>> extends AbstractValueModel implements CollectionValueModel<E>
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
			AbstractCollectionValueModel.this.fireValueChange(evt.getOldValue(), evt.getNewValue());
		}
	}

	private final ValueModel valueModel;
	private final ModelCollectionChangeHandler modelCollectionListener;
	private final DefaultObservableCollectionSupport defaultObservableCollectionSupport;

	public AbstractCollectionValueModel(ValueModel valueModel, ChangeSupportFactory changeSupportFactory)
	{
		super(changeSupportFactory);

		this.defaultObservableCollectionSupport = new DefaultObservableCollectionSupport(this);
		this.valueModel = valueModel;
		this.modelCollectionListener = new ModelCollectionChangeHandler();

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
	public void addCollectionChangeListener(CollectionChangeListener<E> listener)
	{
		defaultObservableCollectionSupport.addCollectionChangeListener(listener);
	}

	@Override
	public void removeCollectionChangeListener(CollectionChangeListener<E> listener)
	{
		defaultObservableCollectionSupport.removeCollectionChangeListener(listener);
	}

	@Override
	public T getValue()
	{
		Object value = valueModel.getValue();
		return (T) ((value == null) ? ObservableCollections.newObservableArrayList() : value);
	}

	@Override
	public void setValue(Object value)
	{
		valueModel.setValue(value);
	}
}
