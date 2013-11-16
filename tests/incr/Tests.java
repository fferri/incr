package incr;

import static org.junit.Assert.*;

import java.util.Arrays;

import incr.formula.And;
import incr.formula.Or;
import incr.golog.syntax.AbstractProgram;
import incr.golog.syntax.Atomic;
import incr.golog.syntax.Empty;
import incr.golog.syntax.If;
import incr.golog.syntax.NDet;
import incr.golog.syntax.Pi;
import incr.subst.Substitution;
import incr.subst.Substitutions;
import incr.subst.Unify;
import incr.term.Functional;
import incr.term.Term;
import incr.term.Variable;

public class Tests {
	@org.junit.Test
	public void testVariableIdentifiers() throws Exception {
		try {
			new Variable("x");
			fail();
		} catch(IllegalArgumentException e) {
		}
		try {
			new Variable("X");
		} catch(IllegalArgumentException e) {
			fail();
		}
	}
	
	@org.junit.Test
	public void testConstantIdentifiers() throws Exception {
		try {
			new Functional("X");
			fail();
		} catch(IllegalArgumentException e) {
		}
		try {
			new Functional("x");
		} catch(IllegalArgumentException e) {
			fail();
		}
	}
	
	@org.junit.Test
	public void testGroundChecking() throws Exception {
		Variable x = new Variable("X");
		assertFalse(x.isGround());
		
		Functional y = new Functional("y");
		assertTrue(y.isGround());
		
		Term t = new And(x, y);
		assertFalse(t.isGround());
	}
	
	@org.junit.Test
	public void testUnify() throws Exception {
		Functional a = new Functional("a"), b = new Functional("b"),
				c = new Functional("c"), d = new Functional("d");
		Variable x = new Variable("X"), y = new Variable("Y");
		testUnify(new And(a, x), new And(a, b));
		testUnify(new And(a, x), y);
		testUnify(new And(a, new Or(b, x)), new And(y, new Or(b, c)));
	}
	
	private void testUnify(Term a, Term b) {
		System.out.println("a=" + a);
		System.out.println("b=" + b);
		Substitutions s = Unify.unify(a, b);
		System.out.println("unify=" + s);
	}
	
	@org.junit.Test
	public void testSubstitution() throws Exception {
		Variable x = new Variable("X");
		Functional y = new Functional("y");
		//Variable z = new Variable("Z");
		Pi pi = new Pi(x, new Atomic(new Functional("a", Arrays.asList((Term)x))));
		Atomic b = new Atomic(new Functional("b"));
		AbstractProgram p1 = new NDet(b, new If(new And(x, y), pi, new Empty()));
		AbstractProgram p2 = new NDet(b, new If(new And(y, y), pi, new Empty()));
		Substitution s = new Substitution(y, x);
		Substitutions ss = new Substitutions(Arrays.asList(s));
		p1 = p1.substitute(ss);
		assertTrue(p1.equals(p2));
	}
}
