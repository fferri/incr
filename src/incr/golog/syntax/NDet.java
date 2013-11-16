package incr.golog.syntax;

import incr.golog.AbstractEntity;
import incr.subst.Substitutions;

public class NDet extends AbstractProgram {
	private AbstractProgram p1;
	private AbstractProgram p2;
	
	public NDet(AbstractProgram p1, AbstractProgram p2) {
		if(p1 == null || p2 == null)
			throw new NullPointerException();
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public NDet(AbstractProgram...ps) {
		this(0, ps);
	}
	
	protected NDet(int skip, AbstractProgram[] ps) {
		this(ps[skip],
				(ps.length - skip) > 2
					? new NDet(skip + 1, ps)
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
		return new NDet(p1.substitute(ss), p2.substitute(ss));
	}
	
	@Override
	public String toString(int i) {
		return indent(i) + "ndet(\n" +
				p1.toString(i + 1)
				+ indent(i + 1) + "|\n"
				+ p2.toString(i + 1) + 
				indent(i) + ")\n";
	}
	
	@Override
	public boolean equals(AbstractEntity e) {
		return e != null && e instanceof NDet && equals((NDet)e);
	}
	
	public boolean equals(NDet n) {
		return n != null && getP1().equals(n.getP1()) && getP2().equals(n.getP2());
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode() + getP1().hashCode() + getP2().hashCode();
	}
}
