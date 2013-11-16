package incr.formula;

import java.util.Arrays;

import incr.term.Functional;
import incr.term.Term;

public abstract class UnaryOp extends Functional {
	public UnaryOp(String operator, Term operand1) {
		super(operator, Arrays.asList(operand1));
	}
	
	public String getOperator() {
		return getFunctor();
	}
	
	public Term getOperand1() {
		return getArgs().get(0);
	}
}
