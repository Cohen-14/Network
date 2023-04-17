package prr;

import java.io.Serializable;

import prr.clients.Clients;

public abstract class Plan implements Serializable{
    private Clients _client;

    public Plan(Clients client){
        _client = client;
    }

    public Clients getClient(){return _client;}

    public abstract int callCost(int duration, boolean areFriends);
    public abstract int textCost(int size);
    public abstract int videoCost(int duration, boolean areFriends);

    public abstract String plan();
}
