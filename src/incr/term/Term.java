package incr.term;

import incr.State;
import incr.subst.Substitution;
import incr.subst.Substitutions;

public abstract class Term {
	public abstract String toString();
	
	@Override
	public final boolean equals(Object obj) {
		return obj != null && obj instanceof Term && equals((Term)obj);
	}
	
	public abstract boolean equals(Term t);
	
	public abstract int hashCode();
	
	public abstract boolean truthValue(State s);
	
	public abstract boolean isGround();

	public abstract Term substitute(Substitution s);
	
	public Term substitute(Substitutions ss) {
		Term tmp = this;
		for(Substitution s : ss)
			tmp = tmp.substitute(s);
		return tmp;
	}
	
	public static final Functional TRUE = new Functional("true");
	public static final Functional FALSE = new Functional("false");
}
