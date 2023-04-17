package prr.clients;

import java.io.Serializable;

public class PlatinumState extends Clients.State implements Serializable{

    PlatinumState(Clients clients){
        clients.super();
    }

    public void makePayment(){}

    public void sendText(){
        if (getClient().getBalance() < 0){
            setState(new NormalState(getClient()));
        }
        else if (getClient().getConsTexts() == 2){
            if (getClient().getBalance() > 0){
                setState(new GoldState(getClient()));
                getClient().setConsTexts(0);
            }
            getClient().setConsTexts(0);
        }
    }

    public void endVideoCall(){
        if (getClient().getBalance() < 0){
            setState(new NormalState(getClient()));
        }
    }

    public void endCall(){
        if (getClient().getBalance() < 0){
            setState(new NormalState(getClient()));
        }
    }

    public String status(){
        return "PLATINUM";
    }
}
