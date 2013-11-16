package incr;

import incr.golog.syntax.AbstractProgram;
import incr.golog.syntax.Atomic;
import incr.golog.syntax.Sequence;
import incr.term.Functional;

public class INCRBuilder {
	private AbstractProgram p;
	
	public INCRBuilder() {
		p = null;
	}
	
	public AbstractProgram getProgram() {
		return p;
	}
	
	private INCRBuilder append(AbstractProgram p1) {
		if(p == null) p = p1;
		else p = new Sequence(p, p1);
		return this;
	}
	
	public INCRBuilder exec(Functional head) {
		return append(new Atomic(head));
	}
}
