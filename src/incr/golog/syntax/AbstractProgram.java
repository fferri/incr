package incr.golog.syntax;

import incr.golog.AbstractEntity;
import incr.subst.Substitutions;

public abstract class AbstractProgram extends AbstractEntity {
	public abstract AbstractProgram substitute(Substitutions ss);
}
