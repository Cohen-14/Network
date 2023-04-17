package prr.communications;

import java.io.Serializable;

import prr.Plan;
import prr.terminals.Terminal;

public class Video extends Communications implements Serializable{

    public Video(int key,Terminal sender, Terminal receiver){
        super(key,sender,receiver);
    }

    @Override
    public void cost(){
        boolean areFriends = getSender().areFriends(getReceiver());
        Plan plan = getSender().getClient().getPlan();
        setPrice(plan.videoCost(getUnits(),areFriends));
    }

    @Override
    public String commType(){
        return "VIDEO";
    }


    @Override
    public String toString(){
        return "VIDEO|" + super.toString();
    }
}
