/**
 * 
 */
package com.netappsid.undoredo;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.beans.support.StandardChangeSupportFactory;

/**
 * @author xjodoin
 * @author NetAppsID inc.
 * 
 * @version
 * 
 */
public class UndoRedoValueModelTest
{

	@Test
	public void test_valueModel_with_undoSupport()
	{
		UndoSupport undoSupport = mock(UndoSupport.class, withSettings().extraInterfaces(ValueModel.class));
		when(undoSupport.getUndoableValue(eq("test"))).thenReturn(1);
		ValueModel valueModel = (ValueModel) undoSupport;

		UndoRedoManager manager = new UndoRedoManager();
		UndoRedoValueModel undoRedoValueModel = new UndoRedoValueModel(manager, (ValueModel) undoSupport, new StandardChangeSupportFactory());
		undoRedoValueModel.setValue("test");
		manager.undo();
		verify(valueModel).setValue(eq(1));
	}
}
