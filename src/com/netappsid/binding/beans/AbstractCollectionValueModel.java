package com.netappsid.binding.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.binding.value.AbstractValueModel;
import com.netappsid.binding.value.BoundValueModel;
import com.netappsid.observable.CollectionChangeEvent;
import com.netappsid.observable.CollectionChangeListener;
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
			final Object oldValue = evt.getOldValue();
			final Object newValue = evt.getNewValue();

			uninstallCollectionChangeHandler((T) oldValue);
			installCollectionChangeHandler((T) newValue);
			fireValueChange(oldValue, newValue);
		}
	}

	private final ValueModel valueModel;
	private final ModelCollectionChangeHandler modelCollectionListener = new ModelCollectionChangeHandler();
	private final ObservableCollectionSupport observableCollectionSupport;
	private final ValueChangeHandler valueChangeHandler = new ValueChangeHandler();

	public AbstractCollectionValueModel(ValueModel valueModel, ChangeSupportFactory changeSupportFactory,
			ObservableCollectionSupportFactory observableCollectionSupportFactory)
	{
		super(changeSupportFactory);
		this.observableCollectionSupport = observableCollectionSupportFactory.newObservableCollectionSupport(this);
		this.valueModel = valueModel;
		installCollectionChangeHandler((ObservableCollection) valueModel.getValue());
		valueModel.addValueChangeListener(valueChangeHandler);
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

	protected void installCollectionChangeHandler(ObservableCollection observableCollection)
	{
		if (observableCollection != null)
		{
			observableCollection.addCollectionChangeListener(modelCollectionListener);
		}
	}

	protected void uninstallCollectionChangeHandler(ObservableCollection observableCollection)
	{
		if (observableCollection != null)
		{
			observableCollection.removeCollectionChangeListener(modelCollectionListener);
		}
	}

	@Override
	public void dispose()
	{
		uninstallCollectionChangeHandler(getValue());
		valueModel.removeValueChangeListener(valueChangeHandler);
	}

	@Override
	public String getPropertyName()
	{
		if (valueModel instanceof BoundValueModel)
		{
			return ((BoundValueModel) valueModel).getPropertyName();
		}

		return super.getPropertyName();
	}
}
