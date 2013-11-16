package incr.subst;

import incr.term.Term;
import incr.term.Variable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import util.MultiHashMap;

public class Substitutions implements Iterable<Substitution> {
	private final MultiHashMap<Variable, Substitution> substsByVar = new MultiHashMap<>();
	private final Set<Substitution> substs = new HashSet<>();
	
	public Substitutions() {
	}
	
	public Substitutions(Collection<Substitution> substs) {
		if(substs == null)
			throw new NullPointerException();
		for(Substitution subst : substs)
			add(subst);
	}
	
	@Override
	public String toString() {
		return substs.toString();
	}
	
	@Override
	public Iterator<Substitution> iterator() {
		return substs.iterator();
	}
	
	public void add(Substitution subst) {
		if(subst == null)
			throw new NullPointerException();
		
		// skip tautological substitution
		Term t = subst.getSubstitution();
		if(t instanceof Variable &&
				((Variable)t).getName().equals(subst.getVariable().getName()))
			return;
		
		substsByVar.add(subst.getVariable(), subst);
		substs.add(subst);
	}
	
	public Substitutions cloneAndRemove(Variable v) {
		Substitutions ss = new Substitutions();
		for(Substitution s : substs) {
			if(s.getVariable().equals(v)) continue;
			ss.add(s);
		}
		return ss;
	}
}
