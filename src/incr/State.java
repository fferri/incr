package incr;

import java.util.List;

import incr.term.Functional;
import incr.term.Term;

public abstract class State {
	/**
	 * Assert a fact in current state (has side effect on current state)
	 * 
	 * @param fact The fact to assert
	 */
	public abstract void assertFact(Term fact);
	
	/**
	 * Retract a fact from current state (has side effect on current state)
	 * 
	 * @param fact The fact to retract
	 */
	public abstract void retractFact(Term fact);
	
	/**
	 * Check whether a fact (or formula) holds in current state.
	 * 
	 * @param factOrFormula The fact or formula to check for
	 * @return <code>true</code> if fact or formula holds, <code>false</code> otherwise
	 */
	public abstract boolean holds(Term factOrFormula);
	
	/**
	 * Append an action to the history of actions attached to
	 * this state (has side effect on current state)
	 * 
	 * @param actionHead the functional representing the action
	 */
	public abstract void appendAction(Functional actionHead);
	
	/**
	 * Retrieve the history of actions attached to this state.
	 * 
	 * @return A list of functionals representing the actions
	 */
	public abstract List<Functional> getActionHistory();
}
