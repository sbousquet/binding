package com.netappsid.binding.selection;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.netappsid.binding.beans.support.ChangeSupportFactory;

@SuppressWarnings("serial")
public class SelectionHolder extends AbstractSelectionModel
{
	private SortedSet<Integer> indexes;
	private final List<Object> origin;

	public SelectionHolder(List<Object> origin, ChangeSupportFactory changeSupportFactory)
	{
		super(changeSupportFactory);
		this.origin = origin;
	}

	@Override
	public SortedSet<Integer> getSelection()
	{
		return indexes != null ? new TreeSet<Integer>(indexes) : null;
	}

	@Override
	public boolean hasSelection()
	{
		return indexes != null;
	}

	@Override
	public void setSelectedItem(Object selection)
	{
		if (selection == null)
		{
			setSelection();
		}
		else
		{
			setSelection(origin.indexOf(selection));
		}
	}

	@Override
	public void setSelection()
	{
		SortedSet<Integer> oldValue = indexes;

		indexes = null;
		fireSelectionChange(oldValue);
	}

	@Override
	public void setSelection(Integer index)
	{
		SortedSet<Integer> oldValue = indexes;

		indexes = new TreeSet<Integer>();
		indexes.add(index);
		fireSelectionChange(oldValue, index);
	}

	@Override
	public void setSelection(SortedSet<Integer> indexes)
	{
		SortedSet<Integer> oldValue = this.indexes;

		this.indexes = new TreeSet<Integer>(indexes);
		fireSelectionChange(oldValue, indexes);
	}

	@Override
	public void setSelectionInterval(Integer index0, Integer index1)
	{
		SortedSet<Integer> indexes = new TreeSet<Integer>();

		for (Integer index = index0; index <= index1; index++)
		{
			indexes.add(index);
		}

		setSelection(indexes);
	}

	@Override
	public void setSelectedItems(Object[] selections)
	{
		SortedSet<Integer> selectionsIndex = new TreeSet<Integer>();
		for (Object object : selections)
		{
			selectionsIndex.add(origin.indexOf(object));
		}

		setSelection(selectionsIndex);

	}
}
