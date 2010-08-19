package com.netappsid.binding.presentation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.netappsid.binding.beans.BeanAdapter;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.binding.state.State;
import com.netappsid.binding.state.StateModel;
import com.netappsid.binding.state.StatePropertyChangeEvent;
import com.netappsid.binding.value.ValueModel;

/**
 * 
 * 
 * @author Eric Belanger
 * @author NetAppsID Inc.
 * @version $Revision: 1.2 $
 */
@SuppressWarnings("serial")
public class DefaultPresentationModel extends PresentationModel
{
	private final BeanAdapter beanAdapter;
	private final StateModel stateModel;
	private final PresentationModelFactory presentationModelFactory;

	protected DefaultPresentationModel(PresentationModelFactory presentationModelFactory, ChangeSupportFactory changeSupportFactory, BeanAdapter beanAdapter,
			StateModel stateModel, Class<?> beanClass)
	{
		super(changeSupportFactory);

		this.presentationModelFactory = presentationModelFactory;
		this.beanAdapter = beanAdapter;
		this.stateModel = stateModel;

		setBeanClass(beanClass);
		beanAdapter.addPropertyChangeListener(BeanAdapter.PROPERTYNAME_BEAN, new BeanChangeHandler());
		beanAdapter.addBeanPropertyChangeListener(new UpdateStateOnBeanPropertyChangeHandler());
	}

	@Override
	public void addBeanPropertyChangeListener(PropertyChangeListener listener)
	{
		beanAdapter.addBeanPropertyChangeListener(listener);
	}

	@Override
	public void addBeanPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		beanAdapter.addBeanPropertyChangeListener(propertyName, listener);
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
	public PropertyChangeListener[] getBeanPropertyChangeListeners()
	{
		return beanAdapter.getBeanPropertyChangeListeners();
	}

	@Override
	public PropertyChangeListener[] getBeanPropertyChangeListeners(String propertyName)
	{
		return beanAdapter.getBeanPropertyChangeListeners(propertyName);
	}

	@Override
	public PresentationModel getSubModel(String modelName)
	{
		if (modelName.contains("."))
		{
			final String propertyName = modelName.substring(0, modelName.indexOf('.'));
			final String subModelName = modelName.substring(modelName.indexOf('.') + 1);

			return getSubModel(propertyName).getSubModel(subModelName);
		}
		else
		{
			if (!getSubModels().containsKey(modelName))
			{
				final PresentationModel subModel = presentationModelFactory.createSubModel(this, modelName);

				getSubModels().put(modelName, subModel);
				stateModel.link(subModel.getStateModel());
			}

			return getSubModels().get(modelName);
		}
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

	/**
	 * Responsible for bubbling bean change events to listeners on the PresentationModel.
	 * 
	 * @author Eric Belanger
	 * @author NetAppsID Inc.
	 * @version $Revision: 1.2 $
	 */
	private final class BeanChangeHandler implements PropertyChangeListener
	{
		public void propertyChange(PropertyChangeEvent evt)
		{
			fireIdentityPropertyChange(PROPERTYNAME_BEAN, evt.getOldValue(), evt.getNewValue());
		}
	}

	private class UpdateStateOnBeanPropertyChangeHandler implements PropertyChangeListener
	{
		public void propertyChange(PropertyChangeEvent evt)
		{
			if (evt instanceof StatePropertyChangeEvent && ((StatePropertyChangeEvent) evt).isAffectingState())
			{
				stateModel.setState(State.DIRTY);
			}
		}
	}

	public static final String PROPERTYNAME_BEAN = "bean";
}