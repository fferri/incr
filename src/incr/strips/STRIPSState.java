package incr.strips;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import incr.State;
import incr.subst.Unify;
import incr.term.Functional;
import incr.term.Term;

public class STRIPSState extends State /*implements Iterable<Term>*/ {
	private Set<Term> predicates;
	private List<Functional> actionHistory;
	
	public STRIPSState() {
		predicates = new HashSet<>();
		actionHistory = new ArrayList<>();
	}
	
	public STRIPSState(Term...terms) {
		this(Arrays.asList(terms), new ArrayList<Functional>());
	}
	
	public STRIPSState(Collection<Term> terms) {
		this(terms, new ArrayList<Functional>());
	}
	
	public STRIPSState(Collection<Term> terms, List<Functional> actionSeq) {
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
	
	public STRIPSState(STRIPSState s) {
		this(s.predicates, s.actionHistory);
	}

	/*
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
	*/
	
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
		return obj != null && obj instanceof STRIPSState && equals((STRIPSState)obj);
	}
	
	public boolean equals(STRIPSState s) {
		return s != null && s.predicates.equals(predicates);
	}
	
	@Override
	public int hashCode() {
		return predicates.hashCode();
	}
	
	@Override
	public boolean holds(Term t) {
		for(Term term : predicates) {
			if(Unify.unify(term, t) != null)
				return true;
		}
		return false;
	}
	
	@Override
	public void assertFact(Term t) {
		if(!t.isGround())
			throw new IllegalArgumentException("A STRIPS state can only contain ground terms");
		predicates.add(t);
	}
	
	@Override
	public void retractFact(Term t) {
		for(Iterator<Term> it = predicates.iterator(); it.hasNext(); ) {
			Term t1 = it.next();
			if(Unify.unify(t, t1) != null)
				it.remove();
		}
	}
	
	@Override
	public void appendAction(Functional actionHead) {
		actionHistory.add(actionHead);
	}
}
