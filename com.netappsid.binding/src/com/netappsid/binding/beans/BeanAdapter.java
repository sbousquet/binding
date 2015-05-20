package com.netappsid.binding.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import com.jgoodies.binding.beans.PropertyUnboundException;
import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.binding.value.ValueHolder;
import com.netappsid.validate.Validate;

public class BeanAdapter extends Bean
{
	private final ChangeSupportFactory changeSupportFactory;
	private final ValueModel beanChannel;
	private final Map<String, SimplePropertyAdapter> propertyAdapters;
	private final IndirectPropertyChangeSupport indirectChangeSupport;
	private PropertyChangeListener propertyChangeHandler;
	private BeanChangeHandler beanChangeHandler;

	private Object storedOldBean;

	public BeanAdapter(ChangeSupportFactory changeSupportFactory)
	{
		this(changeSupportFactory, (ValueModel) null);
	}
	
	public BeanAdapter(ChangeSupportFactory changeSupportFactory, Object bean)
	{
		this(changeSupportFactory, new ValueHolder(changeSupportFactory, bean, true));
	}
	
	public BeanAdapter(ChangeSupportFactory changeSupportFactory, ValueModel beanChannel)
	{
		super(changeSupportFactory);
		this.changeSupportFactory = changeSupportFactory;
		this.beanChannel = beanChannel != null ? beanChannel : new ValueHolder(changeSupportFactory, null, true);
		this.propertyAdapters = new HashMap<String, SimplePropertyAdapter>();
		this.indirectChangeSupport = new IndirectPropertyChangeSupport(this.beanChannel);
		this.propertyChangeHandler = new PropertyChangeHandler(this);
		this.beanChangeHandler = new BeanChangeHandler(this);
		this.beanChannel.addValueChangeListener(beanChangeHandler);
		this.storedOldBean = getBean();

		checkBeanChannelIdentityCheck(this.beanChannel);
		addChangeHandlerTo(getBean());
	}

	public ValueModel getBeanChannel()
	{
		return beanChannel;
	}

	public Object getBean()
	{
		return beanChannel.getValue();
	}

	public void setBean(Object newBean)
	{
		beanChannel.setValue(newBean);
	}

	public Object getValue(String propertyName)
	{
		return getValueModel(propertyName).getValue();
	}

	public void setValue(String propertyName, Object newValue)
	{
		getValueModel(propertyName).setValue(newValue);
	}

	public SimplePropertyAdapter getValueModel(String propertyName)
	{
		Validate.notNull(propertyName, "The property name must not be null.");

		final SimplePropertyAdapter registeredPropertyAdapter = propertyAdapters.get(propertyName);

		if (registeredPropertyAdapter == null)
		{
			propertyAdapters.put(propertyName, new SimplePropertyAdapter(this, propertyName));
		}

		return propertyAdapters.get(propertyName);
	}

	public synchronized void addBeanPropertyChangeListener(PropertyChangeListener listener)
	{
		if (listener != null)
		{
			indirectChangeSupport.addPropertyChangeListener(listener);
		}
	}

	public synchronized void removeBeanPropertyChangeListener(PropertyChangeListener listener)
	{
		if (listener != null)
		{
			indirectChangeSupport.removePropertyChangeListener(listener);
		}
	}

