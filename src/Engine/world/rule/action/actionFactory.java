package Engine.world.rule.action;

import Engine.generated.PRDAction;
import Engine.utilites.Utilites;

public class actionFactory {

    public ActionInterface makeAction(PRDAction action, Utilites util, String ruleName){
        if (action.getType().equals("increase") || action.getType().equals("decrease")) {
            return new addValue(action, util, ruleName);
        } else if (action.getType().equals("calculation")) {
            return new calculation(action, util, ruleName);
        } else if (action.getType().equals("condition")) {
            return new condition(action, util, ruleName);
        } else if (action.getType().equals("set")) {
            return new set(action, util, ruleName);
        } else if (action.getType().equals("kill")) {
            return new kill(action, util, ruleName);
        }else if(action.getType().equals("replace")){
            return new replace(action, util, ruleName);
        } else if (action.getType().equals("proximity")) {
            return new proximity(action, util, ruleName);
        }
        return new action();
    }
}
