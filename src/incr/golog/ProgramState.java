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
	
	protected ProgramState(ProgramState parent, AbstractProgram program, State state) {
		if(parent == null || program == null || state == null)
			throw new NullPointerException();
		this.env = parent.env;
		this.program = program;
		this.state = state;
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
	
	private List<ProgramState> next() {
		return Collections.emptyList();
	}
	
	private List<ProgramState> next(AbstractProgram p, State s) {
		return Arrays.asList(new ProgramState(this,  p, s));
	}
	
	private List<ProgramState> next(AbstractProgram p1, State s1, AbstractProgram p2, State s2) {
		return Arrays.asList(new ProgramState(this,  p1, s1),
				new ProgramState(this,  p2, s2));
	}
	
	public List<ProgramState> trans() {
		if(program instanceof Atomic) {
			Atomic p = (Atomic)program;
			Functional a = p.getTerm();
			List<ProgramState> ret = new ArrayList<>();
			for(Proc proc : env.getProcs(a)) {
				proc = proc.ground(a);
					ret.addAll(next(proc.getBody(), state));
			}
			for(Action action : env.getActions(a)) {
				try {
					action = action.ground(Unify.unify(a, action.getHead()));
				} catch(IllegalArgumentException ex) {
					continue;
				}
				
				if(!action.isGround()) {
					System.err.println("WARNING: skipping execution of unground action " + action.getHead());
					continue;
				}
				
				// if possible, execute it:
				if(action.isPossible(state))
					ret.addAll(next(new Empty(), action.apply(state)));
			}
			return ret;
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
			ProgramState ps = new ProgramState(env, p.getP1(), state);
			List<ProgramState> r = new ArrayList<>();
			for(ProgramState ps1 : ps.trans()) {
				if(ps1 == null) continue;
				r.addAll(next(new Sequence(ps1.getProgram(), p.getP2()), ps1.getState()));
			}
			return r;
		} else if(program instanceof NDet) {
			NDet p = (NDet)program;
			return next(p.getP1(), state, p.getP2(), state);
		} else if(program instanceof Star) {
			Star p = (Star)program;
			return next(p.getP1(), state, new Sequence(p.getP1(), p), state);
		} else if(program instanceof Pi) {
			Pi p = (Pi)program;
			List<ProgramState> r = new ArrayList<>();
			for(Term t : env.getAllIndividuals())
				r.addAll(next(p.getP1().substitute(new Substitutions(Arrays.asList(new Substitution(t, p.getVar())))), state));
			return r;
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
	public SolutionIterator solutionIterator(Environment env) {
		return new SolutionIterator(env, this);
	}
	
	public class SolutionIterator implements Iterator<ProgramState> {
		private Deque<ProgramState> stack = new LinkedList<>();
		private Environment env;
		
		public SolutionIterator(Environment env, ProgramState s0) {
			this.env = env;
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