	public synchronized void addBeanPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		if (listener != null)
		{
			indirectChangeSupport.addPropertyChangeListener(propertyName, listener);
		}
	}

	public synchronized void removeBeanPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		if (listener != null)
		{
			indirectChangeSupport.removePropertyChangeListener(propertyName, listener);
		}
	}

	public synchronized PropertyChangeListener[] getBeanPropertyChangeListeners()
	{
		return indirectChangeSupport.getPropertyChangeListeners();
	}

	public synchronized PropertyChangeListener[] getBeanPropertyChangeListeners(String propertyName)
	{
		return indirectChangeSupport.getPropertyChangeListeners(propertyName);
	}

	public synchronized void release()
	{
		removeChangeHandlerFrom(getBean());
		indirectChangeSupport.removeAll();
		removeChangeHandlerFrom(storedOldBean);
	}

	private void addChangeHandlerTo(Object bean)
	{
		if (bean != null)
		{
			if (BeanUtils.supportsBoundProperties(bean.getClass()))
			{
				BeanUtils.addPropertyChangeListener(bean, bean.getClass(), propertyChangeHandler);
			}
			else
			{
				throw new PropertyUnboundException(
						"The bean must provide support for listening on property changes as described in section 7.4.5 of the Java Bean Specification.");
			}
		}
	}
	
	protected ChangeSupportFactory getChangeSupportFactory()
	{
		return changeSupportFactory;
	}

	private void removeChangeHandlerFrom(Object bean)
	{
		if (bean != null && propertyChangeHandler != null)
		{
			BeanUtils.removePropertyChangeListener(bean, bean.getClass(), propertyChangeHandler);
		}
	}

	private void checkBeanChannelIdentityCheck(ValueModel valueModel)
	{
		if (valueModel instanceof ValueHolder && !((ValueHolder) valueModel).isIdentityCheckEnabled())
		{
			throw new IllegalArgumentException("The bean channel must have the identity check enabled.");
		}
	}

	private static final class BeanChangeHandler implements PropertyChangeListener
	{
		private BeanAdapter beanAdapter;
		
		public BeanChangeHandler(BeanAdapter beanAdapter)
		{
			this.beanAdapter = beanAdapter;
		}
		
		public void propertyChange(PropertyChangeEvent evt)
		{
			final Object newBean = evt.getNewValue() != null ? evt.getNewValue() : beanAdapter.getBean();

			setBean(beanAdapter.storedOldBean, newBean);
			beanAdapter.storedOldBean = newBean;
		}
		
		public void dispose()
		{
			beanAdapter = null;
		}

		private void setBean(Object oldBean, Object newBean)
		{
			beanAdapter.fireIdentityPropertyChange(PROPERTYNAME_BEFORE_BEAN, oldBean, newBean);
			beanAdapter.removeChangeHandlerFrom(oldBean);
			forwardAllAdaptedValuesChanged(oldBean, newBean);
			beanAdapter.addChangeHandlerTo(newBean);
			beanAdapter.fireIdentityPropertyChange(PROPERTYNAME_BEAN, oldBean, newBean);
			beanAdapter.fireIdentityPropertyChange(PROPERTYNAME_AFTER_BEAN, oldBean, newBean);
		}

		private void forwardAllAdaptedValuesChanged(Object oldBean, Object newBean)
		{
			for (Object adapter : beanAdapter.propertyAdapters.values().toArray())
			{
				((SimplePropertyAdapter) adapter).setBean(oldBean, newBean);
			}
		}
	}

	private static final class PropertyChangeHandler implements PropertyChangeListener
	{
		private BeanAdapter beanAdapter;
		
		public PropertyChangeHandler(BeanAdapter beanAdapter)
		{
			this.beanAdapter = beanAdapter;
		}
		
		public void propertyChange(PropertyChangeEvent evt)
		{
			if (evt.getPropertyName() == null)
			{
				forwardAllAdaptedValuesChanged();
			}
			else
			{
				final SimplePropertyAdapter adapter = beanAdapter.propertyAdapters.get(evt.getPropertyName());

				if (adapter != null)
				{
					adapter.fireValueChange(evt.getOldValue(), evt.getNewValue(), true);
				}
			}
		}
		
		public void dispose()
		{
			beanAdapter = null;
		}

		private void forwardAllAdaptedValuesChanged()
		{
			final Object currentBean = beanAdapter.getBean();

			for (Object adapter : beanAdapter.propertyAdapters.values().toArray())
			{
				((SimplePropertyAdapter) adapter).fireChange(currentBean);
			}
		}
	}
	
	public void dispose()
	{
		for (Object adapter : propertyAdapters.values().toArray())
		{
			((SimplePropertyAdapter)adapter).dispose();
		}
		propertyAdapters.clear();
		
		if (storedOldBean instanceof BeanAdapter )
		{
			((BeanAdapter) storedOldBean).dispose();
		}
		
		if (propertyChangeHandler != null)
		{
			((PropertyChangeHandler)propertyChangeHandler).dispose();
			propertyChangeHandler = null;
		}
		
		if (beanChangeHandler != null)
		{
			beanChangeHandler.dispose();
			beanChangeHandler = null;
		}
		
		if (storedOldBean != null && storedOldBean instanceof BeanAdapter)
		{
			((BeanAdapter)storedOldBean).dispose();
		}
		storedOldBean = null;
	}

	public static final String PROPERTYNAME_BEFORE_BEAN = "beforeBean";
	public static final String PROPERTYNAME_BEAN = "bean";
	public static final String PROPERTYNAME_AFTER_BEAN = "afterBean";
}
