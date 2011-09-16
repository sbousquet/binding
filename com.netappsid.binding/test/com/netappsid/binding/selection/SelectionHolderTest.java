package com.netappsid.binding.selection;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.netappsid.binding.beans.support.StandardChangeSupportFactory;

public class SelectionHolderTest
{
	private List<Object> source;
	private SelectionHolder selectionHolder;
	private Object item;

	@Before
	public void setUp()
	{
		item = new Object();
		source = Arrays.asList(item);
		selectionHolder = new SelectionHolder(source, new StandardChangeSupportFactory());
	}

	@Test
	public void testSetSelectedItem_NullItem()
	{
		selectionHolder.setSelectedItem(null);
		assertNull(selectionHolder.getSelection());
	}

	@Test
	public void testSetSelectedItem_ValidItem()
	{
		selectionHolder.setSelectedItem(item);
		assertEquals(new Integer(0), selectionHolder.getSelection().first());
	}
}
