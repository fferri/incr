package incr;

import incr.term.Functional;

public abstract class Action<STATE extends State> {
	protected final Functional head;
	
	public Action(Functional head) {
		if(head == null)
			throw new NullPointerException();
		this.head = head;
	}
	
	public Functional getHead() {
		return head;
	}
	
	/**
	 * Check whether this action can be executed in the specified state
	 * 
	 * @param term The ground term with which this action has been called
	 * @param state The state to check if this action can be executed in.
	 * @return <code>true</code> if it can be executed,
	 *         <code>false</code> otherwise.
	 */
	public abstract boolean isPossible(Functional term, STATE state);
	
	/**
	 * Executes this action in the specified state, and computes the new
	 * state
	 * 
	 * @param term The ground term with which this action has been called
	 * @param state The state to execute this action in
	 * @return The resulting state
	 */
	public abstract STATE apply(Functional term, STATE state);
	
	/**
	 * Shorthand to {@link #apply(Functional, STATE)} which applies only
	 * if this action is ground.
	 * 
	 * @param state The state to execute this action in
	 * @return The resulting state
	 * @see #apply(Functional, STATE)
	 */
	public STATE apply(STATE state) {
		if(isGround())
			return apply(getHead(), state);
		else
			throw new IllegalArgumentException("Cannot apply unground action");
	}
	
	public boolean isGround() {
		return head.isGround();
	}
	
	public abstract Action<STATE> ground(Functional term);
}
