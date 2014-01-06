package incr.strips;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import incr.Action;
import incr.State;
import incr.formula.BinaryOp;
import incr.formula.UnaryOp;
import incr.subst.Substitutions;
import incr.subst.Unify;
import incr.term.Functional;
import incr.term.Term;
import incr.term.Variable;

public class STRIPSAction extends Action<STRIPSState> {
	private Term preconditions;
	private List<Term> addList;
	private List<Term> delList;

	private STRIPSAction(Functional head, Term preconditions, List<? extends Term> addList, List<? extends Term> delList) {
		super(head);
		if(preconditions == null || addList == null || delList == null)
			throw new NullPointerException();
		this.preconditions = preconditions;
		this.addList = new ArrayList<>(addList);
		this.delList = new ArrayList<>(delList);
	}
	
	public STRIPSAction(Functional head, Term preconditions, Term...addDelList) {
		this(head, preconditions, Arrays.asList(addDelList));
	}
	
	public STRIPSAction(Functional head, Term preconditions, List<? extends Term> addDelList) {
		super(head);
		if(preconditions == null || addDelList == null)
			throw new NullPointerException();
		this.preconditions = preconditions;
		this.addList = new ArrayList<>();
		this.delList = new ArrayList<>();
		for(Term p : addDelList) {
			// check negation first:
			boolean neg = false;
			while(p instanceof Functional && ((Functional)p).getSignature().equals("not/1")) {
				p = ((Functional)p).getArg(0);
				neg = !neg;
			}

			if(p instanceof Variable)
				throw new IllegalArgumentException();
			if(p instanceof Functional) {
				Functional f = (Functional)p;
				if(f.getOrder() > 1)
					throw new IllegalArgumentException("only atoms or first order terms allowed in add/del list");
				if(f instanceof UnaryOp || f instanceof BinaryOp)
					throw new IllegalArgumentException("logic operators not allowed in add/del list");
			} else {
				throw new IllegalArgumentException("unsupported term in add/del list: " + p.getClass().getSimpleName());
			}
			if(neg) this.delList.add(p);
			else this.addList.add(p);
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s\n\tPrecond: %s\n\tAdd: %s\n\tDel: %s", head, preconditions, addList, delList);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null &&
				obj instanceof STRIPSAction &&
				equals((STRIPSAction)obj);
	}
	
	public boolean equals(STRIPSAction a) {
		return a != null &&
				head.equals(a.head) &&
				preconditions.equals(a.preconditions) &&
				addList.equals(a.addList) &&
				delList.equals(a.delList);
	}
	
	@Override
	public int hashCode() {
		return 7 * head.hashCode() +
				11 * preconditions.hashCode() +
				23 * addList.hashCode() +
				29 * delList.hashCode();
	}
	
	private Substitutions getUnifier(Functional term) {
		return Unify.unify(head, term);
	}
	
	private Term getGroundPreconditions(Substitutions subs) {
		return preconditions.substitute(subs);
	}
	
	private List<Term> getGroundAddList(Substitutions subs) {
		List<Term> groundAddList = new ArrayList<>(addList.size());
		for(Term add : addList)
			groundAddList.add(add.substitute(subs));
		return groundAddList;
	}
	
	private List<Term> getGroundDelList(Substitutions subs) {
		List<Term> groundDelList = new ArrayList<>(delList.size());
		for(Term add : delList)
			groundDelList.add(add.substitute(subs));
		return groundDelList;
	}
	
	@Override
	public Action<STRIPSState> ground(Functional term) {
		if(!term.isGround())
			throw new IllegalArgumentException("Cannot ground over an unground term");
		
		Substitutions subs = getUnifier(term);
		if(subs == null)
			throw new IllegalArgumentException("Cannot unify " + head + " with " + term);
		
		return new STRIPSAction(term,
				getGroundPreconditions(subs),
				getGroundAddList(subs),
				getGroundDelList(subs));
	}
	
	@Override
	public boolean isPossible(Functional term, STRIPSState s) {
		return isPossible(term, getGroundPreconditions(getUnifier(term)), s);
	}
	
	private boolean isPossible(Functional term, Term groundPreconditions, State s) {
		return groundPreconditions.truthValue(s);
	}
	
	@Override
	public STRIPSState apply(Functional term, STRIPSState s) {
		Substitutions subs = getUnifier(term);
		if(subs == null)
			throw new IllegalArgumentException("Cannot unify " + head + " with " + term);

		if(!isPossible(term, getGroundPreconditions(subs), s))
			return null;

		List<Term> groundAddList = getGroundAddList(subs),
				groundDelList = getGroundDelList(subs);
		
		STRIPSState s1 = new STRIPSState(s);
		s1.addActionToHistory(term);
		for(Term t : groundAddList) s1.assertFact(t);
		for(Term t : groundDelList) s1.retractFact(t);

		return s1;
	}
}
