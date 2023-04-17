package prr.clients;

import java.io.Serializable;

public class NormalState extends Clients.State implements Serializable{

    NormalState(Clients client){
        client.super();
    }

    public void makePayment(){
        if (getClient().getBalance() > 500){
            setState(new GoldState(getClient()));
        }
    }

    public void sendText(){}
    public void endVideoCall(){}
    public void endCall(){}

    public String status(){
        return "NORMAL";
    }
}
