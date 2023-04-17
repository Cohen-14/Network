package prr.clients;

import java.io.Serializable;

public class GoldState extends Clients.State implements Serializable{
    
    GoldState(Clients clients){
        clients.super();
    }

    public void makePayment(){}

    public void sendText(){
        if (getClient().getBalance() < 0){
            setState(new NormalState(getClient()));
        }
    }

    public void endVideoCall(){
        if (getClient().getBalance() < 0){
            setState(new NormalState(getClient()));
        }
        else if(getClient().getConsVideo() == 5){
            if (getClient().getBalance() > 0){
                setState(new PlatinumState(getClient()));
                getClient().setConsVideo(0);
            }
            getClient().setConsVideo(0);
        }
    }

    public void endCall(){
        if (getClient().getBalance() < 0){
            setState(new NormalState(getClient()));
        }
    }

    public String status(){
        return "GOLD";
    }
}
