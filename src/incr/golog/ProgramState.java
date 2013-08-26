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

public class ProgramState {
	private AbstractProgram program;
	private State state;
	
	private ProgramState prec;
	private ProgramState succ1;
	private ProgramState succ2;
	
	public ProgramState(AbstractProgram program, State state) {
		this(null, program, state);
	}
	
	protected ProgramState(ProgramState parent, AbstractProgram program, State state) {
		if(program == null || state == null)
			throw new NullPointerException();
		this.program = program;
		this.state = state;
		
		this.prec = parent;
		if(parent != null) {
			if(parent.succ1 == null) parent.succ1 = this;
			else if(parent.succ2 == null) parent.succ2 = this;
			else throw new IllegalStateException();
		}
	}
	
	public AbstractProgram getProgram() {
		return program;
	}
	
	public State getState() {
		return state;
	}
	
	public ProgramState getPrec() {
		return prec;
	}
	
	public ProgramState getSucc1() {
		return succ1;
	}
	
	public ProgramState getSucc2() {
		return succ2;
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
	
	private List<ProgramState> next() {
		return Collections.emptyList();
	}
	
	private List<ProgramState> next(AbstractProgram p, State s) {
		return Arrays.asList(new ProgramState(this,  p, s));
	}
	
	private List<ProgramState> next(AbstractProgram p1, State s1, AbstractProgram p2, State s2) {
		return Arrays.asList(new ProgramState(this,  p1, s1), new ProgramState(this,  p2, s2));
	}
	
	public List<ProgramState> trans() {
		if(program instanceof ExecAction) {
			ExecAction p = (ExecAction)program;
			Action a = p.getAction();
			return a.isPossible(state)
					? next(new Empty(), a.apply(state))
					: next();
		} else if(program instanceof Test) {
			Test p = (Test)program;
			return p.getFormula().truthValue(state)
					? next(new Empty(), state)
					: next();
		} else if(program instanceof If) {
			If p = (If)program;
			return next(p.getCondition().truthValue(state)
					? p.getThenBranch()
					: p.getElseBranch(),
					state);
		} else if(program instanceof While) {
			While p = (While)program;
			return next(p.getCondition().truthValue(state)
					? p.getBody()
					: new Empty(),
					state);
		} else if(program instanceof Sequence) {
			Sequence p = (Sequence)program;
			if(p.getP1() instanceof Empty)
				return next(p.getP2(), state);
			ProgramState ps = new ProgramState(p.getP1(), state);
			List<ProgramState> r = new ArrayList<>();
			for(ProgramState ps1 : ps.trans()) {
				if(ps1 == null) continue;
				r.add(new ProgramState(this, new Sequence(ps1.getProgram(), p.getP2()), ps1.getState()));
			}
			//return r.isEmpty() ? null : r;
			return r;
		} else if(program instanceof Proc) {
		} else if(program instanceof NDet) {
			NDet p = (NDet)program;
			return next(p.getP1(), state, p.getP2(), state);
		} else if(program instanceof Star) {
		} else {
			throw new UnsupportedOperationException(String.format("Cannot execute incremental step on a '%s'", program.getClass().getSimpleName()));
		}
	}
	
	public boolean isFinal() {
		if(program instanceof Empty) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This is the analogous of Golog's do/3
	 * 
	 * @return An iterator over all the possible programs
	 */
	public Iterator<ProgramState> solutionIterator() {
		return new SolutionIterator();
	}
	
	private class SolutionIterator implements Iterator<ProgramState> {
		private Deque<ProgramState> stack = new LinkedList<>();
		
		{
			stack.push(ProgramState.this);
		}
		
		private void expandNextFinal() {
			while(!stack.isEmpty() && !stack.peek().isFinal()) {
				ProgramState ps = stack.pop();
				List<ProgramState> succ = ps.trans();
				for(int i = succ.size(); i > 0; )
					stack.push(succ.get(--i));
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
