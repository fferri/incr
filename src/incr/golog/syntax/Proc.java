package incr.golog.syntax;

import incr.formula.Predicate;

public class Proc extends AbstractProgram {
	private Predicate head;
	private AbstractProgram body;
	
	public Proc(Predicate head, AbstractProgram body) {
		if(head == null || body == null)
			throw new NullPointerException();
		this.head = head;
		this.body = body;
	}
	
	public Predicate getHead() {
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
}
