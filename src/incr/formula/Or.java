package incr.formula;

import incr.State;
import incr.subst.Substitution;
import incr.term.Term;

public class Or extends BinaryOp {
	public Or(Term operand1, Term operand2) {
		super("or", operand1, operand2);
	}
	
	public Or(Term...terms) {
		this(0, terms);
	}
	
	protected Or(int skip, Term[] terms) {
		this(terms[skip],
				(terms.length - skip) > 2
					? new Or(skip + 1, terms)
					: terms[skip + 1]);
	}
	
	@Override
	public boolean truthValue(State s) {
		return getOperand1().truthValue(s) || getOperand2().truthValue(s);
	}
	
	@Override
	public Term substitute(Substitution s) {
		Term op1 = getOperand1().substitute(s), op2 = getOperand2().substitute(s);
		return op1 == getOperand1() && op2 == getOperand2() ? this : new Or(op1, op2);
	}
}
