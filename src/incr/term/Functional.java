package incr.term;

import incr.State;
import incr.subst.Substitution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Functional extends Term {
	private String functor;
	private List<Term> args;

	public Functional(final String functor) {
		if(functor == null)
			throw new NullPointerException();
		this.functor = functor;
		this.args = new ArrayList<>();
		if(!functor.matches("^[a-z][a-zA-Z0-9_]*"))
			throw new IllegalArgumentException("constants and functions identifiers must start with a lowercase letter");
	}
	
	public Functional(final String functor, final Term...terms) {
		this(functor, Arrays.asList(terms));
	}
	
	public Functional(final String functor, final List<Term> args) {
		this(functor);
		if(args == null)
			throw new NullPointerException();
		for(Term arg : args) {
			if(arg == null)
				throw new NullPointerException();
			this.args.add(arg);
		}
	}
	
	public String getFunctor() {
		return functor;
	}
	
	public String getSignature() {
		return String.format("%s/%d", getFunctor(), getArity());
	}
	
	public List<Term> getArgs() {
		return Collections.unmodifiableList(args);
	}
	
	public int getArity() {
		return getArgs().size();
	}
	
	public Term getArg(int i) {
		return getArgs().get(i);
	}
	
	public int getOrder() {
		if(getArity() == 0) return 0;
		
		int maxOrder = 0;
		for(Term arg : getArgs()) {
			if(arg instanceof Functional) {
				int o = ((Functional)arg).getOrder();
				if(o > maxOrder) maxOrder = o;
			}
		}
		return 1 + maxOrder;
	}
	
	@Override
	public boolean isGround() {
		for(Term arg : getArgs())
			if(arg instanceof Variable)
				return false;
		return true;
	}
	
	@Override
	public Term substitute(Substitution s) {
		boolean changed = false;
		List<Term> newArgs = new ArrayList<>(getArity());
		for(Term arg : getArgs()) {
			Term newArg = arg.substitute(s);
			newArgs.add(newArg);
			if(arg != newArg) changed = true;
		}
		return changed ? new Functional(getFunctor(), newArgs) : this;
	}
	
	@Override
	public boolean equals(Term t) {
		return t instanceof Functional && equals((Functional)t);
	}
	
	public boolean equals(Functional p) {
		return p != null
				&& getFunctor().equals(p.getFunctor())
				&& getArgs().equals(p.getArgs());
	}
	
	@Override
	public boolean truthValue(State s) {
		if(getSignature().equals("true/0")) return true;
		if(getSignature().equals("false/0")) return true;
		if(getSignature().equals("equals/2")) return getArg(0).equals(getArg(1));
		return s.contains(this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getFunctor());
		if(getArity() > 0) {
			sb.append("(");
			String j = "";
			for(Term arg : getArgs()) {
				sb.append(j);
				sb.append(arg.toString());
				j = ", ";
			}
			sb.append(")");
		}
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode() + getFunctor().hashCode() +
				getArgs().hashCode();
	}
}
