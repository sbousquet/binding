package com.netappsid.binding.beans;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;

import org.junit.Test;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.support.StandardChangeSupportFactory;
import com.netappsid.observable.StandardObservableCollectionSupportFactory;
import com.netappsid.test.beans.TestBean;
import com.netappsid.test.beans.TestBeanDetail;

public class SimplePropertyAdapterTest
{
	@Test
	public void testGetPropertyName()
	{
		final SimplePropertyAdapter adapter = new SimplePropertyAdapter(newBeanAdapter(new TestBean("1"), TestBean.class), TestBean.PROPERTYNAME_PROPERTY1);

		assertEquals(TestBean.PROPERTYNAME_PROPERTY1, adapter.getPropertyName());
	}

	@Test
	public void testGetPropertyDescriptor_NonAdaptablePropertyReturnsNull()
	{
		final SimplePropertyAdapter adapter = new SimplePropertyAdapter(newBeanAdapter(new TestBean("1"), TestBean.class), "nonExisting");

		assertNull(adapter.getPropertyDescriptor());
	}

	@Test
	public void testGetPropertyDescriptor_AdaptablePropertyReturnsValidPropertyDescriptor()
	{
		final SimplePropertyAdapter adapter = new SimplePropertyAdapter(newBeanAdapter(new TestBean("1"), TestBean.class), TestBean.PROPERTYNAME_PROPERTY1);
		final PropertyDescriptor propertyDescriptor = adapter.getPropertyDescriptor();

		assertNotNull(propertyDescriptor);
		assertEquals(TestBean.PROPERTYNAME_PROPERTY1, propertyDescriptor.getName());
		assertEquals(String.class, propertyDescriptor.getPropertyType());
		assertEquals("getProperty1", propertyDescriptor.getReadMethod().getName());
		assertEquals("setProperty1", propertyDescriptor.getWriteMethod().getName());
	}

	@Test
	public void testGetValue_NullBean_NonPrimitiveType_ReturnsNull()
	{
		final SimplePropertyAdapter adapter = new SimplePropertyAdapter(newBeanAdapter(TestBean.class), TestBean.PROPERTYNAME_PROPERTY1);

		assertNull(adapter.getValue());
	}

	@Test
	public void testGetValue_NonAdaptableNonPrimitiveProperty_ReturnsNull()
	{
		final SimplePropertyAdapter adapter = new SimplePropertyAdapter(newBeanAdapter(new TestBean("1"), TestBean.class), "nonExisting");

		assertNull(adapter.getValue());
	}

	@Test
	public void testGetValue_AdaptableNonPrimitiveProperty_ReturnsBeanPropertyValue()
	{
		final TestBean bean = new TestBean("1");
		final SimplePropertyAdapter adapter = new SimplePropertyAdapter(newBeanAdapter(bean, TestBean.class), TestBean.PROPERTYNAME_PROPERTY1);

		bean.setProperty1("TEST");
		assertEquals("TEST", adapter.getValue());
	}

	@Test
	public void testSetValue_NonAdaptableNonPrimitiveProperty_DoesntGenerateException()
	{
		final SimplePropertyAdapter adapter = new SimplePropertyAdapter(newBeanAdapter(new TestBean("1"), TestBean.class), "nonExisting");
		adapter.setValue("TEST");
	}

	@Test
	public void testSetValue_AdaptableNonPrimitiveProperty_SetsBeanValue()
	{
		final TestBean bean = new TestBean("1");
		final SimplePropertyAdapter adapter = new SimplePropertyAdapter(newBeanAdapter(bean, TestBean.class), TestBean.PROPERTYNAME_PROPERTY1);

		adapter.setValue("TEST");
		assertEquals("TEST", bean.getProperty1());
	}

	@Test
	public void testSetValue_EnsureParentBeanInstanciated()
	{
		final TestBean bean = new TestBean("1");
		BeanAdapter beanAdapter = newBeanAdapter(bean, TestBean.class);

		SimplePropertyAdapter bean1ValueModel = beanAdapter.getValueModel(TestBean.PROPERTYNAME_BEAN1);
		BeanAdapter bean1Adapter = newBeanAdapter(bean1ValueModel, TestBeanDetail.class);

		final SimplePropertyAdapter adapter = new SimplePropertyAdapter(bean1Adapter, TestBeanDetail.PROPERTYNAME_PROPERTY);

		assertNull(bean.getBean1());
		adapter.setValue("PropertyValue");
		assertEquals("PropertyValue", bean.getBean1().getProperty());
	}

	@Test
	public void testSetValue_PrimitiveProperty_EnsureDefaultValueSetWhenNull()
	{
		FakeBean fakeBean = new FakeBean();
		fakeBean.setBooleanProperty(true);

		final BeanAdapter adapter = newBeanAdapter(fakeBean, FakeBean.class);
		SimplePropertyAdapter valueModel = adapter.getValueModel(FakeBean.PROPERTYNAME_BOOLEANPROPERTY);

		valueModel.setValue(null);

		assertFalse(fakeBean.isBooleanProperty());
	}

	@Test
	public void test_propertyDescriptorNotNull()
	{
		BeanAdapter newBeanAdapter = newBeanAdapter(TestBean.class);
		PropertyDescriptor propertyDescriptor = newBeanAdapter.getValueModel(TestBean.PROPERTYNAME_BEAN1).getPropertyDescriptor();
		assertNotNull(propertyDescriptor);
	}

	@Test
	public void testGetValue_NullBean_PrimitiveTypeProperty_EnsureNull()
	{
		BeanAdapter newBeanAdapter = newBeanAdapter(FakeBean.class);
		Object value = newBeanAdapter.getValueModel(FakeBean.PROPERTYNAME_BOOLEANPROPERTY).getValue();
		assertNull(value);
	}

	@Test
	public void testGetValue_ValidBean_PrimitiveTypeProperty_EnsureNotNull()
	{
		FakeBean newBean = new FakeBean();
		newBean.setBooleanProperty(true);

		BeanAdapter newBeanAdapter = newBeanAdapter(newBean, FakeBean.class);

		Object value = newBeanAdapter.getValueModel(FakeBean.PROPERTYNAME_BOOLEANPROPERTY).getValue();
		assertTrue((Boolean) value);
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
}
