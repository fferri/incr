package incr.golog.syntax;

import incr.formula.AbstractTerm;

public class If extends AbstractProgram {
	private AbstractTerm condition;
	private AbstractProgram thenBranch;
	private AbstractProgram elseBranch;
	
	public If(AbstractTerm condition, AbstractProgram thenBranch, AbstractProgram elseBranch) {
		if(condition == null || thenBranch == null || elseBranch == null)
			throw new NullPointerException();
		this.condition = condition;
		this.thenBranch = thenBranch;
		this.elseBranch = elseBranch;
	}

	public AbstractTerm getCondition() {
		return condition;
	}

	public AbstractProgram getThenBranch() {
		return thenBranch;
	}

	public AbstractProgram getElseBranch() {
		return elseBranch;
	}

	@Override
	public String toString(int i) {
		return indent(i) + "if " + condition + "\n"
				+ indent(i) + "then\n"
				+ thenBranch.toString(i + 1)
				+ indent(i) + "else\n"
				+ elseBranch.toString(i + 1)
				+ indent(i) + "endIf\n";
	}
}
