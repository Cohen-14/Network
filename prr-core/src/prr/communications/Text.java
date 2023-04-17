package prr.communications;

import java.io.Serializable;

import prr.terminals.Terminal;
import prr.Plan;

public class Text extends Communications implements Serializable {

    public Text(int key,Terminal sender, Terminal receiver, String message){
        super(key,sender, receiver,message);
    }

    public void cost(){
        Plan plan = getSender().getClient().getPlan();
        setPrice(plan.textCost(getUnits()));
    }

    @Override
    public String commType(){
        return "TEXT";
    }


    @Override
    public String toString(){
        return "TEXT|" + super.toString();
    }
}
