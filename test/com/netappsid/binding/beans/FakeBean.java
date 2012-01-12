package com.netappsid.binding.beans;

import java.beans.PropertyChangeListener;

import com.jgoodies.binding.beans.Observable;
import com.netappsid.binding.beans.support.IdentityPropertyChangeSupport;
import com.netappsid.binding.beans.support.StandardChangeSupportFactory;

public class FakeBean implements Observable
{
	public static String PROPERTYNAME_BOOLEANPROPERTY = "booleanProperty";
	private final StandardChangeSupportFactory factory = new StandardChangeSupportFactory();
	private boolean booleanProperty;
	private final IdentityPropertyChangeSupport propertyChangeSupport;

	public FakeBean()
	{
		propertyChangeSupport = factory.createIdentityPropertyChangeSupport(this);
	}

	public boolean isBooleanProperty()
	{
		return booleanProperty;
	}

	public void setBooleanProperty(boolean booleanProprety)
	{
		this.booleanProperty = booleanProprety;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
}
