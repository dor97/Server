package Engine.world.rule.actionDefinition;

import Engine.InvalidValue;
import Engine.generated.PRDAction;

public class setDefinition extends actionDefinition{
    public setDefinition(PRDAction action, String ruleName) throws InvalidValue {
        super(action, ruleName);
    }
}
