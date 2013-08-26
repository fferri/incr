package incr.formula;

import incr.State;

public class Not extends AbstractTerm {
	private AbstractTerm t1;
	
	public Not(AbstractTerm t1) {
		if(t1 == null)
			throw new NullPointerException();
		this.t1 = t1;
	}
	
	public AbstractTerm getT1() {
		return t1;
	}
	
	@Override
	public boolean isGround() {
		return t1.isGround();
	}
	
	@Override
	public String toString() {
		return String.format("!%s", t1);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof Not && equals((Not)obj);
	}
	
	public boolean equals(Not n) {
		return n != null && n.getT1().equals(getT1());
	}
	
	@Override
	public int hashCode() {
		return 101 + 13 * t1.hashCode();
	}
	
	@Override
	public boolean truthValue(State s) {
		return !t1.truthValue(s);
	}
}
