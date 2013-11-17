package incr.golog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import incr.Action;
import incr.State;
import incr.golog.syntax.*;
import incr.subst.Substitution;
import incr.subst.Substitutions;
import incr.subst.Unify;
import incr.term.Functional;
import incr.term.Term;

public class ProgramState {
	private Environment env;
	private AbstractProgram program;
	private State state;
	
	public ProgramState(Environment env, AbstractProgram program, State state) {
		if(env == null || program == null || state == null)
			throw new NullPointerException();
		this.env = env;
		this.program = program;
		this.state = state;
	}
	
	public ProgramState(ProgramState parent, AbstractProgram program, State state) {
		if(parent == null || program == null || state == null)
			throw new NullPointerException();
		this.env = parent.env;
		this.program = program;
		this.state = state;
	}
	
	public Environment getEnvironment() {
		return env;
	}
	
	public AbstractProgram getProgram() {
		return program;
	}
	
	public State getState() {
		return state;
	}
	
	@Override
	public String toString() {
		return String.format("ProgramState:\n\tProgram:\n%s\n\tState: %s\n\tHistory: %s", program.toString(2), state, state.getActionHistory());
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null &&
				obj instanceof ProgramState &&
				equals((ProgramState)obj);
	}
	
	public boolean equals(ProgramState ps) {
		return ps != null &&
				ps.getProgram().equals(getProgram()) &&
				ps.getState().equals(getState());
	}
	
	@Override
	public int hashCode() {
		return 17 * program.hashCode() + 31 * state.hashCode();
	}
	
	public List<ProgramState> trans() {
		return program.trans(this);
	}
	
	public boolean isFinal() {
		return program.isFinal(this);
	}
	
	/**
	 * This is the analogous of Golog's do/3
	 * 
	 * @return An iterator over all the possible programs
	 */
	public SolutionIterator solutionIterator() {
		return new SolutionIterator(this);
	}
	
	public static class SolutionIterator implements Iterator<ProgramState> {
		private Deque<ProgramState> stack = new LinkedList<>();
		
		public SolutionIterator(ProgramState s0) {
			stack.push(s0);
		}
		
		private void expandNextFinal() {
			while(!stack.isEmpty() && !stack.peek().isFinal()) {
				ProgramState ps = stack.pop();
				List<ProgramState> succ = ps.trans();
				stack.addAll(succ);
				//for(int i = succ.size(); i > 0; )
				//	stack.push(succ.get(--i));
			}
		}
		
		@Override
		public boolean hasNext() {
			expandNextFinal();
			return !stack.isEmpty();
		}
		
		@Override
		public ProgramState next() {
			expandNextFinal();
			return stack.pop();
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
