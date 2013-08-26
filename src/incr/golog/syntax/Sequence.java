package incr.golog.syntax;

public class Sequence extends AbstractProgram {
	private AbstractProgram p1;
	private AbstractProgram p2;
	
	public Sequence(AbstractProgram p1, AbstractProgram p2) {
		if(p1 == null || p2 == null)
			throw new NullPointerException();
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public AbstractProgram getP1() {
		return p1;
	}
	
	public AbstractProgram getP2() {
		return p2;
	}
	
	@Override
	public String toString(int i) {
		return p1.toString(i) + p2.toString(i);
	}
}
