package incr.golog.syntax;

import java.util.Arrays;
import java.util.List;

import incr.State;
import incr.golog.AbstractEntity;
import incr.golog.ProgramState;
import incr.subst.Substitutions;
import incr.term.Term;

public class While extends AbstractProgram {
	private Term condition;
	private AbstractProgram body;
	
	public While(Term condition, AbstractProgram body) {
		if(condition == null || body == null)
			throw new NullPointerException();
		this.condition = condition;
		this.body = body;
	}

	public Term getCondition() {
		return condition;
	}

	public AbstractProgram getBody() {
		return body;
	}
	
	@Override
	public boolean isGround() {
		return condition.isGround() && body.isGround();
	}
	
	@Override
	public AbstractProgram substitute(Substitutions ss) {
		return new While(condition.substitute(ss), body.substitute(ss));
	}
	
	@Override
	public String toString(int i) {
		return indent(i) + "while " + condition + "\n"
				+ indent(i) + "do\n"
				+ body.toString(i + 1)
				+ indent(i) + "endWhile\n";
	}
	
	@Override
	public boolean equals(AbstractEntity e) {
		return e != null && e instanceof While && equals((While)e);
	}
	
	public boolean equals(While w) {
		return w != null && getCondition().equals(w.getCondition()) && getBody().equals(w.getBody());
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode() + getCondition().hashCode() + getBody().hashCode();
	}
	
	@Override
	public List<ProgramState> trans(ProgramState s) {
		State state = s.getState();
		return Arrays.asList(
			new ProgramState(s, getCondition().truthValue(state)
					? new Sequence(getBody(), this)
					: new Empty(),
				state)
		);
	}
	
	@Override
	public boolean isFinal(ProgramState s) {
		return false;
	}
}
