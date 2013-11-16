package incr.golog.syntax;

import incr.golog.AbstractEntity;
import incr.subst.Substitutions;

public class Star extends AbstractProgram {
	private AbstractProgram p1;
	
	public Star(AbstractProgram p1) {
		if(p1 == null)
			throw new NullPointerException();
		this.p1 = p1;
	}
	
	public AbstractProgram getP1() {
		return p1;
	}

	@Override
	public boolean isGround() {
		return p1.isGround();
	}
	
	@Override
	public AbstractProgram substitute(Substitutions ss) {
		return new Star(p1.substitute(ss));
	}
	
	@Override
	public String toString(int i) {
		return indent(i) + "star(\n" +
				p1.toString(i + 1) +
				indent(i) + ")\n";
	}
	
	@Override
	public boolean equals(AbstractEntity e) {
		return e != null && e instanceof Star && equals((Star)e);
	}
	
	public boolean equals(Star s) {
		return s != null && getP1().equals(s.getP1());
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode() + getP1().hashCode();
	}
}
