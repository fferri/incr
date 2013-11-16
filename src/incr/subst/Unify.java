package incr.subst;

import incr.term.Functional;
import incr.term.Term;
import incr.term.Variable;

public class Unify {
	public static Substitutions unify(Term a, Term b) {
		Substitutions ret = new Substitutions();
		return unify(a, b, ret);
	}
	
	private static Substitutions unify(Term a, Term b, Substitutions s) {
		if(a instanceof Functional && b instanceof Functional) {
			Functional fa = (Functional)a, fb = (Functional)b;
			if(!fa.getFunctor().equals(fb.getFunctor())) return null;
			if(fa.getArity() != fb.getArity()) return null;
			for(int i = 0; i < fa.getArity(); i++) {
				if(unify(fa.getArg(i), fb.getArg(i), s) == null)
					return null;
			}
			return s;
		}
		if(a instanceof Variable) {
			Variable va = (Variable)a;
			s.add(new Substitution(b, va));
			return s;
		}
		if(b instanceof Variable) {
			Variable vb = (Variable)b;
			s.add(new Substitution(a, vb));
			return s;
		}
		return null;
	}
}
