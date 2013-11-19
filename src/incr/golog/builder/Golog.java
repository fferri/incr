package incr.golog.builder;

import incr.*;
import incr.golog.*;
import incr.golog.syntax.*;
import incr.formula.*;
import incr.term.*;

public class Golog {
	public static If If(Object condition, AbstractProgram thenBranch, AbstractProgram elseBranch) {
		return new If(Term(condition), thenBranch, elseBranch);
	}
	
	public static While While(Object condition, AbstractProgram body) {
		return new While(Term(condition), body);
	}
	
	public static NDet NDet(AbstractProgram...ps) {
		return new NDet(ps);
	}
	
	public static Star Star(AbstractProgram p) {
		return new Star(p);
	}
	
	public static Atomic Exec(Object o) {
		return new Atomic((Functional)Term(o));
	}
	
	public static Test Test(Object o) {
		return new Test(Term(o));
	}
	
	public static Pi Pi(Object variable, AbstractProgram p1) {
		return new Pi((Variable)Term(variable), p1);
	}
	
	public static Sequence Seq(AbstractProgram...ps) {
		return new Sequence(ps);
	}
	
	public static Functional Atom(String v) {
		return new Functional(v);
	}
	
	public static Variable Var(String v) {
		return new Variable(v);
	}
	
	public static Term Term(Object o) {
		if(o instanceof String) {
			String v = (String)o, v0 = v.substring(0, 1);
			if(v0.toLowerCase().equals(v0)) return Atom(v);
			else return Var(v);
		} else if(o instanceof Functional || o instanceof Variable) {
			return (Term)o;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public static Term[] Terms(Object...args) {
		Term terms[] = new Term[args.length];
		for(int i = 0; i < args.length; i++)
			terms[i] = Term(args[i]);
		return terms;
	}
	
	public static Functional Func(String v, Object...args) {
		return new Functional(v, Terms(args));
	}
	
	public static And And(Object...args) {
		return new And(Terms(args));
	}
	
	public static Or Or(Object...args) {
		return new Or(Terms(args));
	}
	
	public static Not Not(Object o) {
		return new Not(Term(o));
	}
	
	public static Functional Eq(Object o1, Object o2) {
		return Func("equals", o1, o2);
	}
	
	public static Functional NEq(Object o1, Object o2) {
		return Not(Eq(o1, o2));
	}
	
	public static final Functional TRUE = Atom("true");
	public static final Functional FALSE = Atom("false");
	
	public static Proc Proc(Functional head, AbstractProgram body) {
		return new Proc(head, body);
	}
	
	public static Action Action(Object head, Object precond, Object...addDelTerms) {
		return new Action((Functional)Term(head), Term(precond), Terms(addDelTerms));
	}
}
