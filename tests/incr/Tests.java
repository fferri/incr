package incr;

import static org.junit.Assert.*;

import java.util.Arrays;

import incr.golog.Environment;
import incr.golog.ProgramState;
import incr.golog.syntax.*;
import incr.strips.STRIPSState;
import incr.subst.*;
import incr.term.*;
import static incr.golog.builder.Golog.*;

public class Tests {
	/*
	 * Test utilities
	 */

	private void assertNoException(Runnable r) {
		try {
			r.run();
		} catch(Exception e) {
			fail("assertSucceeds: failed");
		}
	}

	private void assertException(Runnable r) {
		assertException(r, null);
	}
	
	private void assertException(Runnable r, Class<?> exceptionType) {
		try {
			r.run();
			fail("assertFails: testcase didn't fail");
		} catch(Exception e) {
			if(exceptionType != null && !e.getClass().equals(exceptionType))
				fail("assertFails: failed, but with a different exception: " + e);
		}
	}
	
	private void testUnify(Term a, Term b) {
		Substitutions s = Unify.unify(a, b);
		assertNotNull(s);
		//System.out.printf("'%s' unifies with '%s' using subst %s\n", a, b, s);
	}

	/*
	 * some objects used in tests:
	 */
	private static final Functional a = Atom("a"), b = Atom("b"), c = Atom("c");

	private static final Variable X = Var("X"), Y = Var("Y"), Z = Var("Z");
	
	private static Functional f(Object...args) {return Func("f", args);}
	private static Functional g(Object...args) {return Func("g", args);}
	
	private static final Environment env = new Environment(); static {
		env.setIndividuals(a, b, c);
	}
	
	private static final State state = new STRIPSState(f(a), f(b), g(c));

	@org.junit.Test
	public void testUtilities() {
		assertNoException(new Runnable() {
			public void run() {}
		});
		assertException(new Runnable() {
			public void run() {throw new RuntimeException();}
		});
		assertException(new Runnable() {
			public void run() {throw new IllegalArgumentException();}
		}, IllegalArgumentException.class);
	}

	@org.junit.Test
	public void testVariableIdentifiers() throws Exception {
		assertException(new Runnable() {
			public void run() {Var("x");}
		}, IllegalArgumentException.class);
		assertNoException(new Runnable() {
			public void run() {Var("X");}
		});
	}
	
	@org.junit.Test
	public void testConstantIdentifiers() throws Exception {
		assertException(new Runnable() {
			public void run() {Atom("X");}
		}, IllegalArgumentException.class);
		assertNoException(new Runnable() {
			public void run() {Atom("x");}
		});
	}
	
	@org.junit.Test
	public void testGroundChecking() throws Exception {
		assertFalse(Var("X").isGround());
		assertTrue(Func("y", "a").isGround());
		assertFalse(Func("y", "X").isGround());
		assertFalse(And(Atom("x"), Var("Y")).isGround());
		assertTrue(And(Atom("x"), Atom("y")).isGround());
	}
	
	@org.junit.Test
	public void testUnify() throws Exception {
		Functional a = Atom("a"), b = Atom("b"), c = Atom("c");
		Variable X = Var("X"), Y = Var("Y");
		testUnify(And(a, X), And(a, b));
		testUnify(And(a, X), Y);
		testUnify(And(a, Or(b, X)), And(Y, Or(b, c)));
	}
	
	@org.junit.Test
	public void testSubstitution() throws Exception {
		Substitution s = new Substitution(c, X);
		Substitutions ss = new Substitutions(Arrays.asList(s));

		assertFalse(
			Pi(X, Exec(f(X))).substitute(ss)
			.equals(
				Pi(X, Exec(f(c)))
			)
		);
		
		assertTrue(
			NDet(Exec(f(b)), Exec(f(X))).substitute(ss)
			.equals(
				NDet(Exec(f(b)), Exec(f(c)))
			)
		);
	}
	
	@org.junit.Test
	public void testSemanticsIf() {
		AbstractProgram p = If(f(a), Exec(a), Exec(b));
		ProgramState ps = new ProgramState(env, p, state);
		ps = p.trans(ps).get(0);
		assertEquals(ps.getProgram(), Exec(a));
	}
	
	@org.junit.Test
	public void testSemanticsSeq() {
		AbstractProgram p = Seq(If(f(a), Exec(a), Exec(b)), Exec(c));
		ProgramState ps = new ProgramState(env, p, state);
		ps = p.trans(ps).get(0);
		p = ps.getProgram();
		ps = p.trans(ps).get(0);
		p = ps.getProgram();
		System.out.println(""+p);
	}
}
