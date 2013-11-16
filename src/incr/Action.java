package incr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import incr.formula.BinaryOp;
import incr.formula.UnaryOp;
import incr.subst.Substitutions;
import incr.term.Functional;
import incr.term.Term;
import incr.term.Variable;

public class Action {
	private Functional head;
	private Term preconditions;
	private List<Term> addList;
	private List<Term> delList;

	public Action(Functional head) {
		this(head, Term.TRUE);
	}
	
	public Action(Functional head, Term preconditions) {
		this(head, preconditions, NO_TERMS);
	}
	
	public Action(Functional head, Term preconditions, List<? extends Term> addList) {
		this(head, preconditions, addList, NO_TERMS);
	}
	
	public Action(Functional head, Term preconditions, List<? extends Term> addList, List<? extends Term> delList) {
		if(head == null || preconditions == null || addList == null || delList == null)
			throw new NullPointerException();
		this.head = head;
		this.preconditions = preconditions;
		this.addList = new ArrayList<>(addList.size());
		for(Term p : addList) {
			validateAddDelTerm(p);
			this.addList.add(p);
		}
		this.delList = new ArrayList<>(delList.size());
		for(Term p : delList) {
			validateAddDelTerm(p);
			this.delList.add(p);
		}
	}
	
	protected void validateAddDelTerm(Term t) {
		if(t == null)
			throw new NullPointerException();
		if(t instanceof Variable)
			return;
		if(t instanceof Functional) {
			Functional f = (Functional)t;
			if(f.getOrder() > 1)
				throw new IllegalArgumentException("only atoms or first order terms allowed in add/del list");
			if(f instanceof UnaryOp || f instanceof BinaryOp)
				throw new IllegalArgumentException("logic operators not allowed in add/del list");
		} else {
			throw new IllegalArgumentException("unsupported term in add/del list: " + t.getClass().getSimpleName());
		}
	}
	
	public Functional getHead() {
		return head;
	}
	
	public Term getPreconditions() {
		return preconditions;
	}
	
	public List<Term> getAddList() {
		return Collections.unmodifiableList(addList);
	}
	
	public List<Term> getDelList() {
		return Collections.unmodifiableList(delList);
	}
	
	@Override
	public String toString() {
		return String.format("%s\n\tPrecond: %s\n\tAdd: %s\n\tDel: %s", head, preconditions, addList, delList);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null &&
				obj instanceof Action &&
				equals((Action)obj);
	}
	
	public boolean equals(Action a) {
		return a != null &&
				a.getHead().equals(getHead()) &&
				getPreconditions().equals(a.getPreconditions()) &&
				getAddList().equals(a.getAddList()) &&
				getDelList().equals(a.getDelList());
	}
	
	@Override
	public int hashCode() {
		return 7 * head.hashCode() +
				11 * preconditions.hashCode() +
				23 * addList.hashCode() +
				29 * delList.hashCode();
	}
	
	public boolean isGround() {
		if(!head.isGround()) return false;
		if(!preconditions.isGround()) return false;
		for(Term t : addList) if(!t.isGround()) return false;
		for(Term t : delList) if(!t.isGround()) return false;
		return true;
	}
	
	public Action ground(Substitutions sub) {
		List<Term> newAddList = new ArrayList<>(addList.size());
		for(Term add : addList) newAddList.add(add.substitute(sub));
		List<Term> newDelList = new ArrayList<>(delList.size());
		for(Term add : delList) newDelList.add(add.substitute(sub));
		return new Action(
				(Functional)head.substitute(sub),
				preconditions.substitute(sub),
				newAddList,
				newDelList
		);
	}
	
	public boolean isPossible(State s) {
		return isGround() && preconditions.truthValue(s);
	}
	
	public State apply(State s) {
		if(!isPossible(s)) return null;
		State s1 = new State(s);
		s1.addActionToHistory(head);
		s1.addAll(addList);
		s1.removeAll(delList);
		return s1;
	}
	
	public static final List<Term> NO_TERMS = Collections.emptyList();
}
