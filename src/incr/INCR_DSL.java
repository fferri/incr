/**
 * Copyright 2013 by Federico Ferri
 * All Rights Reserved
 */
package incr;

import incr.golog.*;
import incr.golog.syntax.AbstractProgram;
import static incr.golog.DSL.*;

public class INCR_DSL {
	static Environment env = new Environment();
	
	static State state = new State(
		_("on", "b", "table"),
		_("clear", "b"),
		_("clear", "c"),
		_("on", "c", "d"),
		_("on", "d", "a"),
		_("on", "a", "table"),
		_("clear", "table")
	);
	
	static {
		env.addIndividual(_("a"));
		env.addIndividual(_("b"));
		env.addIndividual(_("c"));
		env.addIndividual(_("d"));
		env.addIndividual(_("table"));

		env.addAction(ACTION(_("goal"),
				AND(
					_("clear", "a"),
					_("on", "a", "b"),
					_("on", "b", "c"),
					_("on", "c", "d"),
					_("on", "d", "table")
				)
			)
		);
		env.addAction(ACTION(_("move", "Obj", "From", "To"),
				AND(
					NOT(EQ("From", "To")),
					_("on", "Obj", "From"),
					_("clear", "Obj"),
					_("clear", "To")
				),
				ADD(
					_("on", "Obj", "To"),
					_("clear", "From")
				),
				DEL(
					_("on", "Obj", "From"),
					_("clear", "To")
				)
			)
		);
	}
	
	// define the control program
	static AbstractProgram program = SEQ(
		// nondeterministically unstack all blocks:
		STAR(
			PI("A", PI("B",
					EXEC("move", "A", "B", "table")
			))
		),
		// build the tower in the correct order:
		EXEC("move", "c", "table", "d"),
		EXEC("move", "b", "table", "c"),
		EXEC("move", "a", "table", "b"),
		// goal is an action consisting only of preconditions, saying
		// that the goal must hold:
		EXEC("goal")
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
