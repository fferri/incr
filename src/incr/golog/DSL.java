package incr.golog;

import java.util.Arrays;
import java.util.List;

import incr.Action;
import incr.formula.*;
import incr.golog.syntax.*;
import incr.term.*;

public final class DSL {
	public static And AND(final Term...terms) {
		return new And(terms);
	}
	
	public static Or OR(final Term...terms) {
		return new Or(terms);
	}
	
	public static Not NOT(final Term term) {
		return new Not(term);
	}
	
	public static final Term TRUE = new Functional("true");
	public static final Term FALSE = new Functional("false");
	
	public static final Term EQ(final Term a, final Term b) {
		return new Functional("equals", a, b);
	}
	
	public static final Term EQ(final String a, final String b) {
		return _("equals", a, b);
	}
	
	public static Functional ATOM(final String name) {
		return new Functional(name);
	}
	
	public static Functional FUNC(final String f, final Term...terms) {
		return new Functional(f, terms);
	}
	
	public static Functional _(final String f, final String...args) {
		Term[] terms = new Term[args.length];
		for(int i = 0; i < args.length; i++) {
			String s0 = args[i].substring(0, 1);
			if(s0.toLowerCase().equals(s0))
				terms[i] = new Functional(s0);
			else if(s0.toUpperCase().equals(s0))
				terms[i] = new Variable(s0);
			else throw new IllegalArgumentException();
		}
		return new Functional(f, terms);
	}
	
	public static Variable $(final String name) {
		return new Variable(name);
	}
	
	public static Proc PROC(final Functional head, final AbstractProgram body) {
		return new Proc(head, body);
	}
	
	public static Atomic EXEC(final Functional f) {
		return new Atomic(f);
	}
	
	public static Atomic EXEC(final String arg0, final String...args) {
		return new Atomic(_(arg0, args));
	}
	
	public static Empty EMPTY() {
		return new Empty();
	}
	
	public static If IF(final Term condition, final AbstractProgram thenBranch, final AbstractProgram elseBranch) {
		return new If(condition, thenBranch, elseBranch);
	}
	
	public static NDet NDET(final AbstractProgram...ps) {
		return new NDet(ps);
	}
	
	public static Pi PI(final Variable var, final AbstractProgram p1) {
		return new Pi(var, p1);
	}
	
	public static Pi PI(final String var, final AbstractProgram p1) {
		return new Pi(new Variable(var), p1);
	}
	
	public static Sequence SEQ(final AbstractProgram...ps) {
		return new Sequence(ps);
	}
	
	public static Star STAR(final AbstractProgram p) {
		return new Star(p);
	}
	
	public static Test TEST(final Term formula) {
		return new Test(formula);
	}
	
	public static While WHILE(final Term condition, final AbstractProgram body) {
		return new While(condition, body);
	}
	
	public static List<Term> ADD(final Term...terms) {
		return Arrays.asList(terms);
	}
	
	public static List<Term> DEL(final Term...terms) {
		return Arrays.asList(terms);
	}

	public static Action ACTION(final Functional head, final Term precond) {
		return new Action(head, precond);
	}
	
	public static Action ACTION(final Functional head, final Term precond, final List<Term> add, final List<Term> del) {
		return new Action(head, precond, add, del);
	}
}
