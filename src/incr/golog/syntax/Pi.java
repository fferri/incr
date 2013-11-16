package incr.golog.syntax;

import incr.golog.AbstractEntity;
import incr.subst.Substitutions;
import incr.term.Variable;

public class Pi extends AbstractProgram {
	private Variable var;
	private AbstractProgram p1;
	
	public Pi(Variable var, AbstractProgram p1) {
		if(var == null || p1 == null)
			throw new NullPointerException();
		this.var = var;
		this.p1 = p1;
	}
	
	public Variable getVar() {
		return var;
	}
	
	public AbstractProgram getP1() {
		return p1;
	}
	
	@Override
	public boolean isGround() {
		return p1.isGround();
	}
	
	@Override
	public AbstractProgram substitute(Substitutions ss) {
		/*
		 * pi must protect the scope of its variable
		 * i.e. the variable by pi becomes local and must not be
		 * substituted
		 */
		Substitutions ss1 = ss.cloneAndRemove(var);
		return new Pi(var, p1.substitute(ss1));
	}
	
	@Override
	public String toString(int i) {
		return indent(i) + "pi " + var + "\n"
				+ p1.toString(i + 1);
	}
	
	@Override
	public boolean equals(AbstractEntity e) {
		return e != null && e instanceof Pi && equals((Pi)e);
	}
	
	public boolean equals(Pi p) {
		return p != null && getVar().equals(p.getVar()) && getP1().equals(p.getP1());
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode() + getVar().hashCode() + getP1().hashCode();
	}
}
