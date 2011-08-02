package com.netappsid.binding.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.binding.value.AbstractValueModel;
import com.netappsid.observable.CollectionChangeEvent;
import com.netappsid.observable.CollectionChangeListener;
import com.netappsid.observable.CollectionDifference;
import com.netappsid.observable.DefaultObservableCollectionSupport;
import com.netappsid.observable.ListDifference;
import com.netappsid.observable.ObservableCollection;
import com.netappsid.observable.ObservableList;
import com.netappsid.observable.SwingDefaultObservableCollectionSupport;

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
			List oldValue = (List) evt.getOldValue();
			List newValue = (List) evt.getNewValue();

			uninstall((T) oldValue);
			install((T) newValue);
			AbstractCollectionValueModel.this.fireValueChange(oldValue, newValue);

			ImmutableList<Object> oldDelta = (oldValue == null) ? ImmutableList.of() : ImmutableList.copyOf(oldValue);
			ImmutableList<Object> newDelta = (newValue == null) ? ImmutableList.of() : ImmutableList.copyOf(newValue);

			CollectionDifference difference = ListDifference.difference(oldDelta, newDelta);

			// We need to force a CollectionChange when the value changes to ensure
			// bound components refreshes their content because the source of there
			// binding didn't change.
			CollectionChangeEvent collectionChangeEvent = defaultObservableCollectionSupport.newCollectionChangeEvent(difference, -1);
			defaultObservableCollectionSupport.fireCollectionChangeEvent(collectionChangeEvent);
		}
	}

	private final ValueModel valueModel;
	private final ModelCollectionChangeHandler modelCollectionListener;
	private final DefaultObservableCollectionSupport defaultObservableCollectionSupport;

	public AbstractCollectionValueModel(ValueModel valueModel, ChangeSupportFactory changeSupportFactory)
	{
		super(changeSupportFactory);

		this.defaultObservableCollectionSupport = new SwingDefaultObservableCollectionSupport(this);
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
		return (T) valueModel.getValue();
	}

	@Override
	public void setValue(Object value)
	{
		valueModel.setValue(value);
	}
}
