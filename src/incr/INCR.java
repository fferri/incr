/**
 * Copyright 2013 by Federico Ferri
 * All Rights Reserved
 */
package incr;

import java.io.FileInputStream;

import incr.golog.*;
import incr.golog.syntax.parser.GologParser;
import incr.strips.*;
import incr.term.Functional;

public class INCR {
	public static void main(String[] args) throws Exception {
		GologParser parser = new GologParser(new FileInputStream("programs/blocksworld.txt"));
		parser.parse();
		
		for(STRIPSAction a : parser.environment.getActions()) {
			System.out.println("ACTION " + a);
		}
		for(Proc p : parser.environment.getProcs()) {
			System.out.println("PROC " + p.getHead() + "\n" + p.getBody().toString(1));
		}
		
		STRIPSState s = new STRIPSState();
		STRIPSAction init = parser.environment.getActions(new Functional("init")).iterator().next();
		s = init.apply(s);
		System.out.println("Initial state: " + s);

		System.out.println("Program:\n" + parser.program.toString(1));
		
		ProgramState ps0 = new ProgramState(parser.environment, parser.program, s);
		
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
