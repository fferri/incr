/**
 * Copyright 2013 by Federico Ferri
 * All Rights Reserved
 */
package incr;

import incr.golog.*;
import incr.golog.syntax.AbstractProgram;
import incr.term.Functional;
import incr.term.Variable;
import static incr.golog.builder.Golog.*;

public class INCR_DSL {
	static Environment env = new Environment();
	
	// atoms:
	static final Functional a = Atom("a"), b = Atom("b"),
			c = Atom("c"), d = Atom("d"), table = Atom("table");
	
	// functional shorthands:
	static Functional goal() {return Func("goal");}
	static Functional move(Object obj, Object from, Object to) {return Func("move", obj, from, to);}
	static Functional on(Object a, Object b) {return Func("on", a, b);}
	static Functional clear(Object obj) {return Func("clear", obj);}
	
	// variable shorthands:
	static Variable Obj = Var("Obj"), From = Var("From"), To = Var("To"), A = Var("A"), B = Var("B");
	
	static State state = new State(on(b, table), clear(b), clear(c),
		on(c, d), on(d, a), on(a, table), clear(table));
	
	static {
		env.setIndividuals(a, b, c, d, table);

		env.add(Action(goal(),
				And(clear(a), on(a, b), on(b, c), on(c, d), on(d, table))));

		env.add(Action(move(Obj, From, To),
				And(NEq(From, To), on(Obj, From), clear(Obj), clear(To)),
				on(Obj, To), clear(From), Not(on(Obj, From)), Not(clear(To))));
	}
	
	// define the control program
	static AbstractProgram program = Seq(
		// nondeterministically unstack all blocks:
		Star(Pi(A, Pi(B, Exec(move(A, B, table))))),
		// build the tower in the correct order:
		Exec(move(c, table, d)),
		Exec(move(b, table, c)),
		Exec(move(a, table, b)),
		// goal is an action consisting only of preconditions, saying
		// that the goal must hold:
		Exec(goal())
	);

	public static void main(String[] args) throws Exception {
		// executes the program and print first 10 solutions
		ProgramState ps0 = new ProgramState(env, program, state);
		ProgramState ps = ps0;
		ProgramState.SolutionIterator i = ps0.solutionIterator();
		int n = 0;
		while(i.hasNext()) {
			ps = i.next();
			System.out.println("NEXT SOLUTION: " + ps.getState().getActionHistory());
			if(n++ > 10) break;
		}
	}
}
