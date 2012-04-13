package com.netappsid.binding;

public interface DynamicPresentationModelFactory
{

	public abstract DynamicPresentationModel newDynamicPresentationModel(PresentationModel parentModel, String propertyName);

}