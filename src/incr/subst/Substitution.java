package incr.subst;

import incr.term.Term;
import incr.term.Variable;

public class Substitution {
	private final Variable variable;
	private final Term subst;
	
	public Substitution(Term subst, Variable variable) {
		if(variable == null || subst == null)
			throw new NullPointerException();
		this.variable = variable;
		this.subst = subst;
	}
	
	public Term getSubstitution() {
		return subst;
	}
	
	public Variable getVariable() {
		return variable;
	}

	@Override
	public String toString() {
		return String.format("%s/%s", subst, variable);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof Substitution && equals((Substitution)obj);
	}
	
	public boolean equals(Substitution s) {
		return s != null && variable.equals(s.variable) && subst.equals(s.subst);
	}
}
