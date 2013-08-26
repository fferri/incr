package incr.formula;

import incr.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Predicate extends AbstractTerm {
	private String functor;
	private List<String> args;
	
	public Predicate(final String functor, final String...args) {
		if(functor == null || args == null)
			throw new NullPointerException();
		this.functor = functor;
		this.args = new ArrayList<>();
		for(String arg : args) {
			if(arg == null)
				throw new NullPointerException();
			this.args.add(arg);
		}
	}
	
	public Predicate(final String functor, final List<String> args) {
		if(functor == null || args == null)
			throw new NullPointerException();
		this.functor = functor;
		this.args = new ArrayList<>();
		for(String arg : args) {
			if(arg == null)
				throw new NullPointerException();
			this.args.add(arg);
		}
	}
	
	public String getFunctor() {
		return functor;
	}
	
	public List<String> getArgs() {
		return Collections.unmodifiableList(args);
	}
	
	public int getArity() {
		return args.size();
	}
	
	@Override
	public boolean isGround() {
		for(String arg : args)
			if(arg.charAt(0) == '?')
				return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(functor);
		if(args.size() > 0) {
			sb.append("(");
			String j = "";
			for(String arg : args) {
				sb.append(j);
				sb.append(arg);
				j = ", ";
			}
			sb.append(")");
		}
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null &&
				obj instanceof Predicate &&
				equals((Predicate)obj);
	}
	
	public boolean equals(Predicate p) {
		return p != null &&
				p.getFunctor().equals(getFunctor()) &&
				p.getArgs().equals(getArgs());
	}
	
	@Override
	public int hashCode() {
		return getFunctor().hashCode() +
				13 * getArgs().hashCode();
	}
	
	@Override
	public boolean truthValue(State s) {
		if(functor.equals("true") && args.isEmpty()) return true;
		if(functor.equals("false") && args.isEmpty()) return false;
		return s.contains(this);
	}
}
