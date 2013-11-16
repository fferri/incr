package incr.golog;

public abstract class AbstractEntity {
	protected String indent(int indentLevel) {
		StringBuffer sb = new StringBuffer();
		while(indentLevel-- > 0) sb.append("\t");
		return sb.toString();
	}
	
	public abstract boolean isGround();
	
	@Override
	public final String toString() {
		return toString(0);
	}
	
	public abstract String toString(int indentLevel);
	
	@Override
	public final boolean equals(Object obj) {
		return obj != null && obj instanceof AbstractEntity && equals((AbstractEntity)obj);
	}
	
	public abstract boolean equals(AbstractEntity e);
	
	@Override
	public abstract int hashCode();
}
