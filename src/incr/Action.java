package incr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import incr.formula.AbstractTerm;
import incr.formula.Predicate;
import incr.formula.Substitution;

public class Action {
	private Predicate head;
	private AbstractTerm preconditions;
	private List<Predicate> addList;
	private List<Predicate> delList;
	
	public Action(Predicate head, AbstractTerm preconditions, List<Predicate> addList, List<Predicate> delList) {
		if(head == null || preconditions == null || addList == null || delList == null)
			throw new NullPointerException();
		this.head = head;
		this.preconditions = preconditions;
		this.addList = new ArrayList<>(addList.size());
		for(Predicate p : addList) {
			if(p == null)
				throw new NullPointerException();
			this.addList.add(p);
		}
		this.delList = new ArrayList<>(delList.size());
		for(Predicate p : delList) {
			if(p == null)
				throw new NullPointerException();
			this.delList.add(p);
		}
	}
	
	public Predicate getHead() {
		return head;
	}
	
	public AbstractTerm getPreconditions() {
		return preconditions;
	}
	
	public List<Predicate> getAddList() {
		return Collections.unmodifiableList(addList);
	}
	
	public List<Predicate> getDelList() {
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
		return head.isGround() &&
				preconditions.isGround() &&
				AbstractTerm.isGround(addList) &&
				AbstractTerm.isGround(delList);
	}
	
	public Action ground(Substitution sub) {
		return new Action(
				sub.apply(head),
				sub.apply(preconditions),
				sub.apply(addList),
				sub.apply(delList)
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
}
