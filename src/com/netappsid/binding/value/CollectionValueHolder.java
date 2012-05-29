package com.netappsid.binding.value;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.netappsid.binding.beans.CollectionValueModel;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.observable.BatchAction;
import com.netappsid.observable.CollectionChangeEvent;
import com.netappsid.observable.CollectionChangeListener;
import com.netappsid.observable.CollectionDifference;
import com.netappsid.observable.ObservableCollectionSupport;
import com.netappsid.observable.ObservableCollectionSupportFactory;
import com.netappsid.observable.ObservableCollections;
import com.netappsid.observable.ObservableList;

/**
 * An implementation of CollectionValueModel that delegates to an ObservableList.
 */
public class CollectionValueHolder<E> extends ValueHolder implements CollectionValueModel<E>
{
	private final CollectionChangeHandler collectionChangeHandler = new CollectionChangeHandler();
	private final ObservableCollectionSupport<E> collectionChangeSupport;

	public CollectionValueHolder(ChangeSupportFactory changeSupportFactory, ObservableCollectionSupportFactory observableCollectionSupportFactory)
	{
		this(changeSupportFactory, observableCollectionSupportFactory, (ObservableList<E>) ObservableCollections.newObservableArrayList());
	}

	public CollectionValueHolder(ChangeSupportFactory changeSupportFactory, ObservableCollectionSupportFactory observableCollectionSupportFactory,
			ObservableList<E> observableList)
	{
		this(changeSupportFactory, observableCollectionSupportFactory, observableList, false);
	}

	public CollectionValueHolder(ChangeSupportFactory changeSupportFactory, ObservableCollectionSupportFactory observableCollectionSupportFactory,
			ObservableList<E> observableList, boolean checkIdentity)
	{
		super(changeSupportFactory, observableList, checkIdentity);
		collectionChangeSupport = observableCollectionSupportFactory.newObservableCollectionSupport(this);
		observableList.addCollectionChangeListener(collectionChangeHandler);
	}

	public void setValue(ObservableList<E> newValue)
	{
		setValue((Object) newValue);
	}

	@Override
	public void setValue(Object newValue)
	{
		Preconditions.checkArgument(newValue instanceof ObservableList || newValue == null, "can only set an ObservableList or null.");
		getObservableList().removeCollectionChangeListener(collectionChangeHandler);
		super.setValue(newValue == null ? ObservableCollections.newObservableArrayList() : newValue);
		getObservableList().addCollectionChangeListener(collectionChangeHandler);
	}

	@Override
	public void addCollectionChangeListener(CollectionChangeListener<E> listener)
	{
		collectionChangeSupport.addCollectionChangeListener(listener);
	}

	@Override
	public void removeCollectionChangeListener(CollectionChangeListener<E> listener)
	{
		collectionChangeSupport.removeCollectionChangeListener(listener);
	}

	@Override
	public ImmutableList<CollectionChangeListener<E>> getCollectionChangeListeners()
	{
		return collectionChangeSupport.getCollectionChangeListeners();
	}

	@Override
	public void executeBatchAction(BatchAction action)
	{
		getObservableList().executeBatchAction(action);
	}

	@Override
	public boolean add(E e)
	{
		return getObservableList().add(e);
	}

	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		return getObservableList().addAll(c);
	}

	@Override
	public void clear()
	{
		getObservableList().clear();
	}

	@Override
	public boolean contains(Object o)
	{
		return getObservableList().contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return getObservableList().containsAll(c);
	}

	@Override
	public boolean isEmpty()
	{
		return getObservableList().isEmpty();
	}

	@Override
	public Iterator<E> iterator()
	{
		return getObservableList().iterator();
	}

	@Override
	public boolean remove(Object o)
	{
		return getObservableList().remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return getObservableList().removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return getObservableList().retainAll(c);
	}

	@Override
	public int size()
	{
		return getObservableList().size();
	}

	@Override
	public Object[] toArray()
	{
		return getObservableList().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return getObservableList().toArray(a);
	}

	@Override
	public void add(int index, E element)
	{
		getObservableList().add(index, element);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		return getObservableList().addAll(index, c);
	}

	@Override
	public E get(int index)
	{
		return getObservableList().get(index);
	}

	@Override
	public int indexOf(Object o)
	{
		return getObservableList().indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return getObservableList().lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator()
	{
		return getObservableList().listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index)
	{
		return getObservableList().listIterator(index);
	}

	@Override
	public E remove(int index)
	{
		return getObservableList().remove(index);
	}

	@Override
	public E set(int index, E element)
	{
		return getObservableList().set(index, element);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		final ObservableList<E> subList = (ObservableList<E>) getObservableList().subList(fromIndex, toIndex);
		subList.addCollectionChangeListener(collectionChangeHandler);
		return subList;
	}

	@Override
	public void apply(CollectionDifference<E> difference)
	{
		getObservableList().apply(difference);
	}

	@Override
	public void unapply(CollectionDifference<E> difference)
	{
		getObservableList().unapply(difference);
	}

	private ObservableList<E> getObservableList()
	{
		return (ObservableList<E>) getValue();
	}

	private class CollectionChangeHandler implements CollectionChangeListener<E>
	{
		@Override
		public void onCollectionChange(CollectionChangeEvent<E> event)
		{
			collectionChangeSupport.fireCollectionChangeEvent(new CollectionChangeEvent<E>(CollectionValueHolder.this, event.getDifference()));
		}
	}

	@Override
	public void dispose()
	{
		ObservableList<E> observableList = getObservableList();
		if (observableList != null)
		{
			observableList.removeCollectionChangeListener(collectionChangeHandler);
		}
	}
}
