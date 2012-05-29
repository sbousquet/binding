package com.netappsid.binding.beans;

import static org.mockito.Mockito.*;

import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.jgoodies.binding.beans.PropertyUnboundException;
import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.support.StandardChangeSupportFactory;
import com.netappsid.binding.value.AbstractValueModel;
import com.netappsid.binding.value.ValueHolder;
import com.netappsid.observable.StandardObservableCollectionSupportFactory;
import com.netappsid.test.beans.TestBean;
import com.netappsid.test.tools.PropertyChangeAssertion;

public class BeanAdapterTest
{
	@Test
	public void testInstantiation_NullBean()
	{
		newBeanAdapter((Object) null, Object.class);
	}

	@Test
	public void testInstantiation_NullValueModel()
	{
		newBeanAdapter((ValueModel) null, Object.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInstantiation_RefuseDisabledIdentityCheckValueModel()
	{
		final ValueModel valueModel = new ValueHolder(new StandardChangeSupportFactory(), null, false);

		newBeanAdapter(valueModel, Object.class);
	}

	@Test(expected = PropertyUnboundException.class)
	public void testInstantiation_RefusesBeanThatDoesntSupportJavaBeanSpecs()
	{
		newBeanAdapter("TEST", String.class);
	}

	@Test
	public void testGetBeanChannel_InstantiatedWithBeanReturnsBindedValueModelForBean()
	{
		final TestBean bean = new TestBean("1");
		final BeanAdapter adapter = newBeanAdapter(bean, TestBean.class);

		Assert.assertNotNull(adapter.getBeanChannel());
		Assert.assertEquals(bean, adapter.getBeanChannel().getValue());
	}

	@Test
	public void testGetBeanChannel_InstantiatedWithValueModelReturnsSameValueModel()
	{
		final ValueHolder valueModel = new ValueHolder(new StandardChangeSupportFactory(), null, true);
		final BeanAdapter adapter = newBeanAdapter(valueModel, TestBean.class);

		Assert.assertEquals(valueModel, adapter.getBeanChannel());
	}

	@Test
	public void testGetBean()
	{
		final TestBean bean = new TestBean("1");
		final BeanAdapter adapter = newBeanAdapter(bean, TestBean.class);

		Assert.assertEquals(bean, adapter.getBean());
	}

	@Test
	public void testSetBean()
	{
		final BeanAdapter adapter = newBeanAdapter(TestBean.class);
		final TestBean bean = new TestBean("1");

		adapter.setBean(bean);

		Assert.assertEquals(bean, adapter.getBean());
	}

	@Test(expected = PropertyUnboundException.class)
	public void testSetBean_RefusesBeanThatDoesntSupportJavaBeanSpecs()
	{
		newBeanAdapter(TestBean.class).setBean("TEST");
	}

	@Test
	public void testGetValue()
	{
		final TestBean bean = new TestBean("1");
		final BeanAdapter adapter = newBeanAdapter(bean, TestBean.class);

		bean.setProperty1("TEST_GET_VALUE");

		Assert.assertEquals("TEST_GET_VALUE", adapter.getValue(TestBean.PROPERTYNAME_PROPERTY1));
	}

	@Test
	public void testGetValue_NullBean_ReturnsNull()
	{
		Assert.assertNull(newBeanAdapter(TestBean.class).getValue(TestBean.PROPERTYNAME_PROPERTY1));
	}

	@Test
	public void testGetValue_InvalidProperty_ReturnsNull()
	{
		Assert.assertNull(newBeanAdapter(new TestBean("1"), TestBean.class).getValue("invalid"));
	}

	@Test
	public void testSetValue()
	{
		final TestBean bean = new TestBean("1");
		final BeanAdapter adapter = newBeanAdapter(bean, TestBean.class);

		adapter.setValue(TestBean.PROPERTYNAME_PROPERTY1, "TEST_SET_VALUE");

		Assert.assertEquals("TEST_SET_VALUE", bean.getProperty1());
	}

	@Test
	public void testGetValueModel()
	{
		final TestBean bean = new TestBean("1");
		final BeanAdapter adapter = newBeanAdapter(bean, TestBean.class);
		final SimplePropertyAdapter propertyAdapter = adapter.getValueModel(TestBean.PROPERTYNAME_PROPERTY1);

		bean.setProperty1("TEST_GET_VALUE_MODEL");

		Assert.assertNotNull(propertyAdapter);
		Assert.assertEquals(TestBean.PROPERTYNAME_PROPERTY1, propertyAdapter.getPropertyName());
		Assert.assertEquals("TEST_GET_VALUE_MODEL", propertyAdapter.getValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetValueModel_RefuseNullPropertyName()
	{
		newBeanAdapter(TestBean.class).getValueModel(null);
	}

	@Test
	public void testAddBeanPropertyChangeListener()
	{
		final PropertyChangeListener listener = new PropertyChangeAssertion();
		final BeanAdapter adapter = newBeanAdapter(TestBean.class);

		adapter.addBeanPropertyChangeListener(listener);
		Assert.assertEquals(listener, adapter.getBeanPropertyChangeListeners()[0]);
	}

	@Test
	public void testAddBeanPropertyChangeListener_SpecificProperty()
	{
		final PropertyChangeListener listener = new PropertyChangeAssertion();
		final BeanAdapter adapter = newBeanAdapter(TestBean.class);

		adapter.addBeanPropertyChangeListener(TestBean.PROPERTYNAME_PROPERTY1, listener);
		Assert.assertEquals(listener, adapter.getBeanPropertyChangeListeners(TestBean.PROPERTYNAME_PROPERTY1)[0]);
		Assert.assertEquals(0, adapter.getBeanPropertyChangeListeners().length);
	}

	@Test
	public void testRemoveBeanPropertyChangeListener()
	{
		final PropertyChangeListener listener = new PropertyChangeAssertion();
		final BeanAdapter adapter = newBeanAdapter(TestBean.class);

		adapter.addBeanPropertyChangeListener(listener);
		adapter.removeBeanPropertyChangeListener(listener);
		Assert.assertEquals(0, adapter.getBeanPropertyChangeListeners().length);
	}

	@Test
	public void testRemoveBeanPropertyChangeListener_SpecificProperty()
	{
		final PropertyChangeListener listener = new PropertyChangeAssertion();
		final BeanAdapter adapter = newBeanAdapter(TestBean.class);

		adapter.addBeanPropertyChangeListener(TestBean.PROPERTYNAME_PROPERTY1, listener);
		adapter.removeBeanPropertyChangeListener(TestBean.PROPERTYNAME_PROPERTY1, listener);
		Assert.assertEquals(0, adapter.getBeanPropertyChangeListeners(TestBean.PROPERTYNAME_PROPERTY1).length);
	}

	@Test
	public void testRelease_EnsurePropertyListenersOnBeanNotRemoved()
	{
		final TestBean bean = new TestBean("1");
		final int listenerCount = bean.getPropertyChangeListeners().length;
		final BeanAdapter adapter = newBeanAdapter(bean, TestBean.class);

		adapter.release();
		Assert.assertEquals(listenerCount, bean.getPropertyChangeListeners().length);
	}

	@Test
	public void testRelease_EnsureCollectionValueModelsAreDisposed()
	{
		final TestBean bean = new TestBean("1");

		final BeanAdapter adapter = spy(newBeanAdapter(bean, TestBean.class));
		Map<String, CollectionValueModel> collectionValueModels = new HashMap<String, CollectionValueModel>();

		CollectionValueModel collectionValueModel = mock(CollectionValueModel.class);
		collectionValueModels.put("test", collectionValueModel);
		doReturn(collectionValueModels).when(adapter).getCollectionValueModelsCache();
		adapter.release();
		verify(collectionValueModel).dispose();
	}

	@Test
	public void testSetBean_ForwardsBeanListeners()
	{
		final BeanAdapter adapter = newBeanAdapter(new TestBean("1"), TestBean.class);
		final PropertyChangeAssertion listenerSpy = new PropertyChangeAssertion();

		adapter.getValueModel(TestBean.PROPERTYNAME_PROPERTY1);
		adapter.addBeanPropertyChangeListener(TestBean.PROPERTYNAME_PROPERTY1, listenerSpy);

		final TestBean bean = new TestBean("2");

		adapter.setBean(bean);
		Assert.assertEquals(listenerSpy, bean.getPropertyChangeListeners(TestBean.PROPERTYNAME_PROPERTY1)[0]);
	}

	@Test
	public void testSetBean_AdaptedValuesListenersNotified()
	{
		final BeanAdapter adapter = newBeanAdapter(new TestBean("1"), TestBean.class);
		final PropertyChangeAssertion listenerSpy = new PropertyChangeAssertion();
		final ValueModel valueModel = adapter.getValueModel(TestBean.PROPERTYNAME_PROPERTY1);

		valueModel.addValueChangeListener(listenerSpy);

		final TestBean bean = new TestBean("2");

		bean.setProperty1("TEST_FORWARD");
		adapter.setBean(bean);

		listenerSpy.assertEventFired(AbstractValueModel.PROPERTYNAME_VALUE, valueModel, "TEST_FORWARD");
	}

	@Test
	public void testSetBean_AdaptedValuesListenersNotifiedWhenGlobalPropertyChangeFired()
	{
		final NonSpecificFireBean bean = new NonSpecificFireBean();
		final BeanAdapter adapter = newBeanAdapter(bean, TestBean.class);
		final PropertyChangeAssertion listenerSpy = new PropertyChangeAssertion();
		final ValueModel valueModel = adapter.getValueModel("property");

		valueModel.addValueChangeListener(listenerSpy);

		bean.setProperty("TEST_FORWARD");

		listenerSpy.assertEventFired(AbstractValueModel.PROPERTYNAME_VALUE, valueModel, "TEST_FORWARD");
	}

	private BeanAdapter newBeanAdapter(Class beanClass)
	{
		return new BeanAdapter(new StandardChangeSupportFactory(), new StandardObservableCollectionSupportFactory(), beanClass);
	}

	private BeanAdapter newBeanAdapter(Object bean, Class beanClass)
	{
		return new BeanAdapter(new StandardChangeSupportFactory(), new StandardObservableCollectionSupportFactory(), bean, beanClass);
	}

	private BeanAdapter newBeanAdapter(ValueModel beanChannel, Class beanClass)
	{
		return new BeanAdapter(new StandardChangeSupportFactory(), new StandardObservableCollectionSupportFactory(), beanChannel, beanClass);
	}

	public static class NonSpecificFireBean extends StandardBean
	{
		private String property;

		public String getProperty()
		{
			return property;
		}

		public void setProperty(String property)
		{
			this.property = property;
			firePropertyChange(null, null, property);
		}
	}
}
