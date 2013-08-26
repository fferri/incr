package incr.golog.syntax;

public abstract class AbstractProgram {
	protected String indent(int indentLevel) {
		StringBuffer sb = new StringBuffer();
		while(indentLevel-- > 0) sb.append("\t");
		return sb.toString();
	}
	
	@Override
	public final String toString() {
		return toString(0);
	}
	
	public abstract String toString(int indentLevel);
}
