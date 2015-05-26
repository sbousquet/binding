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

/**
 * 
 * 
 * @author Eric Belanger
 * @author NetAppsID Inc.
 * @version $Revision: 1.15 $
 */
@SuppressWarnings("serial")
public class DefaultPresentationModel extends PresentationModel
{
	public static final String PROPERTYNAME_BEAN = "bean";

	private final BeanAdapter beanAdapter;
	private final StateModel stateModel;

	private BeanChangeHandler beanChangeHandler;
	private UpdateStateOnBeanPropertyChangeHandler updateStateOnBeanPropertyChangeHandler;

	public DefaultPresentationModel(ChangeSupportFactory changeSupportFactory, Class<?> beanClass)
	{
		this(changeSupportFactory, beanClass, new ValueHolder(changeSupportFactory, null, true));
	}

	public DefaultPresentationModel(ChangeSupportFactory changeSupportFactory, Class<?> beanClass, Object bean)
	{
		this(changeSupportFactory, beanClass, new ValueHolder(changeSupportFactory, bean, true));
	}

	public DefaultPresentationModel(ChangeSupportFactory changeSupportFactory, Class<?> beanClass, ValueModel beanChannel)
	{
		super(changeSupportFactory);
		this.beanAdapter = new BeanAdapter(changeSupportFactory, beanChannel);
		this.stateModel = new StateModel(changeSupportFactory);

		setBeanClass(beanClass);
		beanChangeHandler = new BeanChangeHandler();
		beanAdapter.addPropertyChangeListener(BeanAdapter.PROPERTYNAME_BEAN, beanChangeHandler);
		updateStateOnBeanPropertyChangeHandler = new UpdateStateOnBeanPropertyChangeHandler(stateModel);
		beanAdapter.addBeanPropertyChangeListener(updateStateOnBeanPropertyChangeHandler);
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
				final PresentationModel subModel = PresentationModelFactory.createPresentationModel(this, modelName);

				getSubModels().put(modelName, subModel);
				if (subModel.getStateModel() != null)
				{
					stateModel.link(subModel.getStateModel());
				}
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
	 * @version $Revision: 1.15 $
	 */
	private final class BeanChangeHandler implements PropertyChangeListener
	{
		@Override
		public void propertyChange(PropertyChangeEvent evt)
		{
			fireIdentityPropertyChange(PROPERTYNAME_BEAN, evt.getOldValue(), evt.getNewValue());
		}
	}

	private static final class UpdateStateOnBeanPropertyChangeHandler implements PropertyChangeListener
	{
		private final StateModel stateModel;
		
		public UpdateStateOnBeanPropertyChangeHandler(StateModel stateModel)
		{
			this.stateModel = stateModel;
		}
		
		public void propertyChange(PropertyChangeEvent evt)
		{
			if (evt instanceof StatePropertyChangeEvent && ((StatePropertyChangeEvent) evt).isAffectingState())
			{
				stateModel.setState(State.DIRTY);
			}
		}
	}
	
	@Override
	public void dispose()
	{
		beanAdapter.removeBeanPropertyChangeListener(updateStateOnBeanPropertyChangeHandler);
		beanAdapter.removePropertyChangeListener(BeanAdapter.PROPERTYNAME_BEAN, beanChangeHandler);
		beanAdapter.release();
		beanAdapter.dispose();
		updateStateOnBeanPropertyChangeHandler = null;
		beanChangeHandler = null;
	}
}
