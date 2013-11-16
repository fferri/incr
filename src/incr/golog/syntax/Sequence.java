package incr.golog.syntax;

import incr.golog.AbstractEntity;
import incr.subst.Substitutions;

public class Sequence extends AbstractProgram {
	private AbstractProgram p1;
	private AbstractProgram p2;
	
	public Sequence(AbstractProgram p1, AbstractProgram p2) {
		if(p1 == null || p2 == null)
			throw new NullPointerException();
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public Sequence(AbstractProgram...ps) {
		this(0, ps);
	}
	
	protected Sequence(int skip, AbstractProgram[] ps) {
		this(ps[skip],
				(ps.length - skip) > 2
					? new Sequence(skip + 1, ps)
					: ps[skip + 1]);
	}
	
	public AbstractProgram getP1() {
		return p1;
	}
	
	public AbstractProgram getP2() {
		return p2;
	}
	
	@Override
	public boolean isGround() {
		return p1.isGround() && p2.isGround();
	}
	
	@Override
	public AbstractProgram substitute(Substitutions ss) {
		return new Sequence(p1.substitute(ss), p2.substitute(ss));
	}
	
	@Override
	public String toString(int i) {
		return p1.toString(i) + p2.toString(i);
	}
	
	@Override
	public boolean equals(AbstractEntity e) {
		return e != null && e instanceof Sequence && equals((Sequence)e);
	}
	
	public boolean equals(Sequence s) {
		return s != null && getP1().equals(s.getP1()) && getP2().equals(s.getP2());
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode() + getP1().hashCode() + getP2().hashCode();
	}
}
