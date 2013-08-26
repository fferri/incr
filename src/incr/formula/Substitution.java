package incr.formula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Substitution {
	private Map<String, String> subst;
	
	public Substitution() {
		subst = new HashMap<>();
	}

	public Substitution(String...kvpairs) {
		if(kvpairs == null)
			throw new NullPointerException();
		if((kvpairs.length % 2) != 0)
			throw new IllegalArgumentException();
		subst = new HashMap<>();
		for(int i = 0; i < kvpairs.length; i += 2) {
			subst.put(kvpairs[i], kvpairs[i + 1]);
		}
	}
	
	public String put(String k, String v) {
		return subst.put(k, v);
	}
	
	public String remove(String k) {
		return subst.remove(k);
	}
	
	public boolean containsKey(String k) {
		return subst.containsKey(k);
	}
	
	public Predicate apply(Predicate p) {
		List<String> newArgs = new ArrayList<>(p.getArity());
		for(String s : p.getArgs())
			newArgs.add(subst.containsKey(s) ? subst.get(s) : s);
		return new Predicate(p.getFunctor(), newArgs);
	}
	
	public Not apply(Not t) {
		return new Not(apply(t.getT1()));
	}
	
	public And apply(And t) {
		return new And(apply(t.getT1()), apply(t.getT2()));
	}
	
	public Or apply(Or t) {
		return new Or(apply(t.getT1()), apply(t.getT2()));
	}
	
	public AbstractTerm apply(AbstractTerm t) {
		if(t == null)
			throw new NullPointerException();
		
		if(t instanceof Predicate)
			return apply((Predicate)t);
		else if(t instanceof Not)
			return apply((Not)t);
		else if(t instanceof And)
			return apply((And)t);
		else if(t instanceof Or)
			return apply((Or)t);
		else
			throw new IllegalArgumentException();
	}
	
	public List<Predicate> apply(List<Predicate> l) {
		List<Predicate> ret = new ArrayList<>(l.size());
		for(Predicate p : l)
			ret.add(apply(p));
		return ret;
	}
}
