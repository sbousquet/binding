package com.netappsid.binding;

public interface DynamicPresentationModelFactory
{

	public abstract PresentationModel newDynamicPresentationModel(PresentationModel parentModel, String propertyName);

}