package incr.golog;

import incr.golog.syntax.AbstractProgram;
import incr.subst.Substitutions;
import incr.subst.Unify;
import incr.term.Functional;

public class Proc extends AbstractEntity {
	private Functional head;
	private AbstractProgram body;
	
	public Proc(Functional head, AbstractProgram body) {
		if(head == null || body == null)
			throw new NullPointerException();
		this.head = head;
		this.body = body;
	}
	
	public Functional getHead() {
		return head;
	}
	
	public AbstractProgram getBody() {
		return body;
	}
	
	@Override
	public String toString(int i) {
		return indent(i) + "proc " + head + "\n" +
				body.toString(i + 1) +
				"endProc\n";
	}
	
	@Override
	public boolean equals(AbstractEntity e) {
		return e != null && e instanceof Proc && equals((Proc)e);
	}
	
	public boolean equals(Proc p) {
		return p != null && getHead().equals(p.getHead()) && getBody().equals(p.getBody());
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode() + getHead().hashCode() + getBody().hashCode();
	}
	
	public boolean isGround() {
		return head.isGround() && body.isGround();
	}
	
	public Proc ground(Functional f) {
		if(f.getArity() != head.getArity())
			throw new IllegalArgumentException("caller functional must have the same arity of the calling proc");
		
		// build substitutions
		/*Substitutions ss = new Substitutions();
		for(int i = 0; i < head.getArity(); i++) {
			if(head.getArg(i) instanceof Variable)
				ss.add(new Substitution(f.getArg(i), (Variable)head.getArg(i)));
			else
				throw new RuntimeException("proc with ground args");
		}*/
		Substitutions ss = Unify.unify(f, head);
		
		return new Proc((Functional)head.substitute(ss), body.substitute(ss));
	}
}
