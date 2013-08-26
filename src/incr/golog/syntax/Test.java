package incr.golog.syntax;

import incr.formula.AbstractTerm;

public class Test extends AbstractProgram {
	AbstractTerm formula;
	
	public Test(AbstractTerm formula) {
		this.formula = formula;
	}
	
	public AbstractTerm getFormula() {
		return formula;
	}
	
	@Override
	public String toString(int i) {
		return indent(i) + formula + "?\n";
	}
}
