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
import com.netappsid.observable.ListDifference;
import com.netappsid.observable.ObservableCollection;
import com.netappsid.observable.ObservableCollectionSupport;
import com.netappsid.observable.ObservableCollectionSupportFactory;
import com.netappsid.observable.ObservableList;

public abstract class AbstractCollectionValueModel<E, T extends ObservableList<E>> extends AbstractValueModel implements CollectionValueModel<E>
{
	public final class ModelCollectionChangeHandler implements CollectionChangeListener
	{
		@Override
		public void onCollectionChange(CollectionChangeEvent event)
		{
			CollectionChangeEvent collectionChangeEvent = getSupport().newCollectionChangeEvent(event.getDifference(), event.getIndex());
			getSupport().fireCollectionChangeEvent(collectionChangeEvent);
		}
	}

	public final class ValueChangeHandler implements PropertyChangeListener
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
			CollectionChangeEvent collectionChangeEvent = getSupport().newCollectionChangeEvent(difference, -1);
			getSupport().fireCollectionChangeEvent(collectionChangeEvent);
		}
	}

	private final ValueModel valueModel;
	private final ModelCollectionChangeHandler modelCollectionListener = new ModelCollectionChangeHandler();
	private final ObservableCollectionSupport observableCollectionSupport;

	public AbstractCollectionValueModel(ValueModel valueModel, ChangeSupportFactory changeSupportFactory,
			ObservableCollectionSupportFactory observableCollectionSupportFactory)
	{
		super(changeSupportFactory);

		this.observableCollectionSupport = observableCollectionSupportFactory.newObservableCollectionSupport(this);
		this.valueModel = valueModel;

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
		getSupport().addCollectionChangeListener(listener);
	}

	@Override
	public void removeCollectionChangeListener(CollectionChangeListener<E> listener)
	{
		getSupport().removeCollectionChangeListener(listener);
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

	protected ObservableCollectionSupport getSupport()
	{
		return observableCollectionSupport;
	}
}
