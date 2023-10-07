package Engine.world.rule.actionDefinition;

import Engine.InvalidValue;
import Engine.generated.PRDAction;

public class replaceDefinition extends actionDefinition{
    public replaceDefinition(PRDAction action, String ruleName) throws InvalidValue {
        super(action, ruleName);
    }
}
