package incr.golog;

import java.util.List;

public interface TransFinal {
	public List<ProgramState> trans(ProgramState s);
	
	public boolean isFinal(ProgramState s);
}
