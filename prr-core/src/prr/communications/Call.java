package prr.communications;

import java.io.Serializable;

import prr.Plan;
import prr.terminals.Terminal;

public class Call extends Communications implements Serializable {

    public Call(int key,Terminal sender, Terminal receiver){
        super (key,sender, receiver);
    }

    @Override
    public void cost(){
        boolean areFriends = getSender().areFriends(getReceiver());
        Plan plan = getSender().getClient().getPlan();
        setPrice(plan.callCost(getUnits(),areFriends));
    }

    @Override
    public String commType(){
        return "VOICE";
    }

    @Override
    public String toString(){
        return "VOICE|" + super.toString();
    }
}
