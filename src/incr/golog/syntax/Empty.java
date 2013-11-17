package incr.golog.syntax;

import java.util.List;

import incr.golog.AbstractEntity;
import incr.golog.ProgramState;
import incr.subst.Substitutions;

public final class Empty extends AbstractProgram {
	@Override
	public String toString(int i) {
		return indent(i) + "nil\n";
	}
	
	@Override
	public boolean isGround() {
		return true;
	}
	
	@Override
	public AbstractProgram substitute(Substitutions ss) {
		return this;
	}
	
	@Override
	public boolean equals(AbstractEntity e) {
		return e != null && e instanceof Empty && equals((Empty)e);
	}
	
	public boolean equals(Empty e) {
		return e != null;
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
	
	@Override
	public List<ProgramState> trans(ProgramState s) {
		throw new UnsupportedOperationException("Cannot step an empty program");
	}
	
	@Override
	public boolean isFinal(ProgramState s) {
		return true;
	}
}
