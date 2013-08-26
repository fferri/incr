package incr;

import incr.formula.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class State implements Iterable<Predicate> {
	private Set<Predicate> predicates;
	private List<Predicate> actionHistory;
	
	public State() {
		predicates = new HashSet<>();
		actionHistory = new ArrayList<>();
	}
	
	public State(Predicate...preds) {
		this(Arrays.asList(preds), new ArrayList<Predicate>());
	}
	
	public State(Collection<Predicate> preds, List<Predicate> actionSeq) {
		this();
		if(preds == null)
			throw new NullPointerException();
		for(Predicate p : preds) {
			if(p == null)
				throw new NullPointerException();
			predicates.add(p);
		}
		if(actionSeq != null)
			actionHistory.addAll(actionSeq);
	}
	
	public State(State s) {
		this(s.predicates, s.actionHistory);
	}

	@Override
	public Iterator<Predicate> iterator() {
		return predicates.iterator();
	}
	
	public boolean contains(Predicate p) {
		return predicates.contains(p);
	}
	
	public boolean add(Predicate p) {
		return predicates.add(p);
	}
	
	public boolean addAll(Collection<Predicate> ps) {
		return predicates.addAll(ps);
	}
	
	public boolean remove(Predicate p) {
		return predicates.remove(p);
	}
	
	public boolean removeAll(Collection<Predicate> ps) {
		boolean ret = false;
		for(Predicate p : ps)
			ret = ret || remove(p);
		return ret;
	}
	
	public int size() {
		return predicates.size();
	}
	
	public boolean isEmpty() {
		return predicates.isEmpty();
	}
	
	public void addActionToHistory(Predicate action) {
		actionHistory.add(action);
	}
	
	public List<Predicate> getActionHistory() {
		return Collections.unmodifiableList(actionHistory);
	}
	
	@Override
	public String toString() {
		return predicates.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof State && equals((State)obj);
	}
	
	public boolean equals(State s) {
		return s != null && s.predicates.equals(predicates);
	}
	
	@Override
	public int hashCode() {
		return predicates.hashCode();
	}
}
