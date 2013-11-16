package incr.formula;

import incr.State;
import incr.subst.Substitution;
import incr.term.Term;

public class And extends BinaryOp {
	public And(Term operand1, Term operand2) {
		super("and", operand1, operand2);
	}
	
	public And(Term...terms) {
		this(0, terms);
	}
	
	protected And(int skip, Term[] terms) {
		this(terms[skip],
				(terms.length - skip) > 2
					? new And(skip + 1, terms)
					: terms[skip + 1]);
	}
	
	@Override
	public boolean truthValue(State s) {
		return getOperand1().truthValue(s) && getOperand2().truthValue(s);
	}
	
	@Override
	public Term substitute(Substitution s) {
		Term op1 = getOperand1().substitute(s), op2 = getOperand2().substitute(s);
		return op1 == getOperand1() && op2 == getOperand2() ? this : new And(op1, op2);
	}
}
