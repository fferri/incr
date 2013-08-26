package incr.formula;

import java.util.List;

import incr.State;

public abstract class AbstractTerm {
	public abstract boolean truthValue(State s);
	
	public abstract boolean isGround();
	
	public static final boolean isGround(List<? extends AbstractTerm> l) {
		for(AbstractTerm t : l)
			if(!t.isGround())
				return false;
		return true;
	}
}
