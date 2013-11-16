package incr.golog.syntax;

import incr.golog.AbstractEntity;
import incr.subst.Substitutions;
import incr.term.Term;

public class Test extends AbstractProgram {
	Term formula;
	
	public Test(Term formula) {
		this.formula = formula;
	}
	
	public Term getFormula() {
		return formula;
	}
	
	@Override
	public boolean isGround() {
		return formula.isGround();
	}
	
	@Override
	public AbstractProgram substitute(Substitutions ss) {
		return new Test(formula.substitute(ss));
	}
	
	@Override
	public String toString(int i) {
		return indent(i) + formula + "?\n";
	}
	
	@Override
	public boolean equals(AbstractEntity e) {
		return e != null && e instanceof Test && equals((Test)e);
	}
	
	public boolean equals(Test t) {
		return t != null && getFormula().equals(t.getFormula());
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode() + getFormula().hashCode();
	}
}
