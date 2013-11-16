package incr.golog.syntax;

import incr.golog.AbstractEntity;
import incr.subst.Substitutions;
import incr.term.Term;

public class If extends AbstractProgram {
	private Term condition;
	private AbstractProgram thenBranch;
	private AbstractProgram elseBranch;
	
	public If(Term condition, AbstractProgram thenBranch, AbstractProgram elseBranch) {
		if(condition == null || thenBranch == null || elseBranch == null)
			throw new NullPointerException();
		this.condition = condition;
		this.thenBranch = thenBranch;
		this.elseBranch = elseBranch;
	}

	public Term getCondition() {
		return condition;
	}

	public AbstractProgram getThenBranch() {
		return thenBranch;
	}

	public AbstractProgram getElseBranch() {
		return elseBranch;
	}

	@Override
	public boolean isGround() {
		return condition.isGround() &&
				thenBranch.isGround() && elseBranch.isGround();
	}
	
	@Override
	public AbstractProgram substitute(Substitutions ss) {
		return new If(condition.substitute(ss),
				thenBranch.substitute(ss), elseBranch.substitute(ss));
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
	
	@Override
	public boolean equals(AbstractEntity e) {
		return e != null && e instanceof If && equals((If)e);
	}
	
	public boolean equals(If i) {
		return i != null && getCondition().equals(i.getCondition()) &&
				getThenBranch().equals(i.getThenBranch()) &&
				getElseBranch().equals(i.getElseBranch());
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode() + getCondition().hashCode()
				+ getThenBranch().hashCode() + getElseBranch().hashCode();
	}
}
