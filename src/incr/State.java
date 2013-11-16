package incr;

import incr.term.Functional;
import incr.term.Term;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class State implements Iterable<Term> {
	private Set<Term> predicates;
	private List<Functional> actionHistory;
	
	public State() {
		predicates = new HashSet<>();
		actionHistory = new ArrayList<>();
	}
	
	public State(Term...terms) {
		this(Arrays.asList(terms), new ArrayList<Functional>());
	}
	
	public State(Collection<Term> terms) {
		this(terms, new ArrayList<Functional>());
	}
	
	public State(Collection<Term> terms, List<Functional> actionSeq) {
		this();
		if(terms == null)
			throw new NullPointerException();
		for(Term t : terms) {
			if(t == null)
				throw new NullPointerException();
			predicates.add(t);
		}
		if(actionSeq != null)
			actionHistory.addAll(actionSeq);
	}
	
	public State(State s) {
		this(s.predicates, s.actionHistory);
	}

	@Override
	public Iterator<Term> iterator() {
		return predicates.iterator();
	}
	
	public boolean contains(Term p) {
		return predicates.contains(p);
	}

	public boolean containsAll(Collection<? extends Term> ps) {
		for(Term p : ps)
			if(!contains(p))
				return false;
		return true;
	}
	
	public boolean add(Term p) {
		return predicates.add(p);
	}
	
	public boolean addAll(Collection<? extends Term> ps) {
		return predicates.addAll(ps);
	}
	
	public boolean remove(Term p) {
		return predicates.remove(p);
	}
	
	public boolean removeAll(Collection<? extends Term> ps) {
		boolean ret = false;
		for(Term p : ps)
			ret = ret || remove(p);
		return ret;
	}
	
	public int size() {
		return predicates.size();
	}
	
	public boolean isEmpty() {
		return predicates.isEmpty();
	}
	
	public void addActionToHistory(Functional action) {
		actionHistory.add(action);
	}
	
	public List<Functional> getActionHistory() {
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
