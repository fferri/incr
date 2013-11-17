/**
 * Copyright 2013 by Federico Ferri
 * All Rights Reserved
 */
package incr;

import java.io.FileInputStream;
import java.util.Arrays;

import incr.formula.And;
import incr.formula.Not;
import incr.golog.*;
import incr.golog.syntax.AbstractProgram;
import incr.golog.syntax.Atomic;
import incr.golog.syntax.Pi;
import incr.golog.syntax.Sequence;
import incr.golog.syntax.Star;
import incr.golog.syntax.parser.GologParser;
import incr.subst.Unify;
import incr.term.Functional;
import incr.term.Term;
import incr.term.Variable;

public class INCR_NoParser {
	static Environment env = new Environment();
	
	// constants shortcuts
	static final Functional a = new Functional("a");
	static final Functional b = new Functional("b");
	static final Functional c = new Functional("c");
	static final Functional d = new Functional("d");
	static final Functional table = new Functional("table");

	// functional terms shortcuts
	static Functional on(Term arg1, Term arg2) {return new Functional("on", arg1, arg2);}
	static Functional clear(Term arg1) {return new Functional("clear", arg1);}
	static Functional equals(Term arg1, Term arg2) {return new Functional("equals", arg1, arg2);}

	// variables shortcuts
	static Variable Obj = new Variable("Obj");
	static Variable From = new Variable("From");
	static Variable To = new Variable("To");
	static Variable A = new Variable("A");
	static Variable B = new Variable("B");

	// actions
	static final Functional goal = new Functional("goal");
	static Functional move(Term obj, Term from, Term to) {return new Functional("move", obj, from, to);}
	static final Action goalAction = new Action(goal,
				new And(clear(a), on(a,b), on(b,c), on(c,d), on(d,table)));
	static final Action moveAction = new Action(move(Obj, From, To),
			new And(new Not(equals(From, To)), on(Obj, From), clear(Obj), clear(To)),
			Arrays.asList(on(Obj, To), clear(From)),
			Arrays.asList(on(Obj, From), clear(To)));
	
	// initial state
	static State state = new State(on(b, table), clear(b), clear(c), on(c, d), on(d, a), on(a, table), clear(table));
	
	static {
		env.addIndividual(a);
		env.addIndividual(b);
		env.addIndividual(c);
		env.addIndividual(d);
		env.addIndividual(table);

		env.addAction(goalAction);
		env.addAction(moveAction);
	}
	
	// define the control program
	static AbstractProgram program = new Sequence(
		// nondeterministically unstack all blocks:
		new Star(
			new Pi(A, new Pi(B, new Atomic(move(A, B, table))))
		),
		// build the tower in the correct order:
		new Atomic(move(c, table, d)),
		new Atomic(move(b, table, c)),
		new Atomic(move(a, table, b)),
		// goal is an action consisting only of preconditions, saying
		// that the goal must hold:
		new Atomic(goal)
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
