package incr.golog.syntax;

import java.util.ArrayList;
import java.util.List;

import incr.Action;
import incr.State;
import incr.golog.AbstractEntity;
import incr.golog.Environment;
import incr.golog.Proc;
import incr.golog.ProgramState;
import incr.subst.Substitutions;
import incr.term.Functional;

public class Atomic extends AbstractProgram {
	private Functional term;
	
	public Atomic(Functional term) {
		if(term == null)
			throw new NullPointerException();
		this.term = term;
	}
	
	public Functional getTerm() {
		return term;
	}
	
	@Override
	public boolean isGround() {
		return term.isGround();
	}
	
	@Override
	public AbstractProgram substitute(Substitutions ss) {
		return new Atomic((Functional)term.substitute(ss));
	}
	
	@Override
	public String toString(int i) {
		return indent(i) + term + "\n";
	}
	
	@Override
	public boolean equals(AbstractEntity e) {
		return e != null && e instanceof Atomic && equals((Atomic)e);
	}
	
	public boolean equals(Atomic a) {
		return a != null && getTerm().equals(a.getTerm());
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode() + getTerm().hashCode();
	}
	
	@Override
	public List<ProgramState> trans(ProgramState s) {
		if(s.getProgram() != this)
			throw new IllegalArgumentException();
		
		Environment env = s.getEnvironment();
		State state = s.getState();
		Functional a = getTerm();
		List<ProgramState> ret = new ArrayList<>();
		for(Proc proc : env.getProcs(a)) {
			proc = proc.ground(a);
			ret.add(new ProgramState(s, proc.getBody(), state));
		}
		for(Action action : env.getActions(a)) {
			State newState = action.apply(a, state);
			if(newState != null) // action was possible
				ret.add(new ProgramState(s, new Empty(), newState));
		}
		return ret;
	}
	
	@Override
	public boolean isFinal(ProgramState s) {
		return false;
	}
}
