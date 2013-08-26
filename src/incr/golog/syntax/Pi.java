package incr.golog.syntax;

public class Pi extends AbstractProgram {
	private String var;
	private AbstractProgram p1;
	
	public Pi(String var, AbstractProgram p1) {
		if(var == null || p1 == null)
			throw new NullPointerException();
		this.var = var;
		this.p1 = p1;
	}
	
	public String getVar() {
		return var;
	}
	
	public AbstractProgram getP1() {
		return p1;
	}
	
	@Override
	public String toString(int i) {
		return indent(i) + "pi " + var + "\n"
				+ p1.toString(i + 1);
	}
}
