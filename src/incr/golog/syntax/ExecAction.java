package incr.golog.syntax;

import incr.Action;

public class ExecAction extends AbstractProgram {
	private Action action;
	
	public ExecAction(Action action) {
		if(action == null)
			throw new NullPointerException();
		this.action = action;
	}
	
	public Action getAction() {
		return action;
	}
	
	@Override
	public String toString(int i) {
		return indent(i) + action.getHead() + "\n";
	}
}
