package incr.term;

import incr.State;
import incr.subst.Substitution;

public final class Variable extends Term {
	private final String name;
	
	public Variable(String name) {
		if(name == null)
			throw new NullPointerException();
		this.name = name;
		if(!name.matches("^[A-Z][a-zA-Z0-9_]*"))
			throw new IllegalArgumentException("variable identifiers must start with an uppercase letter");
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean truthValue(State s) {
		throw new UnsupportedOperationException("cannot evaluate truth value over unground terms");
	}
	
	@Override
	public boolean isGround() {
		return false;
	}
	
	@Override
	public Term substitute(Substitution s) {
		return s.getVariable().equals(this) ? s.getSubstitution() : this;
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode() + name.hashCode();
	}
	
	@Override
	public boolean equals(Term t) {
		return t != null && t instanceof Variable && equals((Variable)t);
	}
	
	public boolean equals(Variable v) {
		return v != null && getName().equals(v.getName());
	}
	
	@Override
	public String toString() {
		return "?" + getName();
	}
}
