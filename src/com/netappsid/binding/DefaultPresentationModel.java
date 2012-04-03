package com.netappsid.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.BeanAdapter;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.binding.state.State;
import com.netappsid.binding.state.StateModel;
import com.netappsid.binding.state.StatePropertyChangeEvent;
import com.netappsid.binding.value.ValueHolder;
import com.netappsid.observable.ObservableCollectionSupportFactory;
import com.netappsid.undoredo.UndoRedoManager;

/**
 * 
 * 
 * @author Eric Belanger
 * @author NetAppsID Inc.
 */
@SuppressWarnings("serial")
public class DefaultPresentationModel extends PresentationModel
{
	public static final String PROPERTYNAME_BEAN = "bean";

	private final BeanAdapter beanAdapter;
	private final StateModel stateModel;

	private final UndoRedoManager undoRedoManager;

	public DefaultPresentationModel(ChangeSupportFactory changeSupportFactory, ObservableCollectionSupportFactory observableCollectionSupportFactory,
			Class<?> beanClass)
	{
		this(changeSupportFactory, observableCollectionSupportFactory, beanClass, new ValueHolder(changeSupportFactory, null, true));
	}

	public DefaultPresentationModel(ChangeSupportFactory changeSupportFactory, ObservableCollectionSupportFactory observableCollectionSupportFactory,
			Class<?> beanClass, Object bean)
	{
		this(changeSupportFactory, observableCollectionSupportFactory, beanClass, bean, null);
	}

	public DefaultPresentationModel(ChangeSupportFactory changeSupportFactory, ObservableCollectionSupportFactory observableCollectionSupportFactory,
			Class<?> beanClass, Object bean, UndoRedoManager undoRedoManager)
	{
		this(changeSupportFactory, observableCollectionSupportFactory, beanClass, new ValueHolder(changeSupportFactory, bean, true), undoRedoManager);
	}

	public DefaultPresentationModel(ChangeSupportFactory changeSupportFactory, ObservableCollectionSupportFactory observableCollectionSupportFactory,
			Class<?> beanClass, ValueModel beanChannel)
	{
		this(changeSupportFactory, observableCollectionSupportFactory, beanClass, beanChannel, null);
	}

	public DefaultPresentationModel(ChangeSupportFactory changeSupportFactory, ObservableCollectionSupportFactory observableCollectionSupportFactory,
			Class<?> beanClass, ValueModel beanChannel, UndoRedoManager undoRedoManager)
	{
		super(changeSupportFactory, observableCollectionSupportFactory);
		this.undoRedoManager = undoRedoManager;
		this.beanAdapter = new BeanAdapter(changeSupportFactory, observableCollectionSupportFactory, beanChannel, beanClass);
		this.stateModel = new StateModel(changeSupportFactory);

		setBeanClass(beanClass);
		beanAdapter.addPropertyChangeListener(BeanAdapter.PROPERTYNAME_BEAN, new BeanChangeHandler());
		beanAdapter.addBeanPropertyChangeListener(new UpdateStateOnBeanPropertyChangeHandler());
	}

	@Override
	public Object getBean()
	{
		return beanAdapter.getBean();
	}

	@Override
	public ValueModel getBeanChannel()
	{
		return beanAdapter.getBeanChannel();
	}

	@Override
	public Object getValue(String propertyName)
	{
		return getValueModel(propertyName).getValue();
	}

	@Override
	public ValueModel getValueModel(String propertyName)
	{
		ValueModel valueModel = null;
		int index = propertyName.lastIndexOf('.');

		if (index == -1)
		{
			valueModel = beanAdapter.getValueModel(propertyName);
		}
		else
		{
			valueModel = getSubModel(propertyName.substring(0, index)).getValueModel(propertyName.substring(index + 1, propertyName.length()));
		}

		return valueModel;
	}

	@Override
	public void releaseBeanListeners()
	{
		beanAdapter.release();
	}

	@Override
	public void removeBeanPropertyChangeListener(PropertyChangeListener listener)
	{
		beanAdapter.removeBeanPropertyChangeListener(listener);
	}

	@Override
	public void removeBeanPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		beanAdapter.removeBeanPropertyChangeListener(propertyName, listener);
	}

	@Override
	public void setBean(Object newBean)
	{
		beanAdapter.setBean(newBean);
	}

	@Override
	public void setValue(String propertyName, Object newValue)
	{
		getValueModel(propertyName).setValue(newValue);
	}

	@Override
	public StateModel getStateModel()
	{
		return stateModel;
	}

	public UndoRedoManager getUndoRedoManager()
	{
		return undoRedoManager;
	}

	/**
	 * Responsible for bubbling bean change events to listeners on the PresentationModel.
	 * 
	 * @author Eric Belanger
	 * @author NetAppsID Inc.
	 * @version $Revision: 1.16 $
	 */
	private final class BeanChangeHandler implements PropertyChangeListener
	{
		@Override
		public void propertyChange(PropertyChangeEvent evt)
		{
			fireIdentityPropertyChange(PROPERTYNAME_BEAN, evt.getOldValue(), evt.getNewValue());
		}
	}

	private class UpdateStateOnBeanPropertyChangeHandler implements PropertyChangeListener
	{
		@Override
		public void propertyChange(PropertyChangeEvent evt)
		{
			if (evt instanceof StatePropertyChangeEvent && ((StatePropertyChangeEvent) evt).isAffectingState())
			{
				stateModel.setState(State.DIRTY);
			}
		}
	}
}
