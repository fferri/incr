package incr.golog.syntax;

import incr.golog.AbstractEntity;
import incr.golog.TransFinal;
import incr.subst.Substitutions;

public abstract class AbstractProgram extends AbstractEntity implements TransFinal {
	public abstract AbstractProgram substitute(Substitutions ss);
}
