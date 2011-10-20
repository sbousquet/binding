package com.netappsid.binding.state;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import com.netappsid.binding.beans.Bean;
import com.netappsid.binding.beans.support.ChangeSupportFactory;

public class StateModel extends Bean
{
	private final List<StateModel> parents = new ArrayList<StateModel>();
	private final List<StateModel> children = new ArrayList<StateModel>();

	private State state = State.CLEAN;

	public StateModel(ChangeSupportFactory changeSupportFactory)
	{
		super(changeSupportFactory);
	}

	public State getState()
	{
		return state;
	}

	public void setState(State state)
	{
		final State oldValue = this.state;

		this.state = state;
		fireIdentityPropertyChange(PROPERTYNAME_STATE, oldValue, state);
		propagateState(state);
	}

	public void resetState()
	{
		setState(State.CLEAN);
	}

	public void addStateChangeListener(PropertyChangeListener listener)
	{
		addPropertyChangeListener(PROPERTYNAME_STATE, listener);
	}

	public void removeStateChangeListener(PropertyChangeListener listener)
	{
		removePropertyChangeListener(PROPERTYNAME_STATE, listener);
	}

	public void link(StateModel stateModel)
	{
		children.add(stateModel);
		stateModel.parents.add(this);

		if (stateModel.getState().isBubbling())
		{
			setState(stateModel.getState());
		}
	}

	public void unlink(StateModel stateModel)
	{
		children.remove(stateModel);
		stateModel.parents.remove(stateModel);
	}

	private void propagateState(State state)
	{
		if (state.isBubbling())
		{
			propagateStateToNodes(state, parents);
		}
		else
		{
			propagateStateToNodes(state, children);
		}
	}

	private void propagateStateToNodes(State state, List<StateModel> nodes)
	{
		for (StateModel stateModel : nodes)
		{
			stateModel.setState(state);
		}
	}

	public static final String PROPERTYNAME_STATE = "state";
}
