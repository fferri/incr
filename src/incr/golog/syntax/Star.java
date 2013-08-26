package incr.golog.syntax;

public class Star extends AbstractProgram {
	private AbstractProgram p1;
	
	public Star(AbstractProgram p1) {
		if(p1 == null)
			throw new NullPointerException();
		this.p1 = p1;
	}
	
	public AbstractProgram getP1() {
		return p1;
	}

	@Override
	public String toString(int i) {
		return indent(i) + "star(\n" +
				p1.toString(i + 1) +
				indent(i) + ")\n";
	}
}
