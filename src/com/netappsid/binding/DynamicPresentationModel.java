package com.netappsid.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.support.ChangeSupportFactory;
import com.netappsid.binding.state.StateModel;
import com.netappsid.binding.value.ValueHolder;
import com.netappsid.observable.ObservableCollectionSupportFactory;
import com.netappsid.validate.Validate;

@SuppressWarnings("serial")
public class DynamicPresentationModel extends PresentationModel
{
	private final PropertyChangeListener mappedValueChangeHandler;

	private final ValueModel mapChannel;
	private final PropertyChangeSupport propertyChangeSupport;
	private final StateModel stateModel;
	private Map<String, ValueModel> valueModels;
	private Map<ValueModel, String> valueModelNames;

	public DynamicPresentationModel(ChangeSupportFactory changeSupportFactory, ObservableCollectionSupportFactory observableCollectionSupportFactory)
	{
		this(changeSupportFactory, observableCollectionSupportFactory, new ValueHolder(changeSupportFactory));
	}

	public DynamicPresentationModel(ChangeSupportFactory changeSupportFactory, ObservableCollectionSupportFactory observableCollectionSupportFactory,
			Map<String, ?> map)
	{
		this(changeSupportFactory, observableCollectionSupportFactory, new ValueHolder(changeSupportFactory, map));
	}

	public DynamicPresentationModel(ChangeSupportFactory changeSupportFactory, ObservableCollectionSupportFactory observableCollectionSupportFactory,
			ValueModel mapChannel)
	{
		super(changeSupportFactory, observableCollectionSupportFactory);

		this.mapChannel = Validate.notNull(mapChannel, "Map Channel cannot be null.");
		this.propertyChangeSupport = new PropertyChangeSupport(mapChannel);
		this.stateModel = new StateModel(changeSupportFactory);

		setBeanClass(Map.class);
		mapChannel.addValueChangeListener(new MapChangeHandler(this));
		
		mappedValueChangeHandler = new MappedValueChangeHandler(this);
	}

	@Override
	public Object getBean()
	{
		return getBeanChannel().getValue();
	}

	@Override
	public ValueModel getBeanChannel()
	{
		return mapChannel;
	}

	@Override
	public Object getValue(String propertyName)
	{
		return getValueModel(propertyName).getValue();
	}

	@Override
	public ValueModel getValueModel(String propertyName)
	{
		if (getBean() == null)
		{
			setBean(new HashMap<String, Object>());
		}

		ValueModel valueModel = getValueModels().get(propertyName);

		if (valueModel == null)
		{
			valueModel = registerValueModel(propertyName);
		}

		return valueModel;
	}

	@Override
	public void releaseBeanListeners()
	{
		PropertyChangeListener[] propertyChangeListeners = getPropertyChangeListeners();

		if (propertyChangeListeners != null)
		{
			for (PropertyChangeListener listener : propertyChangeListeners)
			{
				if (listener instanceof PropertyChangeListenerProxy)
				{
					removePropertyChangeListener(((PropertyChangeListenerProxy) listener).getPropertyName(), listener);
				}
				else
				{
					removePropertyChangeListener(listener);
				}
			}
		}
	}

	@Override
	public void removeBeanPropertyChangeListener(PropertyChangeListener listener)
	{
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	@Override
	public void removeBeanPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
	}

	@Override
	public void setBean(Object newBean)
	{
		if (newBean != null && !(newBean instanceof Map<?, ?>))
		{
			throw new IllegalArgumentException("Can only set a Map or a null value for the new bean.");
		}

		getBeanChannel().setValue(newBean);
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

	protected Map<ValueModel, String> getValueModelNames()
	{
		if (valueModelNames == null)
		{
			valueModelNames = new HashMap<ValueModel, String>();
		}

		return valueModelNames;
	}

	protected Map<String, ValueModel> getValueModels()
	{
		if (valueModels == null)
		{
			valueModels = new HashMap<String, ValueModel>();
		}

		return valueModels;
	}

	@SuppressWarnings("unchecked")
	protected ValueModel registerValueModel(String propertyName)
	{
		Map map = (Map) getBean();

		if (!map.containsKey(propertyName))
		{
			map.put(propertyName, (Object) null);
		}

		Object mapValue = map.get(propertyName);

		ValueModel valueModel = createValueModelForMapValue(propertyName, mapValue);
		valueModel.addValueChangeListener(mappedValueChangeHandler);
		getValueModels().put(propertyName, valueModel);
		getValueModelNames().put(valueModel, propertyName);

		return valueModel;
	}

	protected ValueModel createValueModelForMapValue(String propertyName, Object mapValue)
	{
		ValueModel valueModel = new ValueHolder(getChangeSupportFactory(), mapValue);
		return valueModel;
	}

	protected void mapChanged(PropertyChangeEvent evt)
	{
		if (evt.getNewValue() instanceof Map)
		{
			Map newMap = (Map) evt.getNewValue();

			for (Entry<String, ValueModel> entry : getValueModels().entrySet())
			{
				String key = entry.getKey();
				ValueModel valueModel = entry.getValue();

				if (newMap.containsKey(key))
				{
					valueModel.setValue(newMap.get(key));
				}
				else
				{
					newMap.put(key, valueModel.getValue());
				}
			}
		}
	}

	protected void mapValueChange(PropertyChangeEvent evt)
	{
		if (getValueModelNames().containsKey(evt.getSource()))
		{
			((Map) getBean()).put(getValueModelNames().get(evt.getSource()), evt.getNewValue());
		}
	}

	private final class MapChangeHandler implements PropertyChangeListener
	{
		private final DynamicPresentationModel dynamicPresentationModel;

		public MapChangeHandler(DynamicPresentationModel dynamicPresentationModel)
		{
			this.dynamicPresentationModel = dynamicPresentationModel;
		}

		@Override
		@SuppressWarnings("unchecked")
		public void propertyChange(PropertyChangeEvent evt)
		{
			dynamicPresentationModel.mapChanged(evt);
		}
	}

	private final class MappedValueChangeHandler implements PropertyChangeListener
	{
		private final DynamicPresentationModel dynamicPresentationModel;

		public MappedValueChangeHandler(DynamicPresentationModel dynamicPresentationModel)
		{
			this.dynamicPresentationModel = dynamicPresentationModel;
		}

		@Override
		@SuppressWarnings("unchecked")
		public void propertyChange(PropertyChangeEvent evt)
		{
			dynamicPresentationModel.mapValueChange(evt);
		}
	}
}
