package incr.golog.syntax;

import incr.golog.AbstractEntity;
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
}
