package com.netappsid.undoredo;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class UndoRedoManagerTest
{
	private UndoRedoManager manager;
	private UndoRedoValueModelOperation undoRedoValueModelOperation;
	private UndoRedoValue undoRedoValue;
	private UndoRedoValueModel undoRedoValueModel;

	@Before
	public void setUp()
	{
		undoRedoValue = new UndoRedoValue("old", "new");
		undoRedoValueModel = mock(UndoRedoValueModel.class);
		undoRedoValueModelOperation = spy(new UndoRedoValueModelOperation(undoRedoValueModel, undoRedoValue));

		manager = spy(new UndoRedoManager());
	}

	@Test
	public void testPush_NotInTransaction()
	{
		manager.push(undoRedoValueModelOperation);
		assertEquals("One operations must be contained in operations", 1, manager.getOperations().size());
		assertTrue("No operation must be contained in redoableOperations", manager.getRedoableOperations().isEmpty());
	}

	@Test
	public void testPush_DuringInTransaction()
	{
		manager.beginTransaction();
		manager.push(undoRedoValueModelOperation);
		assertTrue("No operation must be contained in operations", manager.getOperations().isEmpty());
		assertTrue("No operation must be contained in redoableOperations", manager.getRedoableOperations().isEmpty());
	}

	@Test
	public void testPush_NotInTransaction_EnsureNoClearRedoableOperations()
	{
		LinkedList<Object> operations = Lists.newLinkedList();
		operations.add(undoRedoValueModelOperation);
		doReturn(operations).when(manager).getRedoableOperations();

		manager.push(undoRedoValueModelOperation);
		assertTrue("RedoableOperations must be cleared", operations.isEmpty());
	}

	@Test
	public void testPush_DuringTransaction_EnsureClearRedoableOperations()
	{
		LinkedList<Object> operations = Lists.newLinkedList();
		operations.add(undoRedoValueModelOperation);
		doReturn(operations).when(manager).getRedoableOperations();

		manager.beginTransaction();
		manager.push(undoRedoValueModelOperation);
		assertEquals("RedoableOperations must not be cleared", 1, operations.size());
	}

	@Test
	public void testUndo()
	{
		LinkedList<Object> operations = Lists.newLinkedList();
		operations.add(undoRedoValueModelOperation);
		doReturn(operations).when(manager).getOperations();

		manager.undo();

		verify(undoRedoValueModelOperation).undo();
		assertTrue("operations must now be empty", manager.getOperations().isEmpty());
		assertEquals("1 operation must be contained in redoableOperations", 1, manager.getRedoableOperations().size());
	}

	@Test
	public void testUndo_lastOperationIsSavePoint()
	{
		LinkedList<Object> operations = Lists.newLinkedList();
		operations.add(undoRedoValueModelOperation);
		doReturn(operations).when(manager).getOperations();

		doReturn(true).when(manager).checkIfOperationIsSavePoint(undoRedoValueModelOperation);
		manager.undo();

		verify(undoRedoValueModelOperation, never()).undo();

		assertEquals("1 operation must be contained in operations", 1, manager.getOperations().size());
		assertTrue("redoableOperations must now be empty", manager.getRedoableOperations().isEmpty());
	}

	@Test
	public void testRedo()
	{
		LinkedList<Object> redoableOperations = Lists.newLinkedList();
		redoableOperations.add(undoRedoValueModelOperation);
		doReturn(redoableOperations).when(manager).getRedoableOperations();

		manager.redo();

		verify(undoRedoValueModelOperation).redo();
		assertEquals("1 operation must be contained in operations", 1, manager.getOperations().size());
		assertTrue("redoableOperations must be empty", manager.getRedoableOperations().isEmpty());

	}

	@Test
	public void testBeginTransaction_NotInTransaction()
	{
		boolean transoperationstarted = manager.beginTransaction();
		assertTrue("Transaction has just started", manager.isCurrentlyInTransaction());
	}

	@Test
	public void testBeginTransaction_InTransaction()
	{
		manager.beginTransaction();

		boolean transoperationstarted = manager.beginTransaction();
		assertFalse("Transaction is already started", transoperationstarted);
	}

	@Test
	public void testEndTransaction()
	{
		manager.beginTransaction();
		manager.beginTransaction();

		manager.endTransaction();
		manager.endTransaction();
		assertFalse("Transaction must be completed", manager.isCurrentlyInTransaction());
	}

	@Test
	public void testCreateSavePoint_NoTransaction()
	{
		assertNull("No SavePoint must be returned because there are no operations", manager.createSavePoint());
		assertTrue(manager.getSavePoints().isEmpty());
	}

	@Test
	public void testCreateSavePoint()
	{
		LinkedList<Object> operations = Lists.newLinkedList();
		operations.add(undoRedoValueModelOperation);
		doReturn(operations).when(manager).getOperations();

		SavePoint savePoint = manager.createSavePoint();
		
		assertEquals("SavePoint", undoRedoValueModelOperation, savePoint.getOrigin());
		assertEquals("One SavePoint must be preserved in manager", 1, manager.getSavePoints().size());
	}

	@Test
	public void testRollback_Null()
	{
		LinkedList<Object> operations = Lists.newLinkedList();
		operations.add(undoRedoValueModelOperation);
		doReturn(operations).when(manager).getOperations();

		manager.rollback(null);

		verify(manager, never()).getOperations();
	}

	@Test
	public void testRollback_SavePointEqualsCurrentOperation()
	{
		LinkedList<Object> operations = Lists.newLinkedList();
		operations.add(undoRedoValueModelOperation);
		doReturn(operations).when(manager).getOperations();

		SavePoint savePoint = new DefaultSavePoint(undoRedoValueModelOperation);

		LinkedList<Object> savePoints = Lists.newLinkedList();
		savePoints.add(savePoint);
		doReturn(savePoints).when(manager).getSavePoints();

		manager.rollback(savePoint);

		assertTrue("Nothing should have been undone", manager.getRedoableOperations().isEmpty());
		assertTrue("SavePoint must be removed from SavePoints", savePoints.isEmpty());
	}

	@Test
	public void testRollback_OneOperationAfterSavePoint()
	{
		UndoRedoValueModelOperation operationAfterOrigin = mock(UndoRedoValueModelOperation.class);

		LinkedList<Object> operations = Lists.newLinkedList();
		operations.add(undoRedoValueModelOperation);
		operations.add(operationAfterOrigin);
		doReturn(operations).when(manager).getOperations();

		SavePoint savePoint = new DefaultSavePoint(undoRedoValueModelOperation);
		LinkedList<Object> savePoints = Lists.newLinkedList();
		savePoints.add(savePoint);
		doReturn(savePoints).when(manager).getSavePoints();

		manager.rollback(savePoint);

		verify(operationAfterOrigin).undo();
		verify(undoRedoValueModelOperation, never()).undo();

		assertEquals("One operation must be contained in operations", 1, manager.getOperations().size());
		assertEquals("One operation must be contained in redoableOperations", 1, manager.getRedoableOperations().size());
	}

	@Test(expected = IllegalStateException.class)
	public void testCommit_SavePointNotLastSavePoint()
	{
		SavePoint savePoint = mock(SavePoint.class);

		SavePoint validSavePoint = new DefaultSavePoint(undoRedoValueModelOperation);
		LinkedList<Object> savePoints = Lists.newLinkedList();
		savePoints.add(validSavePoint);
		savePoints.add(savePoint);

		doReturn(savePoints).when(manager).getSavePoints();
		manager.commit(validSavePoint);
	}

	@Test(expected = IllegalStateException.class)
	public void testCommit_SavePointNotInSavePoints()
	{
		SavePoint savePoint = mock(SavePoint.class);

		SavePoint validSavePoint = new DefaultSavePoint(undoRedoValueModelOperation);
		LinkedList<Object> savePoints = Lists.newLinkedList();
		savePoints.add(validSavePoint);

		doReturn(savePoints).when(manager).getSavePoints();
		manager.commit(savePoint);
	}

	@Test(expected = IllegalStateException.class)
	public void testCommit_SavePoint_EmptySavePoints()
	{
		SavePoint savePoint = mock(SavePoint.class);
		manager.commit(savePoint);
	}

	@Test
	public void testCommit_NullSavePoint()
	{
		// Ensure no exception
		manager.commit(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testCommit_NullSavePoint_SavePointsNotEmpty()
	{
		SavePoint validSavePoint = new DefaultSavePoint(undoRedoValueModelOperation);
		LinkedList<Object> savePoints = Lists.newLinkedList();
		savePoints.add(validSavePoint);

		doReturn(savePoints).when(manager).getSavePoints();
		manager.commit(null);
	}
}
