package incr.golog.syntax;

public class NDet extends AbstractProgram {
	private AbstractProgram p1;
	private AbstractProgram p2;
	
	public NDet(AbstractProgram p1, AbstractProgram p2) {
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
		return indent(i) + "ndet(\n" +
				p1.toString(i + 1)
				+ indent(i) + "|\n"
				+ p2.toString(i) + 
				indent(i) + ")\n";
	}
}
