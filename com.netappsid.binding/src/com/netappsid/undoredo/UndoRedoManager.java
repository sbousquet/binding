package com.netappsid.undoredo;

import java.util.Iterator;
import java.util.LinkedList;

public class UndoRedoManager
{
	private final LinkedList<UndoRedoOperation> operations = new LinkedList<UndoRedoOperation>();
	private final LinkedList<UndoRedoOperation> redoableOperations = new LinkedList<UndoRedoOperation>();
	private final LinkedList<SavePoint> savePoints = new LinkedList<SavePoint>();

	private int transactionCount = 0;

	public void push(UndoRedoOperation operation)
	{
		if (!isCurrentlyInTransaction())
		{
			getRedoableOperations().clear();
			getOperations().addLast(operation);
		}
	}

	public void undo()
	{
		UndoRedoOperation peekLast = getOperations().peekLast();

		if (peekLast != null)
		{
			if (!checkIfOperationIsSavePoint(peekLast))
			{
				UndoRedoOperation pollLast = getOperations().pollLast();
				getRedoableOperations().addFirst(peekLast);
				peekLast.undo();
			}
		}
	}

	protected boolean checkIfOperationIsSavePoint(UndoRedoOperation peekLast)
	{
		for (SavePoint savePoint : getSavePoints())
		{
			if (peekLast.equals(savePoint.getOrigin()))
			{
				return true;
			}
		}

		return false;
	}

	public void redo()
	{
		UndoRedoOperation pollFirst = getRedoableOperations().pollFirst();
		if (pollFirst != null)
		{
			getOperations().addLast(pollFirst);
			pollFirst.redo();
		}
	}

	public boolean beginTransaction()
	{
		return ++transactionCount == 1;
	}

	public void endTransaction()
	{
		transactionCount--;
	}

	public SavePoint createSavePoint()
	{
		// We need to clear the redoable operations
		getRedoableOperations().clear();
		UndoRedoOperation peekLast = getOperations().peekLast();

		DefaultSavePoint savePoint = new DefaultSavePoint(peekLast);
		getSavePoints().add(savePoint);

		return savePoint;
	}

	public void commit(SavePoint savePoint)
	{
		if (savePoint == null)
		{
			if (!getSavePoints().isEmpty())
			{
				throw new IllegalStateException("A null SavePoint was committed while there are SavePoints tracked");
			}

			return;
		}

		if (getSavePoints().isEmpty() || !getSavePoints().contains(savePoint) || !getSavePoints().peekLast().equals(savePoint))
		{
			throw new IllegalStateException("Trying to commit SavePoint that is not tracked by the UndoRedoManager");
		}

		getSavePoints().removeLast();
	}

	public void rollback(SavePoint savePoint)
	{
		UndoRedoOperation origin = savePoint.getOrigin();

		// Do not rollback if the origin is not in currently applied actions. e.g.: it was undone earlier.
		// origin null means there was no
		if (origin == null || getOperations().contains(origin))
		{
			Iterator<UndoRedoOperation> descendingIterator = getOperations().descendingIterator();

			LinkedList<UndoRedoOperation> undoSavePointOperations = new LinkedList<UndoRedoOperation>();

			while (descendingIterator.hasNext())
			{
				UndoRedoOperation undoRedoValueModelAction = descendingIterator.next();

				if (origin != null && undoRedoValueModelAction.equals(origin))
				{
					break;
				}

				undoSavePointOperations.addFirst(undoRedoValueModelAction);
				descendingIterator.remove();
				undoRedoValueModelAction.undo();
			}

			// No operations were undone, no need for an undo state
			if (!undoSavePointOperations.isEmpty())
			{
				SavePointUndoRedoOperation savePointUndoRedoOperation = new SavePointUndoRedoOperation(undoSavePointOperations);
				getRedoableOperations().addFirst(savePointUndoRedoOperation);
			}
		}

		getSavePoints().removeLast();
	}

	protected LinkedList<UndoRedoOperation> getOperations()
	{
		return operations;
	}

	protected LinkedList<UndoRedoOperation> getRedoableOperations()
	{
		return redoableOperations;
	}

	protected boolean isCurrentlyInTransaction()
	{
		return transactionCount > 0;
	}

	protected LinkedList<SavePoint> getSavePoints()
	{
		return savePoints;
	}

	public void reset()
	{
		getOperations().clear();
		getRedoableOperations().clear();
		getSavePoints().clear();
	}
}
