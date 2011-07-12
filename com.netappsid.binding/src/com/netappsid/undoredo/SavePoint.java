package com.netappsid.undoredo;

import com.netappsid.undoredo.UndoRedoOperation;

public interface SavePoint
{
	UndoRedoOperation getOrigin();
}
