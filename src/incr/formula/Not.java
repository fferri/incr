package incr.formula;

import incr.State;
import incr.subst.Substitution;
import incr.term.Term;

public class Not extends UnaryOp {
	public Not(Term operand1) {
		super("not", operand1);
	}

	@Override
	public boolean truthValue(State s) {
		return !getOperand1().truthValue(s);
	}
	
	@Override
	public Term substitute(Substitution s) {
		Term op1 = getOperand1().substitute(s);
		return op1 == getOperand1() ? this : new Not(op1);
	}
}
