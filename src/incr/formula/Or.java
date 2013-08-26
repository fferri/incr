package incr.formula;

import java.util.Arrays;
import java.util.List;

import incr.State;

public class Or extends AbstractTerm {
	private AbstractTerm t1;
	private AbstractTerm t2;
	
	public Or(AbstractTerm t1, AbstractTerm t2) {
		if(t1 == null || t2 == null)
			throw new NullPointerException();
		this.t1 = t1;
		this.t2 = t2;
	}
	
	public AbstractTerm getT1() {
		return t1;
	}
	
	public AbstractTerm getT2() {
		return t2;
	}
	
	@Override
	public boolean isGround() {
		return t1.isGround() && t2.isGround();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(AbstractTerm term : new AbstractTerm[]{t1, t2}) {
			sb.append(sb.length() == 0 ? "" : " | ");
			sb.append(term);
		}
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof Or && equals((Or)obj);
	}
	
	public boolean equals(Or n) {
		return n != null && n.getT1().equals(getT1()) && n.getT2().equals(getT2());
	}
	
	@Override
	public int hashCode() {
		return 131 + 19 * t1.hashCode() + 23 * t2.hashCode();
	}
	
	@Override
	public boolean truthValue(State s) {
		return t1.truthValue(s) || t2.truthValue(s);
	}
	
	public static Or OR(AbstractTerm...ts) {
		return OR(Arrays.asList(ts));
	}
	
	public static Or OR(List<AbstractTerm> l) {
		return OR(l, 0);
	}
	
	private static Or OR(List<AbstractTerm> l, int start) {
		int r = l.size() - start;
		if(r < 2)
			throw new IllegalArgumentException();
		if(r == 2)
			return new Or(l.get(start), l.get(start + 1));
		else
			return new Or(l.get(start), OR(l, start + 1));
	}
}
