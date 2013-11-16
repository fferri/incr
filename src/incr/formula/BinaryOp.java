package incr.formula;

import java.util.Arrays;

import incr.term.Functional;
import incr.term.Term;

public abstract class BinaryOp extends Functional {
	public BinaryOp(String operator, Term operand1, Term operand2) {
		super(operator, Arrays.asList(operand1, operand2));
	}
	
	public String getOperator() {
		return getFunctor();
	}
	
	public Term getOperand1() {
		return getArgs().get(0);
	}
	
	public Term getOperand2() {
		return getArgs().get(1);
	}
}
