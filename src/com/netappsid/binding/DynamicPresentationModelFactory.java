package com.netappsid.binding;


public class DynamicPresentationModelFactory
{
	public PresentationModel newDynamicPresentationModel(PresentationModel parentModel, String propertyName)
	{
		PresentationModel presentationModel = new DynamicPresentationModel(parentModel.getChangeSupportFactory(),
				parentModel.getObservableCollectionSupportFactory(), parentModel.getValueModel(propertyName));
		return presentationModel;
	}
}
