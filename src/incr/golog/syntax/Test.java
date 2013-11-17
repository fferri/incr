package incr.golog.syntax;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import incr.State;
import incr.golog.AbstractEntity;
import incr.golog.ProgramState;
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
	
	@Override
	public List<ProgramState> trans(ProgramState s) {
		State state = s.getState();
		if(getFormula().truthValue(state))
			return Arrays.asList(new ProgramState(s, new Empty(), state));
		else
			return Collections.emptyList();
	}
	
	@Override
	public boolean isFinal(ProgramState s) {
		return false;
	}
}
