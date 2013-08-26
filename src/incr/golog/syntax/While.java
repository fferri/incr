package incr.golog.syntax;

import incr.formula.AbstractTerm;

public class While extends AbstractProgram {
	private AbstractTerm condition;
	private AbstractProgram body;
	
	public While(AbstractTerm condition, AbstractProgram body) {
		if(condition == null || body == null)
			throw new NullPointerException();
		this.condition = condition;
		this.body = body;
	}

	public AbstractTerm getCondition() {
		return condition;
	}

	public AbstractProgram getBody() {
		return body;
	}
	
	@Override
	public String toString(int i) {
		return indent(i) + "while " + condition + "\n"
				+ indent(i) + "do\n"
				+ body.toString(i + 1)
				+ indent(i) + "endWhile\n";	}
}
