package incr.golog.syntax;

public final class Empty extends AbstractProgram {
	@Override
	public String toString(int i) {
		return indent(i) + "nil\n";
	}
}
