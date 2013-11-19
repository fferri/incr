package incr.golog;

import incr.Action;
import incr.State;
import incr.golog.syntax.AbstractProgram;
import incr.subst.Substitutions;
import incr.subst.Unify;
import incr.term.Functional;
import incr.term.Term;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import util.MultiHashMap;

public class Environment {
	private Set<Proc> procs = new HashSet<Proc>();
	private MultiHashMap<String, Proc> procsBySignature = new MultiHashMap<>();
	
	private Set<Action> actions = new HashSet<Action>();
	private MultiHashMap<String, Action> actionsBySignature = new MultiHashMap<>();
	
	private AbstractProgram program = null;

	public Environment() {
	}

	public void add(Proc p) {
		procs.add(p);
		procsBySignature.add(p.getHead().getSignature(), p);
	}
	
	public Set<Proc> getProcs() {
		return Collections.unmodifiableSet(procs);
	}
	
	public Set<Proc> getProcs(Functional head) {
		String s = head.getSignature();
		if(procsBySignature.containsKey(s))
			return procsBySignature.get(s);
		else
			return Collections.emptySet();
	}
	
	public void add(Action a) {
		actions.add(a);
		actionsBySignature.add(a.getHead().getSignature(), a);
	}
	
	public Set<Action> getActions() {
		return Collections.unmodifiableSet(actions);
	}
	
	public Set<Action> getActions(Functional head) {
		String s = head.getSignature();
		if(actionsBySignature.containsKey(s))
			return actionsBySignature.get(s);
		else
			return Collections.emptySet();
	}
	
	public void setProgram(AbstractProgram p) {
		program = p;
	}
	
	public AbstractProgram getProgram() {
		return program;
	}
	
	/*
	public Set<Term> getAllIndividuals() {
		Set<Term> ret = new HashSet<>();
		for(Action a : actions)
			getAllIndividuals(a, ret);
		return ret;
	}
	
	private void getAllIndividuals(Action a, Set<Term> ret) {
		getAllIndividuals(a.getPreconditions(), ret);
		for(Term t : a.getAddList())
			getAllIndividuals(t, ret);
		for(Term t : a.getDelList())
			getAllIndividuals(t, ret);
	}
	
	private void getAllIndividuals(Term t, Set<Term> ret) {
		if(t instanceof BinaryOp) {
			BinaryOp t1 = (BinaryOp)t;
			getAllIndividuals(t1.getOperand1(), ret);
			getAllIndividuals(t1.getOperand2(), ret);
		} else if(t instanceof UnaryOp) {
			UnaryOp t1 = (UnaryOp)t;
			getAllIndividuals(t1.getOperand1(), ret);
		} else if(t instanceof Functional) {
			Functional t1 = (Functional)t;
			if(t1.isGround())
				ret.add(t1);
			for(Term t2 : t1.getArgs())
				getAllIndividuals(t2, ret);
		}
	}
	*/
	
	private Set<Term> individuals = new HashSet<>();
	
	public Set<Term> getAllIndividuals() {
		return Collections.unmodifiableSet(individuals);
	}
	
	public void setIndividuals(Term...terms) {
		setIndividuals(Arrays.asList(terms));
	}
	
	public void setIndividuals(Collection<Term> i) {
		individuals.clear();
		individuals.addAll(i);
	}
	
	public void addIndividual(Term i) {
		individuals.add(i);
	}
	
	public State executeAction(Functional head, State s) {
		Set<Action> actions = getActions(head);
		if(actions.isEmpty()) return null;
		if(actions.size() > 1)
			System.out.println("WARNING: more than one action matching " + head);
		Action action = actions.iterator().next();
		Substitutions subs = Unify.unify(action.getHead(), head);
		Action groundAction = action.ground(subs);
		return groundAction.apply(s);
	}
}
