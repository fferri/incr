package incr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import incr.formula.*;
import incr.golog.*;
import incr.golog.syntax.*;

public class INCR {
	public static void main(String[] args) {
		State s = new State(
				new Predicate("on", "a", "b"),
				new Predicate("on", "b", "table"),
				new Predicate("clear", "a"),
				new Predicate("clear", "table")
		);
		Action move = new Action(
				new Predicate("move", "?obj", "?from", "?to"),
				And.AND(
						new Predicate("on", "?obj", "?from"),
						new Predicate("clear", "?obj"),
						new Predicate("clear", "?to")
				),
				Arrays.asList(
						new Predicate("on", "?obj", "?to"),
						new Predicate("clear", "?from")
				),
				Arrays.asList(
						new Predicate("on", "?obj", "?from"),
						new Predicate("clear", "?to")
				)
		);
		System.out.println("Initial state: " + s);
		System.out.println("Action: " + move);
		
		Action move_a_to_table = move.ground(new Substitution(
				"?obj", "a",
				"?from", "b",
				"?to", "table"
		));
		Action move_b_to_c = move.ground(new Substitution(
				"?obj", "b",
				"?from", "table",
				"?to", "c"
		));
		Action foo = new Action(new Predicate("foo"), new Predicate("true"), new ArrayList<Predicate>(), new ArrayList<Predicate>());
		Action bar = new Action(new Predicate("bar"), new Predicate("true"), new ArrayList<Predicate>(), new ArrayList<Predicate>());
		Action foe = new Action(new Predicate("foe"), new Predicate("true"), new ArrayList<Predicate>(), new ArrayList<Predicate>());
		Action baz = new Action(new Predicate("baz"), new Predicate("true"), new ArrayList<Predicate>(), new ArrayList<Predicate>());
		System.out.println("Ground action: " + move_a_to_table);
		System.out.println("Resulting state: " + move_a_to_table.apply(s));
		
		AbstractProgram p = new Sequence(
				new ExecAction(baz),
				new Sequence(
						new NDet(
								new ExecAction(move_a_to_table),
								new ExecAction(move_b_to_c)
						),
						new NDet(
								new ExecAction(foo),
								new NDet(
										new ExecAction(bar),
										new ExecAction(foe)
								)
						)
				)
		);
		System.out.println("Program: " + p);
		
		ProgramState ps0 = new ProgramState(p, s), ps = ps0;
		
		for(Iterator<ProgramState> i = ps0.solutionIterator(); i.hasNext(); ) {
			ps = i.next();
			System.out.println("NEXT SOLUTION: " + ps.getState().getActionHistory());
		}
	}
}
